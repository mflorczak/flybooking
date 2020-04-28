package pl.pk.flybooking.flybooking.user.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;
import pl.pk.flybooking.flybooking.audit.DateAudit;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.role.model.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@Table(name = "users")
public class User extends DateAudit {

    public interface UserViews {
        interface SignUp {}
        interface ForgotPassword {}
        interface ResetPassword {}
        interface Booking {}
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @JsonView({UserViews.SignUp.class, UserViews.Booking.class})
    private String name;

    @NotBlank
    @JsonView({UserViews.SignUp.class, UserViews.Booking.class})
    private String surname;

    @NotBlank
    @JsonView({UserViews.SignUp.class, UserViews.ForgotPassword.class})
    private String username;

    @NotBlank
    @JsonView({UserViews.SignUp.class, UserViews.ResetPassword.class})
    private String password;

    @NaturalId
    @NotBlank
    @Email
    @JsonView({UserViews.SignUp.class, UserViews.ResetPassword.class})
    private String email;

    private Boolean enabled;


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name =" user_id" ),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @JsonView(UserViews.Booking.class)
    @ManyToMany
    @JoinTable(
            name = "user_flights",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id")
    )
    private Set<Flight> flights = new HashSet<>();

    public User(@NotBlank String name, @NotBlank String surname, @NotBlank String username, @NotBlank String password, @NotBlank @Email String email) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
