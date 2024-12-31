package com.example.panttegi.card.entity;

import com.example.panttegi.comment.entity.Comment;
import com.example.panttegi.common.BaseEntity;
import com.example.panttegi.file.entity.File;
import com.example.panttegi.list.entity.BoardList;
import com.example.panttegi.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String position;

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

    @JsonIgnore
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    public Card(String title, String description, String position, LocalDateTime endAt, User user, User manager, BoardList boardList, List<File> files) {
        this.title = title;
        this.description = description;
        this.position = position;
        this.endAt = endAt;
        this.user = user;
        this.manager = manager;
        this.boardList = boardList;
        this.files = files;
    }

    public void updateTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void updateDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }

    public void updatePosition(String position) {
        this.position = position;
    }

    public void updateEndAt(LocalDateTime endAt) {
        if (endAt != null) {
            this.endAt = endAt;
        }
    }

    public void updateManager(User manager) {
        if (manager != null) {
            this.manager = manager;
        }
    }

    public void updateBoardList(BoardList boardList) {
        if (boardList != null) {
            this.boardList = boardList;
        }
    }

    public void updateFiles(List<File> files) {
        if (files != null) {
            this.files.clear();
            this.files.addAll(files);
        }
    }


}
