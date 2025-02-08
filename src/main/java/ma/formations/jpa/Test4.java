package ma.formations.jpa;

import ma.formations.jpa.dao.DaoImpl;
import ma.formations.jpa.dao.IDao;
import ma.formations.jpa.dtos.UsersByCityDto;

import java.util.List;

/**
 * Calculer le nombre des utilisateurs par ville.
 */
public class Test4 {
    private static final IDao dao = new DaoImpl();

    public static void main(String[] args) {
        List<UsersByCityDto> usersByCity = dao.countUsersByCity();
        usersByCity.forEach(System.out::println);

    }
}
