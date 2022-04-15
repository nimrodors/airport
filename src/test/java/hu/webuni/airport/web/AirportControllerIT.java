package hu.webuni.airport.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.airport.dto.AirportDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AirportControllerIT {
	
	//felveszem az alap URI-t, hogy tudjam tesztelni
	private static final String BASE_URI="/api/airports";
	
	//hogy tudjak a bodyban adatokat küldeni, ezt kell felvegyem
	@Autowired
	WebTestClient webTestClient;

	//Létrehozok egy új repteret, majd lekérdezem és megnézzük, hogy ki listázza e?
	@Test
	void testThatCreatedAirportIsListes() throws Exception {
		List<AirportDto> airportsBefore = getAllAirports();
		
		AirportDto newAirport = new AirportDto(5, "fgdfgdgdg", "WER");
		createAirport(newAirport);
		
		List<AirportDto> airportsAfter = getAllAirports();
		
		//a meglévőknek egyeznie kell
		assertThat(airportsAfter.subList(0, airportsBefore.size()))
			.usingRecursiveFieldByFieldElementComparator()
			.containsExactlyElementsOf(airportsBefore);
		
		//az új airport lista utolsó eleme meg kell egyezzen az újjonan létrehozott airport elemével
		assertThat(airportsAfter.get(airportsAfter.size() - 1))
			.usingRecursiveComparison()
			.isEqualTo(newAirport);
	}

	private void createAirport(AirportDto newAirport) {
		webTestClient
			.post()
			.uri(BASE_URI)
			.bodyValue(newAirport)
			.exchange()
			.expectStatus()
			.isOk();
	}

	private List<AirportDto> getAllAirports() {
		List<AirportDto> responseList = webTestClient
			.get()
			.uri(BASE_URI)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBodyList(AirportDto.class)
			.returnResult().getResponseBody();
		
		Collections.sort(responseList, (a1, a2) -> Long.compare(a1.getId(), a2.getId()));
		
		return responseList;
	}
}
