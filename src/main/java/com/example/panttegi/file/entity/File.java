package com.example.panttegi.file.entity;

import com.example.panttegi.card.entity.Card;
import com.example.panttegi.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "file")
@Getter
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void setDefaultCreatedAt() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public File(String url, User user, Card card) {
        this.url = url;
        this.user = user;
        this.card = card;
    }
}
