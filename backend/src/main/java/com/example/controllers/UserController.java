package com.example.controllers;

import com.example.model.Note;
import com.example.model.User;
import com.example.repository.NoteRepository;
import com.example.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("notes", allNotes.stream().filter(note -> note.getOwner_id().equals(user.getUser_id())).toList());

        return ResponseEntity.ok(jsonObject.toString());
    }

    @PostMapping("/board/create-note")
    public ResponseEntity<?> createNote(Principal principal, @RequestBody Note note) {
        if (principal == null)
            return null;

        User user = userRepository.getUserByUsername(principal.getName());
        note.setOwner_id(user.getUser_id());
        noteRepository.save(note);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Заметка успешно создана!");

        return ResponseEntity.ok(jsonObject.toString());
    }

    @PutMapping("/board/update_note")
    public ResponseEntity<?> updateStudent(@RequestBody Note note) {
        // TODO

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Заметка успешно сохранена!");

        return ResponseEntity.ok(jsonObject.toString());
    }

    @DeleteMapping("/board/delete-note/{user_id}")
    public ResponseEntity<?> deleteNote(@PathVariable("user_id") Long user_id, Principal principal) {
        // TODO

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Заметка успешно удалена!");

        return ResponseEntity.ok(jsonObject.toString());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        return ResponseEntity.ok("Какой я красивый!");
    }
}
