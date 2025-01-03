package com.example.panttegi.workspace.entity;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.common.BaseEntity;
import com.example.panttegi.member.entity.Member;
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

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workspace")
@NoArgsConstructor
@Getter
public class Workspace extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "notify_channel")
    private String notifyChannel;

    @Column(name = "notify_token")
    private String notifyToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public Workspace(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public Workspace(String name, String description, String notifyChannel, String notifyToken) {
        this.name = name;
        this.description = description;
        this.notifyChannel = notifyChannel;
        this.notifyToken = notifyToken;
    }

    public void updateWorkspace(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateUser(User user) {
        this.user = user;
    }
}
