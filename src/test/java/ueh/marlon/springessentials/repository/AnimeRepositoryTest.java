package ueh.marlon.springessentials.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import ueh.marlon.springessentials.domain.Anime;
import ueh.marlon.springessentials.util.AnimeCreator;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
public class AnimeRepositoryTest {

	@Autowired
	private AnimeRepository animeRepository;
	
	@Test
	@DisplayName("Create anime when successfull")
	public void whenSaveAnimeRepositoryThenReturnAnimePersisted() {
		Anime animeExpectToBeSaved = AnimeCreator.toBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeExpectToBeSaved);
		
		assertThat(animeSaved).isNotNull();
		assertThat(animeSaved.getName()).isEqualTo(animeExpectToBeSaved.getName());
		assertThat(animeSaved.getId()).isNotNull();
	}
	
	@Test
	@DisplayName("Update anime when successfull")
	public void whenUpdateAnimeRepositoryThenReturnAnimePersisted() {
		Anime animeExpectToBeSaved = AnimeCreator.updatedAnime();
		
		Anime animeSaved = this.animeRepository.save(animeExpectToBeSaved);
		
		animeSaved.setName("As aventuras de Bob esponja");
		
		Anime animeUpdated = this.animeRepository.save(animeSaved);
		
		assertThat(animeUpdated.getId()).isNotNull();
		assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());
	}
	
	@Test
	@DisplayName("Delete anime when successfull")
	public void whenDeleteAnimeRepositoryThenRemoveEntity() {
		Anime animeExpectToBeSaved = AnimeCreator.toBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeExpectToBeSaved);
		
		this.animeRepository.delete(animeSaved);
		
		Optional<Anime> findById = this.animeRepository.findById(animeSaved.getId());
		
		assertThat(findById.isPresent()).isFalse();
	}
	
	@Test
	@DisplayName("Find by name returns list of anime when successfull")
	public void whenFindByNameAnimeRepositoryThenReturnListWithOneElement() {
		Anime animeExpectToBeSaved = AnimeCreator.toBeSaved();
		
		Anime animeSaved = this.animeRepository.save(animeExpectToBeSaved);
		
		List<Anime> animeFoundByName = this.animeRepository.findByName(animeSaved.getName());
		
		assertThat(animeFoundByName)
			.isNotEmpty()
			.contains(animeSaved);
	}
	
	@Test
	@DisplayName("Find by name returns empty list of anime when successfull")
	public void whenFindByNameAnimeRepositoryThenReturnEmptyList() {
		Anime animeExpectToBeSaved = AnimeCreator.toBeSaved();
		
		this.animeRepository.save(animeExpectToBeSaved);
		
		List<Anime> animeFoundByName = this.animeRepository.findByName(animeExpectToBeSaved+"teste");
		
		assertThat(animeFoundByName).isEmpty();
	}
	
	@Test
	@DisplayName("Save throw ConstraintValidationException where name is empty")
	public void whenSaveAnimeWithEmptyNameRepositoryThenThrowException() {
		Anime animeExpectThrow = AnimeCreator.toBeSaved();
		
		animeExpectThrow.setName("");
		
		assertThatThrownBy(() -> this.animeRepository.save(animeExpectThrow))
			.isInstanceOf(ConstraintViolationException.class);
		
	}
	
	@Test
	@DisplayName("Save throw ConstraintValidationException and emit message where name is empty")
	public void whenSaveAnimeWithEmptyNameRepositoryThenThrowExceptionAndMessage() {
		Anime animeExpectThrow = AnimeCreator.toBeSaved();
		
		animeExpectThrow.setName("");
		
		Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> this.animeRepository.save(animeExpectThrow))
			.withMessageContaining("The anime name cannot be empty");
		
	}

	
	
}