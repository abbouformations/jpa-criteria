package ma.formations.jpa;

import ma.formations.jpa.dao.DaoImpl;
import ma.formations.jpa.dao.IDao;

/**
 * Cherchez des utilisateurs ayant un rôle donné.
 */
public class Test2 {
    private static final IDao dao = new DaoImpl();

    public static void main(String[] args) {
        final String role = "user";
        dao.findUsersByRole(role).forEach(System.out::println);
    }
}
