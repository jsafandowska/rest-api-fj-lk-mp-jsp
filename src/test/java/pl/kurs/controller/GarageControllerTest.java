package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.repository.GarageRepository;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class GarageControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper obj;

    @Autowired
    private GarageRepository garageRepository;

    @BeforeEach
    public void init() {
        garageRepository.deleteAll();
    }

    @Test
    public void shouldReturnSingleGarage() throws Exception {
        Garage garage = garageRepository.saveAndFlush(new Garage(3, "Gdańsk", true));
        int id = garage.getId();
        postman.perform(get("/api/v1/garages/" + id))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.places").value(3))
               .andExpect(jsonPath("$.address").value("Gdańsk"))
               .andExpect(jsonPath("$.lpgAllowed").value(true));
    }

    @Test
    public void shouldReturnAllGarages() throws Exception {
        garageRepository.deleteAll();
        Garage garage1 = garageRepository.saveAndFlush(new Garage(3, "Gdańsk", true));
        Garage garage2 = garageRepository.saveAndFlush(new Garage(3, "Gdańsk", false));

        postman.perform(get("/api/v1/garages"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(garage1.getId()))
               .andExpect(jsonPath("$[0].places").value(3))
               .andExpect(jsonPath("$[0].address").value("Gdańsk"))
               .andExpect(jsonPath("$[0].lpgAllowed").value(true))
               .andExpect(jsonPath("$[1].id").value(garage2.getId()))
               .andExpect(jsonPath("$[1].places").value(3))
               .andExpect(jsonPath("$[1].address").value("Gdańsk"))
               .andExpect(jsonPath("$[1].lpgAllowed").value(false));
    }

    @Test
    public void shouldAddGarage() throws Exception {
        CreateGarageCommand command = new CreateGarageCommand(2, "Sopot", true);
        String json = obj.writeValueAsString(command);

        String responseString = postman.perform(post("/api/v1/garages")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(json))
                                       .andExpect(status().isCreated())
                                       .andExpect(jsonPath("$.id").exists())
                                       .andExpect(jsonPath("$.places").value(2))
                                       .andExpect(jsonPath("$.address").value("Sopot"))
                                       .andExpect(jsonPath("$.lpgAllowed").value(true))
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        Garage saved = obj.readValue(responseString, Garage.class);
        Garage recentlyAdded = garageRepository.findById(saved.getId()).get();
        Assertions.assertEquals(2, recentlyAdded.getPlaces());
        Assertions.assertEquals("Sopot", recentlyAdded.getAddress());
        Assertions.assertTrue(recentlyAdded.isLpgAllowed());
        Assertions.assertTrue(recentlyAdded.getId() > 0);

    }

    @Test
    public void shouldDeleteGarage() throws Exception {
        Garage garageToDelete = garageRepository.saveAndFlush(new Garage(1, "Warszawa", true));
        int id = garageToDelete.getId();

        postman.perform(delete("/api/v1/garages/" + id))
               .andExpect(status().isNoContent());

        boolean garageExists = garageRepository.findById(id).isPresent();
        Assertions.assertFalse(garageExists, "The garage should be deleted from the repository");
    }

    @Test
    public void shouldEditGarage() throws Exception {
        Garage garage = garageRepository.saveAndFlush(new Garage(1, "oldAddress", false));
        int id = garage.getId();

        CreateGarageCommand command = new CreateGarageCommand(2, "newAddress", true);
        String json = obj.writeValueAsString(command);

        postman.perform(put("/api/v1/garages/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.places").value(2))
               .andExpect(jsonPath("$.address").value("newAddress"))
               .andExpect(jsonPath("$.lpgAllowed").value(true));

        Garage editedGarage = garageRepository.findById(id).get();
        Assertions.assertEquals(2, editedGarage.getPlaces());
        Assertions.assertEquals("newAddress", editedGarage.getAddress());
        Assertions.assertTrue(editedGarage.isLpgAllowed());
    }

    @Test
    public void shouldEditGaragePartially() throws Exception {
        Garage garage = garageRepository.saveAndFlush(new Garage(1, "oldAddress", false));
        int id = garage.getId();

        EditGarageCommand command = new EditGarageCommand(null, "newAddress", null);
        String json = obj.writeValueAsString(command);

        postman.perform(patch("/api/v1/garages/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.places").value(1))
               .andExpect(jsonPath("$.address").value("newAddress"))
               .andExpect(jsonPath("$.lpgAllowed").value(false));

        Garage editedGarage = garageRepository.findById(id).get();
        Assertions.assertEquals(1, editedGarage.getPlaces());
        Assertions.assertEquals("newAddress", editedGarage.getAddress());
        Assertions.assertFalse(editedGarage.isLpgAllowed());
    }
}
