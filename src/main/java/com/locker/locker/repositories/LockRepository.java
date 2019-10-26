package com.locker.locker.repositories;

import com.locker.locker.entities.Lock;
import com.locker.locker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LockRepository extends JpaRepository<Lock, Long> {
    Optional<List<Lock>> findByUser(User user);
}
