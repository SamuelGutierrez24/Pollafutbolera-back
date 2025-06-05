package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.model.TypeNotification;

public interface TypeNotificationService {
    TypeNotification getOrCreate(String name, String description);
}
