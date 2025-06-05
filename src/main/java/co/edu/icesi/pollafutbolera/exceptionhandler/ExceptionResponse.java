package co.edu.icesi.pollafutbolera.exceptionhandler;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionResponse {

    USER_NOT_FOUND(1, "Nombre de usuario no encontrado", HttpStatus.NOT_FOUND, LogLevel.INFO),
    INCORRECT_PASSWORD(2, "Contraseña incorrecta", HttpStatus.BAD_REQUEST, LogLevel.INFO),
    BLANK_FIELD(3, "Uno o los dos campos están vacíos", HttpStatus.BAD_REQUEST, LogLevel.INFO),

    REWARD_NOT_FOUND(4, "Recompensa no encontrada", HttpStatus.NOT_FOUND, LogLevel.INFO),

    POLLA_NOT_FOUND(10, "Polla no encontrada", HttpStatus.NOT_FOUND, LogLevel.INFO),
    SUBPOLLA_NOT_FOUND(10, "Subpolla no encontrada", HttpStatus.NOT_FOUND, LogLevel.INFO),
    NO_SUBPOLLA_FOR_POLLA(10, "No hay subpollas para la polla especificada", HttpStatus.NOT_FOUND, LogLevel.INFO),
    FAILED_TO_CREATE_SUBPOLLA(10, "No se pudo crear la subpolla", HttpStatus.BAD_REQUEST, LogLevel.INFO),

    USER_NOT_IN_POLLA(11, "Usuario no encontrado en la polla especificada", HttpStatus.NOT_FOUND, LogLevel.INFO),

    UNIQUE_BET(12, "El usuario ya tiene un pronóstico", HttpStatus.CONFLICT, LogLevel.INFO),


    EMAILS_DUPLICATES(14, "Se han encontrado Correos duplicados", HttpStatus.BAD_REQUEST, LogLevel.INFO),

    TIME_BET(13, "El pronóstico no se puede hacer en este momento", HttpStatus.CONFLICT, LogLevel.INFO),

    NIT_EXISTS(77,"El Nit de la empresa ya esta registrado", HttpStatus.CONFLICT, LogLevel.INFO),
    TENANT_NOT_FOUND(20, "Tenant ID no encontrado", HttpStatus.NOT_FOUND, LogLevel.INFO),
    INVALID_TENANT_ID(21, "Formato inválido de ID de tenant", HttpStatus.BAD_REQUEST, LogLevel.INFO),
    TENANT_ACCESS_DENIED(22, "Acceso invalido para el tenant especificado", HttpStatus.FORBIDDEN, LogLevel.WARN),
    NO_TENANT_IN_REQUEST_HEADER(23, "El encabezado X-TenantId es obligatorio pero no fue proporcionado en la solicitud", HttpStatus.BAD_REQUEST, LogLevel.INFO),

    COMPANY_NOT_FOUND(30, "Compañía no encontrada", HttpStatus.NOT_FOUND, LogLevel.INFO),

    COMPANIES_NOT_FOUND(31, "No se encontraron compañías", HttpStatus.NOT_FOUND, LogLevel.INFO),

    INVALID_FILE_FORMAT(300, "Formato de archivo no soportado. Por favor suba un CSV o un XLSX/XLS.",HttpStatus.BAD_REQUEST, LogLevel.INFO),

    FILE_PROCESSING(310, "Ha ocurrido un error procesando el archivo", HttpStatus.INTERNAL_SERVER_ERROR , LogLevel.INFO ),

    MISSING_HEADER(32, "El archivo no contiene alguno de los encabezados esperados (nombre, apellido, cedula o mail) ", HttpStatus.BAD_REQUEST, LogLevel.INFO),

    EMPTY_FILE(33, "El archivo no contiene datos válidos", HttpStatus.BAD_REQUEST, LogLevel.INFO),

    COMPANY_MISMATCH(34, "La compañía del usuario no coincide con la de la polla", HttpStatus.FORBIDDEN, LogLevel.INFO),;

    private final int code;
    private final String message;
    private final HttpStatus responseStatus;
    private final LogLevel logLevel;
    
}
