package ru.practicum.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "commentator")
    private User commentator;

    private String text;

}
