package co.edu.icesi.pollafutbolera.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

@Builder
public record GroupDTO(
        Long id,
        @NotNull
        @Size(max = 255)
        String groupName,
        Long tournamentId,
        List<GroupTeamDTO> teams,
        List<MatchDTO> matches,
        Long firstWinnerTeamId,
        Long secondWinnerTeamId
) implements Serializable {
}
