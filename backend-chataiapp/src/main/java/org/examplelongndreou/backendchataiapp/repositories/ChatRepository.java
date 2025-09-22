package org.examplelongndreou.backendchataiapp.repositories;

import org.examplelongndreou.backendchataiapp.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(nativeQuery = true,
            value = "SELECT c.* " +
                    "FROM public.chats c " +
                    "   inner join public.users u on c.user_id = u.id " +
                    "WHERE (:userId is null or user_id = :userId) AND " +
                    "   (:username is null or u.username = :username) and " +
                    "   (CAST(:from AS timestamp) is null or creation_dt >= :from) AND " +
                    "   (CAST(:to AS timestamp) is null or creation_dt <= :to) ")
    List<Chat> findAll(Long userId,
                       String username,
                       Instant from,
                       Instant to);


    List<Chat> findByUserId(Long userId);


    // same as above with JPA:
//    @Query("SELECT c FROM Chat c " +
//            "  JOIN c.user u " +
//            "WHERE (:userId is null or c.user.id = :userId) AND " +
//            "  (:username is null or u.username = :username) AND " +
//            "  (CAST(:from AS timestamp) is null or c.createdAt >= :from) AND " +
//            "  (CAST(:to AS timestamp) is null or c.createdAt <= :to)")
//    List<Chat> findAllWithJPA(Long userId,
//                       String username,
//                       Instant from,
//                       Instant to);



}