package laheezy.community.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.*;
//import javax.persistence.Entity;

@NoArgsConstructor
@Data
@Entity
@Schema(description = "TEST")
public class User {
    @Id
    @GeneratedValue
    @Schema(description = "ID")
    @Column(name = "user_id")
    private Long id;

    private String name;

    public static User makeUser(String name) {
        User user = new User();
        user.setName(name);
        return user;
    }
}
