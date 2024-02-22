package response;

import java.util.UUID;

public class RegisterResponse extends Response {
    private String authToken;
    private String username;

    public RegisterResponse(String message, String authToken, String username) {
        super(message);
        this.authToken = authToken;
        this.username = username;
    }
}
