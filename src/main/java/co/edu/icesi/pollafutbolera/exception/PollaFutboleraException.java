package co.edu.icesi.pollafutbolera.exception;

import co.edu.icesi.pollafutbolera.enums.PollaFutboleraExceptionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PollaFutboleraException extends RuntimeException {

    private final PollaFutboleraExceptionType pfExceptionType;

    private static final Long serialVersionUID = 1L;

    public PollaFutboleraException(@NotNull PollaFutboleraExceptionType pfExceptionType) {
        super(pfExceptionType.getMessage());
        this.pfExceptionType = pfExceptionType;
    }
    
}
