package com.journalApp.service;

import com.journalApp.entity.JournalEntity;
import com.journalApp.entity.User;
import com.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private  UserService userService;


    @Transactional
    public void  saveEntry(JournalEntity journalEntity, String   username) {
        try {
            User user = userService.findByUsername(username);
            journalEntity.setDate(LocalDateTime.now());
            JournalEntity saved = journalEntryRepository.save(journalEntity);
            user.getJournalEntities().add(saved);
            user.setUsername(null);
            userService.saveEntry(user);

        }
        catch (Exception e) {
            System.out.println(e);
        }

    }

    public void saveEntry(JournalEntity journalEntity) {
        journalEntryRepository.save(journalEntity);
    }

    public List<JournalEntity> getAll() {
        return  journalEntryRepository.findAll();
    }

    public Optional<JournalEntity> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public  void deleteById(ObjectId id,String username) {
        User user = userService.findByUsername(username);
        user.getJournalEntities().removeIf(x -> x.getId().equals(id));
userService.saveEntry(user);
        journalEntryRepository.deleteById(id);
    }
}
