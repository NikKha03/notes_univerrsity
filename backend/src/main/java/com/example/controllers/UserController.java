package com.example.controllers;

import com.example.model.Note;
import com.example.model.User;
import com.example.repository.NoteRepository;
import com.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/notes/user")
@AllArgsConstructor
public class UserController {
    private UserRepository userRepository;
    private NoteRepository noteRepository;

    @GetMapping("/board")
    public ResponseEntity<?> board(Principal principal) {
        if (principal == null)
            return null;

        List<Note> allNotes = noteRepository.findAll();
        User user = userRepository.getUserByUsername(principal.getName());

        return ResponseEntity.ok("{ \"data\": " + allNotes.stream().filter(note -> note.getOwner_id().equals(user.getUser_id())).toList() + " }");
    }

    @PostMapping("/board/create-note")
    public String createNote(Principal principal, @RequestBody Note note) {
        if (principal == null)
            return null;

        User user = userRepository.getUserByUsername(principal.getName());
        note.setOwner_id(user.getUser_id());
        noteRepository.save(note);
        return "Заметка успешно создана!";
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        return ResponseEntity.ok("Какой я красивый!");
    }
}
