package treat_app.web_service.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userLogin;
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Treat> treats;
}