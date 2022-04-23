package hu.webuni.airport.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.airport.model.Airport;

@Service
public class AirportService {

//	private Map<Long, Airport> airports = new HashMap<>();
//
//	{
//		airports.put(1L, new Airport(1, "abc", "XYZ"));
//		airports.put(2L, new Airport(2, "def", "UVW"));
//	}

	@PersistenceContext
	EntityManager em;
	
	@Transactional
	public Airport save(Airport airport) {
		checkUniqueIate(airport.getIata());
		// airports.put(airport.getId(), airport);
		em.persist(airport);
		return airport;
	}
	
	@Transactional
	public Airport update(Airport airport) {
		checkUniqueIate(airport.getIata());
		return em.merge(airport);
	}

	// itt megvizsgálja, hogy létezik e már ez az IATA. Ennek a vizsgálatára
	// csináltunk egy új osztályt: NonUniqueIataException
	public void checkUniqueIate(String iata) {
		Long count = em.createNamedQuery("Airport.countByIata", Long.class).setParameter("iata", iata)
				.getSingleResult();
//		Optional<Airport> airportWithSameIata = airports.values().stream().filter(a -> a.getIata().equals(iata))
//				.findAny();
		if (count > 0)
			throw new NonUniqueIataException(iata);
	}

	public List<Airport> findAll() {
		//return new ArrayList<>(airports.values());
		return em.createQuery("SELECT a FROM Airport a", Airport.class).getResultList();
	}

	public Airport findById(long id) {
		//return airports.get(id);
		return em.find(Airport.class, id);
	}
	
	@Transactional
	public void delete(long id) {
		//airports.remove(id);
		em.remove(findById(id));
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
