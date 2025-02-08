package ma.formations.jpa.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersByAgeAndCityDto {
    private String firstName;
    private String lastName;
    private String city;
    private int age;

}
