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
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.service.CarService;
import pl.kurs.service.GarageService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class GarageControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GarageService garageService;

    @Autowired
    private CarService carService;

    @Test
    public void shouldReturnSingleGarage() throws Exception {
        GarageDto garage = garageService.addGarage(new CreateGarageCommand(2, "Zielona", true));
        int id = garage.id();
        postman.perform(get("/api/v1/garages/" + id))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.places").value(2))
               .andExpect(jsonPath("$.address").value("Zielona"))
               .andExpect(jsonPath("$.lpgAllowed").value(true));
    }

    @Test
    public void shouldAddGarage() throws Exception {
        CreateGarageCommand command = new CreateGarageCommand(2, "Warszawa", true);
        String json = objectMapper.writeValueAsString(command);
        String responseString = postman.perform(post("/api/v1/garages")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(json))
                                       .andExpect(status().isCreated())
                                       .andExpect(jsonPath("$.id").exists())
                                       .andExpect(jsonPath("$.places").value(2))
                                       .andExpect(jsonPath("$.address").value("Warszawa"))
                                       .andExpect(jsonPath("$.lpgAllowed").value(true))
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        GarageDto saved = objectMapper.readValue(responseString, GarageDto.class);
        GarageDto recentlyAdded = garageService.findGarage(saved.id());

        Assertions.assertEquals(2, recentlyAdded.places());
        Assertions.assertEquals("Warszawa", recentlyAdded.address());
        Assertions.assertEquals(saved.id(), recentlyAdded.id());
        Assertions.assertTrue(recentlyAdded.lpgAllowed());
    }

    @Test
    public void shouldDeleteGarage() throws Exception {
        GarageDto garage = garageService.addGarage(new CreateGarageCommand(3, "Piątkowska", false));
        int id = garage.id();
        postman.perform(delete("/api/v1/garages/" + id))
               .andExpect(status().isNoContent());
        Assertions.assertThrows(GarageNotFoundException.class, () -> garageService.findGarage(id));
    }

    @Test
    public void shouldEditGarage() throws Exception {
        GarageDto garage = garageService.addGarage(new CreateGarageCommand(2, "Old Address", true));
        int id = garage.id();
        CreateGarageCommand command = new CreateGarageCommand(3, "New Address", false);
        String json = objectMapper.writeValueAsString(command);

        String responseString = postman.perform(put("/api/v1/garages/" + id)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(json))
                                       .andExpect(status().isOk())
                                       .andExpect(jsonPath("$.id").value(id))
                                       .andExpect(jsonPath("$.places").value(3))
                                       .andExpect(jsonPath("$.address").value("New Address"))
                                       .andExpect(jsonPath("$.lpgAllowed").value(false))
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        GarageDto saved = objectMapper.readValue(responseString, GarageDto.class);
        GarageDto recentlyEdited = garageService.findGarage(saved.id());

        Assertions.assertEquals(3, recentlyEdited.places());
        Assertions.assertEquals("New Address", recentlyEdited.address());
        Assertions.assertEquals(saved.id(), recentlyEdited.id());
        Assertions.assertFalse(recentlyEdited.lpgAllowed());
    }

    @Test
    public void shouldEditGaragePartially() throws Exception {
        GarageDto garage = garageService.addGarage(new CreateGarageCommand(2, "Old Address", true));
        int id = garage.id();
        EditGarageCommand command = new EditGarageCommand(null, "New Address", null);
        String json = objectMapper.writeValueAsString(command);

        String responseString = postman.perform(patch("/api/v1/garages/" + id)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(json))
                                       .andExpect(status().isOk())
                                       .andExpect(jsonPath("$.id").value(id))
                                       .andExpect(jsonPath("$.places").value(2))
                                       .andExpect(jsonPath("$.address").value("New Address"))
                                       .andExpect(jsonPath("$.lpgAllowed").value(true))
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        GarageDto saved = objectMapper.readValue(responseString, GarageDto.class);
        GarageDto recentlyEdited = garageService.findGarage(saved.id());

        Assertions.assertEquals(2, recentlyEdited.places());
        Assertions.assertEquals("New Address", recentlyEdited.address());
        Assertions.assertEquals(saved.id(), recentlyEdited.id());
        Assertions.assertTrue(recentlyEdited.lpgAllowed());
    }

    @Test
    public void shouldAddCarToGarage() throws Exception {
        GarageDto garage = garageService.addGarage(new CreateGarageCommand(2, "Zielona", true));
        CarDto car = carService.addCar(new CreateCarCommand("Audi", "RS", "petrol", garage.id()));
        int id = garage.id();
        int carId = car.id();
        postman.perform(patch("/api/v1/garages/" + id + "/cars/" + carId))
               .andExpect(status().isOk());
        GarageDto updatedGarage = garageService.findGarage(id);
        Assertions.assertTrue(updatedGarage.cars().stream().anyMatch(c -> c.id() == carId));
    }

    @Test
    public void shouldDeleteCarFromGarage() throws Exception {
        GarageDto garage = garageService.addGarage(new CreateGarageCommand(2, "Zielona", true));
        CarDto car = carService.addCar(new CreateCarCommand("Audi", "RS", "petrol", garage.id()));
        int id = garage.id();
        int carId = car.id();
        postman.perform(delete("/api/v1/garages/" + id + "/cars/" + carId))
               .andExpect(status().isOk());
        GarageDto updatedGarage = garageService.findGarage(id);
        Assertions.assertFalse(updatedGarage.cars().stream().anyMatch(c -> c.id() == carId));
    }
}


