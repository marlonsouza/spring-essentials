package ueh.marlon.springessentials.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ueh.marlon.springessentials.domain.Anime;
import ueh.marlon.springessentials.exception.BadRequestException;
import ueh.marlon.springessentials.mapper.AnimeMapper;
import ueh.marlon.springessentials.repository.AnimeRepository;
import ueh.marlon.springessentials.requests.AnimePostRequestBody;
import ueh.marlon.springessentials.requests.AnimePutRequestBody;

@Service
@RequiredArgsConstructor
public class AnimeService {
	
	private final AnimeRepository animeRepository;
	
	private final AnimeMapper animeMapper;
	
	public Page<Anime> listAll(Pageable pageable){
		return animeRepository.findAll(pageable);
	}
	
	public List<Anime> listAllNonPage() {
		return animeRepository.findAll();
	}

	public Anime findByIdOrThrowBadRequestException(Long id) {
		return animeRepository.findById(id)
				.orElseThrow(() -> new BadRequestException(String.format("Anime with id %s not found", id)));
	}

	@Transactional
	public Anime save(AnimePostRequestBody animePostRequestBody) {
		
		Anime animeToSave = animeMapper.toAnime(animePostRequestBody);
		
		return animeRepository.save(animeToSave);
	}

	public void delete(Long id) {
		Anime entityFound = this.findByIdOrThrowBadRequestException(id);
		animeRepository.delete(entityFound);
	}

	@Transactional
	public Anime update(Long id, AnimePutRequestBody animePutRequestBody) {
		this.findByIdOrThrowBadRequestException(id);
		
		Anime animeToUpdate = animeMapper.toAnime(animePutRequestBody);
		
		return animeRepository.save(animeToUpdate);
	}

	public List<Anime> findByName(String name) {
		return animeRepository.findByName(name);
	}
	
}
