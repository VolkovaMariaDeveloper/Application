package ru.tinkoff.edu.java.scrapper.repository.jpa;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.edu.java.scrapper.entity.Chat;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
    @NotNull List<Chat> findAll();

    void deleteAll();
}
