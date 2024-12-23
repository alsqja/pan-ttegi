package com.example.panttegi.card;

import com.example.panttegi.comment.Comment;
import com.example.panttegi.common.BaseEntity;
import com.example.panttegi.file.File;
import com.example.panttegi.list.BoardList;
import com.example.panttegi.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "card")
@Getter
@NoArgsConstructor
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "list_id")
    private BoardList boardList;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    public Card(String title, String description, int position, LocalDateTime endAt, User user, User manager) {
        this.title = title;
        this.description = description;
        this.position = position;
        this.endAt = endAt;
        this.user = user;
        this.manager = manager;
    }
}
