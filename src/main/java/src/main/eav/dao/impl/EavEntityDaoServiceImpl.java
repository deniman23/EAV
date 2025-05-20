package src.main.eav.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import src.main.eav.dao.EavEntityDaoService;
import src.main.eav.model.EavEntity;
import src.main.eav.repository.EavEntityRepository;
import src.main.eav.dao.specification.EavEntitySpecification;
import src.main.eav.controller.filter.EavEntityFilter;

import java.util.List;
import java.util.Optional;

@Service
public class EavEntityDaoServiceImpl implements EavEntityDaoService {

    private final EavEntityRepository repository;
    private final EavEntitySpecification specification;

    @Autowired
    public EavEntityDaoServiceImpl(EavEntityRepository repository, EavEntitySpecification specification) {
        this.repository = repository;
        this.specification = specification;
    }

    @Override
    public Optional<EavEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<EavEntity> findByType(String type) {
        return repository.findAll((root, query, cb) ->
                cb.equal(root.get("type"), type)
        );
    }

    @Override
    public List<EavEntity> findAll() {
        return repository.findAll();
    }


    public List<EavEntity> findAll(EavEntityFilter filter) {
        return repository.findAll(specification.getFilter(filter));
    }

    @Override
    public EavEntity save(EavEntity entity) {
        return repository.save(entity);
    }

    @Override
    public EavEntity edit(EavEntity entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteEntity(Long id) {
        repository.deleteById(id);
    }
}