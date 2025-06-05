package co.edu.icesi.pollafutbolera.unit.mapper;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.PollaGetDTO;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.mapper.PlatformConfigMapper;
import co.edu.icesi.pollafutbolera.mapper.PollaMapper;
import co.edu.icesi.pollafutbolera.mapper.RewardMapper;
import co.edu.icesi.pollafutbolera.mapper.TournamentMapper;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.util.PollaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class PollaMapperTest {

    PollaMapper pollaMapper = Mappers.getMapper(PollaMapper.class);

    PlatformConfigMapper platformConfigMapper = Mappers.getMapper(PlatformConfigMapper.class);

    TournamentMapper tournamentMapper = Mappers.getMapper(TournamentMapper.class);

    RewardMapper rewardMapper = Mappers.getMapper(RewardMapper.class);


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        platformConfigMapper = Mappers.getMapper(PlatformConfigMapper.class);
        tournamentMapper = Mappers.getMapper(TournamentMapper.class);
        rewardMapper = Mappers.getMapper(RewardMapper.class);

        ReflectionTestUtils.setField(pollaMapper, "platformConfigMapper", platformConfigMapper);
        ReflectionTestUtils.setField(pollaMapper, "tournamentMapper", tournamentMapper);
        ReflectionTestUtils.setField(pollaMapper, "rewardMapper", rewardMapper);
    }


    @Test
    public void testToPollaConfigDTO() {
        Polla polla = PollaUtil.polla();

        PollaConfigDTO pollaConfigDTO = pollaMapper.toPollaConfigDTO(polla);

        assertNotNull(pollaConfigDTO);
        assertEquals(polla.getEndDate(), pollaConfigDTO.endDate());
        assertEquals(polla.getStartDate(), pollaConfigDTO.startDate());
        assertEquals(platformConfigMapper.toDTO(polla.getPlatformConfig()), pollaConfigDTO.platformConfig());
        assertEquals(polla.getImageUrl(), pollaConfigDTO.imageUrl());

    }

    @Test
    public void testToPolla() {
        PollaConfigDTO pollaConfigDTO = PollaUtil.pollaConfigDTO;
        Polla polla = pollaMapper.toPolla(pollaConfigDTO);

        assertNotNull(polla);
        assertEquals(polla.getEndDate(), pollaConfigDTO.endDate());
        assertEquals(polla.getStartDate(), pollaConfigDTO.startDate());
        assertEquals(platformConfigMapper.toDTO(polla.getPlatformConfig()), pollaConfigDTO.platformConfig());
        assertEquals(polla.getImageUrl(), pollaConfigDTO.imageUrl());

    }

    @Test
    public void testToPollaGetDTO() {
        Polla polla = PollaUtil.polla();

        PollaGetDTO pollaGetDTO = pollaMapper.toPollaGetDTO(polla);

        assertNotNull(pollaGetDTO);
        assertEquals(polla.getEndDate(), pollaGetDTO.endDate());
        assertEquals(polla.getStartDate(), pollaGetDTO.startDate());
        assertEquals(polla.getImageUrl(), pollaGetDTO.imageUrl());
    }

    @Test
    public void testToPollaConfigDTONull() {
        Polla polla = null;

        PollaConfigDTO pollaConfigDTO = pollaMapper.toPollaConfigDTO(polla);

        assertNull(pollaConfigDTO);
    }

    @Test
    public void testToPollaNull() {
        PollaConfigDTO pollaConfigDTO = null;

        Polla polla = pollaMapper.toPolla(pollaConfigDTO);

        assertNull(polla);
    }

    @Test
    public void testToPollaGetDTONull() {
        Polla polla = null;

        PollaGetDTO pollaGetDTO = pollaMapper.toPollaGetDTO(polla);

        assertNull(pollaGetDTO);
    }

}