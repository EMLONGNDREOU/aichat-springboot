package org.examplelongndreou.backendchataiapp.services;

import org.examplelongndreou.backendchataiapp.exceptions.ExcStatus;
import org.examplelongndreou.backendchataiapp.models.Chat;
import org.examplelongndreou.backendchataiapp.models.criteria.ChatCriteria;
import org.examplelongndreou.backendchataiapp.repositories.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public List<Chat> getAll(ChatCriteria criteria) throws ExcStatus {

        Instant twoYearsAgo = Instant.now().minusSeconds(60 * 60 * 24 * 365 * 2);

        if (criteria.getFrom() != null && criteria.getFrom().isBefore(twoYearsAgo)) {
            throw new ExcStatus(HttpStatus.BAD_REQUEST,
                    "The 'from' date cannot be more than two years ago.");
        }

        return chatRepository.findAll(criteria.getUserId(),
                criteria.getUsername(),
                criteria.getFrom(),
                criteria.getTo());
    }

}
