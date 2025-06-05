package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.service.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        // Asigna valores válidos para las pruebas
        ReflectionTestUtils.setField(encryptionService, "secretKey", "1234567890123456"); // 16 chars para AES
        ReflectionTestUtils.setField(encryptionService, "algorithm", "AES");
    }

    @Test
    void encryptAndDecrypt_ShouldReturnOriginalValue() throws Exception {
        String original = "testData123";
        String encrypted = encryptionService.encrypt(original);
        assertNotNull(encrypted);
        assertNotEquals(original, encrypted);

        String decrypted = encryptionService.decrypt(encrypted);
        assertEquals(original, decrypted);
    }

    @Test
    void encrypt_ShouldThrowException_WhenDataIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> encryptionService.encrypt(null));
        assertTrue(exception.getMessage().contains("no pueden estar vacíos"));
    }

    @Test
    void encrypt_ShouldThrowException_WhenDataIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> encryptionService.encrypt(""));
        assertTrue(exception.getMessage().contains("no pueden estar vacíos"));
    }

    @Test
    void decrypt_ShouldThrowException_WhenDataIsInvalid() {
        assertThrows(Exception.class, () -> encryptionService.decrypt("invalidEncryptedData"));
    }
}