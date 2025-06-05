package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.UserDTO;
import co.edu.icesi.pollafutbolera.model.Role;
import co.edu.icesi.pollafutbolera.model.User;

public class UserUtil {
    public static User   user() {
        return User.builder()
                .id(1L)
                .name("John Doe")
                .cedula("123456789")
                .role(RoleUtil.role())
                .company(CompanyUtil.company())
                .build();
    }

    public static User user2() {
        return User.builder()
                .id(2L)
                .name("Red John")
                .role(RoleUtil.role2())
                .company(CompanyUtil.company2())
                .cedula("987654321")
                .build();
    }


    public static User user3() {
        return User.builder()
                .id(3L)
                .name("Patrick Jane")
                .cedula("132457689")
                .role(RoleUtil.role2())
                .build();
    }


    public static User userDefaultCompany() {
        return User.builder()
                .id(14L)
                .name("User")
                .lastName("DefaultCompany")
                .cedula("666666666")
                .role(RoleUtil.role2())
                .company(CompanyUtil.defaultCompany())
                .build();
    }

    public static UserDTO userDTO() {
        return UserDTO.builder()
                .id(1L)
                .name("John Doe")
                .cedula("123456789")
                .build();
    }

    public static UserDTO userDTO2() {
        return UserDTO.builder()
                .id(2L)
                .name("Red John")
                .cedula("987654321")
                .build();
    }


    public static UserDTO userDTO3() {
        return UserDTO.builder()
                .id(3L)
                .name("Patrick Jane")
                .cedula("132457689")
                .build();
    }



}
