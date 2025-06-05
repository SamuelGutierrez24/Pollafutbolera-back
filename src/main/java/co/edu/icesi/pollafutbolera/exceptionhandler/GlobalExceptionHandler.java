package co.edu.icesi.pollafutbolera.exceptionhandler;

import java.util.Collections;
import java.util.Map;

import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(
            UsernameNotFoundException e) {
        return ResponseEntity.status(ExceptionResponse.USER_NOT_FOUND.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.USER_NOT_FOUND.getCode() + " - "
                        + ExceptionResponse.USER_NOT_FOUND.getMessage(), ExceptionResponse.USER_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Map<String, String>> handleIncorrectPasswordException(
            IncorrectPasswordException e) {
        return ResponseEntity.status(ExceptionResponse.INCORRECT_PASSWORD.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.INCORRECT_PASSWORD.getCode() + " - "
                        + ExceptionResponse.INCORRECT_PASSWORD.getMessage(), ExceptionResponse.INCORRECT_PASSWORD.getMessage()));
    }

    @ExceptionHandler(BlankFieldException.class)
    public ResponseEntity<Map<String, String>> handleBlankFiledException(
            BlankFieldException e) {
        return ResponseEntity.status(ExceptionResponse.BLANK_FIELD.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.BLANK_FIELD.getCode() + " - "
                        + ExceptionResponse.BLANK_FIELD.getMessage(), ExceptionResponse.BLANK_FIELD.getMessage()));
    }

    @ExceptionHandler(PollaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePollaNotFoundException(
            PollaNotFoundException e) {
        return ResponseEntity.status(ExceptionResponse.POLLA_NOT_FOUND.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.POLLA_NOT_FOUND.getCode() + " - "
                        + ExceptionResponse.POLLA_NOT_FOUND.getMessage(), ExceptionResponse.POLLA_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(UserNotInPollaException.class)
    public ResponseEntity<Map<String, String>> handleUserNotInPollaException(
            UserNotInPollaException e) {
        return ResponseEntity.status(ExceptionResponse.USER_NOT_IN_POLLA.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.USER_NOT_IN_POLLA.getCode() + " - "
                        + ExceptionResponse.USER_NOT_IN_POLLA.getMessage(), ExceptionResponse.USER_NOT_IN_POLLA.getMessage()));
    }

    @ExceptionHandler(UniqueBetException.class)
    public ResponseEntity<Map<String, String>> handleUniqueBetException(UniqueBetException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.UNIQUE_BET.getCode() + " - "
                        + ExceptionResponse.UNIQUE_BET.getMessage(), ExceptionResponse.UNIQUE_BET.getMessage()));
    }

    @ExceptionHandler(BetNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleBetNotAvailableException(BetNotAvailableException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.TIME_BET.getCode() + " - "
                        + ExceptionResponse.TIME_BET.getMessage(), ExceptionResponse.TIME_BET.getMessage()));
    }


    @ExceptionHandler(NitExistsException.class)
    public ResponseEntity<Map<String, String>> handleNitExistsExceptionException(NitExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.NIT_EXISTS.getCode() + " - "
                        + ExceptionResponse.NIT_EXISTS.getMessage(), ExceptionResponse.NIT_EXISTS.getMessage()));
    }

    @ExceptionHandler(TenantNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTenantNotFoundException(TenantNotFoundException e) {
        return ResponseEntity.status(ExceptionResponse.TENANT_NOT_FOUND.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.TENANT_NOT_FOUND.getCode() + " - "
                        + ExceptionResponse.TENANT_NOT_FOUND.getMessage(), e.getMessage()));
    }

    @ExceptionHandler(InvalidTenantIdException.class)
    public ResponseEntity<Map<String, String>> handleInvalidTenantIdException(InvalidTenantIdException e) {
        return ResponseEntity.status(ExceptionResponse.INVALID_TENANT_ID.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.INVALID_TENANT_ID.getCode() + " - "
                        + ExceptionResponse.INVALID_TENANT_ID.getMessage(), e.getMessage()));
    }

    @ExceptionHandler(TenantNotProvidedInRequestHeader.class)
    public ResponseEntity<Map<String, String>> handleTenantNotProvidedInRequestHeader(TenantNotProvidedInRequestHeader e) {
        return ResponseEntity.status(ExceptionResponse.INVALID_TENANT_ID.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.INVALID_TENANT_ID.getCode() + " - "
                        + ExceptionResponse.INVALID_TENANT_ID.getMessage(), e.getMessage()));
    }

    @ExceptionHandler(SubPollaNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSubPollaNotFoundException(
            SubPollaNotFoundException e) {
        return ResponseEntity.status(ExceptionResponse.SUBPOLLA_NOT_FOUND.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.SUBPOLLA_NOT_FOUND.getCode() + " - "
                        + ExceptionResponse.SUBPOLLA_NOT_FOUND.getMessage(), ExceptionResponse.SUBPOLLA_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(RewardNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRewardNotFoundException(
            RewardNotFoundException e) {
        return ResponseEntity.status(ExceptionResponse.REWARD_NOT_FOUND.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.REWARD_NOT_FOUND.getCode() + " - "
                        + ExceptionResponse.REWARD_NOT_FOUND.getMessage(), ExceptionResponse.REWARD_NOT_FOUND.getMessage()));
    }
    
    @ExceptionHandler(DuplicateEmailsException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmails(
    		DuplicateEmailsException e) {
    	return ResponseEntity.status(ExceptionResponse.EMAILS_DUPLICATES.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.EMAILS_DUPLICATES.getCode() + " - "
                        + ExceptionResponse.EMAILS_DUPLICATES.getMessage(), e.getMessage()));
    }
    
    @ExceptionHandler(NoSubPollasForPollasException.class)
    public ResponseEntity<Map<String, String>> handleNoSubPollasForPollasException(
            NoSubPollasForPollasException e) {
        return ResponseEntity.status(ExceptionResponse.NO_SUBPOLLA_FOR_POLLA.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.NO_SUBPOLLA_FOR_POLLA.getCode() + " - "
                        + ExceptionResponse.NO_SUBPOLLA_FOR_POLLA.getMessage(), ExceptionResponse.NO_SUBPOLLA_FOR_POLLA.getMessage()));
    }

    @ExceptionHandler(SubPollaCreationException.class)
    public ResponseEntity<Map<String, String>> handleFailedToCreateSubPollaException(
            SubPollaCreationException e) {
        return ResponseEntity.status(ExceptionResponse.FAILED_TO_CREATE_SUBPOLLA.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.FAILED_TO_CREATE_SUBPOLLA.getCode() + " - "
                        + ExceptionResponse.FAILED_TO_CREATE_SUBPOLLA.getMessage(), e.getMessage()));

    }

    @ExceptionHandler(CompanyNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCompanyNotFoundException(
            CompanyNotFoundException e) {
        return ResponseEntity.status(ExceptionResponse.COMPANY_NOT_FOUND.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.COMPANY_NOT_FOUND.getCode() + " - "
                        + ExceptionResponse.COMPANY_NOT_FOUND.getMessage(), ExceptionResponse.COMPANY_NOT_FOUND.getMessage()));
    }

    @ExceptionHandler(NoCompaniesFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoCompaniesFoundException(
            NoCompaniesFoundException e) {
        return ResponseEntity.status(ExceptionResponse.COMPANIES_NOT_FOUND.getResponseStatus())
                .body(Collections.singletonMap("Error code: " + ExceptionResponse.COMPANIES_NOT_FOUND.getCode() + " - "
                        + ExceptionResponse.COMPANIES_NOT_FOUND.getMessage(), ExceptionResponse.COMPANIES_NOT_FOUND.getMessage()));
        }
        
    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFileFormatException(
            InvalidFileFormatException e) {
        return ResponseEntity.status(ExceptionResponse.INVALID_FILE_FORMAT.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.INVALID_FILE_FORMAT.getCode() + " - "
                        + ExceptionResponse.INVALID_FILE_FORMAT.getMessage(), ExceptionResponse.INVALID_FILE_FORMAT.getMessage()));
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<Map<String, String>> handleFileProcessingException(
            FileProcessingException e) {
        return ResponseEntity.status(ExceptionResponse.FILE_PROCESSING.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.FILE_PROCESSING.getCode() + " - "
                        + ExceptionResponse.FILE_PROCESSING.getMessage(), ExceptionResponse.FILE_PROCESSING.getMessage()));
    }

    @ExceptionHandler(MissingHeaderException.class)
    public ResponseEntity<Map<String, String>> handleMissingHeaderException(
            MissingHeaderException e) {
        return ResponseEntity.status(ExceptionResponse.MISSING_HEADER.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.MISSING_HEADER.getCode() + " - "
                        + ExceptionResponse.MISSING_HEADER.getMessage(), ExceptionResponse.MISSING_HEADER.getMessage()));
    }

    @ExceptionHandler(EmptyFileException.class)
    public ResponseEntity<Map<String, String>> handleEmptyFileException(
            EmptyFileException e) {
        return ResponseEntity.status(ExceptionResponse.EMPTY_FILE.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.EMPTY_FILE.getCode() + " - "
                        + ExceptionResponse.EMPTY_FILE.getMessage(), ExceptionResponse.EMPTY_FILE.getMessage()));
    }

    @ExceptionHandler(CompanyMismatchException.class)
    public ResponseEntity<Map<String, String>> handleCompanyMismatchException(
            CompanyMismatchException e) {
        return ResponseEntity.status(ExceptionResponse.COMPANY_MISMATCH.getResponseStatus())
                .body(Collections.singletonMap("Error code :" + ExceptionResponse.COMPANY_MISMATCH.getCode() + " - "
                        + ExceptionResponse.COMPANY_MISMATCH.getMessage(), ExceptionResponse.COMPANY_MISMATCH.getMessage()));
    }
}
