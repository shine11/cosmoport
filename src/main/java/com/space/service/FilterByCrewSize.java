package com.space.service;

import com.space.model.Ship;

public class FilterByCrewSize implements Filter {
    Integer minCrewSize;
    Integer maxCrewSize;

    public FilterByCrewSize(Integer minCrewSize, Integer maxCrewSize) {
        this.minCrewSize = minCrewSize;
        this.maxCrewSize = maxCrewSize;
    }

    @Override
    public Boolean isSatisfy(Ship ship) {
        if (minCrewSize != null)
            if (ship.getCrewSize()<minCrewSize)
                return false;
        if (maxCrewSize != null)
            if (ship.getCrewSize()>maxCrewSize)
                return false;
        return true;
    }
}
