package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.model.Role;

public class RoleUtil {


    public static Role role() {
        return Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

    }

    public static Role role2() {
        return Role.builder()
                .id(2L)
                .name("USER")
                .build();
    }


    public static Role companyAdminRole() {
        return Role.builder()
                .id(3L)
                .name("COMPANY_ADMIN")
                .build();
    }

}
