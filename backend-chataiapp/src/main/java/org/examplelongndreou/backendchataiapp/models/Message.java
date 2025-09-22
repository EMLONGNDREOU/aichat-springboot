package org.examplelongndreou.backendchataiapp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
// TODO this is stored in the database, it is an @Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "creation_dt")
    private Instant createdAt;

    private Long createdByUserId;

    // public Message() {}

    public void setCreatedByUserId(Object o) {

    }
}