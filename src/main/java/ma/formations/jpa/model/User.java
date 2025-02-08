package ma.formations.jpa.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString

public class User implements Serializable {
    @Id
    @GeneratedValue
    private Long userId;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private String role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;
}

