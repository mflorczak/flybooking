package pl.pk.flybooking.flybooking.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.pk.flybooking.flybooking.authentication.service.AuthService;
import pl.pk.flybooking.flybooking.confirmation.model.ConfirmationToken;
import pl.pk.flybooking.flybooking.confirmation.repository.ConfirmationTokenRepository;
import pl.pk.flybooking.flybooking.email.service.ConfirmationAccountStrategy;
import pl.pk.flybooking.flybooking.email.service.SendEmailFactory;
import pl.pk.flybooking.flybooking.exception.GenericValidationException;
import pl.pk.flybooking.flybooking.payload.ApiResponse;
import pl.pk.flybooking.flybooking.payload.LoginRequest;
import pl.pk.flybooking.flybooking.payload.service.MessageTranslateService;
import pl.pk.flybooking.flybooking.role.model.Role;
import pl.pk.flybooking.flybooking.role.model.RoleName;
import pl.pk.flybooking.flybooking.role.repository.RoleRepository;
import pl.pk.flybooking.flybooking.security.service.JwtTokenProvider;
import pl.pk.flybooking.flybooking.user.model.User;
import pl.pk.flybooking.flybooking.user.repository.UserRepository;

import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private MessageTranslateService messageTranslateService;
    @MockBean
    private SendEmailFactory sendEmailFactory;
    @MockBean
    private ConfirmationAccountStrategy confirmationAccountStrategy;
    @MockBean
    private ConfirmationToken confirmationToken;
    @MockBean
    private ConfirmationTokenRepository confirmationTokenRepository;

    private static User user;

    @BeforeClass
    public static void prepareUser() {
        user = new User();
        user.setId(1L);
        user.setUsername("login");
        user.setPassword("password");
        user.setEmail("test@test.com");
        user.setName("name");
        user.setSurname("surname");
    }

    @Test
    public void authenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("login");
        loginRequest.setPassword("password");

        when(userRepository.findByUsernameOrEmail("login", "login")).thenReturn(Optional.of(user));
        when(tokenProvider.generateToken(any())).thenReturn("test-token");


        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.username").value("login"))
                .andExpect(jsonPath("$.emailAddress").value("test@test.com"))
                .andExpect(jsonPath("$.accessToken").value("Bearer test-token"))
                .andExpect(status().isOk());
    }

    @Test
    public void registerUser() throws Exception {
        Role role = new Role();
        role.setId(1L);
        role.setName(RoleName.ROLE_USER);

        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(role));
        when(sendEmailFactory.findEmailStrategy(any())).thenReturn(confirmationAccountStrategy);
        doNothing().when(confirmationAccountStrategy).sendEmail(any(), any());
        when(messageTranslateService.translatedMessage("userRegisteredSuccessfully", Locale.ENGLISH)).thenReturn("Successful registered!");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successful registered!"))
                .andExpect(status().isOk());
    }

    @Test
    public void confirmUserAccount() throws Exception {
        when(confirmationTokenRepository.findByToken("token")).thenReturn(Optional.of(confirmationToken));
        when(confirmationToken.getUser()).thenReturn(user);
        when(userRepository.findByUsername("login")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/auth/confirm-account")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", "token"))
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void forgotUserPassword() throws Exception {
        when(userRepository.findByUsernameOrEmail("login", "login")).thenReturn(Optional.of(user));
        when(sendEmailFactory.findEmailStrategy(any())).thenReturn(confirmationAccountStrategy);
        doNothing().when(confirmationAccountStrategy).sendEmail(any(), any());

        mockMvc.perform(post("/api/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }


    @Test
    public void resetUserPassword() throws Exception {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}