package com.example.panttegi.workspace.entity;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.common.BaseEntity;
import com.example.panttegi.member.entity.Member;
import com.example.panttegi.user.entity.User;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    public Workspace(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public void workspaceUpdate(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
