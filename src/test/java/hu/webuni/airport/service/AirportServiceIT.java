package hu.webuni.airport.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class AirportServiceIT {

	@Autowired
	AirportService airportService;
	
	@Autowired
	AirportRepository airportRepository;
	
	@Autowired
	FlightRepository flightRepository;
	
	@Test
	void testCreateFlight() throws Exception {
		String flightNumber = "AAA";
		long takeoff = airportRepository.save(new Airport("airport1", "iata1")).getId();
		long landing = airportRepository.save(new Airport("airport2", "iata2")).getId();
		LocalDateTime dateTime = LocalDateTime.now();
		Flight flight = airportService.createFlight(flightNumber, takeoff, landing, dateTime);
		
		Optional<Flight> saveFlightOptional = flightRepository.findById(flight.getId());
		//megvizsgáljuk, hogy megcsinálta e a Flight-ot
		assertThat(saveFlightOptional).isNotEmpty();
		
		Flight savedFlight = saveFlightOptional.get();
		assertThat(savedFlight.getFlightNumber()).isEqualTo(flightNumber);
		assertThat(savedFlight.getTakeOffTime()).isCloseTo(dateTime, new TemporalUnitWithinOffset(1, ChronoUnit.MICROS));
		assertThat(savedFlight.getTakeOff().getId()).isEqualTo(takeoff);
		assertThat(savedFlight.getLanding().getId()).isEqualTo(landing);
	}
}
