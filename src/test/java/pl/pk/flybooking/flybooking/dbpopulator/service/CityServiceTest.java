package pl.pk.flybooking.flybooking.dbpopulator.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import pl.pk.flybooking.flybooking.dbpopulator.model.City;
import pl.pk.flybooking.flybooking.dbpopulator.repository.CityRepository;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CityServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CityService cityService;

    @MockBean
    private CityRepository cityRepository;
    @MockBean
    private City city;

    @Test
    public void findCities() throws Exception {
        when(cityRepository.findCitiesByName("Krakow")).thenReturn(Collections.singletonList(city));

        mockMvc.perform(get("/api/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .param("cityName","Krakow"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}