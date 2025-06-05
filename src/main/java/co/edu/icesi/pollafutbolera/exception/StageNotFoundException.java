package co.edu.icesi.pollafutbolera.exception;

public class StageNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StageNotFoundException() {
        super("Stage Doesn't found");
    }
}