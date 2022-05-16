package hu.webuni.airport.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.data.TemporalUnitWithinOffset;
import org.junit.jupiter.api.BeforeEach;
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
	
	@BeforeEach
	public void init() {
		flightRepository.deleteAll();
		airportRepository.deleteAll();
	}
	
	@Test
	void testCreateFlight() throws Exception {
		String flightNumber = "AAA";
		long takeoff = createAirport("airport1", "iata1");
		long landing = createAirport("airport2", "iata2");
		
		LocalDateTime dateTime = LocalDateTime.now();
		long flight = createFlight(flightNumber, takeoff, landing, dateTime);
		
		Optional<Flight> saveFlightOptional = flightRepository.findById(flight);
		//megvizsgáljuk, hogy megcsinálta e a Flight-ot
		assertThat(saveFlightOptional).isNotEmpty();
		
		Flight savedFlight = saveFlightOptional.get();
		assertThat(savedFlight.getFlightNumber()).isEqualTo(flightNumber);
		assertThat(savedFlight.getTakeOffTime()).isCloseTo(dateTime, new TemporalUnitWithinOffset(1, ChronoUnit.MICROS));
		assertThat(savedFlight.getTakeOff().getId()).isEqualTo(takeoff);
		assertThat(savedFlight.getLanding().getId()).isEqualTo(landing);
	}

	private long createAirport(String name, String iata) {
		return airportRepository.save(new Airport(name, iata)).getId();
	}
	
	@Test
	void testFindFlightsByExmaple() throws Exception {
		long airportId1 = createAirport("airport1", "iata1");
		long airportId2 = createAirport("airport1", "iata2");
		long airportId3 = createAirport("airport1", "3iata");
		long airportId4 = createAirport("airport1", "2iata4");
		LocalDateTime takeoff = LocalDateTime.of(2021, 4, 23, 8, 0, 0);
		long flight1 = createFlight("ABC123", airportId1, airportId3, takeoff);
		long flight2 = createFlight("ABC1234", airportId2, airportId3, takeoff.plusHours(2));
		long flight3 = createFlight("BC123", airportId1, airportId3, takeoff);
		long flight4 = createFlight("ABC123", airportId1, airportId3, takeoff.plusDays(1));
		createFlight("ABC123", airportId3, airportId3, takeoff);
		
		Flight example = new Flight();
		example.setFlightNumber("ABC123");
		example.setTakeOffTime(takeoff);
		example.setTakeOff(new Airport("sasa", "iata"));
		List<Flight> foundFlight = this.airportService.findflightsByExample(example);
		
		assertThat(foundFlight.stream()
				.map(Flight::getId)
				.collect(Collectors.toList()))
				.containsExactly(flight1, flight2);
		
	}

	private long createFlight(String flightNumber,long takeoff, long landing, LocalDateTime dateTime) {
		return airportService.createFlight(flightNumber, takeoff, landing, dateTime).getId();
	}
}
