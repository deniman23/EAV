package src.main.eav.service;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import src.main.eav.controller.filter.EavEntityFilter;
import src.main.eav.dao.EavEntityDaoService;
import src.main.eav.dto.EavEntityDto;
import src.main.eav.dto.EavRelationDto;
import src.main.eav.error.ResourceNotFoundException;
import src.main.eav.model.EavEntity;
import src.main.eav.model.EavRelation;
import src.main.eav.service.impl.EavEntityServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EavEntityServiceImplTest {

    @Mock
    private EavEntityDaoService daoService;

    private EavEntityServiceImpl service;

    private AutoCloseable closeable;

    @BeforeMethod
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        service = new EavEntityServiceImpl(daoService);
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

        when(daoService.findById(id)).thenReturn(Optional.of(entity));

        EavEntityDto dto = service.findById(id);

        Assert.assertNotNull(dto, "DTO не должен быть null");
        Assert.assertEquals(dto.getId(), id, "ID сущности должен совпадать");
        Assert.assertEquals(dto.getType(), "TestEntity", "Тип сущности должен совпадать");

        verify(daoService, times(1)).findById(id);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testFindById_NotFound() {
        Long id = 2L;
        when(daoService.findById(id)).thenReturn(Optional.empty());
        service.findById(id);
    }

    @Test
    public void testFindByType() {
        String type = "TestEntity";
        EavEntity entity = new EavEntity();
        entity.setId(1L);
        entity.setType(type);
        entity.setAttributes(new HashSet<>());
        entity.setRelations(new HashSet<>());

        List<EavEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        when(daoService.findByType(type)).thenReturn(entityList);

        List<EavEntityDto> dtos = service.findByType(type);

        Assert.assertNotNull(dtos, "Список DTO не должен быть null");
        Assert.assertEquals(dtos.size(), 1, "Должна вернуться одна сущность");
        Assert.assertEquals(dtos.get(0).getType(), type, "Тип сущности должен совпадать");

        verify(daoService, times(1)).findByType(type);
    }

    @Test
    public void testFindAll() {
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

        List<EavEntity> entityList = new ArrayList<>();
        entityList.add(entity1);
        entityList.add(entity2);

        when(daoService.findAll()).thenReturn(entityList);

        List<EavEntityDto> dtos = service.findAll();

        Assert.assertNotNull(dtos, "Список DTO не должен быть null");
        Assert.assertEquals(dtos.size(), 2, "Должны вернуться две сущности");

        verify(daoService, times(1)).findAll();
    }

    // Новый тест для метода findAll(EavEntityFilter filter)
    @Test
    public void testFindAll_WithFilter() {
        EavEntityFilter filter = new EavEntityFilter(); // Параметры фильтра можно задать по необходимости

        EavEntity entity1 = new EavEntity();
        entity1.setId(1L);
        entity1.setType("FilteredEntity1");
        entity1.setAttributes(new HashSet<>());
        entity1.setRelations(new HashSet<>());

        EavEntity entity2 = new EavEntity();
        entity2.setId(2L);
        entity2.setType("FilteredEntity2");
        entity2.setAttributes(new HashSet<>());
        entity2.setRelations(new HashSet<>());

        List<EavEntity> entityList = new ArrayList<>();
        entityList.add(entity1);
        entityList.add(entity2);

        when(daoService.findAll(filter)).thenReturn(entityList);

        List<EavEntityDto> dtos = service.findAll(filter);

        Assert.assertNotNull(dtos, "Список DTO не должен быть null");
        Assert.assertEquals(dtos.size(), 2, "Должны вернуться две сущности");

        verify(daoService, times(1)).findAll(filter);
    }

    @Test
    public void testSave() {
        EavEntityDto dto = new EavEntityDto();
        dto.setType("NewEntity");
        dto.setAttributes(new HashSet<>());
        dto.setRelations(new HashSet<>());

        EavEntity savedEntity = new EavEntity();
        savedEntity.setId(10L);
        savedEntity.setType(dto.getType());
        savedEntity.setAttributes(new HashSet<>());
        savedEntity.setRelations(new HashSet<>());

        when(daoService.save(any(EavEntity.class))).thenReturn(savedEntity);

        EavEntityDto resultDto = service.save(dto);

        Assert.assertNotNull(resultDto, "Возвращенный DTO не должен быть null");
        Assert.assertEquals(resultDto.getId(), Long.valueOf(10L), "ID сохранённой сущности должен совпадать");
        Assert.assertEquals(resultDto.getType(), dto.getType(), "Тип сохранённой сущности должен совпадать");

        verify(daoService, times(1)).save(any(EavEntity.class));
    }

    @Test
    public void testUpdate_Success() {
        Long id = 5L;
        EavEntity existingEntity = new EavEntity();
        existingEntity.setId(id);
        existingEntity.setType("OldEntity");
        existingEntity.setAttributes(new HashSet<>());
        existingEntity.setRelations(new HashSet<>());

        when(daoService.findById(id)).thenReturn(Optional.of(existingEntity));

        EavEntityDto updateDto = new EavEntityDto();
        updateDto.setType("UpdatedEntity");
        updateDto.setAttributes(new HashSet<>());
        updateDto.setRelations(new HashSet<>());

        EavEntity updatedEntity = new EavEntity();
        updatedEntity.setId(id);
        updatedEntity.setType("UpdatedEntity");
        updatedEntity.setAttributes(new HashSet<>());
        updatedEntity.setRelations(new HashSet<>());

        when(daoService.edit(existingEntity)).thenReturn(updatedEntity);

        EavEntityDto resultDto = service.update(id, updateDto);

        Assert.assertNotNull(resultDto, "Возвращённый DTO не должен быть null");
        Assert.assertEquals(resultDto.getType(), "UpdatedEntity", "Тип сущности должен быть обновлён");

        verify(daoService, times(1)).findById(id);
        verify(daoService, times(1)).edit(existingEntity);
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void testUpdate_NotFound() {
        Long id = 20L;
        when(daoService.findById(id)).thenReturn(Optional.empty());

        EavEntityDto updateDto = new EavEntityDto();
        updateDto.setType("UpdatedEntity");
        updateDto.setAttributes(new HashSet<>());
        updateDto.setRelations(new HashSet<>());

        service.update(id, updateDto);
    }

    @Test
    public void testUpdate_WithRelations() {
        Long mainId = 5L;
        EavEntity existingEntity = new EavEntity();
        existingEntity.setId(mainId);
        existingEntity.setType("OldEntity");
        existingEntity.setAttributes(new HashSet<>());
        existingEntity.setRelations(new HashSet<>());

        when(daoService.findById(mainId)).thenReturn(Optional.of(existingEntity));

        EavEntityDto updateDto = new EavEntityDto();
        updateDto.setType("UpdatedEntity");
        // Подготавливаем DTO отношения
        EavRelationDto relationDto = new EavRelationDto();
        relationDto.setTargetId(100L);
        relationDto.setRelationType("friend");
        HashSet<EavRelationDto> relationsDto = new HashSet<>();
        relationsDto.add(relationDto);
        updateDto.setRelations(relationsDto);
        updateDto.setAttributes(new HashSet<>());

        // Stub поиска целевой сущности для отношения
        EavEntity targetEntity = new EavEntity();
        targetEntity.setId(100L);
        targetEntity.setType("TargetEntity");
        targetEntity.setAttributes(new HashSet<>());
        targetEntity.setRelations(new HashSet<>());
        when(daoService.findById(100L)).thenReturn(Optional.of(targetEntity));

        // После обновления возвращаем обновлённую сущность с добавленным отношением
        EavEntity updatedEntity = new EavEntity();
        updatedEntity.setId(mainId);
        updatedEntity.setType("UpdatedEntity");
        updatedEntity.setAttributes(new HashSet<>());

        HashSet<EavRelation> updatedRelations = new HashSet<>();
        EavRelation relation = new EavRelation();
        relation.setRelationType("friend");
        relation.setTarget(targetEntity);
        relation.setSource(existingEntity);
        updatedRelations.add(relation);
        updatedEntity.setRelations(updatedRelations);

        when(daoService.edit(existingEntity)).thenReturn(updatedEntity);

        EavEntityDto resultDto = service.update(mainId, updateDto);

        Assert.assertNotNull(resultDto, "Возвращённый DTO не должен быть null");
        Assert.assertEquals(resultDto.getType(), "UpdatedEntity", "Тип сущности должен быть обновлён");
        Assert.assertNotNull(resultDto.getRelations(), "Список отношений не должен быть null");
        Assert.assertEquals(resultDto.getRelations().size(), 1, "Должна быть одна связь");

        EavRelationDto resultRelation = resultDto.getRelations().iterator().next();
        Assert.assertEquals(resultRelation.getTargetId(), Long.valueOf(100L), "Целевая сущность должна иметь id 100");

        verify(daoService, times(1)).findById(mainId);
        verify(daoService, times(1)).findById(100L);
        verify(daoService, times(1)).edit(existingEntity);
    }

    @Test
    public void testDeleteEntity() {
        Long id = 3L;
        service.deleteEntity(id);
        verify(daoService, times(1)).deleteEntity(id);
    }
}