package src.main.eav.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import src.main.eav.controller.filter.EavEntityFilter;
import src.main.eav.dto.EavEntityDto;
import src.main.eav.service.EavEntityService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(EavEntityControllerTest.TestConfig.class)
@WebMvcTest(EavEntityController.class)
public class EavEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Теперь бин EavEntityService предоставляется из тестовой конфигурации TestConfig
    @Autowired
    private EavEntityService eavEntityService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public EavEntityService eavEntityService() {
            // Создаем мок EavEntityService вручную
            return Mockito.mock(EavEntityService.class);
        }
    }

    @Test
    public void testCreateEntity() throws Exception {
        EavEntityDto requestDto = new EavEntityDto();
        requestDto.setType("TestEntity");

        EavEntityDto responseDto = new EavEntityDto();
        responseDto.setId(1L);
        responseDto.setType("TestEntity");

        when(eavEntityService.save(any(EavEntityDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/entities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("TestEntity"));
    }

    @Test
    public void testGetEntity() throws Exception {
        EavEntityDto responseDto = new EavEntityDto();
        responseDto.setId(1L);
        responseDto.setType("TestEntity");

        when(eavEntityService.findById(1L)).thenReturn(responseDto);

        mockMvc.perform(get("/api/entities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("TestEntity"));
    }

    @Test
    public void testGetEntitiesByType() throws Exception {
        EavEntityDto dto = new EavEntityDto();
        dto.setId(1L);
        dto.setType("FilteredType");

        List<EavEntityDto> dtoList = Collections.singletonList(dto);

        when(eavEntityService.findByType("FilteredType")).thenReturn(dtoList);

        mockMvc.perform(get("/api/entities")
                        .param("type", "FilteredType")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("FilteredType"));
    }

    @Test
    public void testGetEntitiesWithFilter() throws Exception {
        EavEntityDto dto = new EavEntityDto();
        dto.setId(2L);
        dto.setType("SearchEntity");

        List<EavEntityDto> dtoList = Collections.singletonList(dto);

        when(eavEntityService.findAll(any(EavEntityFilter.class))).thenReturn(dtoList);

        mockMvc.perform(get("/api/entities")
                        .param("search", "searchText")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].type").value("SearchEntity"));
    }

    @Test
    public void testGetAllEntities() throws Exception {
        EavEntityDto dto1 = new EavEntityDto();
        dto1.setId(1L);
        dto1.setType("Entity1");

        EavEntityDto dto2 = new EavEntityDto();
        dto2.setId(2L);
        dto2.setType("Entity2");

        List<EavEntityDto> dtoList = Arrays.asList(dto1, dto2);

        when(eavEntityService.findAll()).thenReturn(dtoList);

        mockMvc.perform(get("/api/entities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type").value("Entity1"))
                .andExpect(jsonPath("$[1].type").value("Entity2"));
    }

    @Test
    public void testUpdateEntity() throws Exception {
        EavEntityDto updateDto = new EavEntityDto();
        updateDto.setType("UpdatedEntity");

        EavEntityDto responseDto = new EavEntityDto();
        responseDto.setId(1L);
        responseDto.setType("UpdatedEntity");

        when(eavEntityService.update(eq(1L), any(EavEntityDto.class))).thenReturn(responseDto);

        mockMvc.perform(put("/api/entities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.type").value("UpdatedEntity"));
    }

    @Test
    public void testDeleteEntity() throws Exception {
        doNothing().when(eavEntityService).deleteEntity(1L);

        mockMvc.perform(delete("/api/entities/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}