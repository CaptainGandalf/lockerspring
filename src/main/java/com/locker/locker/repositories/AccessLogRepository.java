package com.locker.locker.repositories;

import com.locker.locker.entities.AccessLog;
import com.locker.locker.entities.Key;
import com.locker.locker.entities.Lock;
import com.locker.locker.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    Optional<List<AccessLog>> findByUser(User user);
    Optional<List<AccessLog>> findByLock(Lock lock);
}
