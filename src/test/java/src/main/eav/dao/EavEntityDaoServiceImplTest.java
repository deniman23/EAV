package src.main.eav.dao;

import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import src.main.eav.controller.filter.EavEntityFilter;
import src.main.eav.dao.impl.EavEntityDaoServiceImpl;
import src.main.eav.model.EavEntity;
import src.main.eav.repository.EavEntityRepository;
import src.main.eav.dao.specification.EavEntitySpecification;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EavEntityDaoServiceImplTest {

    @Mock
    private EavEntityRepository repository;

    @Mock
    private EavEntitySpecification specification;

    private EavEntityDaoService daoService;

    private AutoCloseable closeable;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        daoService = new EavEntityDaoServiceImpl(repository, specification);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testFindById_Success() {
        Long id = 1L;
        EavEntity entity = new EavEntity();
        entity.setId(id);
        entity.setType("TestEntity");
        entity.setAttributes(new HashSet<>());
        entity.setRelations(new HashSet<>());

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        Optional<EavEntity> result = daoService.findById(id);

        Assert.assertTrue(result.isPresent(), "Entity должна быть найдена");
        Assert.assertEquals(result.get().getId(), id, "ID сущности должен совпадать");
        Assert.assertEquals(result.get().getType(), "TestEntity", "Тип сущности должен совпадать");

        verify(repository, times(1)).findById(id);
    }

    @Test
    public void testFindByType() {
        String type = "TestEntity";
        EavEntity entity = new EavEntity();
        entity.setId(1L);
        entity.setType(type);
        entity.setAttributes(new HashSet<>());
        entity.setRelations(new HashSet<>());

        List<EavEntity> entityList = Collections.singletonList(entity);

        // Явно указываем, что аргумент any() следует трактовать как Specification<EavEntity>
        when(repository.findAll(ArgumentMatchers.<Specification<EavEntity>>any())).thenReturn(entityList);

        List<EavEntity> result = daoService.findByType(type);

        Assert.assertNotNull(result, "Список сущностей не должен быть null");
        Assert.assertEquals(result.size(), 1, "Должна быть возвращена одна сущность");
        Assert.assertEquals(result.get(0).getType(), type, "Тип сущности должен совпадать");

        verify(repository, times(1)).findAll(ArgumentMatchers.<Specification<EavEntity>>any());
    }

    @Test
    public void testFindAll_NoFilter() {
        EavEntity entity1 = new EavEntity();
        entity1.setId(1L);
        entity1.setType("Entity1");
        entity1.setAttributes(new HashSet<>());
        entity1.setRelations(new HashSet<>());

        EavEntity entity2 = new EavEntity();
        entity2.setId(2L);
        entity2.setType("Entity2");
        entity2.setAttributes(new HashSet<>());
        entity2.setRelations(new HashSet<>());

        List<EavEntity> entityList = Arrays.asList(entity1, entity2);

        when(repository.findAll()).thenReturn(entityList);

        List<EavEntity> result = daoService.findAll();

        Assert.assertNotNull(result, "Результирующий список не должен быть null");
        Assert.assertEquals(result.size(), 2, "Должны быть возвращены две сущности");

        verify(repository, times(1)).findAll();
    }

    @Test
    public void testFindAll_WithFilter() {
        EavEntityFilter filter = new EavEntityFilter();
        // Создаем мнимую Specification, которую должен вернуть specification.getFilter(filter)
        Specification<EavEntity> specMock = mock(Specification.class);
        when(specification.getFilter(filter)).thenReturn(specMock);

        EavEntity entity = new EavEntity();
        entity.setId(1L);
        entity.setType("FilteredEntity");
        entity.setAttributes(new HashSet<>());
        entity.setRelations(new HashSet<>());

        List<EavEntity> entityList = Collections.singletonList(entity);

        when(repository.findAll(specMock)).thenReturn(entityList);

        List<EavEntity> result = daoService.findAll(filter);

        Assert.assertNotNull(result, "Список сущностей не должен быть null");
        Assert.assertEquals(result.size(), 1, "Должна быть возвращена одна сущность");
        Assert.assertEquals(result.get(0).getType(), "FilteredEntity", "Тип сущности должен совпадать");

        verify(specification, times(1)).getFilter(filter);
        verify(repository, times(1)).findAll(specMock);
    }

    @Test
    public void testSave() {
        EavEntity entity = new EavEntity();
        entity.setType("NewEntity");
        entity.setAttributes(new HashSet<>());
        entity.setRelations(new HashSet<>());

        EavEntity savedEntity = new EavEntity();
        savedEntity.setId(10L);
        savedEntity.setType(entity.getType());
        savedEntity.setAttributes(new HashSet<>());
        savedEntity.setRelations(new HashSet<>());

        when(repository.save(entity)).thenReturn(savedEntity);

        EavEntity result = daoService.save(entity);

        Assert.assertNotNull(result, "Сохраненная сущность не должна быть null");
        Assert.assertEquals(result.getId(), Long.valueOf(10L), "ID сохраненной сущности должен совпадать");
        Assert.assertEquals(result.getType(), "NewEntity", "Тип сохраненной сущности должен совпадать");

        verify(repository, times(1)).save(entity);
    }

    @Test
    public void testEdit() {
        EavEntity entity = new EavEntity();
        entity.setId(5L);
        entity.setType("OldEntity");
        entity.setAttributes(new HashSet<>());
        entity.setRelations(new HashSet<>());

        EavEntity updatedEntity = new EavEntity();
        updatedEntity.setId(5L);
        updatedEntity.setType("UpdatedEntity");
        updatedEntity.setAttributes(new HashSet<>());
        updatedEntity.setRelations(new HashSet<>());

        when(repository.save(entity)).thenReturn(updatedEntity);

        EavEntity result = daoService.edit(entity);

        Assert.assertNotNull(result, "Измененная сущность не должна быть null");
        Assert.assertEquals(result.getType(), "UpdatedEntity", "Тип сущности должен быть обновлен");

        verify(repository, times(1)).save(entity);
    }

    @Test
    public void testDeleteEntity() {
        Long id = 3L;
        daoService.deleteEntity(id);
        verify(repository, times(1)).deleteById(id);
    }
}