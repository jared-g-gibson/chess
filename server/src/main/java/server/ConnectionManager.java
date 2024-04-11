package server;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

    public void removeSession(javax.websocket.Session session) {
        for(ConcurrentHashMap.Entry<Integer, ConcurrentHashMap<String, Connection>> set : connections.entrySet()) {
            for(ConcurrentHashMap.Entry<String, Connection> mySet : set.getValue().entrySet()) {
                if(mySet.getValue().session.equals(session))
                    connections.remove(set.getKey(), set.getValue());
            }
        }
    }

    public ConcurrentHashMap<String, Connection> getSessionsForGame(int gameID) {
        return connections.get(gameID);
    }

    /*public final ConcurrentHashMap<Integer, ArrayList<ConcurrentHashMap<String, Connection>>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {
        ConcurrentHashMap<String, Connection> map = new ConcurrentHashMap<>();
        var connection = new Connection(authToken, session);
        map.put(authToken, connection);
        ArrayList<ConcurrentHashMap<String, Connection>> list = connections.get(gameID);
        if(list == null) {
            list = new ArrayList<>();
            list.add(map);
        }
        else {
            list.add(map);
        }
        connections.put(gameID, list);
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) {
        ConcurrentHashMap<String, Connection> map = new ConcurrentHashMap<>();
        var connection = new Connection(authToken, session);
        map.put(authToken, connection);
        connections.remove(gameID, map);
    }



    public void broadcast(int gameID, String message, String exceptThisAuthToken) throws IOException {
        var removeList = new ArrayList<Connection>();
        ArrayList<ConcurrentHashMap<String, Connection>> listOfConnections = getSessionsForGame(gameID);
        for(var l : listOfConnections) {
            for (var c : l.values()) {
                if (c.session.isOpen()) {
                    if (!c.authToken.equals(exceptThisAuthToken)) {
                        c.send(message);
                    }
                } else {
                    removeList.add(c);
                }
            }
        }


        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.authToken);
        }
    }

    public ArrayList<ConcurrentHashMap<String, Connection>> getSessionsForGame(int gameID) {
        return connections.get(gameID);
    }*/

}
