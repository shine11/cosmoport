package com.space.service;

import com.space.model.Ship;

public class FilterByPlanet implements Filter{
    private String planet;

    public FilterByPlanet(String planet) {
        this.planet = planet;
    }

    @Override
    public Boolean isSatisfy(Ship ship) {
        return ship.getPlanet().contains(planet);
    }
}
