package ueh.marlon.springessentials.controller;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ueh.marlon.springessentials.domain.Anime;
import ueh.marlon.springessentials.requests.AnimePostRequestBody;
import ueh.marlon.springessentials.requests.AnimePutRequestBody;
import ueh.marlon.springessentials.service.AnimeService;
import ueh.marlon.springessentials.util.AnimeCreator;
import ueh.marlon.springessentials.util.AnimePostRequestBodyCreator;
import ueh.marlon.springessentials.util.AnimePutRequestBodyCreator;

@DisplayName("Tests for Anime Controller")
@ExtendWith(SpringExtension.class)
public class AnimeControllerTest {
	
	@InjectMocks
	private AnimeController animeController;
	
	@Mock
	private AnimeService animeServiceMock;
	
	@BeforeEach
	void setUp(){
		List<Anime> animes = List.of(AnimeCreator.validAnimeWithId());
		PageImpl<Anime> animePage = new PageImpl<>(animes);
		
		BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any()))
			.thenReturn(animePage);
		
		BDDMockito.when(animeServiceMock.listAllNonPage())
			.thenReturn(animes);
		
		BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
			.thenReturn(AnimeCreator.validAnimeWithId());
		
		BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
			.thenReturn(animes);
		
		BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
			.thenReturn(AnimeCreator.validAnimeWithId());
		
		BDDMockito.when(animeServiceMock.update(ArgumentMatchers.anyLong(), ArgumentMatchers.any(AnimePutRequestBody.class)))
			.thenReturn(AnimeCreator.validAnimeWithId());
		
		BDDMockito.doNothing().when(animeServiceMock).delete(ArgumentMatchers.anyLong());
	}
	
	@Test
	@DisplayName("Returns list of anime inside page object when successfull (Status Ok)")
	void whenGETTisCalledThenOnePageOfAnimesAndStatusOKIsReturned() {
		
		String expectedName = AnimeCreator.validAnimeWithId().getName();
		
		ResponseEntity<Page<Anime>> responseGET = animeController.list(null);
		
		
		Page<Anime> animePage = responseGET.getBody();
		
		assertThat(responseGET.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
		
		assertThat(animePage).isNotNull();
		
		assertThat(animePage.toList())
			.isNotEmpty()
			.hasSize(1);
		
		assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
		
	}
	
	@Test
	@DisplayName("Returns list of animes when successfull (Status Ok)")
	void whenGETTListAllisCalledThenOnePageOfAnimesAndStatusOKIsReturned() {
		
		String expectedName = AnimeCreator.validAnimeWithId().getName();
		
		ResponseEntity<List<Anime>> responseAnimes = animeController.listAll();
		
		
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
		
		Long expectedId = AnimeCreator.validAnimeWithId().getId();
		
		ResponseEntity<Anime> responseFindById = animeController.findById(expectedId);
		
		Anime anime = responseFindById.getBody();
		
		assertThat(responseFindById.getStatusCode())
			.isEqualTo(HttpStatus.OK);
		
		assertThat(anime).isNotNull();
			
		assertThat(anime.getId()).isEqualTo(expectedId);
		
	}
	
	@Test
	@DisplayName("Find by name returns a list of anime when successfull (Status Ok)")
	void whenGETTWithNameisCalledThenOneAnimeIsReturned() {
		
		Anime expectedAnime = AnimeCreator.validAnimeWithId();
		
		ResponseEntity<List<Anime>> responseFindById = animeController.findByName(expectedAnime.getName());
		
		List<Anime> animes= responseFindById.getBody();
		
		assertThat(responseFindById.getStatusCode())
			.isEqualTo(HttpStatus.OK);
		
		assertThat(animes)
			.isNotEmpty()
			.hasSize(1);
			
		assertThat(animes.get(0).getId()).isEqualTo(expectedAnime.getId());
		
	}
	
	@Test
	@DisplayName("Find by name returns a empty list when animes is not found (Status Ok)")
	void whenGETTWithNameisCalledThenAEmptyListIsReturned() {
		
		Anime expectedAnime = AnimeCreator.validAnimeWithId();
		
		BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
			.thenReturn(Collections.emptyList());
		
		ResponseEntity<List<Anime>> responseFindById = animeController.findByName(expectedAnime.getName());
		
		List<Anime> animes= responseFindById.getBody();
		
		assertThat(responseFindById.getStatusCode())
			.isEqualTo(HttpStatus.OK);
		
		assertThat(animes)
			.isEmpty();
			
	}
	
	@Test
	@DisplayName("Create returns anime when successfull (Status Created)")
	void whenPOSTIsCalledAAnimeIsCreated(){
		Long expectedId = AnimeCreator.validAnimeWithId().getId();
		
		ResponseEntity<Anime> responseCreatedAnime = animeController.create(AnimePostRequestBodyCreator.validPostAnime());
		
		assertThat(responseCreatedAnime.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		
		Anime createdAnime = responseCreatedAnime.getBody();
		
		assertThat(createdAnime).isNotNull()
			.isEqualTo(AnimeCreator.validAnimeWithId());
		
		assertThat(createdAnime.getId()).isEqualTo(expectedId);
	}
	
	@Test
	@DisplayName("Update replace anime when successfull (Status No Content)")
	void whenPUTIsCalledAAnimeIsUpdated(){
		ResponseEntity<Anime> responseUpdatedAnime = animeController.update(1L,AnimePutRequestBodyCreator.validAnimePut());
		
		assertThat(responseUpdatedAnime).isNotNull();
		assertThat(responseUpdatedAnime.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		//OR
		
		Assertions.assertThatCode(() -> animeController.update(1L,AnimePutRequestBodyCreator.validAnimePut()))
			.doesNotThrowAnyException();
	}
	
	@Test
	@DisplayName("delete removes anime when successfull (Status No Content)")
	void whenDELETEIsCalledAAnimeIsRemoved(){
		ResponseEntity<Void> responseDeletedAnime = animeController.delete(1L);
		
		assertThat(responseDeletedAnime).isNotNull();
		assertThat(responseDeletedAnime.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		
		//OR
		
		Assertions.assertThatCode(() -> animeController.delete(1L))
			.doesNotThrowAnyException();
	}
	
}