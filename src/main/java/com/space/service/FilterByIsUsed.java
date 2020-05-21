package com.space.service;

import com.space.model.Ship;

public class FilterByIsUsed implements  Filter {
    public FilterByIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    Boolean isUsed;
    @Override
    public Boolean isSatisfy(Ship ship) {
        return ship.getUsed().equals(isUsed);
    }
}
