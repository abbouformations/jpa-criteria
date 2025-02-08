package ma.formations.jpa;

import ma.formations.jpa.dao.DaoImpl;
import ma.formations.jpa.dao.IDao;
import ma.formations.jpa.dtos.UsersByAgeAndCityDto;

import java.util.List;

/**
 * Cherchez le nombre des utilisateurs ayant une moyenne d'age donné et habitant dans une ville donnée.
 */
public class Test5 {
    private static final IDao dao = new DaoImpl();

    public static void main(String[] args) {
        final String city = "rabat";
        final int minAge = 30;
        List<UsersByAgeAndCityDto> usersByAgeAndCity = dao.usersByAgeAndCity(minAge, city);
        usersByAgeAndCity.forEach(System.out::println);
    }
}
