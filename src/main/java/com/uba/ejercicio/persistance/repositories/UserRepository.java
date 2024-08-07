package com.uba.ejercicio.persistance.repositories;

import com.uba.ejercicio.persistance.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value =
            "SELECT * FROM users u WHERE u.id IN " +
                    "(SELECT uf.follower_id FROM user_follows uf WHERE uf.followed_id = :id)",
            nativeQuery = true
    )
    List<User> getFollowers(@Param("id") Long id);
}
