package pl.pk.flybooking.flybooking.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.pk.flybooking.flybooking.booking.service.BookingService;
import pl.pk.flybooking.flybooking.carrier.CarrierRepository;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.flight.FlightRepository;
import pl.pk.flybooking.flybooking.user.model.User;
import pl.pk.flybooking.flybooking.user.repository.UserRepository;

import java.util.Optional;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private BookingService bookingService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CarrierRepository carrierRepository;
    @MockBean
    private FlightRepository flightRepository;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private Flight flight;

    @Test
    public void bookFlights() throws Exception {
        String username = "username";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsernameOrEmail(username, username)).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/booking/flight")
                .param("usernameOrEmail", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writerWithView(User.UserViews.Booking.class).writeValueAsString(flight)))
                .andDo(print())
                .andExpect(status().isOk());

    }

}