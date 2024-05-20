package pl.kurs.controller;

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
import pl.kurs.Main;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.service.GarageIdGenerator;

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
    private ObjectMapper obj;
    @SpyBean
    private GarageIdGenerator idGenerator;

    @Test
    public void shouldReturnSingleGarage() throws Exception {
       Garage garage = new Garage(idGenerator.getId(), 3, "Gdańsk", true);
       garages.add(garage);
        postman.perform(get("/api/v1/garages/"+garage.getId()))
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

        postman.perform(post("/api/v1/garages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.places").value(2))
                .andExpect(jsonPath("$.address").value("Sopot"))
                .andExpect(jsonPath("$.lpgAllowed").value(true));

        Garage recentlyAdded = garages.get(garages.size() - 1);
        Assertions.assertEquals(2, recentlyAdded.getPlaces());
        Assertions.assertEquals("Sopot", recentlyAdded.getAddress());
        Assertions.assertTrue(recentlyAdded.isLpgAllowed());
        Assertions.assertTrue(recentlyAdded.getId() > 0);

        Mockito.verify(idGenerator, Mockito.times(1)).getId();
    }

    //
    @Test
    public void shouldDeleteGarage() throws Exception {
        Garage garageToDelete = new Garage(idGenerator.getId(), 1, "Warszawa", true);
        garages.add(garageToDelete);

        postman.perform(delete("/api/v1/garages/" + garageToDelete.getId()))
                .andExpect(status().isNoContent());
        boolean garageExists = garages.stream().anyMatch(garage -> garage.getId() == garageToDelete.getId());
        Assertions.assertFalse(garageExists, "The garage should be deleted from the list");
    }

    @Test
    public void shouldEditGarage() throws Exception {
        Garage garage = new Garage(idGenerator.getId(), 1, "oldAddress", false);
        garages.add(garage);

        CreateGarageCommand command = new CreateGarageCommand(2, "newAddress", true);
        String json = obj.writeValueAsString(command);

        postman.perform(put("/api/v1/garages/" + garage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.places").value(2))
                .andExpect(jsonPath("$.address").value("newAddress"))
                .andExpect(jsonPath("$.lpgAllowed").value(true));
        Garage createdgarage = garages.stream().filter(b -> b.getId() == garage.getId()).findFirst().orElse(null);

        Assertions.assertNotNull(createdgarage, "The book should exist in the list");
        Assertions.assertEquals(2, createdgarage.getPlaces());
        Assertions.assertEquals("newAddress", createdgarage.getAddress());
        Assertions.assertTrue(createdgarage.isLpgAllowed());
    }

    @Test
    public void shouldEditGaragePartially() throws Exception {

        Garage garage = new Garage(idGenerator.getId(), 1, "oldAddress", false);
        garages.add(garage);

        EditGarageCommand command = new EditGarageCommand(null, "newAddress", null);
        String json = obj.writeValueAsString(command);
        postman.perform(patch("/api/v1/garages/" + garage.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.places").value(1))
                .andExpect(jsonPath("$.address").value("newAddress"))
                .andExpect(jsonPath("$.lpgAllowed").value(false));
        Garage editedGarage = garages.stream().filter(b -> b.getId() == garage.getId()).findFirst().orElse(null);

        Assertions.assertNotNull(editedGarage, "The garage should exist in the list");
        Assertions.assertEquals(1, editedGarage.getPlaces());
        Assertions.assertEquals("newAddress", editedGarage.getAddress());
        Assertions.assertFalse(editedGarage.isLpgAllowed());

    }
}
