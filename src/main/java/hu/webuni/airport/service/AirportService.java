package hu.webuni.airport.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hu.webuni.airport.model.Airport;

@Service
public class AirportService {

	private Map<Long, Airport> airports = new HashMap<>();

	{
		airports.put(1L, new Airport(1, "abc", "XYZ"));
		airports.put(2L, new Airport(2, "def", "UVW"));
	}

	public Airport save(Airport airport) {
		checkUniqueIate(airport.getIata());
		airports.put(airport.getId(), airport);
		return airport;
	}

	// itt megvizsgálja, hogy létezik e már ez az IATA. Ennek a vizsgálatára
	// csináltunk egy új osztályt: NonUniqueIataException
	public void checkUniqueIate(String iata) {
		Optional<Airport> airportWithSameIata = airports.values().stream().filter(a -> a.getIata().equals(iata))
				.findAny();
		if (airportWithSameIata.isPresent())
			throw new NonUniqueIataException(iata);
	}

	public List<Airport> findAll() {
		return new ArrayList<>(airports.values());
	}

	public Airport findById(long id) {
		return airports.get(id);
	}

	public void delete(long id) {
		airports.remove(id);
	}

	public Map<Long, Airport> getAirports() {
		return airports;
	}

	public void setAirports(Map<Long, Airport> airports) {
		this.airports = airports;
	}
}
