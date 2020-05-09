package pl.pk.flybooking.flybooking.booking.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pk.flybooking.flybooking.booking.service.BookingService;
import pl.pk.flybooking.flybooking.flight.Flight;
import pl.pk.flybooking.flybooking.user.model.User;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/booking")
public class BookingController {

    private final BookingService bookingService;

    @JsonView(User.UserViews.Booking.class)
    @PostMapping("flight")
    public ResponseEntity<User> bookFlights(@RequestParam String  usernameOrEmail, @RequestBody Flight flight) {
        return ResponseEntity.ok(bookingService.bookFlights(usernameOrEmail, flight));
    }

    @JsonView(User.UserViews.Booking.class)
    @GetMapping("user-flights")
    public ResponseEntity<User> bookedUserFlights(@RequestParam String  usernameOrEmail) {
        return ResponseEntity.ok(bookingService.bookedUserFlights(usernameOrEmail));
    }

    @DeleteMapping("cancel-flight")
    public void cancelFlight(@RequestParam String usernameOrEmail, @RequestParam Long flightId) {
        bookingService.cancelFlight(usernameOrEmail, flightId);
    }
}
