package co.edu.icesi.pollafutbolera.util;

import co.edu.icesi.pollafutbolera.dto.SubPollaCreateDTO;
import co.edu.icesi.pollafutbolera.dto.SubPollaGetDTO;
import co.edu.icesi.pollafutbolera.model.Polla;
import co.edu.icesi.pollafutbolera.model.SubPolla;

import java.util.Arrays;
import java.util.List;

public class SubPollaUtil {

    public static List<SubPolla> createSampleSubPollas() {
        Polla polla1 = new Polla();
        polla1.setId(1L);

        Polla polla2 = new Polla();
        polla2.setId(2L);

        Polla polla3 = new Polla();
        polla3.setId(3L);

        SubPolla subPolla1 = SubPolla.builder()
                .id(1L)
                .isPrivate(true)
                .polla(polla1)
                .build();

        SubPolla subPolla2 = SubPolla.builder()
                .id(2L)
                .isPrivate(false)
                .polla(polla2)
                .build();

        SubPolla subPolla3 = SubPolla.builder()
                .id(3L)
                .isPrivate(true)
                .polla(polla3)
                .build();

        return Arrays.asList(subPolla1, subPolla2, subPolla3);
    }

    public static List<SubPollaCreateDTO> createSampleSubPollaCreateDTOs() {
        SubPollaCreateDTO dto1 = SubPollaCreateDTO.builder()
                .isPrivate(false)
                .pollaId(13L)
                .userId(13L)
                .build();

        SubPollaCreateDTO dto2 = SubPollaCreateDTO.builder()
                .isPrivate(true)
                .pollaId(11L)
                .userId(11L)
                .build();

        SubPollaCreateDTO dto3 = SubPollaCreateDTO.builder()
                .isPrivate(false)
                .pollaId(12L)
                .userId(12L)
                .build();

        return Arrays.asList(dto1, dto2, dto3);
    }

    public static List<SubPollaGetDTO> createSampleSubPollaGetDTOs() {
        SubPollaGetDTO dto1 = SubPollaGetDTO.builder()
                .id(1L)
                .isPrivate(false)
                .pollaId(10L)
                .build();

        SubPollaGetDTO dto2 = SubPollaGetDTO.builder()
                .id(2L)
                .isPrivate(true)
                .pollaId(11L)
                .build();

        SubPollaGetDTO dto3 = SubPollaGetDTO.builder()
                .id(3L)
                .isPrivate(false)
                .pollaId(12L)
                .build();

        return Arrays.asList(dto1, dto2, dto3);
    }
}