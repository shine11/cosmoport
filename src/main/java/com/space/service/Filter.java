package com.space.service;

import com.space.model.Ship;

public interface Filter {
    public Boolean isSatisfy(Ship ship);
}
