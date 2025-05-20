package src.main.eav.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import src.main.eav.controller.filter.EavEntityFilter;
import src.main.eav.model.EavEntity;

import java.util.List;
import java.util.Optional;

public interface EavEntityDaoService {

    Optional<EavEntity> findById(Long id);

    List<EavEntity> findByType(String type);

    List<EavEntity> findAll();

    List<EavEntity> findAll(EavEntityFilter filter);

    EavEntity save(EavEntity entity);

    EavEntity edit(EavEntity entity);

    void deleteEntity(Long id);
}