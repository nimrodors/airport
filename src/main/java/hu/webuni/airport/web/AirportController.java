package hu.webuni.airport.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.airport.dto.AirportDto;
import hu.webuni.airport.mapper.AirportMapper;
import hu.webuni.airport.model.Airport;
import hu.webuni.airport.service.AirportService;
import hu.webuni.airport.service.NonUniqueIataException;

@RestController
@RequestMapping("/api/airports")
public class AirportController {
	
	@Autowired
	AirportService airportService;
	
	@Autowired
	AirportMapper airportMapper;
	
	@GetMapping
	public List<AirportDto> getAll() {
		return airportMapper.airportsToDtos(airportService.findAll());
	}
//	
	@GetMapping("/{id}")
	//public ResponseEntity<AirportDto> getById(@PathVariable long id) {
	public AirportDto getById(@PathVariable long id) {
		Airport airport = airportService.findById(id);
		//AirportDto airportDto = airports.get(id);
//		if(airportDto != null)
//			return ResponseEntity.ok(airportDto);
//		else
//			return ResponseEntity.notFound().build();
		if(airport != null)
			return airportMapper.airportsToDto(airport);
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
	}
	
	@PostMapping
	public AirportDto createAirport(@RequestBody @Valid AirportDto airportDto) {
			Airport airport = airportService.save(airportMapper.dtoToAirport(airportDto));
			return airportMapper.airportsToDto(airport);
	}
//	
//	//meglévő módosítása
	@PutMapping("/{id}")
	public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id, @RequestBody AirportDto airportDto) {
		if(airportService.getAirports().containsValue(id))
			return ResponseEntity.notFound().build();
//		if(!airports.containsKey(id))
//			return ResponseEntity.notFound().build();
		Airport airport = airportMapper.dtoToAirport(airportDto);
		
		airportService.checkUniqueIate(airport.getIata());
		
		//checkUniqueIate(airportDto.getIata());
		//airportDto.setId(id);
		airport.setId(id);
		//airports.put(id, airportDto);
		airportService.getAirports().put(id, airport);
		return ResponseEntity.ok(airportDto);
	}
//

	@DeleteMapping("/{id}")
	public void deleteAirport(@PathVariable long id) {
		airportService.delete(id);
	}
}
