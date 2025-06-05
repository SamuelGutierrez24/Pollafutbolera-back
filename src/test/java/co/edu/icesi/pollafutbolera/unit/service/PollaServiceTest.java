package co.edu.icesi.pollafutbolera.unit.service;

import co.edu.icesi.pollafutbolera.config.TestDatabaseConfig;
import co.edu.icesi.pollafutbolera.config.TestSecurityConfig;
import co.edu.icesi.pollafutbolera.dto.PollaConfigDTO;
import co.edu.icesi.pollafutbolera.mapper.PlatformConfigMapper;
import co.edu.icesi.pollafutbolera.mapper.PollaMapper;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.UserScoresPolla;
import co.edu.icesi.pollafutbolera.repository.PollaRepository;
import co.edu.icesi.pollafutbolera.repository.UserScoresPollaRepository;
import co.edu.icesi.pollafutbolera.service.PollaServiceImpl;
import co.edu.icesi.pollafutbolera.util.PollaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@Import({TestSecurityConfig.class, TestDatabaseConfig.class})
public class PollaServiceTest {


    @Mock
    private PollaRepository pollaRepository;


    @Mock
    private PollaMapper pollaMapper;

    @Mock
    private UserScoresPollaRepository userScoresPollaRepository;

    @InjectMocks
    private PollaServiceImpl pollaService;

    @Mock
    private PlatformConfigMapper platformConfigMapper;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        //ReflectionTestUtils.setField(pollaMapper, "platformConfigMapper", platformConfigMapper);

    }


    @Test
    public void testSavePolla() {
        PollaConfigDTO pollaConfigDTO = PollaUtil.pollaConfigDTO;
        Polla polla = PollaUtil.polla();

        when(pollaMapper.toPolla(pollaConfigDTO)).thenReturn(polla);
        when(pollaRepository.save(polla)).thenReturn(polla);

        when(pollaMapper.toPolla(pollaConfigDTO)).thenReturn(polla);

        pollaService.savePolla(pollaConfigDTO);

        verify(pollaRepository, times(1)).save(polla);
    }

    @Test
    public void testDeletePolla() {
        Long pollaId = 10L;

        when(pollaRepository.findById(pollaId)).thenReturn(Optional.ofNullable(PollaUtil.polla()));

        when(pollaRepository.findById(pollaId)).thenReturn(Optional.ofNullable(PollaUtil.polla()));

        when(userScoresPollaRepository.findTop10ByPollaIdOrderByScoresDesc(pollaId, null)).thenReturn(List.of(new UserScoresPolla()));

        pollaService.deletePolla(pollaId);

        verify(pollaRepository, times(1)).deleteById(pollaId);
    }

    @Test
    public void testUpdatePolla() {
        Long pollaId = 11L;
        PollaConfigDTO pollaConfigDTO = PollaUtil.pollaConfigDTO;
        Polla polla = PollaUtil.polla();

        when(pollaMapper.toPolla(pollaConfigDTO)).thenReturn(polla);
        when(pollaRepository.findById(pollaId)).thenReturn(Optional.of(polla));

        pollaService.updatePolla(pollaId, pollaConfigDTO);

        verify(pollaRepository, times(1)).save(polla);
    }

}
