package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.*;
import co.edu.icesi.pollafutbolera.enums.PollaFutboleraExceptionType;
import co.edu.icesi.pollafutbolera.enums.UserPollaState;
import co.edu.icesi.pollafutbolera.exception.*;
import co.edu.icesi.pollafutbolera.mapper.UserMapper;
import co.edu.icesi.pollafutbolera.model.*;
import co.edu.icesi.pollafutbolera.repository.RoleRepository;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.PreloadedUserRepository;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.service.EmailServiceImpl;
import co.edu.icesi.pollafutbolera.repository.UserScoresPollaRepository;
import co.edu.icesi.pollafutbolera.service.UserScoresPollaService;
import co.edu.icesi.pollafutbolera.service.UserServiceImpl;
import co.edu.icesi.pollafutbolera.util.UserUtil;
import co.edu.icesi.pollafutbolera.service.JwtService;
import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import jakarta.mail.MessagingException;
import co.edu.icesi.pollafutbolera.utils.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailServiceImpl emailService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserServiceImpl userServiceSpy;

    @Mock
    private FileUtils fileUtils;
    @Mock
    private PollaRepository pollaRepository;
    @Mock
    private UserScoresPollaRepository userScoresPollaRepository;
    @Mock
    private PreloadedUserRepository preloadedUserRepository;

    @Mock
    private UserScoresPollaService userScoresPollaService;
    private User testUser;
    private UserDTO testUserDTO;
    private UserDTO testExceptionUserDTO;
    private LoginInDTO loginInDTO;
    private List<PreloadUserDTO> preloadUserDTOs;


    @BeforeEach
    void setUp() {
        userServiceSpy = spy(userService);

        testUser = User.builder()
                .id(1L)
                .nickname("testuser")
                .password("encodedPassword1*")
                .name("Test User")
                .cedula("123456789")
                .photo("testPhoto")
                .mail("test@mail.com")
                .phoneNumber("+571234567890")
                .notificationsEmailEnabled(true)
                .notificationsSMSEnabled(true)
                .notificationsWhatsappEnabled(true)
                .build();
        Company company = new Company();
        company.setId(1L);
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");

        testUser = new User();
        testUser.setId(1L);
        testUser.setNickname("testuser");
        testUser.setPassword("encodedPassword");
        testUser.setName("Test User");
        testUser.setCedula("123456789");
        testUser.setRole(role);
        testUser.setCompany(company);
        testUser.setNotificationsEmailEnabled(true);
        testUser.setNotificationsSMSEnabled(true);
        testUser.setNotificationsWhatsappEnabled(true);


        testUserDTO = UserDTO.builder()
                .id(1L)
                .nickname("testuser")
                .password("encodedPassword1*")
                .name("Test User")
                .cedula("123456789")
                .company(1L)
                .photo("testPhoto")
                .mail("test@mail.com")
                .phoneNumber("+571234567890")
                .notificationsEmailEnabled(true)
                .notificationsSMSEnabled(true)
                .notificationsWhatsappEnabled(true)
                .build();

        testExceptionUserDTO = UserDTO.builder()
                .id(1L)
                .nickname("testuser")
                .password("insecurePassword")
                .name("Test User")
                .cedula("123456789")
                .company(1L)
                .photo("testPhoto")
                .mail("")
                .phoneNumber("+571234567890")
                .notificationsEmailEnabled(true)
                .notificationsSMSEnabled(true)
                .notificationsWhatsappEnabled(true)
                .build();

        loginInDTO = new LoginInDTO("testuser", "password123");
    }



    @Test
    void login_ShouldReturnLoginOutDTO_WhenCredentialsAreValid() {
        // Arrange
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn("valid.jwt.token");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("testuser", "password123"));

        // Act
        LoginOutDTO result = userService.login(loginInDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Bearer", result.tokenType());
        assertEquals("valid.jwt.token", result.accessToken());

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByNickname("testuser");
        verify(jwtService, times(1)).generateToken(testUser);
    }

    @Test
    void login_ShouldThrowUsernameNotFoundException_WhenUserDoesNotExist() {
        LoginInDTO loginInDTO = new LoginInDTO("testuser", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.login(loginInDTO));
    }

    @Test
    void login_ShouldThrowUsernameNotFoundException_WhenUserNotFound() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("testuser", "password123"));
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.login(loginInDTO));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByNickname("testuser");
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void getUserById_ShouldReturnUserDTO_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        ResponseEntity<UserDTO> response = userService.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUserDTO, response.getBody());
    }

    @Test
    void getUserById_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userService.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDTOs() {
        List<User> users = List.of(testUser);
        List<UserDTO> userDTOs = List.of(testUserDTO);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTOList(users)).thenReturn(userDTOs);

        ResponseEntity<List<UserDTO>> response = userService.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTOs, response.getBody());
    }

    @Test
    void createUser_ShouldReturnEmailError() {
        // Arrange
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ADMIN");

        Role userRole = new Role();
        userRole.setId(2L);
        userRole.setName("USER");

        when(userMapper.toEntity(testUserDTO)).thenReturn(testUser);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);
        try {
            doNothing().when(emailService).sendEmailConfirmRegistration(anyString(), anyString());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        // Act
        PollaResponseEntity response = userService.createUser(testUserDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.status());
        assertEquals(PollaFutboleraExceptionType.EMAIL_SEND_ERROR, response.body());
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenValidationFails() {
        doThrow(new PollaFutboleraException(PollaFutboleraExceptionType.BAD_REQUEST)).when(userServiceSpy).validateInput(testUserDTO);

        PollaResponseEntity response = userServiceSpy.createUser(testUserDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.status());
        assertEquals(PollaFutboleraExceptionType.BAD_REQUEST, response.body());
    }

    @Test
    void updateUser_ShouldReturnUpdatedUserDTO_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        PollaResponseEntity response = userService.updateUser(1L, testUserDTO);

        assertEquals(HttpStatus.OK, response.status());
        assertEquals(testUserDTO, response.body());
    }

    @Test
    void updateUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        PollaResponseEntity response = userService.updateUser(1L, testUserDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.status());
    }

    @Test
    void updateUser_ShouldReturnBadRequest_WhenValidationFails() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        PollaResponseEntity response = userService.updateUser(1L, testExceptionUserDTO);
        assertEquals(HttpStatus.BAD_REQUEST, response.status());
        assertEquals(PollaFutboleraExceptionType.INSECURE_PASSWORD, response.body());
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        ResponseEntity<Void> response = userService.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteUser_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = userService.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void login_ShouldPropagateAuthenticationException_WhenAuthenticationFails() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new IncorrectPasswordException());

        // Act & Assert
        assertThrows(IncorrectPasswordException.class, () -> userService.login(loginInDTO));

        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findByNickname(any());
        verify(jwtService, never()).generateToken(any());
    }



    @Test
    void changePassword_WithValidToken_ShouldChangePasswordAndReturnTrue() {
        // Arrange
        String token = "valid.jwt.token";
        String newPassword = "newSecurePassword";

        String previusPassword = testUser.getPassword();

        when(jwtService.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        boolean result = userService.changePassword(token, newPassword);

        // Assert
        assertTrue(result);
        assertNotEquals(previusPassword, testUser.getPassword());
        verify(jwtService).extractUsername(token);
        verify(userRepository).findByNickname("testuser");
        verify(userRepository).save(testUser);
    }

    @Test
    void changePassword_WithInvalidToken_ShouldReturnFalse() {
        // Arrange
        String token = "invalid.jwt.token";
        String newPassword = "newSecurePassword";

        when(jwtService.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.empty());

        // Act
        boolean result = userService.changePassword(token, newPassword);

        // Assert
        assertFalse(result);
        verify(jwtService).extractUsername(token);
        verify(userRepository).findByNickname("testuser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_WithNullToken_ShouldHandleGracefully() {
        // Arrange
        String newPassword = "newSecurePassword";
        // Suponemos que el JwtService lanzará una excepción al extraer el username de un token nulo
        when(jwtService.extractUsername(null)).thenThrow(new RuntimeException("Invalid token"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                userService.changePassword(null, newPassword)
        );

        assertEquals("Invalid token", exception.getMessage());
        verify(jwtService).extractUsername(null);
        verifyNoInteractions(userRepository);
    }

    @Test
    void changePassword_EnsurePasswordIsStoredAsProvided() {
        // Arrange
        String token = "valid.jwt.token";
        String newPassword = "newSecurePassword";

        when(jwtService.extractUsername(token)).thenReturn("testuser");
        when(userRepository.findByNickname("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.changePassword(token, newPassword);

        // Assert - capturamos el usuario para verificar que se guardó
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        // Verificamos que la contraseña está encriptada (no será igual a newPassword)
        User savedUser = userCaptor.getValue();
        assertNotEquals(newPassword, savedUser.getPassword());
        // Si tienes acceso al encoder, puedes verificar que la contraseña coincide:
        // assertTrue(passwordEncoder.matches(newPassword, savedUser.getPassword()));
    }


    @Test
    public void testGetUsersByPollaId_ShouldReturnUserList() {
        // Arrange
        Long pollaId = 1L;
        List<UserDTO> userList = List.of(UserUtil.userDTO(), UserUtil.userDTO());
        when(userScoresPollaService.getUsersByPollaId(pollaId)).thenReturn(ResponseEntity.ok(userList));

        // Act
        ResponseEntity<List<UserDTO>> response = userService.getUsersByPollaId(pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userScoresPollaService, times(1)).getUsersByPollaId(pollaId);
    }

    @Test
    public void testGetUsersByPollaId_ShouldReturnNoContentWhenNoUsersFound() {
        // Arrange
        Long pollaId = 1L;
        when(userScoresPollaService.getUsersByPollaId(pollaId)).thenReturn(ResponseEntity.noContent().build());

        // Act
        ResponseEntity<List<UserDTO>> response = userService.getUsersByPollaId(pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userScoresPollaService, times(1)).getUsersByPollaId(pollaId);
    }

    @Test
    public void testBanUserFromPolla_ShouldReturnTrueOnSuccess() {
        // Arrange
        Long userId = 1L;
        Long pollaId = 1L;
        when(userScoresPollaService.banUserFromPolla(userId, pollaId)).thenReturn(ResponseEntity.ok(true));

        // Act
        ResponseEntity<Boolean> response = userService.banUserFromPolla(userId, pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(userScoresPollaService, times(1)).banUserFromPolla(userId, pollaId);
    }

    @Test
    public void testBanUserFromPolla_ShouldReturnFalseOnFailure() {
        // Arrange
        Long userId = 1L;
        Long pollaId = 1L;
        when(userScoresPollaService.banUserFromPolla(userId, pollaId)).thenReturn(ResponseEntity.ok(false));

        // Act
        ResponseEntity<Boolean> response = userService.banUserFromPolla(userId, pollaId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
        verify(userScoresPollaService, times(1)).banUserFromPolla(userId, pollaId);
    }

    @Test
    void nicknameExists_ShouldReturnTrue_WhenNicknameExists() {
        // Arrange
        String nickname = "existingNickname";
        when(userRepository.existsByNickname(nickname)).thenReturn(true);

        // Act
        boolean result = userService.nicknameExists(nickname);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).existsByNickname(nickname);
    }

    @Test
    void nicknameExists_ShouldReturnFalse_WhenNicknameDoesNotExist() {
        // Arrange
        String nickname = "nonExistingNickname";
        when(userRepository.existsByNickname(nickname)).thenReturn(false);

        // Act
        boolean result = userService.nicknameExists(nickname);

        // Assert
        assertFalse(result);
        verify(userRepository, times(1)).existsByNickname(nickname);
    }

    @Test
    void updateCurrentUser_ShouldReturnUpdatedUserDTO_WhenValidInput() {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);
        when(jwtService.generateToken(testUser)).thenReturn("new.jwt.token");

        // Act
        ResponseEntity<UpdatedUserDTO> response = userService.updateCurrentUser(token, testUserDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("new.jwt.token", response.getBody().accessToken());
        verify(jwtService).extractUsername(token);
        verify(userRepository).findByNickname(username);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateCurrentUser_ShouldReturnBadRequest_WhenValidationFails() {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.of(testUser));
        doThrow(new PollaFutboleraException(PollaFutboleraExceptionType.EMAIL_FORMAT))
                .when(userServiceSpy).validateUpdateInput(testUserDTO, username);

        // Act
        ResponseEntity<UpdatedUserDTO> response = userServiceSpy.updateCurrentUser(token, testUserDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(jwtService).extractUsername(token);
        verify(userRepository).findByNickname(username);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateCurrentUser_ShouldThrowUsernameNotFoundException_WhenUserNotFound() {
        // Arrange
        String token = "valid.jwt.token";
        String username = "nonExistingUser";
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.updateCurrentUser(token, testUserDTO));
        verify(jwtService).extractUsername(token);
        verify(userRepository).findByNickname(username);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getCurrentUser_ShouldReturnUserDTO_WhenValidToken() {
        // Arrange
        String token = "valid.jwt.token";
        String username = "testuser";
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        // Act
        ResponseEntity<UserDTO> response = userService.getCurrentUser(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUserDTO, response.getBody());
        verify(jwtService).extractUsername(token);
        verify(userRepository).findByNickname(username);
    }

    @Test
    void getCurrentUser_ShouldThrowUsernameNotFoundException_WhenUserNotFound() {
        // Arrange
        String token = "valid.jwt.token";
        String username = "nonExistingUser";
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.getCurrentUser(token));
        verify(jwtService).extractUsername(token);
        verify(userRepository).findByNickname(username);
    }

    @Test
    void preloadUsers_ShouldReturnError_WhenUserAlreadyExistsInSamePolla() throws IOException {
        Long pollaId = 1L;
        Long companyId = 100L;

        // Mock del archivo
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.csv");

        // Polla con compañía
        Polla polla = new Polla();
        polla.setId(pollaId);
        Company company = new Company();
        company.setId(companyId);
        polla.setCompany(company);

        // Datos del usuario precargado
        PreloadUserDTO preloadUserDTO = new PreloadUserDTO(
                4L, "123456", null, "Juan", "Pérez", null, "juan@example.com", null, pollaId
        );
        preloadUserDTOs = List.of(preloadUserDTO);

        // Usuario existente
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setCedula("123456");

        // Usuario ya inscrito en la polla
        UserScoresPolla userScoresPolla = UserScoresPolla.builder()
                .user(existingUser)
                .polla(polla)
                .scores(0)
                .state(UserPollaState.ACTIVO)
                .build();

        // Mocks
        when(fileUtils.processCSV(file)).thenReturn(preloadUserDTOs);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(polla));
        when(userScoresPollaRepository.findByUser_CedulaAndPolla("123456", polla))
                .thenReturn(List.of(userScoresPolla));
        when(preloadedUserRepository.findByCedulaAndPolla("123456", polla))
                .thenReturn(List.of()); // No ha sido precargado antes

        // Ejecutar el servicio
        ResponseEntity<PreloadUserValidationResultDTO> response =
                userService.preloadUsers(pollaId, file, companyId);

        // Verificar que el usuario haya sido marcado como inválido por ya estar inscrito
        assertTrue(response.getBody().getInvalidUsers().stream()
                .anyMatch(dto -> dto.cedula().equals("123456")
                        && dto.errorMessage().toLowerCase().contains("ya está inscrito")));
    }

    @Test
    void preloadUsers_ShouldReturnError_WhenMandatoryFieldsAreEmpty() throws IOException {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.csv");

        Polla polla = new Polla();
        polla.setId(pollaId);
        Company newCompany = new Company();
        newCompany.setId(companyId);
        polla.setCompany(newCompany);

        preloadUserDTOs = List.of(new PreloadUserDTO(6L, "112233", null, "Carlos", "", null, null, null, null));

        when(fileUtils.processCSV(file)).thenReturn(preloadUserDTOs);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(polla));
        ResponseEntity<PreloadUserValidationResultDTO> response = userService.preloadUsers(pollaId, file, companyId);

        assertTrue(response.getBody().getInvalidUsers().stream()
                .anyMatch(dto -> dto.cedula().equals("112233") && dto.errorMessage().contains("Faltan campos obligatorios")));
    }

    @Test
    void preloadUsers_ShouldReturnError_WhenDuplicateCedulasInFile() throws IOException {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.csv");

        Polla polla = new Polla();
        polla.setId(pollaId);
        Company newCompany = new Company();
        newCompany.setId(companyId);
        polla.setCompany(newCompany);

        PreloadUserDTO validDTO = new PreloadUserDTO(7L, "123456", null, "Laura", "López", null, "LauraL@mail.com", null, null);
        PreloadUserDTO duplicatedDTO = new PreloadUserDTO(8L, "123456", null, "Pedro", "García", null, "PedroG@mail.com", null, null);
        preloadUserDTOs = List.of(validDTO, duplicatedDTO);

        when(fileUtils.processCSV(file)).thenReturn(preloadUserDTOs);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(polla));

        // mock para el mapeo del DTO válido
        PreloadedUser mappedUser = new PreloadedUser();
        mappedUser.setId(1L); // o cualquier valor
        when(userMapper.toEntity(validDTO)).thenReturn(mappedUser);

        ResponseEntity<PreloadUserValidationResultDTO> response = userService.preloadUsers(pollaId, file, companyId);

        assertTrue(response.getBody().getInvalidUsers().stream()
                .anyMatch(dto -> dto.cedula().equals("123456") && dto.errorMessage().toLowerCase().contains("duplicada")));
    }

    @Test
    void preloadUsers_ShouldThrowInvalidFileFormatException_WhenFileExtensionIsNotSupported() {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.txt");

        Exception exception = assertThrows(InvalidFileFormatException.class,
                () -> userService.preloadUsers(pollaId, file, companyId));

        assertTrue(exception.getMessage().contains("Formato de archivo no soportado"));
    }

    @Test
    void preloadUsers_ShouldThrowInvalidFileFormatException_WhenFileNameIsNull() {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(null);

        Exception exception = assertThrows(InvalidFileFormatException.class,
                () -> userService.preloadUsers(pollaId, file, companyId));

        assertTrue(exception.getMessage().contains("File name cannot be null"));
    }

    @Test
    void preloadUsers_ShouldThrowFileProcessingException_WhenIOExceptionOccurs() throws IOException {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.csv");

        when(fileUtils.processCSV(file)).thenThrow(new IOException("Error reading file"));

        Exception exception = assertThrows(FileProcessingException.class,
                () -> userService.preloadUsers(pollaId, file, companyId));

        assertTrue(exception.getMessage().contains("Error procesando el archivo"));
    }

    @Test
    void preloadUsers_ShouldThrowPollaNotFoundException_WhenPollaDoesNotExist() throws IOException {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.csv");

        when(fileUtils.processCSV(file)).thenReturn(List.of(new PreloadUserDTO(1L, "123456", null, "Juan", "Pérez", null, null, null, null)));
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(PollaNotFoundException.class,
                () -> userService.preloadUsers(pollaId, file, companyId));

        assertTrue(exception.getMessage().contains("Polla no encontrada"));
    }

    @Test
    void preloadUsers_ShouldThrowCompanyMismatchException_WhenCompanyIdDoesNotMatchPolla() throws IOException {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.csv");

        Polla polla = new Polla();
        polla.setId(pollaId);
        Company newCompany = new Company();
        newCompany.setId(999L);
        polla.setCompany(newCompany); // Diferente ID

        when(fileUtils.processCSV(file)).thenReturn(List.of());
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(polla));

        Exception exception = assertThrows(CompanyMismatchException.class,
                () -> userService.preloadUsers(pollaId, file, companyId));
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains("pertenece a otra compañía"));
    }

    @Test
    void preloadUsers_ShouldReturnError_WhenUserAlreadyPreloadedInSamePolla() throws IOException {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.csv");

        Company company = new Company();
        company.setId(companyId);
        Polla polla = new Polla();
        polla.setId(pollaId);
        polla.setCompany(company);

        PreloadUserDTO preloadUserDTO = new PreloadUserDTO(
                10L, "999888", null, "Ana", "Ramírez", null, "ana@email.com", null, null
        );
        preloadUserDTOs = List.of(preloadUserDTO);

        PreloadedUser preloadedUser = new PreloadedUser();
        preloadedUser.setCedula("999888");
        preloadedUser.setPolla(polla);

        when(fileUtils.processCSV(file)).thenReturn(preloadUserDTOs);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(polla));
        when(preloadedUserRepository.findByCedulaAndPolla("999888", polla)).thenReturn(List.of(preloadedUser));

        ResponseEntity<PreloadUserValidationResultDTO> response =
                userService.preloadUsers(pollaId, file, companyId);

        assertTrue(response.getBody().getInvalidUsers().stream()
                .anyMatch(dto -> dto.cedula().equals("999888")
                        && dto.errorMessage().toLowerCase().contains("ya ha sido precargado")));
    }

    @Test
    void preloadUsers_ShouldSaveValidUsersSuccessfully() throws IOException {
        Long pollaId = 1L;
        Long companyId = 100L;

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.csv");

        Company company = new Company();
        company.setId(companyId);
        Polla polla = new Polla();
        polla.setId(pollaId);
        polla.setCompany(company);

        // DTO válido
        PreloadUserDTO preloadUserDTO = new PreloadUserDTO(
                5L, "555444", null, "John", "Doe", null, "JohnDoe@mail.com", null, null
        );
        preloadUserDTOs = List.of(preloadUserDTO);

        PreloadedUser mappedUser = new PreloadedUser();
        mappedUser.setCedula("555444");
        mappedUser.setPolla(polla);

        when(fileUtils.processCSV(file)).thenReturn(preloadUserDTOs);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(polla));
        when(preloadedUserRepository.findByCedulaAndPolla("555444", polla)).thenReturn(List.of());
        when(userScoresPollaRepository.findByUser_CedulaAndPolla("555444", polla)).thenReturn(List.of());
        when(userMapper.toEntity(preloadUserDTO)).thenReturn(mappedUser);

        ResponseEntity<PreloadUserValidationResultDTO> response =
                userService.preloadUsers(pollaId, file, companyId);

        assertTrue(response.getBody().getValidUsers().stream()
                .anyMatch(dto -> dto.cedula().equals("555444")));

        ArgumentCaptor<List<PreloadedUser>> captor = ArgumentCaptor.forClass(List.class);
        verify(preloadedUserRepository).saveAll(captor.capture());

        List<PreloadedUser> savedUsers = captor.getValue();
        assertEquals(1, savedUsers.size());
        assertEquals("555444", savedUsers.get(0).getCedula());
    }

    @Test
    public void testGetPreloadedUsersByPollaId_ShouldReturnUserList() {
        // Arrange
        Long pollaId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        PreloadedUser user1 = new PreloadedUser();
        user1.setId(1L);
        user1.setCedula("1234567");
        user1.setName("Juan");
        user1.setLastName("Pérez");
        user1.setMail("juanperez@gmail.com");


        PreloadedUser user2 = new PreloadedUser();
        user2.setId(2L);
        user2.setCedula("1234567");
        user2.setName("Maria");
        user2.setLastName("Gómez");
        user2.setMail("mariagomez@gmail.com");

        Page<PreloadedUser> userPage = new PageImpl<>(List.of(user1, user2), pageable, 2);

        when(preloadedUserRepository.findByPollaId(pollaId, pageable)).thenReturn(userPage);

        when(userMapper.toPreloadUserDTO(user1)).thenReturn(new PreloadUserDTO(1L, "1234567", "", "Juan", "Pérez", "", "juanperez@gmail.com", null, pollaId));
        when(userMapper.toPreloadUserDTO(user2)).thenReturn(new PreloadUserDTO(2L, "1234567", "", "Maria", "Gómez", "", "mariagomez@gmail.com", null, pollaId));

        // Act
        ResponseEntity<Page<PreloadUserDTO>> response = userService.getPreloadedUsersByPollaId(pollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals("Juan", response.getBody().getContent().get(0).name());
        assertEquals("Maria", response.getBody().getContent().get(1).name());
    }

    @Test
    public void testGetPreloadedUsersByPollaId_ShouldReturnNoContent() {
        // Arrange
        Long pollaId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<PreloadedUser> emptyPage = Page.empty(pageable);

        when(preloadedUserRepository.findByPollaId(pollaId, pageable)).thenReturn(emptyPage);

        // Act
        ResponseEntity<Page<PreloadUserDTO>> response = userService.getPreloadedUsersByPollaId(pollaId, pageable);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }


}
