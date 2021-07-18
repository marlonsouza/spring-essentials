package ueh.marlon.springessentials.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ueh.marlon.springessentials.domain.Anime;
import ueh.marlon.springessentials.exception.BadRequestException;
import ueh.marlon.springessentials.mapper.AnimeMapper;
import ueh.marlon.springessentials.repository.AnimeRepository;
import ueh.marlon.springessentials.requests.AnimePostRequestBody;
import ueh.marlon.springessentials.requests.AnimePutRequestBody;
import ueh.marlon.springessentials.util.AnimeCreator;
import ueh.marlon.springessentials.util.AnimePostRequestBodyCreator;
import ueh.marlon.springessentials.util.AnimePutRequestBodyCreator;

@DisplayName("Tests for Anime Service")
@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

	@InjectMocks
	private AnimeService animeService;
	
	@Mock
	private AnimeRepository animeRepositoryMock;
	
	@Mock
	private AnimeMapper animeMapperMock; 
	
	@BeforeEach
	void setUp(){
		List<Anime> animes = List.of(AnimeCreator.validAnimeWithId());
		PageImpl<Anime> animePage = new PageImpl<>(animes);
		
		BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(Pageable.class)))
			.thenReturn(animePage);
		
		BDDMockito.when(animeRepositoryMock.findAll())
			.thenReturn(animes);
		
		BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
			.thenReturn(Optional.of(AnimeCreator.validAnimeWithId()));
		
		BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
			.thenReturn(animes);
		
		BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
			.thenReturn(AnimeCreator.validAnimeWithId());
		
		BDDMockito.doNothing().when(animeRepositoryMock).delete(ArgumentMatchers.any(Anime.class));
		
		BDDMockito.when(animeMapperMock.toAnime(ArgumentMatchers.any(AnimePostRequestBody.class)))
			.thenReturn(AnimeCreator.validAnimeWithId());
		
		BDDMockito.when(animeMapperMock.toAnime(ArgumentMatchers.any(AnimePutRequestBody.class)))
			.thenReturn(AnimeCreator.validAnimeWithId());
		
	}
	
	@Test
	@DisplayName("Returns list of anime inside page object when successfull")
	void whenListAllisCalledWithPageableThenOnePageOfAnimesAndStatusOKIsReturned() {
		
		String expectedName = AnimeCreator.validAnimeWithId().getName();
		
		Page<Anime> animePage = animeService.listAll(PageRequest.of(1, 1));
		
		
		assertThat(animePage).isNotNull();
		
		assertThat(animePage.toList())
			.isNotEmpty()
			.hasSize(1);
		
		assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
		
	}
	
	@Test
	@DisplayName("Returns list of animes when successfull")
	void whenListAllIsCalledThenOnePageOfAnimesAndStatusOKIsReturned() {
		
		String expectedName = AnimeCreator.validAnimeWithId().getName();
		
		List<Anime> animesList = animeService.listAllNonPage();
		
		assertThat(animesList).isNotNull();
		
		assertThat(animesList)
			.isNotEmpty()
			.hasSize(1);
		
		assertThat(animesList.get(0).getName()).isEqualTo(expectedName);
		
	}
	
	@Test
	@DisplayName("Returns one anime when successfull")
	void whenFindByIdOrThrowBadRequestExceptionIsCalledThenOneAnimeIsReturned() {
		
		Long expectedId = AnimeCreator.validAnimeWithId().getId();
		
		Anime anime = animeService.findByIdOrThrowBadRequestException(expectedId);
		
		assertThat(anime).isNotNull();
			
		assertThat(anime.getId()).isEqualTo(expectedId);
		
	}
	
	@Test
	@DisplayName("Find by id throw BadRequestException when animes is not found")
	void whenFindByIdOrThrowBadRequestExceptionIsCalledThenThrowException() {
		
		BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
			.thenReturn(Optional.empty());
		
		assertThatCode(() -> animeService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
				.isInstanceOf(BadRequestException.class);
		
		//OR
		
		Assertions.assertThatExceptionOfType(BadRequestException.class)
			.isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()));
		
	}
	
	@Test
	@DisplayName("Find by name returns a list of anime when successfull")
	void whenFindByNameIsCalledThenOneAnimeIsReturned() {
		
		Anime expectedAnime = AnimeCreator.validAnimeWithId();
		
		List<Anime> animes = animeService.findByName(expectedAnime.getName());
		
		assertThat(animes)
			.isNotEmpty()
			.hasSize(1);
			
		assertThat(animes.get(0).getId()).isEqualTo(expectedAnime.getId());
		
	}
	
	@Test
	@DisplayName("Find by name returns a empty list when animes is not found")
	void whenGETTWithNameisCalledThenAEmptyListIsReturned() {
		
		Anime expectedAnime = AnimeCreator.validAnimeWithId();
		
		BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
			.thenReturn(Collections.emptyList());
		
		List<Anime> animes = animeService.findByName(expectedAnime.getName());
		
		assertThat(animes)
			.isNotNull()
			.isEmpty();
			
	}
	
	@Test
	@DisplayName("Create returns anime when successfull")
	void whenSaveIsCalledAAnimeIsCreated(){
		Long expectedId = AnimeCreator.validAnimeWithId().getId();
		
		Anime createdAnime = animeService.save(AnimePostRequestBodyCreator.validPostAnime());
		
		assertThat(createdAnime).isNotNull()
			.isEqualTo(AnimeCreator.validAnimeWithId());
		
		assertThat(createdAnime.getId()).isEqualTo(expectedId);
	}
	
	@Test
	@DisplayName("Update replace anime when successfull")
	void whenUpdateIsCalledAAnimeIsUpdated(){
		Assertions.assertThatCode(() -> animeService.update(1L,AnimePutRequestBodyCreator.validAnimePut()))
			.doesNotThrowAnyException();
	}
	
	@Test
	@DisplayName("delete removes anime when successfull")
	void whenDeleteIsCalledAAnimeIsRemoved(){
		Assertions.assertThatCode(() -> animeService.delete(1L))
			.doesNotThrowAnyException();
	}

}
