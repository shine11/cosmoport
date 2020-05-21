package com.space.service;

import com.space.model.Ship;

public class FilterByName implements Filter {

    private String name;

    public FilterByName(String name) {
        this.name = name;
    }

    @Override
    public Boolean isSatisfy(Ship ship) {
        return ship.getName().contains(name);
    }
}
