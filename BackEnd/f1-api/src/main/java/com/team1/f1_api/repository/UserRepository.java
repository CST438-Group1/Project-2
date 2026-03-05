package com.team1.f1_api.repository;


//Adds user repo
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.team1.f1_api.model.User;
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
