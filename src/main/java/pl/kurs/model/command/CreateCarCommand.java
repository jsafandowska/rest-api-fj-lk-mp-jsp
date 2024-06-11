package pl.kurs.model.command;

import lombok.*;

public class CreateCarCommand {
    private String brand;
    private String model;
    private String fuelType;
    private int garageId;

    public CreateCarCommand(String brand, String model, String fuelType, int garageId) {
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
        this.garageId = garageId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }
}

