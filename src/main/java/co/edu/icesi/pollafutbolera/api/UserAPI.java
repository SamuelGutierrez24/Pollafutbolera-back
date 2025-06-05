package co.edu.icesi.pollafutbolera.api;


import co.edu.icesi.pollafutbolera.dto.*;
import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@RequestMapping("/user")
public interface UserAPI {
    @PostMapping("/authenticate")
    @PreAuthorize("permitAll()")
    ResponseEntity<LoginOutDTO> login(@RequestBody LoginInDTO loginInDTO);

    @PostMapping("/changePassword")
    @PreAuthorize("permitAll()")
    ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request);

    @GetMapping("/polla/{pollaId}")
    ResponseEntity<List<UserDTO>> getUsersByPollaId(@PathVariable Long pollaId);

    @PutMapping("/polla/{pollaId}/ban/user/{userId}")
    ResponseEntity<Boolean> banUserFromPolla(@PathVariable Long userId, @PathVariable Long pollaId);

    @GetMapping("/default-company")
    ResponseEntity<List<UserDTO>> getUsersByDefaultCompany();

    @GetMapping("/{id}")
    ResponseEntity<UserDTO> getUserById(@PathVariable Long id);

    @GetMapping
    ResponseEntity<List<UserDTO>> getAllUsers();

    @PostMapping("/create")
    @PreAuthorize("permitAll()")
    PollaResponseEntity createUser(@RequestBody UserDTO userDTO);

    @PutMapping("/{id}")
    PollaResponseEntity updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);

    @PutMapping("/{id}/notifications")
    ResponseEntity<NotificationSettingsDTO> updateNotificationSettings(@PathVariable Long id, @RequestBody NotificationSettingsDTO settings);

    @GetMapping("/{id}/notifications")
    ResponseEntity<NotificationSettingsDTO> getNotificationSettings(@PathVariable Long id);

    @GetMapping("/nickname/exists/{nickname}")
    ResponseEntity<Boolean> nicknameExists(@PathVariable String nickname);

    @PutMapping("/update")
    ResponseEntity<UpdatedUserDTO> updateCurrentUser(@RequestBody UserDTO userDTO, @RequestHeader("Authorization") String token);

    @GetMapping("/profile")
    ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String token);

    @PostMapping("/preload/polla/{pollaId}")
    ResponseEntity<PreloadUserValidationResultDTO> preloadUsers(@PathVariable Long pollaId, @RequestBody MultipartFile file,   @RequestHeader("X-Company-Id") Long companyId);

    @GetMapping("/preloaded/polla/{pollaId}")
    ResponseEntity<Page<PreloadUserDTO>> getPreloadedUsersByPollaId(@PathVariable Long pollaId, Pageable pageable);

    @GetMapping("/preloadedusers/{id}")
    ResponseEntity<PreloadUserDTO> getPreloadedUsersById(@PathVariable String id);

}