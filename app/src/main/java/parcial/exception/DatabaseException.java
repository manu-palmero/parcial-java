package parcial.exception;

public class DatabaseException extends Exception {
    public DatabaseException (String msg) {
        super(msg);
    }
    public DatabaseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
