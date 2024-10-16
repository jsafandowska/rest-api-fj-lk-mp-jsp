package pl.kurs.dictionary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.command.CreateDictionaryValuesCommand;
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.repository.DictionaryRepository;
import pl.kurs.dictionary.repository.DictionaryValueRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class DictionaryControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DictionaryRepository dictionaryRepository;
    @Autowired
    private DictionaryValueRepository dictionaryValueRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldReturnSingleDictionary() throws Exception {
        int id = dictionaryRepository.saveAndFlush(new Dictionary("test dictionary 1")).getId();

        postman.perform(get("/api/v1/dictionaries/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("test dictionary 1"))
                .andExpect(jsonPath("$.values").isEmpty())
                .andExpect(jsonPath("$.deleted").value(false));
    }

    @Test
    void shouldSaveDictionary() throws Exception {
        Set<String> testValues = Set.of("test value 1", "test value 2");
        CreateDictionaryCommand testCommand = CreateDictionaryCommand.builder()
                .name("test dictionary")
                .launchValues(testValues)
                .build();

        String json = objectMapper.writeValueAsString(testCommand);
        String responseJson = postman.perform(post("/api/v1/dictionaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("test dictionary"))
                .andExpect(jsonPath("$.values", hasSize(2)))
                .andExpect(jsonPath("$.values[0]").isNotEmpty())
                .andExpect(jsonPath("$.values[1]").isNotEmpty())
                .andExpect(jsonPath("$.deleted").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DictionaryDto saved = objectMapper.readValue(responseJson, DictionaryDto.class);
        Dictionary recentlyAdded = dictionaryRepository.findByIdWithValues(saved.id()).get();
        assertEquals("test dictionary", recentlyAdded.getName());
        assertFalse(recentlyAdded.isDeleted());
        Set<String> values = new HashSet<>();
        for (DictionaryValue dictionaryValue : recentlyAdded.getValues()) {
            values.add(dictionaryValue.getValue());
        }
        assertTrue(values.contains("test value 1"));
        assertTrue(values.contains("test value 2"));
    }

    @Test
    void shouldDeleteDictionaryAndAllValues() throws Exception {
        int id = dictionaryRepository.saveAndFlush(new Dictionary("test dictionary 2")).getId();
        Set<String> testValues = Set.of("test value 1", "test value 2");
        CreateDictionaryValuesCommand testCommand = CreateDictionaryValuesCommand.builder()
                .dictionaryId(id)
                .values(testValues)
                .build();
        System.out.println("-----------------sekcja 0-------------------");
        String json = objectMapper.writeValueAsString(testCommand);
        postman.perform(post("/api/v1/dictionaries/" + id + "/values")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        System.out.println("-----------------sekcja 1-------------------");
        Dictionary beforeDeleted = dictionaryRepository.findByIdWithValues(id).get();
        System.out.println("-----------------sekcja 2-------------------");
        Session session = entityManager.unwrap(Session.class);
        Statistics statistics = session.getSessionFactory().getStatistics();
        statistics.setStatisticsEnabled(true);
        System.out.println("QUERY COUNT STATISTICS IS ENABLE: " + statistics.isStatisticsEnabled());
        statistics.clear();
        System.out.println("QUERY COUNT BEFORE METHOD EXECUTION: " + statistics.getQueries().length);
        assertEquals(0, statistics.getQueries().length);
        postman.perform(delete("/api/v1/dictionaries/" + id))
                .andExpect(status().isNoContent());
        String[] queries = statistics.getQueries();
        System.out.println(Arrays.toString(queries));
        System.out.println("QUERY COUNT AFTER METHOD EXECUTION: " + queries.length);
        assertEquals(2, queries.length);
        System.out.println("-----------------sekcja 3-------------------");
        assertFalse(dictionaryRepository.existsById(id));
        beforeDeleted.getValues().stream().forEach(value -> assertFalse(dictionaryValueRepository.existsById(value.getId())));
    }


}