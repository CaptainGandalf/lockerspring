package com.locker.locker.repositories;

import com.locker.locker.entities.Key;
import com.locker.locker.entities.Lock;
import com.locker.locker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeyRepository extends JpaRepository<Key, Long> {
    Optional<List<Key>> findByIssuedBy(User user);
    Optional<List<Key>> findByIssuedFor(User user);
    Optional<List<Key>> findAllByLock(Lock lock);
}
