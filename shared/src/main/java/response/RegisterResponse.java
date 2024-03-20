package response;

public class RegisterResponse extends Response {
    private String authToken;
    private String username;

    public RegisterResponse(String message, String authToken, String username) {
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

    public String toString() {
        return "authToken: " + this.authToken +
                "\nusername: " + this.username;
    }
}
