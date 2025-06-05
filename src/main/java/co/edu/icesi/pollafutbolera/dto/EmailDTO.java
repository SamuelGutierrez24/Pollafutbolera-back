package co.edu.icesi.pollafutbolera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor 
public class EmailDTO {
    private String id;
    private String emailaddressee;
    private String subject;
    private String username;
    private String cedula;
}