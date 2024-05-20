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
class GarageControllerTest {

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
        garages.add(new Garage(garageIdGenerator.getId(), 100, "Prosta 2", true));

        postman.perform(get("/api/v1/garages/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.places").value(100))
               .andExpect(jsonPath("$.address").value("Prosta 2"))
               .andExpect(jsonPath("$.lpgAllowed").value(true));
    }


    @Test
    public void shouldAddGarage() throws Exception {
        CreateGarageCommand command = new CreateGarageCommand(200, "Kreta 10", false);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(post("/api/v1/garages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.places").value(200))
               .andExpect(jsonPath("$.address").value("Kreta 10"))
               .andExpect(jsonPath("$.lpgAllowed").value(false));

        Garage recentlyAdded = garages.get(garages.size() - 1);
        Assertions.assertEquals(200, recentlyAdded.getPlaces());
        Assertions.assertEquals("Kreta 10", recentlyAdded.getAddress());
        Assertions.assertFalse(recentlyAdded.isLpgAllowed());
        Assertions.assertTrue(recentlyAdded.getId() > 0);

        Mockito.verify(garageIdGenerator, Mockito.times(1)).getId();
    }

    @Test
    public void shouldReturnAllGarages() throws Exception {
        garages.add(new Garage(garageIdGenerator.getId(), 100, "Prosta 2", true));
        garages.add(new Garage(garageIdGenerator.getId(), 200, "Kreta 10", false));
        postman.perform(get("/api/v1/garages"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].places").value(100))
               .andExpect(jsonPath("$[0].address").value("Prosta 2"))
               .andExpect(jsonPath("$[0].lpgAllowed").value(true))
               .andExpect(jsonPath("$[1].id").value(2))
               .andExpect(jsonPath("$[1].places").value(200))
               .andExpect(jsonPath("$[1].address").value("Kreta 10"))
               .andExpect(jsonPath("$[1].lpgAllowed").value(false));
    }

    @Test
    public void shouldDeleteOneGarage() throws Exception {
        garages.add(new Garage(garageIdGenerator.getId(), 100, "Prosta 2", true));
        garages.add(new Garage(garageIdGenerator.getId(), 200, "Kreta 10", false));
        postman.perform(delete("/api/v1/garages/1"))
               .andExpect(status().isNoContent());
        Assertions.assertEquals(garages.size(), 1);
    }

    @Test
    public void shouldEditGarage() throws Exception {
        garages.add(new Garage(garageIdGenerator.getId(), 100, "Prosta 2", true));
        EditGarageCommand command = new EditGarageCommand(200, "Nowa 1", false);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(put("/api/v1/garages/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.places").value(200))
               .andExpect(jsonPath("$.address").value("Nowa 1"))
               .andExpect(jsonPath("$.lpgAllowed").value(false));

        Garage updatedGarage = garages.get(0);
        Assertions.assertEquals(200, updatedGarage.getPlaces());
        Assertions.assertEquals("Nowa 1", updatedGarage.getAddress());
        Assertions.assertFalse(updatedGarage.isLpgAllowed());
    }

    @Test
    public void shouldPartiallyEditGarage() throws Exception {
        garages.add(new Garage(garageIdGenerator.getId(), 100, "Prosta 2", true));
        EditGarageCommand command = new EditGarageCommand(300, null, true);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/garages/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.places").value(300))
               .andExpect(jsonPath("$.address").value("Prosta 2"))
               .andExpect(jsonPath("$.lpgAllowed").value(true));

        Garage partiallyUpdatedGarage = garages.get(0);
        Assertions.assertEquals(300, partiallyUpdatedGarage.getPlaces());
        Assertions.assertEquals("Prosta 2", partiallyUpdatedGarage.getAddress());
        Assertions.assertTrue(partiallyUpdatedGarage.isLpgAllowed());
    }
}