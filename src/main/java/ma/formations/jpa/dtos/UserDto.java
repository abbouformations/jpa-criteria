package ma.formations.jpa.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDto {
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String role;
    private List<AddressDto> addresses;
}
