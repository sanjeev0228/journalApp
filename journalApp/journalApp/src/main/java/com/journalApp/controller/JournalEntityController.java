package com.journalApp.controller;



import com.journalApp.entity.JournalEntity;
import com.journalApp.entity.User;
import com.journalApp.service.JournalEntryService;
import com.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/journal")
public class JournalEntityController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{/username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String  username){
        User user = userService.findByUsername(username);
        List<JournalEntity> all =  user.getJournalEntities();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }

    @PostMapping("{username}")
    public ResponseEntity <JournalEntity> createEntry(@RequestBody JournalEntity journalEntity,@PathVariable String username){
        try {
            User user = userService.findByUsername(username);

            journalEntryService.saveEntry(journalEntity ,username);
            return new ResponseEntity<>(journalEntity, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }



    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntity> journalEntity = journalEntryService.findById(id);
        if (journalEntity.isPresent()) {
            return  new ResponseEntity<>(journalEntity.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @DeleteMapping("id/{username}/{id}")
    public ResponseEntity<?> deleteEntryById(@PathVariable ObjectId id,@PathVariable String username){
        journalEntryService.deleteById(id,username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateEntryById(@PathVariable ObjectId id, @RequestBody JournalEntity newEntry,@PathVariable String username){

        JournalEntity jrn = journalEntryService.findById(id).orElse(null);
        if(jrn != null){
            jrn.setTitle(!newEntry.getTitle().isEmpty() ? newEntry.getTitle() : jrn.getTitle());
            jrn.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : jrn.getContent());
            journalEntryService.saveEntry(jrn);
            return new ResponseEntity<>(jrn, HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }



}
