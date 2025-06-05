package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.CompanyDTO;
import co.edu.icesi.pollafutbolera.dto.UpdateCompanyDTO;
import co.edu.icesi.pollafutbolera.dto.CreateCompanyDTO;
import co.edu.icesi.pollafutbolera.model.Company;

import java.util.List;

public class CompanyUtil {


    public static Company company() {
        return Company.builder()
                .id(1L)
                .name("ICESI")
                .nit("123456789")
                .logo("logo.png")
                .address("Calle 18 # 122 - 45")
                .contact("123456789")
                .build();

    }

    public static Company company2() {
        return Company.builder()
                .id(2L)
                .name("COMFANDI")
                .nit("987654321")
                .logo("logo.png")
                .address("Calle 18 # 122 - 45")
                .contact("987654321")
                .build();
    }

    public static Company defaultCompany() {
        return Company.builder()
                .id(777L)
                .name("Default Company")
                .nit("000000000")
                .logo("logo.png")
                .address("Calle 18 # 122 - 45")
                .contact("987654321")
                .build();
    }

    public static CompanyDTO companyDTO() {
        return CompanyDTO.builder()
                .id(1L)
                .name("ICESI")
                .nit( "123456789")
                .logo("logo.png")
                .address("Calle 18 # 122 - 21")
                .contact("324901")
                .build();
    }


    public static CompanyDTO newCompanyDTO() {
        return CompanyDTO.builder()
                .name("Carvajal")
                .nit( "13245768")
                .address("Cll 23 Cr32")
                .contact("32545656")
                .logo("logo.png")
                .build();
    }


    public static CreateCompanyDTO createCompanyDTO() {
        return CreateCompanyDTO.builder()
                .companyDTO(newCompanyDTO())
                .companyAdminId(UserUtil.userDefaultCompany().getId())
                .build();
    }

    public static UpdateCompanyDTO createUpdateCompanyDTO(String name, String address, String contact) {
        return new UpdateCompanyDTO(name, address, contact);
    }

    public static List<CompanyDTO> createCompanyDTOList() {
        return List.of(
                new CompanyDTO(1L, "ICESI", "123456", "Calle 18 # 122 - 45", "123456789", null),
                new CompanyDTO(2L, "COMFANDI", "654321", "Calle 18 # 122 - 45", "987654321", null),
                new CompanyDTO(3L, "Carvajal", "13245768", "Cll 23 Cr32", "32545656", null)
        );
    }

    public static List<Company> createCompanyList() {
        return List.of(
                new Company(1L, "ICESI", "123456", "Calle 18 # 122 - 45", "123456789", null, null),
                new Company(2L, "COMFANDI", "654321", "Calle 18 # 122 - 45", "987654321",null, null),
                new Company(3L, "Carvajal", "13245768", "Cll 23 Cr32", "32545656",null, null)
        );
    }

}
