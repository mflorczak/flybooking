package pl.pk.flybooking.flybooking.booking.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.user.model.User;
import pl.pk.flybooking.flybooking.user.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Test
    public void bookFlightsShouldReturnUser(){
        //given
        Flight flight = new Flight();
        Set<Flight> flightSet = new HashSet<>();
        flightSet.add(flight);
        String username = "username";
        User user = new User();

        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(java.util.Optional.of(user));
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(username, username);

        //when
        boolean isSetAdded = user.getFlights().addAll(flightSet);

        //then
        assertTrue(isSetAdded);
        assertTrue(user.getFlights().containsAll(flightSet));
        assertThat(optionalUser).contains(user);
        verify(userRepository).findByUsernameOrEmail(username, username);
        //verifyNoInteractions(userRepository);
    }

}