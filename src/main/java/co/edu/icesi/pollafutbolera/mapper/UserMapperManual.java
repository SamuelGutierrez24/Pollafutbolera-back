package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.model.User;

import java.util.List;

/**
 * Interface for manual User mapping operations
 */
public interface UserMapperManual {
    
    UserDTO toDTO(User user);
    
    List<UserDTO> toDTOList(List<User> users);
    
    User toEntity(UserDTO userDTO);
    
    void updateFromDTO(UserDTO userDTO, User user);
}
