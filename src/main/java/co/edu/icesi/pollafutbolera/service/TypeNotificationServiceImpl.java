package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.model.TypeNotification;
import co.edu.icesi.pollafutbolera.repository.TypeNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TypeNotificationServiceImpl implements TypeNotificationService {
    private final TypeNotificationRepository typeNotificationRepository;

    @Override
    @Transactional
    public TypeNotification getOrCreate(String name, String description) {
        TypeNotification type = typeNotificationRepository.findByName(name);
        if (type == null) {
            type = new TypeNotification(name, description);
            type = typeNotificationRepository.save(type);
        }
        return type;
    }
}
