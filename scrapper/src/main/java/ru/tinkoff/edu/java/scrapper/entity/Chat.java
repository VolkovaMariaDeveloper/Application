package ru.tinkoff.edu.java.scrapper.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {
    @ManyToMany(mappedBy = "subscribers", cascade = CascadeType.PERSIST)
    Set<Links> trackedLinks;
    @Id
    @Column(name = "id")
    private Long id;
}
