package com.space.service;

import com.space.model.Ship;

public class FilterByRating implements Filter {
    Double minRating;
    Double maxRating;

    public FilterByRating(Double minRating, Double maxRating) {
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    @Override
    public Boolean isSatisfy(Ship ship) {
        if (minRating != null)
            if (ship.getRating() < minRating)
                return false;
        if (maxRating != null)
            if (ship.getRating() > maxRating)
                return false;
        return true;
    }
}

