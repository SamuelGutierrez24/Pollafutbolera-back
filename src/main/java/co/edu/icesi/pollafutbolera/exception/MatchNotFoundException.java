package co.edu.icesi.pollafutbolera.exception;

public class MatchNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MatchNotFoundException() {
        super("Match Doesn't found");
    }
}