package ueh.marlon.springessentials.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import org.springframework.web.client.RestTemplate;
import ueh.marlon.springessentials.domain.Anime;
import ueh.marlon.springessentials.repository.AnimeRepository;
import ueh.marlon.springessentials.requests.AnimePostRequestBody;
import ueh.marlon.springessentials.util.AnimeCreator;
import ueh.marlon.springessentials.util.AnimePostRequestBodyCreator;
import ueh.marlon.springessentials.wrapper.PageableResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT {
	
	@Autowired
	@Qualifier(value = "testRestTemplateRoleUser")
	private TestRestTemplate testRestTemplate;

	@LocalServerPort
	private int port;

	@Autowired
	private AnimeRepository animeRepository;

	@TestConfiguration
	@Lazy
	static class Config{

		@Bean(name = "testRestTemplateRoleUser")
		public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port){
			RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
					.rootUri("http://localhost:"+port)
					.basicAuthentication("marlon", "PHSouza");

			return new TestRestTemplate(restTemplateBuilder);
		}
	}

	@Test
	@DisplayName("Returns list of anime inside page object when successfull (Status Ok)")
	void whenGETTisCalledThenOnePageOfAnimesAndStatusOKIsReturned() {
		
		animeRepository.save(AnimeCreator.toBeSaved());
		
		String expectedName = AnimeCreator.toBeSaved().getName();
		
		ResponseEntity<PageableResponse<Anime>> responseAnimePage = testRestTemplate.exchange("/animes", HttpMethod.GET,null, new ParameterizedTypeReference<PageableResponse<Anime>>() {
		});
		
		assertThat(responseAnimePage.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		PageableResponse<Anime> animePage = responseAnimePage.getBody();
		
		assertThat(animePage).isNotNull();
		
		assertThat(animePage.toList())
			.isNotEmpty()
			.hasSize(1);
		
		assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
		
	}
	
	@Test
	@DisplayName("Returns list of animes when successfull (Status Ok)")
	void whenGETTListAllisCalledThenOnePageOfAnimesAndStatusOKIsReturned() {
		
		animeRepository.save(AnimeCreator.toBeSaved());
		
		String expectedName = AnimeCreator.toBeSaved().getName();
		
		ResponseEntity<List<Anime>> responseAnimes = testRestTemplate.exchange("/animes/all", HttpMethod.GET,null, new ParameterizedTypeReference<List<Anime>>() {
		});
		
		List<Anime> animesList = responseAnimes.getBody();
		
		assertThat(responseAnimes.getStatusCode()).isEqualTo(HttpStatus.OK);
		
		assertThat(animesList).isNotNull();
		
		assertThat(animesList)
			.isNotEmpty()
			.hasSize(1);
		
		assertThat(animesList.get(0).getName()).isEqualTo(expectedName);
		
	}
	
	@Test
	@DisplayName("Returns one anime when successfull (Status Ok)")
	void whenGETTisCalledThenOneAnimeIsReturned() {
		
		Anime savedAnime = animeRepository.save(AnimeCreator.toBeSaved());
		
		Long expectedId = savedAnime.getId();
		
		Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class,expectedId);
		
		assertThat(anime).isNotNull();
			
		assertThat(anime.getId()).isEqualTo(expectedId);
		
	}
	
	@Test
	@DisplayName("Find by name returns a list of anime when successfull (Status Ok)")
	void whenGETTWithNameisCalledThenOneAnimeIsReturned() {
		
		Anime expectedAnime = animeRepository.save(AnimeCreator.toBeSaved());
		
		String url = String.format("/animes/find?name=%s", expectedAnime.getName());
		
		ResponseEntity<List<Anime>> responseFindById = testRestTemplate.exchange(url, HttpMethod.GET,null, new ParameterizedTypeReference<List<Anime>>() {
		});
		
		List<Anime> animes= responseFindById.getBody();
		
		assertThat(responseFindById.getStatusCode())
			.isEqualTo(HttpStatus.OK);
		
		assertThat(animes)
			.isNotEmpty()
			.hasSize(1);
			
		assertThat(animes.get(0).getId()).isEqualTo(expectedAnime.getId());
		assertThat(animes.get(0).getName()).isEqualTo(expectedAnime.getName());
		
	}
	
	@Test
	@DisplayName("Find by name returns a empty list when animes is not found (Status Ok)")
	void whenGETTWithNameisCalledThenAEmptyListIsReturned() {
		
		animeRepository.save(AnimeCreator.toBeSaved());
		
		Anime expectedAnime = AnimeCreator.validAnimeWithId();
		
		String url = String.format("/animes/find?name=%s", expectedAnime.getName());
		ResponseEntity<List<Anime>> responseFindById = testRestTemplate.exchange(url, HttpMethod.GET,null, new ParameterizedTypeReference<List<Anime>>() {
		});
		
		List<Anime> animes= responseFindById.getBody();
		
		assertThat(responseFindById.getStatusCode())
			.isEqualTo(HttpStatus.OK);
		
		assertThat(animes)
			.isEmpty();
			
	}
	
	
	@Test
	@DisplayName("Create returns anime when successfull (Status Created)")
	void whenPOSTIsCalledAAnimeIsCreated(){
		
		AnimePostRequestBody validPostAnime = AnimePostRequestBodyCreator.validPostAnime();
		
		ResponseEntity<Anime> responseCreatedAnime = testRestTemplate.postForEntity("/animes", validPostAnime, Anime.class);
	
		assertThat(responseCreatedAnime.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		Anime createdAnime = responseCreatedAnime.getBody();
		
		assertThat(createdAnime).isNotNull();
		assertThat(createdAnime.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Update replace anime when successfull (Status No Content)")
	void whenPUTIsCalledAAnimeIsUpdated(){
		Anime createdAnime = animeRepository.save(AnimeCreator.toBeSaved());
		
		createdAnime.setName(createdAnime.getName()+" alterado");
		
		String url = String.format("/animes/%s", createdAnime.getId());
		
		ResponseEntity<Void> responseUpdatedAnime = testRestTemplate.exchange(url, HttpMethod.PUT,new HttpEntity<>(createdAnime), Void.class);
		
		assertThat(responseUpdatedAnime).isNotNull();
		assertThat(responseUpdatedAnime.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
	
	@Test
	@DisplayName("delete removes anime when successfull (Status No Content)")
	void whenDELETEIsCalledAAnimeIsRemoved(){
		
		Anime createdAnime = animeRepository.save(AnimeCreator.toBeSaved());
		
		ResponseEntity<Void> responseDeletedAnime = 
				testRestTemplate.exchange("/animes/{id}", HttpMethod.DELETE, null, Void.class,createdAnime.getId());
		
		assertThat(responseDeletedAnime).isNotNull();
		assertThat(responseDeletedAnime.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
	}
}
