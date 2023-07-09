package com.home.example.pgEnum.core.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SortableRepository extends JpaRepository<SortableDbo, Long> {

    List<SortableDbo> findByStatus(SortableStatus status);

}
