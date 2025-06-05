package co.edu.icesi.pollafutbolera.mapper;

import co.edu.icesi.pollafutbolera.dto.CompanyDTO;
import co.edu.icesi.pollafutbolera.dto.UpdateCompanyDTO;
import co.edu.icesi.pollafutbolera.dto.RoleDTO;
import co.edu.icesi.pollafutbolera.model.Company;
import org.mapstruct.*;

import java.util.List;
import co.edu.icesi.pollafutbolera.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {


    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "nit", target = "nit")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "contact", target = "contact")
    @Mapping(source = "logo", target = "logo")
    Company toEntity(CompanyDTO companyDTO);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "nit", target = "nit")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "contact", target = "contact")
    @Mapping(source = "logo", target = "logo")
    CompanyDTO toDTO(Company company);

    @Mapping(source = "name", target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "address", target = "address", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "contact", target = "contact", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePartial(UpdateCompanyDTO dto, @MappingTarget Company company);

    List<CompanyDTO> toDTOList(List<Company> all);

}
