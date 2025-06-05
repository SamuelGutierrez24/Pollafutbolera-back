package co.edu.icesi.pollafutbolera.unit.controller;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.controller.UserController;
import co.edu.icesi.pollafutbolera.dto.*;
import co.edu.icesi.pollafutbolera.exception.EmptyFileException;
import co.edu.icesi.pollafutbolera.service.UserService;
import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import co.edu.icesi.pollafutbolera.util.UserUtil;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
class UserControllerTests {

    @Mock
    private UserService userService;

    private Long pollaId;
    private Long userId;
    private List<UserDTO> userList;

    @InjectMocks
    private UserController userController;

    private UserDTO testUserDTO;

    private LoginInDTO loginInDTO;

    private LoginOutDTO loginOutDTO;

    private UpdatedUserDTO updatedUserDTO;

    private MultipartFile validCsvFile;
    private MultipartFile invalidFile;

    private NotificationSettingsDTO testNotificationSettings;

    @BeforeEach
    void setUp() {
        testUserDTO = UserDTO.builder()
                .id(1L)
                .nickname("testuser")
                .password("encodedPassword1*")
                .name("Test User")
                .cedula("123456789")
                .company(1L)
                .photo("testPhoto")
                .mail("test@mail.com")
                .phoneNumber("1234567890")
                .role(1L)
                .extraInfo("extraInfo")
                .build();
        loginInDTO = LoginInDTO.builder()
                .nickname("admin")
                .password("admin")
                .build();

        loginOutDTO = LoginOutDTO.builder()
                .tokenType("Bearer")
                .accessToken("testToken")
                .build();

        updatedUserDTO = UpdatedUserDTO.builder()
                .id(1L)
                .accessToken("newToken")
                .build();

        pollaId = 1L;
        userId = 1L;

        userList = List.of(UserUtil.userDTO(), UserUtil.userDTO2());

        testNotificationSettings = NotificationSettingsDTO.builder()
                .userId(1L)
                .enabledEmail(true)
                .enabledSMS(true)
                .enabledWhatsapp(true)
                .build();

        validCsvFile = new MockMultipartFile("test.csv", "test.csv", "text/csv", "Nombre;Apellido;Cedula;mail\nJuan;Perez;123456;juan@example.com".getBytes());
        invalidFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", "Texto sin formato válido".getBytes());
    }

    @Test
    void getUserById_ShouldReturnUserDTO_WhenUserExists() {
        when(userService.getUserById(1L)).thenReturn(new ResponseEntity<>(testUserDTO, HttpStatus.OK));

        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUserDTO, response.getBody());
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userService.getUserById(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDTOs() {
        List<UserDTO> userDTOs = List.of(testUserDTO);

        when(userService.getAllUsers()).thenReturn(new ResponseEntity<>(userDTOs, HttpStatus.OK));

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOs, response.getBody());
    }

