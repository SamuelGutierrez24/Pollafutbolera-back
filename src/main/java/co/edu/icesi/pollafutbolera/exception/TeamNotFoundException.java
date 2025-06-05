package co.edu.icesi.pollafutbolera.exception;

public class TeamNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TeamNotFoundException() {
        super("Team Doesn't found");
    }
}