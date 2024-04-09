package exception;

import response.Response;

public class ResponseException extends Exception {
    private final int statusCode;

    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
