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
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.service.CarIdGenerator;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class CarControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private List<Car> cars;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private CarIdGenerator carIdGenerator;

    @Test
    public void shouldReturnSingleCar() throws Exception {
        cars.add(new Car(carIdGenerator.getId(), "BMW", "M3", "Petrol"));

        postman.perform(get("/api/v1/cars/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.brand").value("BMW"))
               .andExpect(jsonPath("$.model").value("M3"))
               .andExpect(jsonPath("$.fuelType").value("Petrol"));
    }


    @Test
    public void shouldAddCar() throws Exception {
        CreateCarCommand command = new CreateCarCommand("BMW", "M3", "Petrol");
        String json = objectMapper.writeValueAsString(command);

        postman.perform(post("/api/v1/cars")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.brand").value("BMW"))
               .andExpect(jsonPath("$.model").value("M3"))
               .andExpect(jsonPath("$.fuelType").value("Petrol"));

        Car recentlyAdded = cars.get(cars.size() - 1);
        Assertions.assertEquals("BMW", recentlyAdded.getBrand());
        Assertions.assertEquals("M3", recentlyAdded.getModel());
        Assertions.assertEquals("Petrol", recentlyAdded.getFuelType());
        Assertions.assertTrue(recentlyAdded.getId() > 0);

        Mockito.verify(carIdGenerator, Mockito.times(1)).getId();
    }

    @Test
    public void shouldReturnAllCars() throws Exception {
        cars.add(new Car(carIdGenerator.getId(), "BMW", "M3", "Petrol"));
        cars.add(new Car(carIdGenerator.getId(), "BMW", "M2", "Petrol"));
        postman.perform(get("/api/v1/cars"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].brand").value("BMW"))
               .andExpect(jsonPath("$[0].model").value("M3"))
               .andExpect(jsonPath("$[0].fuelType").value("Petrol"))
               .andExpect(jsonPath("$[1].id").value(2))
               .andExpect(jsonPath("$[1].brand").value("BMW"))
               .andExpect(jsonPath("$[1].model").value("M2"))
               .andExpect(jsonPath("$[1].fuelType").value("Petrol"));
    }

    @Test
    public void shouldDeleteOneCar() throws Exception {
        cars.add(new Car(carIdGenerator.getId(), "BMW", "M3", "Petrol"));
        cars.add(new Car(carIdGenerator.getId(), "BMW", "M2", "Petrol"));
        postman.perform(delete("/api/v1/cars/1"))
               .andExpect(status().isNoContent());
        Assertions.assertEquals(cars.size(), 1);
    }

    @Test
    public void shouldEditCar() throws Exception {
        cars.add(new Car(carIdGenerator.getId(), "BMW", "M3", "Petrol"));
        EditCarCommand command = new EditCarCommand("Opel", "Calibra", "LPG");
        String json = objectMapper.writeValueAsString(command);

        postman.perform(put("/api/v1/cars/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.brand").value("Opel"))
               .andExpect(jsonPath("$.model").value("Calibra"))
               .andExpect(jsonPath("$.fuelType").value("LPG"));

        Car updateCar = cars.get(0);
        Assertions.assertEquals("Opel", updateCar.getBrand());
        Assertions.assertEquals("Calibra", updateCar.getModel());
        Assertions.assertEquals("LPG", updateCar.getFuelType());
    }

    @Test
    public void shouldPartiallyEditCar() throws Exception {
        cars.add(new Car(carIdGenerator.getId(), "BMW", "M3", "Petrol"));
        EditCarCommand command = new EditCarCommand(null, "320d", "Diesel");
        String json = objectMapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/cars/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.brand").value("BMW"))
               .andExpect(jsonPath("$.model").value("320d"))
               .andExpect(jsonPath("$.fuelType").value("Diesel"));

        Car partiallyUpdatedCar = cars.get(0);
        Assertions.assertEquals("BMW", partiallyUpdatedCar.getBrand());
        Assertions.assertEquals("320d", partiallyUpdatedCar.getModel());
    }
}

