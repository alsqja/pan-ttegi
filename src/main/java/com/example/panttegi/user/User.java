package com.example.panttegi.user;

import com.example.panttegi.board.Board;
import com.example.panttegi.card.Card;
import com.example.panttegi.comment.Comment;
import com.example.panttegi.common.BaseEntity;
import com.example.panttegi.enums.UserRole;
import com.example.panttegi.file.File;
import com.example.panttegi.list.BoardList;
import com.example.panttegi.member.Member;
import com.example.panttegi.workspace.Workspace;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted is false")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_url", nullable = true)
    private String profileUrl;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Workspace> workspaces = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BoardList> boardLists = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Card> cards = new ArrayList<>();

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Card> managingCard = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<File> files = new ArrayList<>();

    @PrePersist
    public void setDefaultIsDeleted() {
        if (this.isDeleted == null) {
            this.isDeleted = false;
        }
    }

    public User(String email, String password, String profileUrl, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.profileUrl = profileUrl;
        this.name = name;
        this.role = role;
    }
}