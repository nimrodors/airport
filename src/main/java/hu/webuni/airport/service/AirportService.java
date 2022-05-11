package hu.webuni.airport.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.airport.model.Airport;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.repository.AirportRepository;
import hu.webuni.airport.repository.FlightRepository;

@Service
public class AirportService {

//	private Map<Long, Airport> airports = new HashMap<>();
//
//	{
//		airports.put(1L, new Airport(1, "abc", "XYZ"));
//		airports.put(2L, new Airport(2, "def", "UVW"));
//	}
	@Autowired
	AirportRepository airportRepository;
	
	@Autowired
	FlightRepository flightRepository;
	
	@Transactional
	public Airport save(Airport airport) {
		checkUniqueIate(airport.getIata(), null);
		// airports.put(airport.getId(), airport);
//		em.persist(airport);
		return airportRepository.save(airport);
	}
	
	@Transactional
	public Airport update(Airport airport) {
		checkUniqueIate(airport.getIata(), airport.getId());
		if(airportRepository.existsById(airport.getId()))
			return airportRepository.save(airport);
		else
			throw new NoSuchElementException();
	}

	// itt megvizsgálja, hogy létezik e már ez az IATA. Ennek a vizsgálatára
	// csináltunk egy új osztályt: NonUniqueIataException
	public void checkUniqueIate(String iata, Long id) {
		boolean forUpdate = id != null;
//		TypedQuery<Long> query = em.createNamedQuery(forUpdate ? "Airport.countByIataAndIdNotIn" : "Airport.countByIata", Long.class)
//				.setParameter("iata", iata);
//
//		if(forUpdate)
//			query.setParameter("id", id);
////		
//		Long count = query
//				.getSingleResult();
//		Optional<Airport> airportWithSameIata = airports.values().stream().filter(a -> a.getIata().equals(iata))
//				.findAny();
		Long count = forUpdate ? airportRepository.countByIataAndIdNot(iata, id) 
				: airportRepository.countByIata(iata); 
		
		if (count > 0)
			throw new NonUniqueIataException(iata);
	}

	public List<Airport> findAll() {
		//return new ArrayList<>(airports.values());
//		return em.createQuery("SELECT a FROM Airport a", Airport.class).getResultList();
		return airportRepository.findAll();
	}

	public Optional<Airport> findById(long id) {
		//return airports.get(id);
//		return em.find(Airport.class, id);
		return airportRepository.findById(id);
	}
	
	@Transactional
	public void delete(long id) {
		//airports.remove(id);
//		em.remove(findById(id));
		airportRepository.deleteById(id);
	}
	
	@Transactional
	public Flight createFlight(String flightNumber, long takeOffAirportId, long landingAirportId, LocalDateTime takeoffDateTime) {
		Flight flight = new Flight();
		flight.setFlightNumber(flightNumber);
		flight.setTakeOff(airportRepository.findById(takeOffAirportId).get());
		flight.setLanding(airportRepository.findById(landingAirportId).get());
		flight.setTakeOffTime(takeoffDateTime);
		return flightRepository.save(flight);
	}
	
	public List<Flight> findflightsByExample(Flight example) {
		
		long id = example.getId();
		String flightNumber = example.getFlightNumber();
		String takeoffIata = null;
		Airport takeOff = example.getTakeOff();
		if(takeOff != null) 
			takeoffIata = takeOff.getIata();
		LocalDateTime takeOffTime = example.getTakeOffTime();
		
		Specification<Flight> spec = Specification.where(null);
		if(id > 0) {
			spec = spec.and(FlightSpecification.hasId(id));
		}
		
		return null;
	}
//
//	public Map<Long, Airport> getAirports() {
//		return airports;
//	}
//
//	public void setAirports(Map<Long, Airport> airports) {
//		this.airports = airports;
//	}
}
