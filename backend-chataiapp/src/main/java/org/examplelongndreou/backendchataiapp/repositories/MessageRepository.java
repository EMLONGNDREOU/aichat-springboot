package org.examplelongndreou.backendchataiapp.repositories;


import org.examplelongndreou.backendchataiapp.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
    @Query(nativeQuery = true,
            value = "SELECT m.* " +
                    "FROM public.messages m " +
                    "   inner join public.chats c on m.chat_id = c.id " +
                    "WHERE (:chat_id is null or chat_id = :chat_id) ")

    List<Message> findAll(Long chatId);

   //Asc
    List<Message> findByChatId(Long chatId);
}