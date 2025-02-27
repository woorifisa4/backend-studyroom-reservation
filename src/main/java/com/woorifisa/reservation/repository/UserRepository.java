package com.woorifisa.reservation.repository;

import com.woorifisa.reservation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIdIn(List<Long> ids);

    Optional<User> findByEmail(String email);

    List<User> findByNameContaining(String keyword);
}
