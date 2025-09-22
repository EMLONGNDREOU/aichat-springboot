package org.examplelongndreou.backendchataiapp.controllers;

import org.examplelongndreou.backendchataiapp.models.Message;
import org.examplelongndreou.backendchataiapp.services.MessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagesController {

    private MessageService messageService;

    public MessagesController(MessageService messageService) {
        this.messageService = messageService;
    }


    @PostMapping("/messages")
    public Message createMessage(@RequestBody Message message) {

        return messageService.createMessage(message);
    }
}
