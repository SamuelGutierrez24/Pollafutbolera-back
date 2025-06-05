-- Tabla de tipos de notificación
CREATE TABLE type_notification (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200)
);

-- Tabla de notificaciones
CREATE TABLE notification (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT NOT NULL,
    content VARCHAR(500) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    type_id BIGINT NOT NULL,
    read BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_notification_type FOREIGN KEY (type_id) REFERENCES type_notification(id)
);
