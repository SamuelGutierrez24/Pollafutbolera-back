package co.edu.icesi.pollafutbolera.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PollaFutboleraExceptionType {

    USER_NOT_FOUND(13, "USER NOT FOUND", ParameterNameConstants.USER_ID, HttpStatus.NOT_FOUND, LogLevel.INFO),
    INSECURE_PASSWORD(14, "INSECURE PASSWORD", ParameterNameConstants.USER_ID, HttpStatus.BAD_REQUEST, LogLevel.WARN),
    ALREADY_EXISTS(15, "ALREADY EXISTS", ParameterNameConstants.USER_ID, HttpStatus.BAD_REQUEST, LogLevel.WARN),
    CEDULA_FORMAT(16, "CEDULA LENGTH", ParameterNameConstants.USER_ID, HttpStatus.BAD_REQUEST, LogLevel.WARN),
    PHONE_FORMAT(17, "PHONE FORMAT", ParameterNameConstants.USER_ID, HttpStatus.BAD_REQUEST, LogLevel.WARN),
    EMAIL_FORMAT(18, "EMAIL FORMAT", ParameterNameConstants.USER_ID, HttpStatus.BAD_REQUEST, LogLevel.WARN),
    BAD_REQUEST(19, "BAD REQUEST", ParameterNameConstants.USER_ID, HttpStatus.BAD_REQUEST, LogLevel.WARN),
    ROLE_NOT_FOUND(20, "ROLE NOT FOUND", ParameterNameConstants.USER_ID, HttpStatus.NOT_FOUND, LogLevel.INFO),
    EMAIL_SEND_ERROR(21, "EMAIL SEND ERROR", ParameterNameConstants.USER_ID, HttpStatus.INTERNAL_SERVER_ERROR, LogLevel.ERROR),
    POLLA_NOT_FOUND(22, "POLLA NOT FOUND", ParameterNameConstants.USER_ID, HttpStatus.NOT_FOUND, LogLevel.INFO);

    private final int code;
    private final String message;
    private final String parameterName; 
    private final HttpStatus responseStatus; 
    private final LogLevel logLevel;

    private static class ParameterNameConstants {
        public static final String USER_ID = "userId";
    }
}