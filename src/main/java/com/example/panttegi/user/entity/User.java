package com.example.panttegi.user.entity;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.card.entity.Card;
import com.example.panttegi.comment.Comment;
import com.example.panttegi.common.BaseEntity;
import com.example.panttegi.enums.UserRole;
import com.example.panttegi.file.repository.entity.File;
import com.example.panttegi.list.BoardList;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.workspace.entity.Workspace;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.jetbrains.annotations.TestOnly;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at is null")
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

    @Column(name = "deleted_at", nullable = true)
    private LocalDateTime deletedAt;

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

    public User(String email, String password, String profileUrl, String name, UserRole role) {
        this.email = email;
        this.password = password;
        this.profileUrl = profileUrl;
        this.name = name;
        this.role = role;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole role) {
        this.role = role;
    }

    public void updateProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public void updateName(String name) {
        this.name = name;
    }

    @TestOnly
    public User(Long id, String email, String password, String profileUrl, String name, UserRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.profileUrl = profileUrl;
        this.name = name;
        this.role = role;
    }
}
