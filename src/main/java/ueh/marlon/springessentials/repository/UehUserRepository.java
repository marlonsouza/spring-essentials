package ueh.marlon.springessentials.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ueh.marlon.springessentials.domain.UehUser;

public interface UehUserRepository extends JpaRepository<UehUser, Long> {

	UehUser findByUsername(String username);
	
}
