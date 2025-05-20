package src.main.eav.service;

import src.main.eav.controller.filter.EavEntityFilter;
import src.main.eav.dto.EavEntityDto;

import java.util.List;

public interface EavEntityService {
    EavEntityDto findById(Long id);

    List<EavEntityDto> findByType(String type);

    List<EavEntityDto> findAll();

    List<EavEntityDto> findAll(EavEntityFilter filter);

    EavEntityDto save(EavEntityDto dto);

    EavEntityDto update(Long id, EavEntityDto dto);

    void deleteEntity(Long id);
}