package src.main.eav.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import src.main.eav.controller.filter.EavEntityFilter;
import src.main.eav.dto.EavEntityDto;
import src.main.eav.service.EavEntityService;

import java.util.List;

@RestController
@RequestMapping("/api/entities")
public class EavEntityController {

    private final EavEntityService eavEntityService;

    @Autowired
    public EavEntityController(EavEntityService eavEntityService) {
        this.eavEntityService = eavEntityService;
    }

    /**
     * API для создания сущности.
     * Получает DTO, преобразует его в сущность и возвращает сохранённый объект в виде DTO.
     */
    @PostMapping
    public ResponseEntity<EavEntityDto> createEntity(@RequestBody EavEntityDto dto) {
        EavEntityDto createdEntity = eavEntityService.save(dto);
        return ResponseEntity.ok(createdEntity);
    }

    /**
     * API для получения сущности по идентификатору, включая атрибуты и связи.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EavEntityDto> getEntity(@PathVariable Long id) {
        EavEntityDto entityDto = eavEntityService.findById(id);
        return ResponseEntity.ok(entityDto);
    }

    /**
     * API для поиска сущностей.
     * Если передан параметр type (например, /api/entities?type=example), то выполняется поиск по типу;
     * если же переданы другие параметры фильтра (например, search, sortOrder и т.п.) через EavEntityFilter,
     * то используется фильтрация с динамическим построением запроса.
     * Если ничего не передано — возвращаются все сущности.
     */
    @GetMapping
    public ResponseEntity<List<EavEntityDto>> getEntities(
            @RequestParam(required = false) String type,
            EavEntityFilter filter) {
        List<EavEntityDto> dtos;
        if (type != null && !type.isEmpty()) {
            dtos = eavEntityService.findByType(type);
        } else if (filter.getSearch() != null && !filter.getSearch().trim().isEmpty()) {
            dtos = eavEntityService.findAll(filter);
        } else {
            dtos = eavEntityService.findAll();
        }
        return ResponseEntity.ok(dtos);
    }


    /**
     * API для обновления сущности.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EavEntityDto> updateEntity(@PathVariable Long id, @RequestBody EavEntityDto dto) {
        EavEntityDto updatedEntity = eavEntityService.update(id, dto);
        return ResponseEntity.ok(updatedEntity);
    }

    /**
     * API для удаления сущности по id.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntity(@PathVariable Long id) {
        eavEntityService.deleteEntity(id);
        return ResponseEntity.noContent().build();
    }
}