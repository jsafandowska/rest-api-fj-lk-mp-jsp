package pl.kurs.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;
import pl.kurs.service.CarService;
import pl.kurs.service.GarageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
public class GarageControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper obj;
    @Autowired
    private GarageService garageService;


    @Test
    public void shouldReturnSingleGarage() throws Exception {
        Garage garage = garageService.addGarage(new CreateGarageCommand(3, "Gdańsk", true));
        postman.perform(get("/api/v1/garages/" + garage.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(garage.getId()))
                .andExpect(jsonPath("$.places").value(3))
                .andExpect(jsonPath("$.address").value("Gdańsk"))
                .andExpect(jsonPath("$.lpgAllowed").value(true));
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
        Garage recentlyAdded = garageService.findGarage(saved.getId());
        Assertions.assertEquals(2, recentlyAdded.getPlaces());
        Assertions.assertEquals("Sopot", recentlyAdded.getAddress());
        assertTrue(recentlyAdded.isLpgAllowed());
        assertTrue(recentlyAdded.getId() > 0);
        Assertions.assertEquals(saved.getId(), recentlyAdded.getId());
    }

//    @Test
//    public void shouldDeleteGarage() throws Exception {
//        Garage garageToDelete = garageService.addGarage(new CreateGarageCommand(1, "Warszawa", true));
//        postman.perform(delete("/api/v1/garages/" + garageToDelete.getId()))
//                .andExpect(status().isNoContent());
//        boolean garageExists = garageRepository.existsById(garageToDelete.getId());
//        assertFalse(garageExists, "The garage should be deleted from the list");
//    }

    @Test
    public void shouldEditGarage() throws Exception {
        Garage garage = garageService.addGarage(new CreateGarageCommand(1, "oldAddress", false));
        CreateGarageCommand command = new CreateGarageCommand(2, "newAddress", true);
        String json = obj.writeValueAsString(command);
        String responseString = postman.perform(put("/api/v1/garages/" + garage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.places").value(2))
                .andExpect(jsonPath("$.address").value("newAddress"))
                .andExpect(jsonPath("$.lpgAllowed").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Garage saved = obj.readValue(responseString, Garage.class);
        Garage recentlyAdded = garageService.findGarage(saved.getId());
        Assertions.assertNotNull(recentlyAdded, "The book should exist in the list");
        Assertions.assertEquals(2, recentlyAdded.getPlaces());
        Assertions.assertEquals("newAddress", recentlyAdded.getAddress());
        assertTrue(recentlyAdded.isLpgAllowed());
        assertTrue(recentlyAdded.getId() > 0);
    }

    @Test
    public void shouldEditGaragePartially() throws Exception {
        Garage garage = garageService.addGarage(new CreateGarageCommand(1, "oldAddress", false));
        EditGarageCommand command = new EditGarageCommand(null, "newAddress", null);
        String json = obj.writeValueAsString(command);
        String responseString = postman.perform(patch("/api/v1/garages/" + garage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.places").value(1))
                .andExpect(jsonPath("$.address").value("newAddress"))
                .andExpect(jsonPath("$.lpgAllowed").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Garage saved = obj.readValue(responseString, Garage.class);
        Garage recentlyAdded = garageService.findGarage(saved.getId());
        Assertions.assertNotNull(recentlyAdded, "The garage should exist in the list");
        Assertions.assertEquals(1, recentlyAdded.getPlaces());
        Assertions.assertEquals("newAddress", recentlyAdded.getAddress());
        assertFalse(recentlyAdded.isLpgAllowed());
    }
}



