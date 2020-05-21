package com.space.service;

import com.space.model.Ship;

public class FilterBySpeed implements Filter {
    Double minSpeed;
    Double maxSpeed;

    public FilterBySpeed(Double minSpeed, Double maxSpeed) {
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public Boolean isSatisfy(Ship ship) {
        if (minSpeed != null)
            if (ship.getSpeed()<minSpeed)
                return false;
        if (maxSpeed != null)
            if (ship.getSpeed()>maxSpeed)
                return false;
        return true;
    }
}
