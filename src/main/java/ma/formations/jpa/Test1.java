package ma.formations.jpa;

import ma.formations.jpa.dao.DaoImpl;
import ma.formations.jpa.dao.IDao;
import ma.formations.jpa.model.Address;
import ma.formations.jpa.model.User;

import java.util.List;

/**
 * Ajouter des utilisateurs avec leurs adresses.
 */
public class Test1 {
    private static final IDao dao = new DaoImpl();

    public static void main(String[] args) {

        User user1 = User.builder().
                firstName("first_name_1").
                lastName("last_name_1").
                age(34).
                email("first_name_1@gmail.com").
                role("user").build();
        Address address1 = Address.builder().city("rabat").country("Maroc").street("Riad").user(user1).build();
        Address address2 = Address.builder().city("casablanca").country("Maroc").street("Medina").user(user1).build();
        user1.setAddresses(List.of(address1, address2));
        dao.save(user1);

        User user2 = User.builder().
                firstName("first_name_2").
                lastName("last_name_2").
                age(50).
                email("first_name_2@gmail.com").
                role("user").build();
        Address address3 = Address.builder().city("rabat").country("Maroc").street("Riad").user(user2).build();
        user2.setAddresses(List.of(address3));
        dao.save(user2);

        User user3 = User.builder().
                firstName("first_name_3").
                lastName("last_name_3").
                age(44).
                email("first_name_3@gmail.com").
                role("admin").build();

        Address address4 = Address.builder().city("agadir").country("Maroc").street("Nassr").user(user3).build();
        user3.setAddresses(List.of(address4));
        dao.save(user3);

        User user4 = User.builder().
                firstName("first_name_4").
                lastName("last_name_4").
                age(25).
                email("first_name_3@gmail.com").
                role("user").build();
        Address address5 = Address.builder().city("rabat").country("Maroc").street("Nassr").user(user4).build();
        user4.setAddresses(List.of(address5));
        dao.save(user4);
    }
}
