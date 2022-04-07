package hu.webuni.airport.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

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
import hu.webuni.airport.service.NonUniqueIataException;

@RestController
@RequestMapping("/api/airports")
public class AirportController {
	
	private Map<Long, AirportDto> airports = new HashMap<>();
	
	{
		airports.put(1L, new AirportDto(1, "abc", "XYZ"));
		airports.put(2L, new AirportDto(2, "def", "UVW"));
	}
	
	@GetMapping
	public List<AirportDto> getAll() {
		return new ArrayList<>(airports.values());
	}
	
	@GetMapping("/{id}")
	//public ResponseEntity<AirportDto> getById(@PathVariable long id) {
	public AirportDto getById(@PathVariable long id) {
		AirportDto airportDto = airports.get(id);
//		if(airportDto != null)
//			return ResponseEntity.ok(airportDto);
//		else
//			return ResponseEntity.notFound().build();
		if(airportDto != null)
			return airportDto;
		else 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND); 
	}
	
	@PostMapping
	public AirportDto createAirport(@RequestBody @Valid AirportDto airportDto) {
			checkUniqueIate(airportDto.getIata());
			airports.put(airportDto.getId(), airportDto);
			return airportDto;
	}
	
	//meglévő módosítása
	@PutMapping("/{id}")
	public ResponseEntity<AirportDto> modifyAirport(@PathVariable long id, @RequestBody AirportDto airportDto) {
		if(!airports.containsKey(id))
			return ResponseEntity.notFound().build();
		
		checkUniqueIate(airportDto.getIata());
		airportDto.setId(id);
		airports.put(id, airportDto);
		return ResponseEntity.ok(airportDto);
	}

	//itt megvizsgálja, hogy létezik e már ez az IATA. Ennek a vizsgálatára csináltunk egy új osztályt: NonUniqueIataException
	private void checkUniqueIate(String iata) {
		Optional<AirportDto> airportWithSameIata = airports
				.values()
				.stream()
				.filter(a -> a.getIata().equals(iata))
				.findAny();
		if(airportWithSameIata.isPresent())
			throw new NonUniqueIataException(iata);
	}

	@DeleteMapping("/{id}")
	public void deleteAirport(@PathVariable long id) {
		airports.remove(id);
	}
}
