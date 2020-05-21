package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import com.space.service.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/rest")
public class ShipsController {
    @Autowired
    private ShipService shipService ;

    @GetMapping("/ships")
    @ResponseStatus(HttpStatus.OK)
    public List<Ship> getAllShips(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating,
            @RequestParam(value = "order", required = false) ShipOrder shipOrder,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize
            ){
        return shipService.getAll(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating, shipOrder, pageNumber, pageSize);
    }
    @GetMapping("/ships/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getCountShips(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "planet", required = false) String planet,
            @RequestParam(value = "shipType", required = false) ShipType shipType,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "isUsed", required = false) Boolean isUsed,
            @RequestParam(value = "minSpeed", required = false) Double minSpeed,
            @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(value = "minRating", required = false) Double minRating,
            @RequestParam(value = "maxRating", required = false) Double maxRating
    ){
        return shipService.getCount(name, planet, shipType,after,before, isUsed, minSpeed,
                maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);
    }
    @PostMapping("/ships")
    @ResponseStatus(HttpStatus.OK)
    public Ship createShip(@RequestBody Ship ship) {
        if (Utils.isBadRequest(ship)) {
            throw new IncorrectRequestException("bad Request");
        }
        return shipService.save(ship);
    }

    @GetMapping("/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Ship getShip(@PathVariable("id") long id) {

        return checkById(id);
    }
    @PostMapping("/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Ship updateShip  (@PathVariable("id") long id, @RequestBody Ship newShip) {
        Ship shipByIdFromDB = checkById(id);
        Ship shipToSave = shipService.preUpdate(shipByIdFromDB,newShip);
        if (Utils.isBadRequest(shipToSave)) {
            throw new IncorrectRequestException("bad Request");
        }
        return shipService.save(shipToSave);
    }

    @DeleteMapping("/ships/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteShip (@PathVariable("id") long id) {
        checkById(id);
        shipService.remove(id);
    }

    private Ship checkById(long id) {
        if (id<1)
            throw new IncorrectRequestException("ship id must be greater than 1");
        Ship shipById = shipService.getById(id);
        if (shipById == null)
            throw new NotFoundException("ship by id = " + id + "; not found");
        return shipById;
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    class IncorrectRequestException extends RuntimeException {

        public IncorrectRequestException(String msg) {
            super(msg);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    class NotFoundException extends RuntimeException {

        public NotFoundException(String msg) {
            super(msg);
        }
    }

}