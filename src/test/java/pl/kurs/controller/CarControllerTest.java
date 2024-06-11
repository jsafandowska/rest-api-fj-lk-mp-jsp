package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditCarCommand;
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
class CarControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CarService carService;

    @Autowired
    private GarageService garageService;

    private GarageDto garage1;
    private GarageDto garage2;

    @BeforeEach
    public void setup() {
        garage1 = garageService.addGarage(new CreateGarageCommand(2, "Zielona", true));
        garage2 = garageService.addGarage(new CreateGarageCommand(1, "Żółta", false));
    }

    @Test
    public void shouldReturnSingleCar() throws Exception {
        CarDto car = carService.addCar(new CreateCarCommand("Audi", "RS", "petrol", garage2.id()));
        int id = car.id();
        postman.perform(get("/api/v1/cars/" + id))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.brand").value("Audi"))
               .andExpect(jsonPath("$.model").value("RS"))
               .andExpect(jsonPath("$.fuelType").value("petrol"));
    }

    @Test
    public void shouldAddCar() throws Exception {
        CreateCarCommand command = new CreateCarCommand("Mercedes", "S-class", "petrol", garage1.id());
        String json = objectMapper.writeValueAsString(command);
        String responseString = postman.perform(post("/api/v1/cars")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(json))
                                       .andExpect(status().isCreated())
                                       .andExpect(jsonPath("$.id").exists())
                                       .andExpect(jsonPath("$.brand").value("Mercedes"))
                                       .andExpect(jsonPath("$.model").value("S-class"))
                                       .andExpect(jsonPath("$.fuelType").value("petrol"))
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        CarDto saved = objectMapper.readValue(responseString, CarDto.class);
        CarDto recentlyAdded = carService.findCar(saved.id());

        Assertions.assertEquals("Mercedes", recentlyAdded.brand());
        Assertions.assertEquals("S-class", recentlyAdded.model());
        Assertions.assertEquals("petrol", recentlyAdded.fuelType());
        Assertions.assertEquals(saved.id(), recentlyAdded.id());
    }

    @Test
    public void shouldDeleteCar() throws Exception {
        CarDto car = carService.addCar(new CreateCarCommand("BMW", "X5", "diesel", garage1.id()));
        int id = car.id();
        postman.perform(delete("/api/v1/cars/" + id))
               .andExpect(status().isNoContent());
        Assertions.assertThrows(CarNotFoundException.class, () -> carService.findCar(id));
    }

    @Test
    public void shouldEditCar() throws Exception {
        CarDto car = carService.addCar(new CreateCarCommand("Audi", "A4", "petrol", garage2.id()));
        int id = car.id();
        EditCarCommand command = new EditCarCommand("Audi", "A6", "diesel");
        String json = objectMapper.writeValueAsString(command);

        String responseString = postman.perform(put("/api/v1/cars/" + id)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(json))
                                       .andExpect(status().isOk())
                                       .andExpect(jsonPath("$.id").value(id))
                                       .andExpect(jsonPath("$.brand").value("Audi"))
                                       .andExpect(jsonPath("$.model").value("A6"))
                                       .andExpect(jsonPath("$.fuelType").value("diesel"))
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        CarDto saved = objectMapper.readValue(responseString, CarDto.class);
        CarDto recentlyEdited = carService.findCar(saved.id());

        Assertions.assertEquals("Audi", recentlyEdited.brand());
        Assertions.assertEquals("A6", recentlyEdited.model());
        Assertions.assertEquals("diesel", recentlyEdited.fuelType());
        Assertions.assertEquals(saved.id(), recentlyEdited.id());
    }

    @Test
    public void shouldEditCarPartially() throws Exception {
        CarDto car = carService.addCar(new CreateCarCommand("Audi", "A4", "petrol", garage2.id()));
        int id = car.id();
        EditCarCommand command = new EditCarCommand(null, "A6", null);
        String json = objectMapper.writeValueAsString(command);

        String responseString = postman.perform(patch("/api/v1/cars/" + id)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(json))
                                       .andExpect(status().isOk())
                                       .andExpect(jsonPath("$.id").value(id))
                                       .andExpect(jsonPath("$.brand").value("Audi"))
                                       .andExpect(jsonPath("$.model").value("A6"))
                                       .andExpect(jsonPath("$.fuelType").value("petrol"))
                                       .andReturn()
                                       .getResponse()
                                       .getContentAsString();

        CarDto saved = objectMapper.readValue(responseString, CarDto.class);
        CarDto recentlyEdited = carService.findCar(saved.id());

        Assertions.assertEquals("Audi", recentlyEdited.brand());
        Assertions.assertEquals("A6", recentlyEdited.model());
        Assertions.assertEquals("petrol", recentlyEdited.fuelType());
        Assertions.assertEquals(saved.id(), recentlyEdited.id());
    }
}

