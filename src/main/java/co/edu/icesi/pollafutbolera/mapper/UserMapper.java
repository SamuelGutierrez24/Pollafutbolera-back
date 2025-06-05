package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.PreloadUserDTO;
import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.model.Company;
import co.edu.icesi.pollafutbolera.model.PreloadedUser;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.CompanyRepository;
import co.edu.icesi.pollafutbolera.repository.RoleRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Mapping(source = "id", target = "id")
    @Mapping(source = "cedula", target = "cedula")
    //null if no company
    @Mapping(target = "company", expression = "java(user.getCompany() != null ? user.getCompany().getId() : null)")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "mail", target = "mail")
    @Mapping(source = "nickname", target = "nickname")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "photo", target = "photo")
    @Mapping(target = "role", expression = "java(user.getRole().getId())")
    @Mapping(source = "extraInfo", target = "extraInfo")
    @Mapping(source = "notificationsEmailEnabled", target = "notificationsEmailEnabled")
    @Mapping(source = "notificationsSMSEnabled", target = "notificationsSMSEnabled")
    @Mapping(source = "notificationsWhatsappEnabled", target = "notificationsWhatsappEnabled")
    public abstract UserDTO toDTO(User user);

    public abstract List<UserDTO> toDTOList(List<User> users);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "cedula", target = "cedula")
    @Mapping(target = "company", expression = "java(mapCompany(userDTO.company()))")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "mail", target = "mail")
    @Mapping(source = "nickname", target = "nickname")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "photo", target = "photo")
    @Mapping(target = "role", expression = "java(mapRole(userDTO.role()))")
    @Mapping(source = "extraInfo", target = "extraInfo")
    @Mapping(source = "notificationsEmailEnabled", target = "notificationsEmailEnabled")
    @Mapping(source = "notificationsSMSEnabled", target = "notificationsSMSEnabled")
    @Mapping(source = "notificationsWhatsappEnabled", target = "notificationsWhatsappEnabled")
    public abstract User toEntity(UserDTO userDTO);

    //ignore cedula, company, role and extra info
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "cedula", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "extraInfo", ignore = true)
    public abstract void updateFromDTO(UserDTO userDTO, @MappingTarget User user);

    protected Role mapRole(Long roleId) {
        if (roleId == null) {
            return null;
        }

        return roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));
    }

    protected Company mapCompany(Long companyId) {
        if (companyId == null) {
            return null;
        }

        return companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with ID: " + companyId));
    }


    @AfterMapping
    protected void assignRole(@MappingTarget User user, UserDTO userDTO) {
        if (userDTO.role() != null) {
            user.setRole(roleRepository.findById(userDTO.role())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + userDTO.role())));
        }
    }

    @Mapping(source = "cedula", target = "cedula")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "mail", target = "mail")
    @Mapping(source = "extraInfo", target = "extraInfo")
    @Mapping(target = "role", expression = "java(mapRoleByName(\"USER\"))")
    @Mapping(target = "notificationsEnabled", constant = "true")
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "polla", ignore = true)
    public abstract PreloadedUser toEntity(PreloadUserDTO preloadUserDTO);

    public abstract List<PreloadUserDTO> toPreloadUserDTOList(List<PreloadedUser> users);


    protected Role mapRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with name: " + roleName));
    }
    @Mapping(source = "cedula", target = "cedula")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "mail", target = "mail")
    @Mapping(source = "extraInfo", target = "extraInfo")
    @Mapping(source = "polla.id", target = "pollaId")
    public abstract PreloadUserDTO toPreloadUserDTO(PreloadedUser preloadedUser);

}