package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;

public class FilterByShipType implements Filter {
    ShipType shipType;

    public FilterByShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    @Override
    public Boolean isSatisfy(Ship ship) {
        return ship.getShipType().equals(shipType);
    }
}
