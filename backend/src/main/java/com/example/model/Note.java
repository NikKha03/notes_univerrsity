package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notes")
public class Note {
    public Note() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noteId;

    @JoinColumn(name = "owner_id")
    private Long owner_id;

    private String header;

    @Column(length = 3000)
    private String text;

    private LocalDateTime date_creation;

    @PrePersist
    private void init() {
        date_creation = LocalDateTime.now();
    }
}
