package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.controller.ImageController;
import co.edu.icesi.pollafutbolera.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageControllerTest {

    private ImageController imageController;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imageController = new ImageController();
        imageController.cloudinaryService = cloudinaryService;
    }

    @Test
    void uploadImage_ShouldReturnImageUrl_WhenUploadIsSuccessful() throws Exception {
        // Arrange
        String expectedUrl = "https://example.com/image.jpg";
        when(cloudinaryService.uploadImage(file)).thenReturn(expectedUrl);

        // Act
        ResponseEntity<String> response = imageController.uploadImage(file);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedUrl, response.getBody());
        verify(cloudinaryService, times(1)).uploadImage(file);
    }

    @Test
    void uploadImage_ShouldReturnErrorMessage_WhenUploadFails() throws Exception {
        // Arrange
        when(cloudinaryService.uploadImage(file)).thenThrow(new RuntimeException("Upload failed"));

        // Act
        ResponseEntity<String> response = imageController.uploadImage(file);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Error al subir imagen"));
        verify(cloudinaryService, times(1)).uploadImage(file);
    }

    @Test
    void deleteImage_ShouldReturnTrue_WhenDeletionIsSuccessful() throws Exception {
        // Arrange
        String publicId = "samplePublicId";
        when(cloudinaryService.deleteImage(publicId)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = imageController.deleteImage(publicId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody());
        verify(cloudinaryService, times(1)).deleteImage(publicId);
    }

    @Test
    void deleteImage_ShouldReturnFalse_WhenDeletionFails() throws Exception {
        // Arrange
        String publicId = "samplePublicId";
        when(cloudinaryService.deleteImage(publicId)).thenThrow(new RuntimeException("Deletion failed"));

        // Act
        ResponseEntity<Boolean> response = imageController.deleteImage(publicId);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertFalse(response.getBody());
        verify(cloudinaryService, times(1)).deleteImage(publicId);
    }

    @Test
    void extractPublicId_ShouldReturnPublicId_WhenExtractionIsSuccessful() throws Exception {
        // Arrange
        String url = "https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg";
        String expectedPublicId = "sample";
        when(cloudinaryService.extractPublicId(url)).thenReturn(expectedPublicId);

        // Act
        ResponseEntity<String> response = imageController.extractPublicId(url);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedPublicId, response.getBody());
        verify(cloudinaryService, times(1)).extractPublicId(url);
    }

    @Test
    void extractPublicId_ShouldReturnErrorMessage_WhenExtractionFails() throws Exception {
        // Arrange
        String url = "invalid_url";
        when(cloudinaryService.extractPublicId(url)).thenThrow(new RuntimeException("Extraction failed"));

        // Act
        ResponseEntity<String> response = imageController.extractPublicId(url);

        // Assert
        assertNotNull(response);
        assertEquals(500, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Error al extraer el public_id"));
        verify(cloudinaryService, times(1)).extractPublicId(url);
    }
}