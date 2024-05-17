package ru.iu3.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.iu3.backend.models.Artists;

import java.util.Optional;

@Repository
public interface ArtistsRepository extends JpaRepository<Artists, Long>
{
    Optional<Artists> findByName(String name);
}
