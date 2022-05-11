package hu.webuni.airport.service;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.airport.model.Flight;

public class FlightSpecification {
	
	public static Specification<Flight> hasId(long id){
		return (root, cq, cb) -> cb.equal(root.get("id"), id);
	}
}
