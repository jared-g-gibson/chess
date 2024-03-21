package response;

public class LoginResponse extends Response {
    private String authToken;
    private String username;
    public LoginResponse(String message, String authToken, String username) {
        super(message);
        this.authToken = authToken;
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public String getAuthToken() {
        return authToken;
    }
}
