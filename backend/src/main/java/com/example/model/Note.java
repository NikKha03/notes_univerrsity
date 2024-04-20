package com.example.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "notes")
public class Note {
    public Note() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long note_id;

    @JoinColumn(name = "owner_id")
    private Long owner_id;

    private String header;

    @Column(length = 3000)
    private String text;
}
