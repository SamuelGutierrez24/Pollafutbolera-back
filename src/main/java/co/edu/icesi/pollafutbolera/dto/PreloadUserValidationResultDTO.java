package co.edu.icesi.pollafutbolera.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreloadUserValidationResultDTO {

    private List<PreloadUserDTO> validUsers;
    private List<PreloadUserErrorDTO> invalidUsers;

}
