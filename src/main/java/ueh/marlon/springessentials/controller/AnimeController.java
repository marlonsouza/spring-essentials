package ueh.marlon.springessentials.controller;

import java.util.List;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ueh.marlon.springessentials.domain.Anime;
import ueh.marlon.springessentials.requests.AnimePostRequestBody;
import ueh.marlon.springessentials.requests.AnimePutRequestBody;
import ueh.marlon.springessentials.service.AnimeService;

@RestController
@RequestMapping("/animes")
@RequiredArgsConstructor
@Slf4j
public class AnimeController {

    private final AnimeService animeService;

    @GetMapping
    @Operation(summary = "List all animes paginated", description = "The default size is 20, use the parameter size to change the default value",
    tags = {"anime"})
    public ResponseEntity<Page<Anime>> list(@ParameterObject Pageable pageable){
    	return ResponseEntity.ok(animeService.listAll(pageable));
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Anime>> listAll(){
    	return ResponseEntity.ok(animeService.listAllNonPage());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Anime> findById(@PathVariable Long id){
    	return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }
    
    @GetMapping("/by-id/{id}")
    public ResponseEntity<Anime> findByIdAuthenticated(@PathVariable Long id, 
    												   @AuthenticationPrincipal UserDetails userDetails){
    	log.info(userDetails.getUsername());
    	return ResponseEntity.ok(animeService.findByIdOrThrowBadRequestException(id));
    }
    
    @GetMapping("/find")
    public ResponseEntity<List<Anime>> findByName(@RequestParam String name){
    	return ResponseEntity.ok(animeService.findByName(name));
    }
    
    @PostMapping
    public ResponseEntity<Anime> create(@RequestBody @Valid AnimePostRequestBody anime){
    	Anime createdAnime = animeService.save(anime);
    	
    	return new ResponseEntity<Anime>(createdAnime, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "When anime doesnt exists")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id){
    	animeService.delete(id);
    	return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Anime> update(@PathVariable Long id, @RequestBody @Valid AnimePutRequestBody animePutRequestBody){
    	animeService.update(id, animePutRequestBody);
    	return ResponseEntity.noContent().build();
    }
}
