package hu.webuni.airport.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.airport.model.Airport_;
import hu.webuni.airport.model.Flight;
import hu.webuni.airport.model.Flight_;

public class FlightSpecification {
	
	public static Specification<Flight> hasId(long id){
		return (root, cq, cb) -> cb.equal(root.get(Flight_.id), id);
	}

	public static Specification<Flight> hasFlightnumber(String flightNumber) {
		return (root, cq, cb) -> cb.like(root.get(Flight_.flightNumber), flightNumber + "%");
	}
	
	public static Specification<Flight> hasTakeofftime(LocalDateTime takeoffTime) {
		LocalDateTime startofday = LocalDateTime.of(takeoffTime.toLocalDate(), LocalTime.of(0, 0));
		return (root, cq, cb) -> cb.between(root.get(Flight_.takeOffTime),
				startofday, startofday.plusDays(1)); 
	}
	
	public static Specification<Flight> hasTakeoffIata(String takeoffIata) {
		
		return (root, cq, cb) -> cb.like(root.get(Flight_.takeOff).get(Airport_.iata),
				takeoffIata + "%"); 
	}
}
