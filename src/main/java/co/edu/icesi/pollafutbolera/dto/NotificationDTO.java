package co.edu.icesi.pollafutbolera.dto;

import lombok.*;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO extends RepresentationModel<NotificationDTO> {
    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime timestamp;
    private Long typeId;
    private String typeName;
    private boolean read;
}
