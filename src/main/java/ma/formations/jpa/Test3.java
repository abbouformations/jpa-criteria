package ma.formations.jpa;

import ma.formations.jpa.dao.DaoImpl;
import ma.formations.jpa.dao.IDao;

/**
 * Calculer l'age moyen des utilisateurs.
 */
public class Test3 {
    private static final IDao dao = new DaoImpl();

    public static void main(String[] args) {
        Double ageMoyen = dao.getAverageAge();
        System.out.println(ageMoyen);

    }
}
