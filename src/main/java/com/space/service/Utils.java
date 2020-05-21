package com.space.service;

import com.space.model.Ship;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    /**
     Мы не можем создать корабль, если:
     - указаны не все параметры из Data Params (кроме isUsed);
     - длина значения параметра “name” или “planet” превышает размер соответствующего поля в БД (50 символов);
     - значение параметра “name” или “planet” пустая строка;
     - скорость или размер команды находятся вне заданных пределов; - “prodDate”:[Long] < 0;
     - год производства находятся вне заданных пределов.
     */
    public static Boolean isBadRequest(Ship ship) {
        if (ship == null || ship.getProdDate() == null || ship.getSpeed() == null || ship.getCrewSize() == null)
            return true;
        Calendar calendar = Calendar.getInstance();
        calendar.set(2800, Calendar.JANUARY, 1, 0, 0, 0);
        if (calendar.getTime().getTime()>=ship.getProdDate().getTime())
            return true;
        calendar.set(3019, Calendar.DECEMBER, 31, 23, 59, 59);
        if (calendar.getTime().getTime()<=ship.getProdDate().getTime())
            return true;
        double speed = round(ship.getSpeed(),2);
        if (speed<0.01 || speed>0.99)
            return true;
        Integer crewSize = ship.getCrewSize();
        if (crewSize<1||crewSize>9999)
            return true;
        if (isBadString(ship.getName()) || isBadString(ship.getPlanet()))
            return true;
        return false;
    }

    private static boolean isBadString(String field) {
        return field == null || field.length()<1 || field.length()>50;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
/**
 * Перед сохранением корабля в базу данных (при добавлении нового или при апдейте характеристик существующего),
 * должен высчитываться рейтинг корабля и сохраняться в БД.
 * Рейтинг корабля рассчитывается по формуле: 𝑅 = 80·𝑣·𝑘 𝑦0−𝑦1+1  ,
 * где: v — скорость корабля; k — коэффициент, который равен 1 для нового корабля и 0,5 для использованного;
 * y0 — текущий год (не забудь, что «сейчас» 3019 год);
 * y1 — год выпуска корабля.
 *
 */
    public static Double calculateRating (Double speed, Date ProdDate, Boolean isUsed) {
        double k;
        if (isUsed)
            k = 0.5;
        else
            k = 1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(3019,Calendar.JANUARY, 1, 0, 0, 0);
        return round(80*k*speed/(calendar.getTime().getYear() - ProdDate.getYear() + 1),2);
    }
}
