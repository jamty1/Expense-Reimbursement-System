package com.jam.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jam.data.ReimbursementRepository;
import com.jam.data.UserRepository;
import com.jam.model.Reimbursement;
import com.jam.model.User;
import com.jam.model.UserType;
import com.jam.service.AuthService;
import com.jam.service.ReimbursementService;
import com.jam.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestReimbursements {
    @Autowired
    private MockMvc mockMvc;
    private ReimbursementService reimbursementService;
    private AuthService authService;
    private UserService userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ReimbursementRepository reimbursementRepository;
    @Autowired
    private ObjectMapper mapper;

    User u1 = new User("John Smith", "password1", "test1@gmail.com", UserType.EMPLOYEE);
    User u2 = new User("William Lam", "password2", "test2@gmail.com", UserType.MANAGER);

    /**
     * For each test, mock the repositories by setting up the data
     * and mocking the returned values of the necessary repository methods.
     */
    @BeforeEach
    public void init() {
        userService = new UserService(userRepository);
        authService = new AuthService(userRepository);
        reimbursementService = new ReimbursementService(reimbursementRepository);
        u1.setId(1L);
        u2.setId(2L);
        u1.setApikey("Sm9obiBTbWl0aDpwYXNzd29yZDE=");
        u2.setApikey("V2lsbGlhbSBMYW06d2lsbGlhbXBhc3M=");
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(u1);
        Mockito.when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(u1));
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));
    }

    /**
     * Performs an HTTP request to add a user to the user repository.
     */
    @Test
    public void shouldAddUser() throws Exception {
        User input = new User("John Smith", "password1", "test1@gmail.com", UserType.EMPLOYEE);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andDo(response -> {
                    Assertions.assertThat(response.getResponse().getContentAsString())
                            .isEqualTo(mapper.writeValueAsString(u1));
                });
    }

    /**
     * Performs an HTTP request to get all the users in the user repository.
     */
    @Test
    public void shouldGetUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user"))
                .andExpect(status().isOk())
                .andDo(response -> {
                    Assertions.assertThat(response.getResponse().getContentAsString())
                            .isEqualTo(mapper.writeValueAsString(Arrays.asList(u1, u2)));
                });
    }

    /**
     * Performs an HTTP request to request a new reimbursement request
     * for user 1 (The sample employee user)
     */
    @Test
    public void shouldRequestNewReimbursement() throws Exception {
        Reimbursement mockR = new Reimbursement(LocalDate.now(), "For eating", BigDecimal.valueOf(50.20), u1);
        mockR.setId(1L);
        Mockito.when(reimbursementRepository.save(Mockito.any())).thenReturn(mockR);

        Reimbursement r = new Reimbursement(LocalDate.now(), "For eating", BigDecimal.valueOf(50.20), u1);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/reimbursement/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(r))
                        .header("Authorization", u1.getApikey()))
                .andExpect(status().isCreated())
                .andDo(response -> {
                    Assertions.assertThat(response.getResponse().getContentAsString())
                            .isEqualTo(mapper.writeValueAsString(mockR));
                });
    }

    /**
     * Performs an HTTP request to approve a reimbursement request
     * using user 2 (The sample manager user)
     * @throws Exception
     */
    @Test
    public void shouldApproveReimbursement() throws Exception {
        Reimbursement mockR = new Reimbursement(LocalDate.now(), "For eating", BigDecimal.valueOf(50.20), u1);
        mockR.setId(1L);
        Mockito.when(reimbursementRepository.save(Mockito.any())).thenReturn(mockR);
        Mockito.when(reimbursementRepository.getById(Mockito.anyLong())).thenReturn(mockR);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(u2));

        Map<String, Object> m = new HashMap<>();
        m.put("managerid", 2);
        m.put("status", "approve");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/reimbursement/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(m))
                        .header("Authorization", u2.getApikey()))
                .andExpect(status().isNoContent());


    }
}
