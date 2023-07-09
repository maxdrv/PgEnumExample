package com.home.example.pgEnum.core.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

@Entity
@Table(name = "sortable")
public class SortableDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SortableStatus status;

    @Enumerated(EnumType.STRING)
    private SortableType type;

    public SortableDbo(SortableType type, SortableStatus status) {
        this.type = type;
        this.status = status;
    }
}
