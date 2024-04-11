package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {
        var connection = new Connection(authToken, session);
        if(connections.get(gameID) == null) {
            ConcurrentHashMap<String, Connection> map = new ConcurrentHashMap<>();
            map.put(authToken, connection);
            connections.put(gameID, map);
        }
        else {
            ConcurrentHashMap<String, Connection> map = connections.get(gameID);
            map.put(authToken, connection);
            connections.put(gameID, map);
        }
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) {
        ConcurrentHashMap<String, Connection> map = connections.get(gameID);
        map.remove(authToken);
        connections.put(gameID, map);
    }



    public void broadcast(int gameID, String message, String exceptThisAuthToken) throws IOException {
        var removeList = new ArrayList<Connection>();
        ConcurrentHashMap<String, Connection> map = getSessionsForGame(gameID);
        for (var c : map.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(exceptThisAuthToken)) {
                    c.send(message);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }
    public ConcurrentHashMap<String, Connection> getSessionsForGame(int gameID) {
        return connections.get(gameID);
    }

}
