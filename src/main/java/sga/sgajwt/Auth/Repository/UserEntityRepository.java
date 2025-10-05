package sga.sgajwt.Auth.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sga.sgajwt.Auth.Models.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Integer> {
    // el ?1 indica el orden del parametro a recibir 
    // @Query("SELECT u FROM User u WHERE u.username = ?1")
    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
