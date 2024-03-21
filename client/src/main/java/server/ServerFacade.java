package server;

import com.google.gson.Gson;
import model.UserData;
import request.*;
import response.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String url) {
        this.serverUrl = url;
    }

    public ServerFacade(int port) {
        this.serverUrl = "http://localhost:" + port;
    }

    public String register(UserData data) {
        var path = "/user";
        var method = "POST";
        UserData req = data;
        return "";
    }

    public RegisterResponse registerUser(UserData data) throws Exception {
        // Set up Connection
        URI uri = new URI(serverUrl + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out the body
        //var body = Map.of("username", data.username(), "password", data.password(), "email", data.email());
        try(var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(data);
            outputStream.write(jsonBody.getBytes());
        }

        // Make Connection
        http.connect();

        // Output Response
        /*try(InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            RegisterResponse response = new Gson().fromJson(inputStreamReader, RegisterResponse.class);
            //System.out.println(response);
            return response;
        }*/

        if(http.getResponseCode() == 200) {
            try(InputStream respBody = http.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                RegisterResponse response = new Gson().fromJson(inputStreamReader, RegisterResponse.class);
                //System.out.println(response);
                return response;
            }
        }
        else {
            try(InputStream respBody = http.getErrorStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(respBody);
                RegisterResponse response = new Gson().fromJson(inputStreamReader, RegisterResponse.class);
                //System.out.println(response);
                return response;
            }
        }

    }

    public LoginResponse loginUser(LoginRequest req) throws Exception {
        // Set up Connection
        URI uri = new URI(serverUrl + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out the body
        var body = Map.of("username", req.username(), "password", req.password());
        try(var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make Connection
        http.connect();

        // Output Response
        try(InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            LoginResponse response = new Gson().fromJson(inputStreamReader, LoginResponse.class);
            //System.out.println(response);
            return response;
        }
    }

    public LogoutResponse logoutUser(LogoutRequest request) throws Exception {
        // Set up Connection
        URI uri = new URI(serverUrl + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        // Write out a header
        http.addRequestProperty("authorization", request.authToken());

        // Make Connection
        http.connect();

        // Output Response
        try(InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            LogoutResponse response = new Gson().fromJson(inputStreamReader, LogoutResponse.class);
            //System.out.println(response);
            return response;
        }
    }

    public CreateGameResponse createGame(CreateGameRequest req, String authToken) throws Exception {
        // Set up Connection
        URI uri = new URI(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        // Write out a header
        http.addRequestProperty("authorization", authToken);

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out the body
        var body = Map.of("gameName", req.gameName());
        try(var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make Connection
        http.connect();

        // Output Response
        try(InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            CreateGameResponse response = new Gson().fromJson(inputStreamReader, CreateGameResponse.class);
            //System.out.println(response);
            return response;
        }
    }

    public ListGamesResponse listGames(ListGamesRequest req) throws Exception {
        // Set up Connection
        URI uri = new URI(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("GET");

        // Write out a header
        http.addRequestProperty("authorization", req.authToken());

        // Make Connection
        http.connect();

        // Output Response
        try(InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            ListGamesResponse response = new Gson().fromJson(inputStreamReader, ListGamesResponse.class);
            //System.out.println(response);
            return response;
        }
    }

    public String joinGame(JoinRequest request) throws Exception {
        // Set up Connection
        URI uri = new URI(serverUrl + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("PUT");

        // Write out a header
        http.addRequestProperty("authorization", request.authToken());

        // Specify that we are going to write out data
        http.setDoOutput(true);

        // Write out the body
        /*Map<String, String> body;
        if(request.color() == null)
            body = Map.of("playerColor", null, "gameID", request.gameID());
        else
            body = Map.of("playerColor", request.color(), "gameID", request.gameID());*/
        try(var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(request);
            outputStream.write(jsonBody.getBytes());
        }

        // Make Connection
        http.connect();

        // Output Response
        try(InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            ListGamesResponse response = new Gson().fromJson(inputStreamReader, ListGamesResponse.class);
            //System.out.println(response);
            return "joined game successfully";
        }
    }

    public void clear() throws Exception {
        // Set up Connection
        URI uri = new URI(serverUrl + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");

        // Make Connection
        http.connect();
        if(http.getResponseCode() == 200)
            System.out.println("Deleted database successfully");
    }

}
