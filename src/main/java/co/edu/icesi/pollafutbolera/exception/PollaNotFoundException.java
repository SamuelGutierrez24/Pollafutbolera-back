package co.edu.icesi.pollafutbolera.exception;

public class PollaNotFoundException extends RuntimeException{

    public PollaNotFoundException(String message) {
        super(message);
    }
    public PollaNotFoundException(){
        super();
    }
}
