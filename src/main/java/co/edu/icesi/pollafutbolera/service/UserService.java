package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import co.edu.icesi.pollafutbolera.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    LoginOutDTO login (LoginInDTO loginInDTO);

    ResponseEntity<UserDTO> getUserById(Long id);

    Boolean nicknameExists(String nickname);

    Boolean changePassword(String token, String newPassword);

    ResponseEntity<List<UserDTO>> getAllUsers();

    PollaResponseEntity createUser(UserDTO userDTO);

    ResponseEntity<UpdatedUserDTO> updateCurrentUser(String toke, UserDTO userDTO);

    PollaResponseEntity updateUser(Long id, UserDTO userDTO);

    ResponseEntity<Void> deleteUser(Long id);

    ResponseEntity<List<UserDTO>> getUsersByPollaId(Long pollaId);
    ResponseEntity<List<UserDTO>> getUsersByDefaultCompany();

    ResponseEntity<Boolean> banUserFromPolla(Long userId, Long pollaId);

    ResponseEntity<NotificationSettingsDTO> updateNotificationSettings(Long userId, NotificationSettingsDTO settings);

    ResponseEntity<NotificationSettingsDTO> getNotificationSettings(Long userId);

    ResponseEntity<UserDTO> getCurrentUser(String token);


    ResponseEntity<PreloadUserValidationResultDTO> preloadUsers(Long pollaId, MultipartFile file, Long companyId);

    ResponseEntity<Page<PreloadUserDTO>> getPreloadedUsersByPollaId(Long pollaId, Pageable pageable);

    ResponseEntity<PreloadUserDTO> getPreloadedUsersById(String cedula);
}
