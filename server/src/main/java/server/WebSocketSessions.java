package server;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jetty.websocket.api.Session;

public class WebSocketSessions {

    // Implementation from Rodham Diagram
    private Map<Integer, Map<String, Session>> sessionMap;

    public WebSocketSessions() {
        sessionMap = new HashMap<>();
    }
    public void addSessionToGame(int gameID, String authToken, Session session) {
        Map<String, Session> map = new HashMap<>();
        map.put(authToken, session);
        sessionMap.put(gameID, map);
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) {
        Map<String, Session> map = new HashMap<>();
        map.put(authToken, session);
        sessionMap.remove(gameID, map);
    }

    public void removeSession(Session session) {
        for(Map.Entry<Integer, Map<String, Session>> set : sessionMap.entrySet()) {
            for(Map.Entry<String, Session> mySet : set.getValue().entrySet()) {
                if(mySet.getValue().equals(session))
                    sessionMap.remove(set.getKey(), set.getValue());
            }
        }
    }

    public Map<String, Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }

}