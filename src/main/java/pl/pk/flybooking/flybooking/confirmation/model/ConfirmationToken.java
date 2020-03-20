package pl.pk.flybooking.flybooking.confirmation.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.pk.flybooking.flybooking.audit.DateAudit;
import pl.pk.flybooking.flybooking.user.model.User;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ConfirmationToken extends DateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(User user) {
        this.user = user;
        token = UUID.randomUUID().toString();
    }
}
