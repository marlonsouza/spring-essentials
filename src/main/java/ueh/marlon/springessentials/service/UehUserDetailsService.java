package ueh.marlon.springessentials.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ueh.marlon.springessentials.repository.UehUserRepository;

@Service
@RequiredArgsConstructor
public class UehUserDetailsService implements UserDetailsService{

	private final UehUserRepository uehUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return Optional
				.ofNullable(uehUserRepository.findByUsername(username))
				.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
	}

}
