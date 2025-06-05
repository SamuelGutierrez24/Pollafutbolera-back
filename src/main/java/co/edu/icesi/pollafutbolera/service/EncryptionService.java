package co.edu.icesi.pollafutbolera.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EncryptionService {
    
    @Value("${encript.secret}")
    private String secretKey;

    @Value("${encript.algorithm}")
    private String algorithm;

    /**
     * Encripta un string y lo devuelve en Base64 URL-safe
     */
    public String encrypt(String data) throws Exception {
        // Validaciones
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Los datos a encriptar no pueden estar vacíos");
        }

        // 1. Configurar la clave
        SecretKeySpec keySpec = new SecretKeySpec(
            secretKey.getBytes(StandardCharsets.UTF_8), 
            algorithm
        );

        // 2. Configurar el cifrado
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // 3. Encriptar
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        // 4. Convertir a Base64 normal
        String base64Encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
        
        // 5. Convertir a Base64 URL-safe (para usar en URLs)
        String urlSafeEncrypted = base64Encrypted
            .replace('+', '-')
            .replace('/', '_')
            .replace("=", "");

        return urlSafeEncrypted;
    }

    /**
     * Desencripta un string en Base64 (normal o URL-safe)
     */
    public String decrypt(String encryptedData) throws Exception {
        // 1. Convertir de URL-safe a Base64 normal si es necesario
        String base64Data = encryptedData
            .replace('-', '+')
            .replace('_', '/');

        // Añadir padding '=' si es necesario
        switch (base64Data.length() % 4) {
            case 2: base64Data += "=="; break;
            case 3: base64Data += "="; break;
        }

        // 2. Decodificar Base64
        byte[] encryptedBytes = Base64.getDecoder().decode(base64Data);

        // 3. Configurar el cifrado
        SecretKeySpec keySpec = new SecretKeySpec(
            secretKey.getBytes(StandardCharsets.UTF_8), 
            algorithm
        );
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        // 4. Desencriptar
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}