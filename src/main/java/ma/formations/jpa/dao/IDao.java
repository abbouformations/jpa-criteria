package ma.formations.jpa.dao;

import ma.formations.jpa.dtos.UserDto;
import ma.formations.jpa.dtos.UsersByAgeAndCityDto;
import ma.formations.jpa.dtos.UsersByCityDto;
import ma.formations.jpa.model.User;

import java.util.List;

public interface IDao {
    void save(User user);

    List<UserDto> findUsersByRole(String role);

    Double getAverageAge();

    List<UsersByAgeAndCityDto> usersByAgeAndCity(Integer minAge, String city);

    List<UsersByCityDto> countUsersByCity();
}
