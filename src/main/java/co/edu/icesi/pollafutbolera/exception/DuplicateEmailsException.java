package co.edu.icesi.pollafutbolera.exception;

public class DuplicateEmailsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DuplicateEmailsException(String message) {
        super(message);
    }

}
