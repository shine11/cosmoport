package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShipServiceImpl implements ShipService {
    @Autowired
    ShipsRepository shipsRepository;
    @Override
    public List<Ship> getAll(String name, String planet, ShipType shipType, Long after, Long before,
                             Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize,
                             Integer maxCrewSize, Double minRating, Double maxRating, ShipOrder shipOrder,
                             Integer pageNumber, Integer pageSize) {
        List<Ship> allShip = getListAfterFilters(name, planet, shipType, after, before,isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
        if (shipOrder!=null)
        allShip = orderListShip(allShip,shipOrder);
        List<Ship> shipsOnPage = new ArrayList<>();
        for (int i = pageNumber*pageSize; i < allShip.size() && i < (pageNumber + 1)*pageSize; i++) {
            shipsOnPage.add(allShip.get(i));
        }
        return shipsOnPage;
    }

    /**
     *  Метод организует работу фильтров.
     * В метод передаётся набор аргуметов.
     * Для аргументов не равных null, будет включен соответствующий фильтр.
     * Исходны набор сущностей- все корабли хранящиеся в БД.
     ***/
    public List<Ship> getListAfterFilters(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
                                          Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                                          Double maxRating) {
        List<Ship> allShip= shipsRepository.findAll();
        if (name!=null) {
            allShip = acceptFilter(allShip,new FilterByName(name));
        }
        if (planet!=null) {
            allShip = acceptFilter(allShip, new FilterByPlanet(planet));
        }
        if (after!=null || before!=null) {
            allShip = acceptFilter(allShip,new FilterByProdDate(after,before));
        }
        if (shipType!=null) {
            allShip = acceptFilter(allShip, new FilterByShipType(shipType));
        }
        if (isUsed!=null) {
            allShip = acceptFilter(allShip,new FilterByIsUsed(isUsed));
        }
        if (minSpeed!=null || maxSpeed!=null ) {
            allShip = acceptFilter(allShip, new FilterBySpeed(minSpeed,maxSpeed));
        }
        if (minCrewSize!=null || maxCrewSize!=null) {
            allShip = acceptFilter(allShip, new FilterByCrewSize(minCrewSize,maxCrewSize));
        }
        if (minRating!=null||maxRating!=null) {
            allShip = acceptFilter(allShip, new FilterByRating(minRating,maxRating));
        }
        return allShip;
    }


    /**
     * Метод организует цикл по всем кораблям. В зависимости от типа
     * фильтра, отбирает нужные.
     */

    private List<Ship> acceptFilter (List<Ship> allShip,Filter filter) {
        List<Ship> afterFilter = new ArrayList<>();
        for (Ship ship:allShip) {
            if (filter.isSatisfy(ship))
                afterFilter.add(ship);
        }
        return afterFilter;
    }

    private List<Ship> orderListShip(List<Ship> ships, ShipOrder shipOrder) {
        Collections.sort(ships, new Comparator<Ship>() {
            @Override
            public int compare(Ship o1, Ship o2) {
                return getCompare(shipOrder,o1,o2);
            }
        });
        return ships;
    }

    private int getCompare(ShipOrder shipOrder, Ship o1, Ship o2) {
        if (shipOrder.equals(ShipOrder.ID))
            return o1.getId().compareTo(o2.getId());
        else if (shipOrder.equals(ShipOrder.SPEED))
            return o1.getSpeed().compareTo(o2.getSpeed());
        else if (shipOrder.equals(ShipOrder.DATE))
            return o1.getProdDate().compareTo(o2.getProdDate());
        else if (shipOrder.equals(ShipOrder.RATING))
            return o1.getRating().compareTo(o2.getRating());
        else return 0;
    }



    @Override
    public Integer getCount(String name, String planet, ShipType shipType, Long after, Long before, Boolean isUsed, Double minSpeed,
                            Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating,
                            Double maxRating) {
        return getListAfterFilters(name, planet, shipType,after, before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating).size();
    }

    @Override
    public Ship save(Ship ship) {
        if (ship.getUsed() == null)
            ship.setUsed(false);
        ship.setRating(Utils.calculateRating(ship.getSpeed(),ship.getProdDate(),ship.getUsed()));
        return shipsRepository.saveAndFlush(ship);
    }

    @Override
    public Ship getById(long id) {
        List<Ship> allShip = shipsRepository.findAll();
        Ship shipById = null;
        for (Ship ship : allShip) {
            if (ship.getId() == id) {
                shipById = ship;
                break;
            }
        }
        return shipById;
    }

    @Override
    public Ship preUpdate(Ship shipByIdFromDB, Ship newShip) {
        Ship shipToSave = new Ship();
        shipToSave.setId(shipByIdFromDB.getId());
        if (newShip.getName() == null)
            shipToSave.setName(shipByIdFromDB.getName());
        else shipToSave.setName(newShip.getName());
        if (newShip.getPlanet() == null)
            shipToSave.setPlanet(shipByIdFromDB.getPlanet());
        else shipToSave.setPlanet(newShip.getPlanet());
        if (newShip.getProdDate() == null)
            shipToSave.setProdDate(shipByIdFromDB.getProdDate());
        else shipToSave.setProdDate(newShip.getProdDate());
        if (newShip.getSpeed() == null)
            shipToSave.setSpeed(shipByIdFromDB.getSpeed());
        else shipToSave.setSpeed(newShip.getSpeed());
        if (newShip.getUsed() == null)
            shipToSave.setUsed(shipByIdFromDB.getUsed());
        else shipToSave.setUsed(newShip.getUsed());
        if (newShip.getShipType() == null)
            shipToSave.setShipType(shipByIdFromDB.getShipType());
        else shipToSave.setShipType(newShip.getShipType());
        if (newShip.getCrewSize() == null)
            shipToSave.setCrewSize(shipByIdFromDB.getCrewSize());
        else shipToSave.setCrewSize(newShip.getCrewSize());
        return shipsRepository.saveAndFlush(shipToSave);
    }

    @Override
    public void remove(long id) {
        shipsRepository.deleteById(id);
    }

}
