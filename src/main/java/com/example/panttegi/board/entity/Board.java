package com.example.panttegi.board.entity;

import com.example.panttegi.common.BaseEntity;
import com.example.panttegi.list.BoardList;
import com.example.panttegi.user.entity.User;
import com.example.panttegi.workspace.entity.Workspace;
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
@Table(name = "board")
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color", nullable = true)
    private String color;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardList> boardLists = new ArrayList<>();

    public Board(String color, String imageUrl, String name, User user, Workspace workspace) {
        this.color = color;
        this.imageUrl = imageUrl;
        this.name = name;
        this.user = user;
        this.workspace = workspace;
    }

    public Board(String color, String imageUrl, String name) {
        this.color = color;
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public void updateWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public void patchField(String color, String imageUrl, String name) {
        if (color != null) {
            this.color = color;
        }
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
        if (name != null) {
            this.name = name;
        }
    }
}
