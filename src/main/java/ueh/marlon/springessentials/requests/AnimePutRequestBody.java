package ueh.marlon.springessentials.requests;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnimePutRequestBody {
	
	@NotNull(message = "The anime id cannot be null")
	private Long id;
	
	@NotEmpty(message = "The anime name cannot be empty")
	@Schema(description = "This is the Anime's name", example = "Goku", required = true)
	private String name;
}
