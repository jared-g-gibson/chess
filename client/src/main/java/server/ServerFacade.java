package server;

import com.google.gson.Gson;
import model.UserData;
import response.RegisterResponse;

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
        var body = Map.of("username", data.username(), "password", data.password(), "email", data.email());
        try(var outputStream = http.getOutputStream()) {
            var jsonBody = new Gson().toJson(body);
            outputStream.write(jsonBody.getBytes());
        }

        // Make Connection
        http.connect();

        // Output Response
        try(InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            RegisterResponse response = new Gson().fromJson(inputStreamReader, RegisterResponse.class);
            //System.out.println(response);
            return response;
        }
    }

}
