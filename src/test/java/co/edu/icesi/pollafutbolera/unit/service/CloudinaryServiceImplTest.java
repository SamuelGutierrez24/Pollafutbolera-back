package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.service.CloudinaryServiceImpl;
import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CloudinaryServiceImplTest {

    private CloudinaryServiceImpl cloudinaryService;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cloudinaryService = new CloudinaryServiceImpl();
        cloudinaryService.cloudinary = cloudinary;
    }

    @Test
    void uploadImage_ShouldReturnSecureUrl_WhenUploadIsSuccessful() throws IOException {
        // Arrange
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "https://example.com/image.jpg");
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(uploadResult);
        when(file.getBytes()).thenReturn(new byte[0]);

        // Act
        String result = cloudinaryService.uploadImage(file);

        // Assert
        assertEquals("https://example.com/image.jpg", result);
        verify(cloudinary.uploader(), times(1)).upload(any(byte[].class), any(Map.class));
    }

    @Test
    void uploadImage_ShouldThrowIOException_WhenUploadFails() throws IOException {
        // Arrange
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.upload(any(byte[].class), any(Map.class))).thenThrow(new IOException("Upload failed"));
        when(file.getBytes()).thenReturn(new byte[0]);

        // Act & Assert
        assertThrows(IOException.class, () -> cloudinaryService.uploadImage(file));
    }

    @Test
    void deleteImage_ShouldReturnTrue_WhenDeletionIsSuccessful() throws IOException {
        // Arrange
        Map<String, Object> deleteResult = new HashMap<>();
        deleteResult.put("result", "ok");
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(anyString(), any(Map.class))).thenReturn(deleteResult);

        // Act
        Boolean result = cloudinaryService.deleteImage("public_id");

        // Assert
        assertTrue(result);
        verify(cloudinary.uploader(), times(1)).destroy(anyString(), any(Map.class));
    }

    @Test
    void deleteImage_ShouldReturnFalse_WhenDeletionFails() throws IOException {
        // Arrange
        Map<String, Object> deleteResult = new HashMap<>();
        deleteResult.put("result", "not_found");
        when(cloudinary.uploader()).thenReturn(uploader);
        when(uploader.destroy(anyString(), any(Map.class))).thenReturn(deleteResult);

        // Act
        Boolean result = cloudinaryService.deleteImage("public_id");

        // Assert
        assertFalse(result);
        verify(cloudinary.uploader(), times(1)).destroy(anyString(), any(Map.class));
    }

    @Test
    void extractPublicId_ShouldReturnPublicId_WhenUrlIsValid() {
        // Arrange
        String url = "https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg";

        // Act
        String publicId = cloudinaryService.extractPublicId(url);

        // Assert
        assertEquals("sample", publicId);
    }

    @Test
    void extractPublicId_ShouldThrowRuntimeException_WhenUrlIsInvalid() {
        // Arrange
        String invalidUrl = "invalid_url";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cloudinaryService.extractPublicId(invalidUrl));
        assertEquals("No se pudo extraer el public_id desde la URL", exception.getMessage());
    }
}