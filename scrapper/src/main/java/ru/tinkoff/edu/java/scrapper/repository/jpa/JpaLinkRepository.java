package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.entity.Links;

public interface JpaLinkRepository extends JpaRepository<Links, Long> {
    Links findByUrl(String url);

}
