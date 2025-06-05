package co.edu.icesi.pollafutbolera.unit.mapper;

import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.mapper.SubPollaMapper;
import co.edu.icesi.pollafutbolera.model.SubPolla;
import co.edu.icesi.pollafutbolera.util.SubPollaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SubPollaMapperTest {

    private SubPollaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(SubPollaMapper.class);
    }

    @Test
    void testToDTO() {
        List<SubPolla> subPollas = SubPollaUtil.createSampleSubPollas();

        for (SubPolla subPolla : subPollas) {
            SubPollaGetDTO dto = mapper.toDTO(subPolla);
            assertEquals(subPolla.getId(), dto.id());
            assertEquals(subPolla.getIsPrivate(), dto.isPrivate());
            assertEquals(subPolla.getPolla().getId(), dto.pollaId());
        }
    }

    @Test
    void testToEntity() {
        List<SubPollaCreateDTO> createDTOs = SubPollaUtil.createSampleSubPollaCreateDTOs();

        for (SubPollaCreateDTO createDTO : createDTOs) {
            SubPolla entity = mapper.toEntity(createDTO);
            assertEquals(createDTO.isPrivate(), entity.getIsPrivate());
            assertEquals(createDTO.pollaId(), entity.getPolla().getId());
        }
    }

    @Test
    void testToDTO_NullSubPolla() {
        SubPollaGetDTO dto = mapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void testToEntity_NullSubPollaCreateDTO() {
        SubPolla entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void testToEntity_NullPollaId() {
        SubPollaCreateDTO createDTO = SubPollaCreateDTO.builder()
                .isPrivate(true)
                .pollaId(null)
                .build();

        SubPolla entity = mapper.toEntity(createDTO);
        assertNull(entity.getPolla());
    }
}