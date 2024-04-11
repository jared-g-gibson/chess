package server;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import webSocketMessages.serverMessages.ErrorClass;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    ConnectionManager connections;
    private AuthDAO auths;
    private GameDAO games;
    private UserDAO users;

    public WebSocketHandler(AuthDAO auths, GameDAO games, UserDAO users) {
        this.auths = auths;
        this.games = games;
        this.users = users;
        connections = new ConnectionManager();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
                this.joinPlayer(joinPlayer, session);
            }
            case JOIN_OBSERVER -> {
                JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
                this.joinObserver(joinObserver, session);
            }
            case MAKE_MOVE -> {
                MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
                this.makeMove(makeMove, session);
            }
            case LEAVE -> {
                Leave leave = new Gson().fromJson(message, Leave.class);
                this.leave(leave, session);
            }
            case RESIGN -> {
                Resign resign = new Gson().fromJson(message, Resign.class);
                this.resign(resign, session);
            }
        }
        System.out.printf("Received: %s", command.toString());
        // session.getRemote().sendString("WebSocket response: " + message);
    }

    private void makeMove(MakeMove makeMove, Session session) {
        try {
            String username;
            // Getting the username
            try {
                username = auths.getAuth(makeMove.getAuthString()).username();
                if(username == null)
                    throw new DataAccessException("Invalid authToken");
            }
            catch (Exception e){
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Check if game is already over (you can't resign)
            ChessGame game = games.getGame(Integer.toString(makeMove.getGameID())).game();
            if(game.isGameOver()) {
                sendError(session, "Error: Game is already over.");
                return;
            }

            // Check if user is allowed to make moves (has to be a player)
            if(!username.equals(games.getGame(Integer.toString(makeMove.getGameID())).whiteUsername()) &&
                    !username.equals(games.getGame(Integer.toString(makeMove.getGameID())).blackUsername())) {
                sendError(session, "Error: Observer is not allowed to make moves.");
                return;
            }

            // Check to see if it is correct player's turn
            if(username.equals(games.getGame(Integer.toString(makeMove.getGameID())).whiteUsername()) && game.getTeamTurn() != ChessGame.TeamColor.WHITE) {
                sendError(session, "Error: It is not your turn.");
                return;
            }
            else if(username.equals(games.getGame(Integer.toString(makeMove.getGameID())).blackUsername()) && game.getTeamTurn() != ChessGame.TeamColor.BLACK) {
                sendError(session, "Error: It is not your turn.");
                return;
            }

            // Check to see if move is a valid move
            if(!game.isValidMove(makeMove.getMove())) {
                sendError(session, "Error: This move is not valid");
                return;
            }

            // The move passes all of my checks
            game.makeMove(makeMove.getMove());
            games.updateGameState(Integer.toString(makeMove.getGameID()), game);

            // Broadcast new board to everyone
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, games.getGame(Integer.toString(makeMove.getGameID())).game());
            String json = new Gson().toJson(loadGame);
            connections.broadcast(makeMove.getGameID(), json, makeMove.getAuthString());
            session.getRemote().sendString(json);

            // Broadcast move description to everyone except root client
            String startPos = null;
            String endPos = null;
            if(username.equals(games.getGame(Integer.toString(makeMove.getGameID())).whiteUsername())) {
                startPos = getPosition(makeMove.getMove().getStartPosition());
                endPos = getPosition(makeMove.getMove().getEndPosition());
            }
            else if(username.equals(games.getGame(Integer.toString(makeMove.getGameID())).blackUsername())) {
                startPos = getPosition(makeMove.getMove().getStartPosition());
                endPos = getPosition(makeMove.getMove().getEndPosition());
            }

            Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " moved from " + startPos + " to " + endPos);
            json = new Gson().toJson(notification);
            connections.broadcast(makeMove.getGameID(), json, makeMove.getAuthString());

            if(game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, games.getGame(Integer.toString(makeMove.getGameID())).blackUsername() + " is in checkmate. White wins");
                sendGameOverNotification(notification, game, makeMove, session);
                return;
            }
            if(game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, games.getGame(Integer.toString(makeMove.getGameID())).whiteUsername() + " is in checkmate. Black wins");
                sendGameOverNotification(notification, game, makeMove, session);
                return;
            }
            if(game.isInCheck(ChessGame.TeamColor.BLACK)) {
                notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, games.getGame(Integer.toString(makeMove.getGameID())).blackUsername() + " is in check");
                sendGameOverNotification(notification, game, makeMove, session);
                return;
            }
            if(game.isInCheck(ChessGame.TeamColor.WHITE)) {
                notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, games.getGame(Integer.toString(makeMove.getGameID())).whiteUsername() + " is in check");
                sendGameOverNotification(notification, game, makeMove, session);
                return;
            }
            if(game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "Stalemate. It's a draw.");
                sendGameOverNotification(notification, game, makeMove, session);
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void sendGameOverNotification(Notification notification, ChessGame game, MakeMove makeMove, Session session) throws IOException, DataAccessException {
        String json = new Gson().toJson(notification);
        connections.broadcast(makeMove.getGameID(), json, makeMove.getAuthString());
        session.getRemote().sendString(json);

        // Update game status to become no longer playable
        game.setGameOver();
        games.updateGameState(Integer.toString(makeMove.getGameID()), game);
    }

    private void setGameOver() {

    }

    private String getPosition(ChessPosition position) {
        switch (position.getColumn()) {
            case 1 -> {
                return "a" + Integer.toString(position.getRow());
            }
            case 2 -> {
                return "b" + Integer.toString(position.getRow());
            }
            case 3 -> {
                return "c" + Integer.toString(position.getRow());
            }
            case 4 -> {
                return "d" + Integer.toString(position.getRow());
            }
            case 5 -> {
                return "e" + Integer.toString(position.getRow());
            }
            case 6 -> {
                return "f" + Integer.toString(position.getRow());
            }
            case 7 -> {
                return "g" + Integer.toString(position.getRow());
            }
            case 8 -> {
                return "h" + Integer.toString(position.getRow());
            }
        }
        return null;
    }

    private void leave(Leave leave, Session session) {
        try {
            String username;
            // Getting the username
            try {
                username = auths.getAuth(leave.getAuthString()).username();
                if(username == null)
                    throw new DataAccessException("Invalid authToken");
            }
            catch (Exception e){
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Prepping to broadcast message to all clients connected to websocket
            Notification message = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left the game");
            var json = new Gson().toJson(message);

            // Remove from Database
            if(username.equals(games.getGame(Integer.toString(leave.getGameID())).whiteUsername())) {
                games.removePlayer(Integer.toString(leave.getGameID()), ChessGame.TeamColor.WHITE);
            }
            else if(username.equals(games.getGame(Integer.toString(leave.getGameID())).blackUsername())) {
                games.removePlayer(Integer.toString(leave.getGameID()), ChessGame.TeamColor.BLACK);
            }

            // Broadcast message to clients
            connections.broadcast(leave.getGameID(), json, leave.getAuthString());
            connections.removeSessionFromGame(leave.getGameID(), leave.getAuthString(), session);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void resign(Resign resign, Session session) {
        try {
            String username;
            // Getting the username
            try {
                username = auths.getAuth(resign.getAuthString()).username();
                if(username == null)
                    throw new DataAccessException("Invalid authToken");
            }
            catch (Exception e){
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Check if game is already over (you can't resign)
            ChessGame game = games.getGame(Integer.toString(resign.getGameID())).game();
            if(game.isGameOver()) {
                sendError(session, "Error: Game is already over.");
                return;
            }

            // Check if user is allowed to resign (has to be a player)
            if(!username.equals(games.getGame(Integer.toString(resign.getGameID())).whiteUsername()) &&
                    !username.equals(games.getGame(Integer.toString(resign.getGameID())).blackUsername())) {
                sendError(session, "Error: Observer is not allowed to resign.");
                return;
            }

            // Prepping to broadcast message to all clients connected to websocket
            Notification message = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has resigned from the game");
            var json = new Gson().toJson(message);

            // Update game status to become no longer playable
            game.setGameOver();
            games.updateGameState(Integer.toString(resign.getGameID()), game);

            // Broadcast message to clients and root client
            session.getRemote().sendString(json);
            connections.broadcast(resign.getGameID(), json, resign.getAuthString());
            connections.removeSessionFromGame(resign.getGameID(), resign.getAuthString(), session);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void joinPlayer(JoinPlayer joinPlayer, Session session) {
        try {
            if(joinPlayer.getPlayerColor() == null) {
                JoinObserver observer = new JoinObserver(joinPlayer.getAuthString(), joinPlayer.getGameID());
                joinObserver(observer, session);
                return;
            }
            String username;
            // Invalid authToken
            if(joinPlayer.getAuthString() == null) {
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Getting the username
            try {
                username = auths.getAuth(joinPlayer.getAuthString()).username();
                if(username == null)
                    throw new DataAccessException("Invalid authToken");
            }
            catch (Exception e){
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Check if gameID is valid
            try {
                GameData game = games.getGame(Integer.toString(joinPlayer.getGameID()));
                if(game == null)
                    throw new DataAccessException("Error: invalid game");
            }
            catch (DataAccessException e){
                sendError(session, "Error: Invalid game");
                return;
            }

            // Check if username matches (don't steal someone's invitation)
            if(joinPlayer.getPlayerColor() == ChessGame.TeamColor.BLACK) {
                if(!username.equals(games.getGame(Integer.toString(joinPlayer.getGameID())).blackUsername())) {
                    sendError(session, "Error: Username already taken");
                    return;
                }
            }
            else {
                if(!username.equals(games.getGame(Integer.toString(joinPlayer.getGameID())).whiteUsername())) {
                    sendError(session, "Error: Username already taken");
                    return;
                }
            }

            // Prepping to broadcast message to all clients connected to websocket
            Notification message = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has joined the game as " + joinPlayer.getPlayerColor().toString());
            var json = new Gson().toJson(message);

            // Broadcast message to clients
            connections.add(joinPlayer.getGameID(), joinPlayer.getAuthString(), session);
            connections.broadcast(joinPlayer.getGameID(), json, joinPlayer.getAuthString());
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, games.getGame(Integer.toString(joinPlayer.getGameID())).game());
            String res = new Gson().toJson(loadGame);
            session.getRemote().sendString(res);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendError(Session session, String errorMessage) throws IOException {
        ErrorClass error = new ErrorClass(ServerMessage.ServerMessageType.ERROR, errorMessage);
        String message = new Gson().toJson(error);
        session.getRemote().sendString(message);
    }

    public void joinObserver(JoinObserver joinObserver, Session session) {
        try {
            String username;
            // Invalid authToken
            if(joinObserver.getAuthString() == null) {
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Getting the username
            try {
                username = auths.getAuth(joinObserver.getAuthString()).username();
                if(username == null)
                    throw new DataAccessException("Invalid authToken");
            }
            catch (Exception e){
                sendError(session, "Error: Invalid authToken");
                return;
            }

            // Check if gameID is valid
            try {
                GameData game = games.getGame(Integer.toString(joinObserver.getGameID()));
                if(game == null)
                    throw new DataAccessException("Error: invalid game");
            }
            catch (DataAccessException e){
                sendError(session, "Error: Invalid game");
                return;
            }

            // Adding user to game using service
            // GameService service = new GameService(auths, games);
            // service.joinGame(joinRequest);

            // Prepping to broadcast message to all clients connected to websocket
            Notification message = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has joined the game as an observer.");
            var json = new Gson().toJson(message);

            // Broadcast message to clients
            // ConnectionManager broadcast = new ConnectionManager();
            connections.add(joinObserver.getGameID(), joinObserver.getAuthString(), session);
            connections.broadcast(joinObserver.getGameID(), json, joinObserver.getAuthString());
            LoadGame loadGame = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, games.getGame(Integer.toString(joinObserver.getGameID())).game());
            String res = new Gson().toJson(loadGame);
            session.getRemote().sendString(res);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*@OnWebSocketConnect
    public void onConnect(Session session) {}

    @OnWebSocketClose
    public void onClose(Session session) {}*/

    @OnWebSocketError
    public void onError(Session session, Throwable throwable) {}


}
