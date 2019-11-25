package exception;

public class AuthException extends RuntimeException {

    private int errorCode;
    private String errorMessage;

    public AuthException(int errorCode, String erroMessage) {
        super(erroMessage);
        this.errorCode = errorCode;
        this.errorMessage = erroMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
