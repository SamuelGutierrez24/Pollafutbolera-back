package co.edu.icesi.pollafutbolera.service;

import co.edu.icesi.pollafutbolera.config.PollaResponseEntity;
import co.edu.icesi.pollafutbolera.dto.*;
import co.edu.icesi.pollafutbolera.enums.PollaFutboleraExceptionType;
import co.edu.icesi.pollafutbolera.exception.*;
import co.edu.icesi.pollafutbolera.mapper.UserMapper;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.PreloadedUser;
import co.edu.icesi.pollafutbolera.model.Company;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.PreloadedUserRepository;
import co.edu.icesi.pollafutbolera.repository.CompanyRepository;
import co.edu.icesi.pollafutbolera.repository.RoleRepository;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import co.edu.icesi.pollafutbolera.repository.UserScoresPollaRepository;
import co.edu.icesi.pollafutbolera.utils.FileUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final UserScoresPollaService userScoresPollaService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CompanyRepository companyRepository;
    private final EmailServiceImpl emailService;
    private final EncryptionService encripterService;

    private final FileUtils fileUtils;
    private final PollaRepository pollaRepository;
    private final PreloadedUserRepository preloadedUserRepository;
    private final UserScoresPollaRepository userScoresPollaRepository;

    @Transactional
    @Override
    public LoginOutDTO login(LoginInDTO loginInDTO) {
        authenticationManager.authenticate( 
            new UsernamePasswordAuthenticationToken(
                loginInDTO.nickname(),
                loginInDTO.password()
            )
        );

        User user = userRepository.findByNickname(loginInDTO.nickname())
                     .orElseThrow(UsernameNotFoundException::new);

        String jwtToken = jwtService.generateToken(user);


        return LoginOutDTO.builder()
                .tokenType("Bearer")
                .role(user.getRole())
                .userId(user.getId())
                .userUsername(user.getNickname())
                .userDocumentId(user.getCedula())
                .userEmail(user.getMail())
                .userLastname(user.getLastName())
                .accessToken(jwtToken)
                .userId(user.getId())
                .role(user.getRole())
                .company(user.getCompany().getId())
                .build();
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserDTO userDTO = userMapper.toDTO(user.get());
            return ResponseEntity.ok(userDTO);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = userMapper.toDTOList(users);
        return ResponseEntity.ok(userDTOS);
    }

    @Override
    public PollaResponseEntity createUser(UserDTO userDTO) {

        User user;

        try {
            user = userMapper.toEntity(userDTO);
        }catch (Exception e) {
            return new PollaResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        try {
            validateInput(userDTO);
        } catch (PollaFutboleraException e) {
            return new PollaResponseEntity(HttpStatus.BAD_REQUEST, e.getPfExceptionType());
        }

        if (userRepository.findAll().isEmpty()){
            user.setRole(roleRepository.findByName("ADMIN").orElseThrow(() -> new PollaFutboleraException(PollaFutboleraExceptionType.ROLE_NOT_FOUND)));
        }else{
            user.setRole(roleRepository.findByName("USER").orElseThrow(() -> new PollaFutboleraException(PollaFutboleraExceptionType.ROLE_NOT_FOUND)));
        }

        if (user.getCompany() == null) {
            Optional<Company> defaultCompany = companyRepository.findByName("Default Company");
            if (defaultCompany.isPresent()) {
                user.setCompany(defaultCompany.get());
            } else {
                Company newCompany = companyRepository.save(new Company(null, "Default Company", "00000000", null, null, "https://placehold.co/200", null));
                user.setCompany(newCompany);
            }
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);


        user = userRepository.save(user);

        UserDTO createdUserDTO = userMapper.toDTO(user);

        try {
            emailService.sendEmailConfirmRegistration(user.getMail(), user.getNickname());
        } catch (Exception e) {
            return new PollaResponseEntity(HttpStatus.BAD_REQUEST, PollaFutboleraExceptionType.EMAIL_SEND_ERROR);
        }

        return new PollaResponseEntity(HttpStatus.CREATED, createdUserDTO);
    }

    public Boolean changePassword(String token, String newPassword) {
        String username = jwtService.extractUsername(token);
        System.out.println(username);

        User user1;
        try {
            user1 = userRepository.findByNickname(username)
                    .orElseThrow(UsernameNotFoundException::new);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (user1 != null) {
                String hashedPassword =  passwordEncoder.encode(newPassword);

                user1.setPassword(hashedPassword);
                userRepository.save(user1);
                return true;
            }

        }catch (Exception e){
            return false;
        }
        return false;

    }

    @Override
    public PollaResponseEntity updateUser(Long id, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            try {
                validateInput(userDTO);
            } catch (PollaFutboleraException e) {
                return new PollaResponseEntity(HttpStatus.BAD_REQUEST, e.getPfExceptionType());
            }

            User updatedUser;

            try {
                userMapper.updateFromDTO(userDTO, existingUser);
                updatedUser = userRepository.save(existingUser);
            } catch (Exception e) {
                return new PollaResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
            }

            UserDTO updatedUserDTO = userMapper.toDTO(updatedUser);

            return new PollaResponseEntity(HttpStatus.OK, updatedUserDTO);
        }
        return new PollaResponseEntity(HttpStatus.NOT_FOUND, PollaFutboleraExceptionType.USER_NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public void validateInput(UserDTO userDTO) throws PollaFutboleraException {
        String cedula = userDTO.cedula();
        String password = userDTO.password();

        //Cedula
        if (
                cedula.length() < 6 ||
                cedula.length() > 10 ||
                !(cedula.matches("^[0-9]{6,10}$"))
        )
        {
            throw new PollaFutboleraException(PollaFutboleraExceptionType.CEDULA_FORMAT);
        }

        //Cédula duplicada para la Default Company

        List<User> users = userRepository.findByCedula(cedula);
        if (!users.isEmpty()) {
            for (User user : users) {
                if (user.getCompany() != null && user.getCompany().getName().equals("Default Company")) {
                    throw new PollaFutboleraException(PollaFutboleraExceptionType.ALREADY_EXISTS);
                }
            }
        }


        //Contraseña
        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new PollaFutboleraException(PollaFutboleraExceptionType.INSECURE_PASSWORD);
        }

        //Nickname
        if (userRepository.existsByNickname(userDTO.nickname()) && !Objects.equals(userRepository.findByNickname(userDTO.nickname()).get().getId(), userDTO.id())) {
            throw new PollaFutboleraException(PollaFutboleraExceptionType.ALREADY_EXISTS);
        }

        //Mail
        if(!userDTO.mail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new PollaFutboleraException(PollaFutboleraExceptionType.EMAIL_FORMAT);
        }

        //Telefono
        if(!userDTO.phoneNumber().matches("^\\+57\\d{10}$")) {
            throw new PollaFutboleraException(PollaFutboleraExceptionType.PHONE_FORMAT);
        }

    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsersByPollaId(Long pollaId) {

        try {
            return userScoresPollaService.getUsersByPollaId(pollaId);
        } catch (PollaNotFoundException e) {
            return ResponseEntity.noContent().build();
        }

    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsersByDefaultCompany() {
        List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getCompany() != null && "Default Company".equals(user.getCompany().getName()))
                .toList();

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<UserDTO> userDTOS = userMapper.toDTOList(users);
        return ResponseEntity.ok(userDTOS);
    }

    @Override
    public ResponseEntity<Boolean> banUserFromPolla(Long userId, Long pollaId) {
        try {
            return userScoresPollaService.banUserFromPolla(userId, pollaId);
        } catch (UserNotInPollaException e) {
            return ResponseEntity.ok(false);
        }
    }

    @Override
    public ResponseEntity<NotificationSettingsDTO> updateNotificationSettings(Long userId, NotificationSettingsDTO settings) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userOptional.get();

        user.setNotificationsEmailEnabled(settings.enabledEmail());
        user.setNotificationsSMSEnabled(settings.enabledSMS());
        user.setNotificationsWhatsappEnabled(settings.enabledWhatsapp());
        userRepository.save(user);

        NotificationSettingsDTO updatedSettings = NotificationSettingsDTO.builder()
                .userId(user.getId())
                .enabledEmail(user.isNotificationsEmailEnabled())
                .enabledSMS(user.isNotificationsSMSEnabled())
                .enabledWhatsapp(user.isNotificationsWhatsappEnabled())
                .build();
                
        return ResponseEntity.ok(updatedSettings);
    }
    
    @Override
    public ResponseEntity<NotificationSettingsDTO> getNotificationSettings(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User user = userOptional.get();
        
        NotificationSettingsDTO settings = NotificationSettingsDTO.builder()
                .userId(user.getId())
                .enabledEmail(user.isNotificationsEmailEnabled())
                .enabledSMS(user.isNotificationsSMSEnabled())
                .enabledWhatsapp(user.isNotificationsWhatsappEnabled())
                .build();
                
        return ResponseEntity.ok(settings);
    }

    @Override
    public Boolean nicknameExists(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public ResponseEntity<UpdatedUserDTO> updateCurrentUser(String token, UserDTO userDTO) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByNickname(username)
                .orElseThrow(UsernameNotFoundException::new);

        try {
            validateUpdateInput(userDTO, username);
        } catch (PollaFutboleraException e) {
            System.out.println(e.getPfExceptionType());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userMapper.updateFromDTO(userDTO, user);
        user = userRepository.save(user);

        UserDTO updatedUserDTO = userMapper.toDTO(user);

        String jwtToken = jwtService.generateToken(user);

        UpdatedUserDTO dto = UpdatedUserDTO.builder()
                .id(updatedUserDTO.id())
                .cedula(updatedUserDTO.cedula())
                .company(updatedUserDTO.company())
                .name(updatedUserDTO.name())
                .lastName(updatedUserDTO.lastName())
                .nickname(updatedUserDTO.nickname())
                .mail(updatedUserDTO.mail())
                .phoneNumber(updatedUserDTO.phoneNumber())
                .notificationsEmailEnabled(updatedUserDTO.notificationsEmailEnabled())
                .notificationsSMSEnabled(updatedUserDTO.notificationsSMSEnabled())
                .notificationsWhatsappEnabled(updatedUserDTO.notificationsWhatsappEnabled())
                .role(updatedUserDTO.role())
                .photo(updatedUserDTO.photo())
                .accessToken(jwtToken)
                .build();

        return ResponseEntity.ok(dto);
    }

    public void validateUpdateInput(UserDTO userDTO, String username) throws PollaFutboleraException {

        //Nickname
        if (userRepository.existsByNickname(userDTO.nickname()) && !Objects.equals(userRepository.findByNickname(userDTO.nickname()).get().getNickname(), username)) {
            throw new PollaFutboleraException(PollaFutboleraExceptionType.ALREADY_EXISTS);
        }

        //Mail
        if(!userDTO.mail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            throw new PollaFutboleraException(PollaFutboleraExceptionType.EMAIL_FORMAT);
        }

        //Telefono
        if(!userDTO.phoneNumber().matches("^\\+57\\d{10}$")) {
            throw new PollaFutboleraException(PollaFutboleraExceptionType.PHONE_FORMAT);
        }

    }

    @Override
    public ResponseEntity<UserDTO> getCurrentUser(String token) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByNickname(username)
                .orElseThrow(UsernameNotFoundException::new);

        //Don't return password
        user.setPassword(null);
        UserDTO userDTO = userMapper.toDTO(user);
        return ResponseEntity.ok(userDTO);
    }
    private PreloadUserValidationResultDTO validatePreloadInput(List<PreloadUserDTO> dtoList, Polla polla) {
        Set<String> seenCedulas = new HashSet<>();
        List<PreloadUserDTO> validUsers = new ArrayList<>();
        List<PreloadUserErrorDTO> invalidUsers = new ArrayList<>();

        int lineNumber = 1;
        for (PreloadUserDTO dto : dtoList) {
            String cedula = dto.cedula();
            String name = dto.name();
            String lastName = dto.lastName();
            String mail = dto.mail();

            if (cedula == null || name == null || lastName == null || mail == null ||
                    cedula.isEmpty() || name.isEmpty() || lastName.isEmpty() || mail.isEmpty()) {
                invalidUsers.add(new PreloadUserErrorDTO(lineNumber, cedula, name, lastName, mail, "Faltan campos obligatorios"));
            } else if (seenCedulas.contains(cedula)) {
                invalidUsers.add(new PreloadUserErrorDTO(lineNumber, cedula, name, lastName, mail, "Cédula duplicada en el archivo"));
            } else if (!preloadedUserRepository.findByCedulaAndPolla(cedula, polla).isEmpty()) {
                invalidUsers.add(new PreloadUserErrorDTO(lineNumber, cedula, name, lastName, mail, "El usuario ya ha sido precargado en esta polla"));
            } else if (!userScoresPollaRepository.findByUser_CedulaAndPolla(cedula, polla).isEmpty()) {
                invalidUsers.add(new PreloadUserErrorDTO(lineNumber, cedula, name, lastName, mail, "El usuario ya está inscrito en esta polla"));
            } else {
                seenCedulas.add(cedula);
                validUsers.add(dto);
            }
            lineNumber++;
        }

        return PreloadUserValidationResultDTO.builder()
                .validUsers(validUsers)
                .invalidUsers(invalidUsers)
                .build();
    }


    @Override
    public ResponseEntity<PreloadUserValidationResultDTO> preloadUsers(Long pollaId, MultipartFile file, Long companyId) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new InvalidFileFormatException("File name cannot be null.");
        }

        List<PreloadUserDTO> dtoList;
        try {
            if (fileName.endsWith(".csv")) {
                dtoList = fileUtils.processCSV(file);
            } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
                dtoList = fileUtils.processExcel(file);
            } else {
                throw new InvalidFileFormatException("Formato de archivo no soportado. Por favor suba un CSV o un XLSX/XLS.");
            }
        } catch (IOException e) {
            throw new FileProcessingException("Error procesando el archivo: " + fileName);
        }

        Polla polla = pollaRepository.findById(pollaId)
                .orElseThrow(() -> new PollaNotFoundException("Polla no encontrada con ID: " + pollaId));

        if (polla.getCompany() == null || !polla.getCompany().getId().equals(companyId)) {
            throw new CompanyMismatchException("La polla pertenece a otra compañía");
        }

        PreloadUserValidationResultDTO validationResult = validatePreloadInput(dtoList, polla);

        if (validationResult.getValidUsers().isEmpty() && validationResult.getInvalidUsers().isEmpty()) {
            throw new EmptyFileException("El archivo no contiene datos válidos");
        }

        List<PreloadedUser> entities = new ArrayList<>();
        for (PreloadUserDTO dto : validationResult.getValidUsers()) {
            PreloadedUser entity = userMapper.toEntity(dto);
            entity.setPolla(polla);
            entity.setCompany(polla.getCompany());
            entities.add(entity);
        }

        preloadedUserRepository.saveAll(entities);

        return ResponseEntity.ok(validationResult);
    }


    @Override
    public ResponseEntity<Page<PreloadUserDTO>> getPreloadedUsersByPollaId(Long pollaId, Pageable pageable) {
        Page<PreloadedUser> page = preloadedUserRepository.findByPollaId(pollaId, pageable);
        Page<PreloadUserDTO> dtoPage = page.map(userMapper::toPreloadUserDTO);
        if(dtoPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dtoPage);
    }


    @Override
    public ResponseEntity<PreloadUserDTO> getPreloadedUsersById(String id) {
        String descrypId = null;
        try {
            descrypId = encripterService.decrypt(id);
            Long longID=Long.parseLong(descrypId);
            PreloadUserDTO preUser = userMapper.toPreloadUserDTO(preloadedUserRepository.findById(longID).get());
            return ResponseEntity.ok(preUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}