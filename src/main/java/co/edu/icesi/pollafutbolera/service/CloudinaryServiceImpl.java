package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    public Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString(); // URL pública de la imagen
    }

    public Boolean deleteImage(String publicId) throws IOException {
        Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        return result.get("result").equals("ok"); // Verifica si la eliminación fue exitosa
    }

    public String extractPublicId(String url) {
        try {
            String[] parts = url.split("/");
            String filename = parts[parts.length - 1];
            return filename.substring(0, filename.lastIndexOf('.'));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo extraer el public_id desde la URL");
        }
    }

}
