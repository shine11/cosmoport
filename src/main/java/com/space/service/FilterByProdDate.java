package com.space.service;

import com.space.model.Ship;

import java.util.Date;

public class FilterByProdDate implements Filter {
    Long before;
    Long after;

    public FilterByProdDate(Long before, Long after) {
        this.before = before;
        this.after = after;
    }

    @Override
    public Boolean isSatisfy(Ship ship) {
        if (before != null)
            if (ship.getProdDate().getTime()<before)
                return false;
        if (after != null)
            if (ship.getProdDate().getTime()>after)
                return false;
        return true;
    }
}
