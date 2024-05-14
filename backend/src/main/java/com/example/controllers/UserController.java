package com.example.controllers;

import com.example.model.Note;
import com.example.model.User;
import com.example.repository.NoteRepository;
import com.example.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notes/user")
@AllArgsConstructor
public class UserController {
    private final NamedParameterJdbcTemplate template;
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
        jsonObject.put("note_id", note.getNoteId());

        return ResponseEntity.ok(jsonObject.toString());
    }

    @PutMapping("/board/update_note/{note_id}")
    public ResponseEntity<?> updateNote(@PathVariable("note_id") Long note_id, Principal principal, @RequestBody Note note) {
        JSONObject jsonObject = new JSONObject();
        User user = userRepository.getUserByUsername(principal.getName());

        Note currentNote = noteRepository.findByNoteId(note_id);
        if (!currentNote.getOwner_id().equals(user.getUser_id())) {
            jsonObject.put("message", "ОШИБКА! Нельзя менять чужие заметки!");
            return ResponseEntity.ok(jsonObject.toString());
        }

        note.setNoteId(note_id);
        note.setOwner_id(user.getUser_id());
        note.setDate_creation(LocalDateTime.now());
        noteRepository.save(note);

        jsonObject.put("message", "Заметка успешно сохранена!");
        return ResponseEntity.ok(jsonObject.toString());
    }

    @Transactional
    @DeleteMapping("/board/delete_note/{note_id}")
    public ResponseEntity<?> deleteNote(@PathVariable("note_id") Long note_id, Principal principal) {
        // TODO
        JSONObject jsonObject = new JSONObject();
        User user = userRepository.getUserByUsername(principal.getName());

        Note currentNote = noteRepository.findByNoteId(note_id);
        if (!currentNote.getOwner_id().equals(user.getUser_id())) {
            jsonObject.put("message", "ОШИБКА! Нельзя удалять чужие заметки!");
            return ResponseEntity.ok(jsonObject.toString());
        }

        String sqlPurchaseHistory = String.format("DELETE FROM notes WHERE note_id = %d", note_id);
        template.update(sqlPurchaseHistory, new MapSqlParameterSource());

        jsonObject.put("message", "Заметка успешно удалена!");
        return ResponseEntity.ok(jsonObject.toString());
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        return ResponseEntity.ok("Какой я красивый!");
    }
}
