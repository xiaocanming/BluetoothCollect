package com.example.ming.bluetoothcollect.model;

import java.util.UUID;

public class DeviceService {
    private int type;//FFE1 = 1 FFF6 = 2

    private UUID service;//Service

    private UUID character;//Character

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public UUID getService() {
        return service;
    }

    public void setService(UUID service) {
        this.service = service;
    }

    public UUID getCharacter() {
        return character;
    }

    public void setCharacter(UUID character) {
        this.character = character;
    }
}
