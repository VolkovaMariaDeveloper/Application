package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.entity.Chat;

import java.util.List;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    @NotNull List<Chat> findAll();
    void deleteAll();
}
