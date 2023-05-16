package ru.tinkoff.edu.java.scrapper.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "links")
public class Links {

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "chat_link",
        joinColumns = @JoinColumn(name = "link_id"),
        inverseJoinColumns = @JoinColumn(name = "chat_id"))
    Set<Chat> subscribers;
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url")
    private String url;
    @CreationTimestamp
    @Column(name = "last_check_time")
    private OffsetDateTime lastCheckTime;
    @Column(name = "count")
    private int count;

}
