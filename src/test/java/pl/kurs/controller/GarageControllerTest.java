package pl.kurs.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.Main;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.service.GarageIdGenerator;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class GarageControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private List<Garage> garages;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private GarageIdGenerator garageIdGenerator;


    @Test
    public void shouldReturnSingleGarage() throws Exception {
        Garage garage = new Garage(garageIdGenerator.getId(), 4, "Gdansk", true);
        garages.add(garage);

        postman.perform(get("/api/v1/garages/" + garage.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garage.getId()))
                .andExpect(jsonPath("$.places").value(4))
                .andExpect(jsonPath("$.address").value("Gdansk"))
                .andExpect(jsonPath("$.lpgAllowed").value(true));
    }

    @Test
    public void shouldAddGarage() throws Exception {
        CreateGarageCommand command = new CreateGarageCommand(4, "Gdynia", true);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(post("/api/v1/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.places").value(4))
                .andExpect(jsonPath("$.address").value("Gdynia"))
                .andExpect(jsonPath("$.lpgAllowed").value(true));
        Garage recentlyAdded = garages.get(garages.size() - 1);
        Assertions.assertEquals(4, recentlyAdded.getPlaces());
        Assertions.assertEquals("Gdynia", recentlyAdded.getAddress());
        Assertions.assertTrue(recentlyAdded.isLpgAllowed());
        Mockito.verify(garageIdGenerator, Mockito.times(1)).getId();
    }

    @Test
    public void shouldFindGarage() throws Exception {
        Garage garage = new Garage(garageIdGenerator.getId(), 4, "Gdynia", true);
        garages.add(garage);


        postman.perform(get("/api/v1/garages/" + garage.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garage.getId()))
                .andExpect(jsonPath("$.places").value(4))
                .andExpect(jsonPath("$.address").value("Gdynia"))
                .andExpect(jsonPath("$.lpgAllowed").value(true));
    }

    @Test
    public void shouldDeleteGarage() throws Exception {
        Garage garage = new Garage(garageIdGenerator.getId(), 4, "Gdansk", true);
        garages.add(garage);

        postman.perform(delete("/api/v1/garages/" + garage.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Assertions.assertFalse(garages.stream().anyMatch(g -> g.getId() == garage.getId()));
    }

    @Test
    public void shouldEditGarage() throws Exception {
        Garage garage = new Garage(garageIdGenerator.getId(), 4, "Gdansk", true);
        garages.add(garage);

        EditGarageCommand command = new EditGarageCommand(10, "Gdynia", false);
        String json = objectMapper.writeValueAsString(command);
        postman.perform(put("/api/v1/garages/"+garage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.id").value(garage.getId()))
                .andExpect(jsonPath("$.places").value(10))
                .andExpect(jsonPath("$.address").value("Gdynia"))
                .andExpect(jsonPath("$.lpgAllowed").value(false));

        Garage updatedGarage = garages.stream().filter(g -> g.getId() == garage.getId()).findFirst().orElseThrow(GarageNotFoundException::new);
        Assertions.assertEquals(10, updatedGarage.getPlaces());
        Assertions.assertEquals("Gdynia", updatedGarage.getAddress());
        Assertions.assertFalse(updatedGarage.isLpgAllowed());

    }

    @Test
    public void shouldPartiallyEditGarage() throws Exception {
        Garage garage = new Garage(garageIdGenerator.getId(), 4, "Gdansk", true);
        garages.add(garage);
        EditGarageCommand command = new EditGarageCommand(null, "Gdynia", null);
        String json = objectMapper.writeValueAsString(command);
        postman.perform(patch("/api/v1/garages/"+garage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.id").value(garage.getId()))
                .andExpect(jsonPath("$.places").value(4))
                .andExpect(jsonPath("$.address").value("Gdynia"))
                .andExpect(jsonPath("$.lpgAllowed").value(true));
        Garage updatedGarage = garages.stream().filter(g -> g.getId() == garage.getId()).findFirst().orElseThrow(GarageNotFoundException::new);
        Assertions.assertEquals(4, updatedGarage.getPlaces());
        Assertions.assertEquals("Gdynia", updatedGarage.getAddress());
        Assertions.assertTrue(updatedGarage.isLpgAllowed());
    }

}