    @Test
    void createUser_ShouldReturnCreatedUserDTO() {
        PollaResponseEntity pollaResponseEntity = new PollaResponseEntity(HttpStatus.CREATED, testUserDTO);
        when(userService.createUser(testUserDTO)).thenReturn(pollaResponseEntity);

        PollaResponseEntity response = userController.createUser(testUserDTO);

        assertEquals(HttpStatus.CREATED, response.status());
        assertEquals(testUserDTO, response.body());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDTO_WhenUserExists() {
        PollaResponseEntity pollaResponseEntity = new PollaResponseEntity(HttpStatus.OK, testUserDTO);
        when(userService.updateUser(1L, testUserDTO)).thenReturn(pollaResponseEntity);

        PollaResponseEntity response = userController.updateUser(1L, testUserDTO);

        assertEquals(HttpStatus.OK, response.status());
        assertEquals(testUserDTO, response.body());
    }

    @Test
    void updateUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        PollaResponseEntity pollaResponseEntity = new PollaResponseEntity(HttpStatus.NOT_FOUND, null);
        when(userService.updateUser(1L, testUserDTO)).thenReturn(pollaResponseEntity);

        PollaResponseEntity response = userController.updateUser(1L, testUserDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.status());
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserExists() {
        when(userService.deleteUser(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userService.deleteUser(1L)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getUsersByPollaId_ShouldReturnUsersList() {
        // Arrange
        when(userService.getUsersByPollaId(pollaId)).thenReturn(ResponseEntity.ok(userList));

        // Act
        ResponseEntity<List<UserDTO>> response = userController.getUsersByPollaId(pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
        verify(userService, times(1)).getUsersByPollaId(pollaId);
    }

    @Test
    void getUsersByPollaId_ShouldReturnNoContentWhenNoUsersFound() {
        // Arrange
        when(userService.getUsersByPollaId(pollaId)).thenReturn(ResponseEntity.noContent().build());

        // Act
        ResponseEntity<List<UserDTO>> response = userController.getUsersByPollaId(pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getUsersByPollaId(pollaId);
    }

    @Test
    void banUserFromPolla_ShouldReturnTrueOnSuccess() {
        // Arrange
        when(userService.banUserFromPolla(userId, pollaId)).thenReturn(ResponseEntity.ok(true));

        // Act
        ResponseEntity<Boolean> response = userController.banUserFromPolla(userId, pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(userService, times(1)).banUserFromPolla(userId, pollaId);
    }

    @Test
    void banUserFromPolla_ShouldReturnFalseOnFailure() {
        // Arrange
        when(userService.banUserFromPolla(userId, pollaId)).thenReturn(ResponseEntity.ok(false));

        // Act
        ResponseEntity<Boolean> response = userController.banUserFromPolla(userId, pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
        verify(userService, times(1)).banUserFromPolla(userId, pollaId);
    }

    @Test
    void updateNotificationSettings_ShouldReturnUpdatedSettings() {
        when(userService.updateNotificationSettings(userId, testNotificationSettings))
                .thenReturn(ResponseEntity.ok(testNotificationSettings));

        ResponseEntity<NotificationSettingsDTO> response = userController.updateNotificationSettings(userId, testNotificationSettings);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testNotificationSettings, response.getBody());
        verify(userService, times(1)).updateNotificationSettings(userId, testNotificationSettings);
    }

    @Test
    void updateNotificationSettings_ShouldReturnNotFoundWhenUserDoesNotExist() {
        when(userService.updateNotificationSettings(userId, testNotificationSettings))
                .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<NotificationSettingsDTO> response = userController.updateNotificationSettings(userId, testNotificationSettings);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).updateNotificationSettings(userId, testNotificationSettings);
    }

    @Test
    void getNotificationSettings_ShouldReturnSettings() {
        when(userService.getNotificationSettings(userId))
                .thenReturn(ResponseEntity.ok(testNotificationSettings));

        ResponseEntity<NotificationSettingsDTO> response = userController.getNotificationSettings(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testNotificationSettings, response.getBody());
        verify(userService, times(1)).getNotificationSettings(userId);
    }

    @Test
    void getNotificationSettings_ShouldReturnNotFoundWhenUserDoesNotExist() {
        when(userService.getNotificationSettings(userId))
                .thenReturn(ResponseEntity.notFound().build());

        ResponseEntity<NotificationSettingsDTO> response = userController.getNotificationSettings(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getNotificationSettings(userId);
    }

    @Test
    void login_ShouldReturnOkResponseWithLoginOutDTO() {
        when(userService.login(loginInDTO)).thenReturn(loginOutDTO);

        ResponseEntity<LoginOutDTO> response = userController.login(loginInDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(loginOutDTO, response.getBody());
    }

    @Test
    void login_ShouldReturnCorrectToken() {
        when(userService.login(loginInDTO)).thenReturn(loginOutDTO);

        ResponseEntity<LoginOutDTO> response = userController.login(loginInDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testToken", response.getBody().accessToken());
    }

    @Test
    void login_ShouldReturnCorrectLoginOutDTO() {
        when(userService.login(loginInDTO)).thenReturn(loginOutDTO);

        ResponseEntity<LoginOutDTO> response = userController.login(loginInDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(loginOutDTO, response.getBody());
    }

    @Test
    void updateCurrentUser_ShouldReturnUpdatedUserDTO_WhenValidToken() {
        // Arrange
        String authHeader = "Bearer valid.jwt.token";
        String jwtToken = "valid.jwt.token";
        when(userService.updateCurrentUser(jwtToken, testUserDTO)).thenReturn(ResponseEntity.ok(updatedUserDTO));

        // Act
        ResponseEntity<UpdatedUserDTO> response = userController.updateCurrentUser(testUserDTO, authHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newToken", response.getBody().accessToken());
        verify(userService, times(1)).updateCurrentUser(jwtToken, testUserDTO);
    }

    @Test
    void updateCurrentUser_ShouldReturnBadRequest_WhenTokenIsInvalid() {
        // Arrange
        String authHeader = "InvalidToken";

        // Act
        ResponseEntity<UpdatedUserDTO> response = userController.updateCurrentUser(testUserDTO, authHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, never()).updateCurrentUser(anyString(), any(UserDTO.class));
    }

    @Test
    void updateCurrentUser_ShouldReturnBadRequest_WhenTokenIsMissing() {
        // Arrange
        String authHeader = null;

        // Act
        ResponseEntity<UpdatedUserDTO> response = userController.updateCurrentUser(testUserDTO, authHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, never()).updateCurrentUser(anyString(), any(UserDTO.class));
    }

    @Test
    void getCurrentUser_ShouldReturnUserDTO_WhenValidToken() {
        // Arrange
        String authHeader = "Bearer valid.jwt.token";
        String jwtToken = "valid.jwt.token";
        when(userService.getCurrentUser(jwtToken)).thenReturn(ResponseEntity.ok(testUserDTO));

        // Act
        ResponseEntity<UserDTO> response = userController.getCurrentUser(authHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUserDTO, response.getBody());
        verify(userService, times(1)).getCurrentUser(jwtToken);
    }

    @Test
    void getCurrentUser_ShouldReturnBadRequest_WhenTokenIsInvalid() {
        // Arrange
        String authHeader = "InvalidToken";

        // Act
        ResponseEntity<UserDTO> response = userController.getCurrentUser(authHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, never()).getCurrentUser(anyString());
    }

    @Test
    void getCurrentUser_ShouldReturnBadRequest_WhenTokenIsMissing() {
        // Arrange
        String authHeader = null;

        // Act
        ResponseEntity<UserDTO> response = userController.getCurrentUser(authHeader);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, never()).getCurrentUser(anyString());
    }

    @Test
    void preloadUsers_ShouldReturnOk_WhenFileIsValid() {
        Long pollaId = 1L;
        // Datos esperados para la respuesta
        List<PreloadUserDTO> validUsers = List.of(new PreloadUserDTO(1L,"123456", "", "Juan", "Perez","311222333" ,"juan.perez@example.com", "", pollaId));
        List<PreloadUserErrorDTO> invalidUsers = List.of(new PreloadUserErrorDTO(1, "123457", "Pedro", "Lopez", "", "Faltan campos obligatorios"));

        PreloadUserValidationResultDTO expectedResponse = PreloadUserValidationResultDTO.builder()
                .validUsers(validUsers)
                .invalidUsers(invalidUsers)
                .build();

        when(userService.preloadUsers(eq(1L), any(), eq(100L))).thenReturn(ResponseEntity.ok(expectedResponse));

        ResponseEntity<PreloadUserValidationResultDTO> response = userController.preloadUsers(1L, validCsvFile, 100L);

        // Verificaciones
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(userService, times(1)).preloadUsers(eq(1L), any(), eq(100L));
    }

    @Test
    void getPreloadedUsersByPollaId_ShouldReturnUserList() {
        // Arrange
        Long pollaId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        PreloadUserDTO user1 = new PreloadUserDTO(1L, "1234567", "", "Juan", "Pérez", "", "juanperez@gmail.com", null, pollaId);
        PreloadUserDTO user2 = new PreloadUserDTO(2L, "1234567", "", "Maria", "Gómez", "", "mariagomez@gmail.com", null, pollaId);
        Page<PreloadUserDTO> dtoPage = new PageImpl<>(List.of(user1, user2), pageable, 2);

        when(userService.getPreloadedUsersByPollaId(pollaId, pageable)).thenReturn(ResponseEntity.ok(dtoPage));

        // Act
        ResponseEntity<Page<PreloadUserDTO>> response = userController.getPreloadedUsersByPollaId(pollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("Juan", response.getBody().getContent().get(0).name());
        assertEquals("Maria", response.getBody().getContent().get(1).name());
        verify(userService, times(1)).getPreloadedUsersByPollaId(pollaId, pageable);
    }

    @Test
    void getPreloadedUsersByPollaId_ShouldReturnNoContent() {
        // Arrange
        Long pollaId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(userService.getPreloadedUsersByPollaId(pollaId, pageable)).thenReturn(ResponseEntity.noContent().build());

        // Act
        ResponseEntity<Page<PreloadUserDTO>> response = userController.getPreloadedUsersByPollaId(pollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).getPreloadedUsersByPollaId(pollaId, pageable);
    }



}