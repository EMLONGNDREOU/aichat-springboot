package org.examplelongndreou.backendchataiapp.controllers;

import org.examplelongndreou.backendchataiapp.exceptions.ExcStatus;
import org.examplelongndreou.backendchataiapp.models.Chat;
import org.examplelongndreou.backendchataiapp.models.criteria.ChatCriteria;
import org.examplelongndreou.backendchataiapp.services.ChatService;
import org.examplelongndreou.backendchataiapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
@RestController
@Scope(
        value         = WebApplicationContext.SCOPE_REQUEST,
        proxyMode     = ScopedProxyMode.TARGET_CLASS
)
public class ChatController {

    Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;
    private UserService userService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;

        logger.debug("ChatController initialized with ChatService: " + userService);

    }

    @GetMapping("/chats")
    public List<Chat> getAllChats(ChatCriteria criteria) throws ExcStatus {

        // validate that the login username is equal to the username parameter
        //   criteria.setUserId(loggedInUser.getId());

        return chatService.getAll(criteria);
    }
}
