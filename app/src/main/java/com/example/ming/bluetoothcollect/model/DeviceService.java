package com.example.ming.bluetoothcollect.model;

import java.util.UUID;

public class DeviceService {
    private UUID service;//Service

    private UUID character;//Character

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
