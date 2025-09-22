package org.examplelongndreou.backendchataiapp.services;

import org.examplelongndreou.backendchataiapp.models.Message;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MessageService {

    private CompletionsApiService completionsApiService;




    public MessageService(CompletionsApiService completionsApiService) {
        this.completionsApiService = completionsApiService;
    }


    public Message createMessage(Message message) {

        // TODO Save message to the database

        // get response from LLM
        String llmResponse = completionsApiService.getCompletion("https://api.groq.com/openai/v1/chat/completions", "llama-3.1-8b-instant", message.getContent());



        Message llmMessage = new Message();
        llmMessage.setContent(llmResponse);
        llmMessage.setChatId(message.getChatId());
        llmMessage.setCreatedAt(Instant.now());
        llmMessage.setCreatedByUserId(null);
        // TODO Save response to the database



        return llmMessage;
    }
}

