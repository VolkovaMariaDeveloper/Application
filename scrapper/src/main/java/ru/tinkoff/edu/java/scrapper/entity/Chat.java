package ru.tinkoff.edu.java.scrapper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name ="chat")
public class Chat {
    @Id
    @Column(name="id")
    private Long id;

    @ManyToMany(mappedBy = "subscribers", cascade = CascadeType.PERSIST)
    Set<Links> trackedLinks;
}
