package com.team1.f1_api.controller;

import com.team1.f1_api.controller.dto.ContinentDto;
import com.team1.f1_api.controller.dto.CountryDto;
import com.team1.f1_api.repository.ContinentCountView;
import com.team1.f1_api.repository.CountryView;
import com.team1.f1_api.repository.TrackRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContinentControllerTest {

    // ── getContinents ─────────────────────────────────────────────────────────

    @Test
    void testGetContinentsReturnsSortedList() {
        TrackRepository mockRepo = mock(TrackRepository.class);

        ContinentCountView eu = mock(ContinentCountView.class);
        when(eu.getRegion()).thenReturn("EU");
        when(eu.getCountryCount()).thenReturn(10L);

        ContinentCountView na = mock(ContinentCountView.class);
        when(na.getRegion()).thenReturn("NA");
        when(na.getCountryCount()).thenReturn(3L);

        when(mockRepo.countCountriesByRegion()).thenReturn(Arrays.asList(na, eu));

        ContinentController controller = new ContinentController(mockRepo);
        List<ContinentDto> result = controller.getContinents();

        assertEquals(2, result.size());
        // Should be sorted alphabetically — Europe before North America
        assertEquals("Europe", result.get(0).name());
        assertEquals("North America", result.get(1).name());
    }

    @Test
    void testGetContinentsReturnsEmptyList() {
        TrackRepository mockRepo = mock(TrackRepository.class);
        when(mockRepo.countCountriesByRegion()).thenReturn(Collections.emptyList());

        ContinentController controller = new ContinentController(mockRepo);
        List<ContinentDto> result = controller.getContinents();

        assertTrue(result.isEmpty());
    }

    // ── getCountriesByContinent ───────────────────────────────────────────────

    @Test
    void testGetCountriesByContinentReturns200WhenValid() {
        TrackRepository mockRepo = mock(TrackRepository.class);

        CountryView france = mock(CountryView.class);
        when(france.getCountry()).thenReturn("France");

        CountryView germany = mock(CountryView.class);
        when(germany.getCountry()).thenReturn("Germany");

        when(mockRepo.findCountriesByRegion("EU")).thenReturn(Arrays.asList(france, germany));

        ContinentController controller = new ContinentController(mockRepo);
        ResponseEntity<List<CountryDto>> response = controller.getCountriesByContinent("Europe");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("France", response.getBody().get(0).name());
    }

    @Test
    void testGetCountriesByContinentReturns404WhenUnknown() {
        TrackRepository mockRepo = mock(TrackRepository.class);

        ContinentController controller = new ContinentController(mockRepo);
        ResponseEntity<List<CountryDto>> response = controller.getCountriesByContinent("Fake Continent");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(mockRepo, never()).findCountriesByRegion(any());
    }
}