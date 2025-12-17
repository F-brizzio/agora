package com.tuempresa.bodega.note;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getAll() {
        return noteService.getAllNotes();
    }

    @PostMapping
    public Note create(@RequestBody Note note) {
        return noteService.saveNote(note);
    }

    @PutMapping("/{id}")
    public Note update(@PathVariable Long id, @RequestBody Note note) {
        note.setId(id); // Asegurar ID
        return noteService.saveNote(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}