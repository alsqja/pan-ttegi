package com.example.panttegi.list.entity;

import com.example.panttegi.board.entity.Board;
import com.example.panttegi.card.entity.Card;
import com.example.panttegi.common.BaseEntity;
import com.example.panttegi.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "list")
@Getter
@NoArgsConstructor
public class BoardList extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "position", nullable = false, precision = 17, scale = 5)
    private BigDecimal position;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "boardList", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Card> cards = new ArrayList<>();

    public BoardList(String title, BigDecimal position, User user, Board board) {
        this.title = title;
        this.position = position;
        this.user = user;
        this.board = board;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updatePosition(BigDecimal position) {
        this.position = position;
    }
}
