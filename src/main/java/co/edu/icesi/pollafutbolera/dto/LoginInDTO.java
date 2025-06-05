package co.edu.icesi.pollafutbolera.dto;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginInDTO(@ApiModelProperty(required = true) @NotBlank String nickname,
                         @ApiModelProperty(required = true) @NotBlank String password) {
}