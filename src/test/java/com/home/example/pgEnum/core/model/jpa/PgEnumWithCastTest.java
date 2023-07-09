package com.home.example.pgEnum.core.model.jpa;

import com.home.example.pgEnum.core.model.SortableDbo;
import com.home.example.pgEnum.core.model.SortableRepository;
import com.home.example.pgEnum.core.model.SortableStatus;
import com.home.example.pgEnum.core.model.SortableType;
import com.home.example.pgEnum.util.WithDataBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessResourceUsageException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class PgEnumWithCastTest extends WithDataBase {

    @Autowired
    private SortableRepository sortableRepository;

    @BeforeAll
    void init() {
        jdbcTemplate.update("CREATE CAST (CHARACTER VARYING as sortable_type) WITH INOUT AS IMPLICIT");
        jdbcTemplate.update("CREATE CAST (CHARACTER VARYING as sortable_status) WITH INOUT AS IMPLICIT");
    }

    @Test
    void insert() {
        SortableDbo sortableDbo = new SortableDbo(SortableType.PALLET, SortableStatus.ARRIVED_DIRECT);
        sortableRepository.save(sortableDbo);

        List<SortableDbo> all = sortableRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0))
                .extracting(SortableDbo::getType, SortableDbo::getStatus)
                .contains(SortableType.PALLET, SortableStatus.ARRIVED_DIRECT);
    }

    @Test
    void update() {
        SortableDbo sortableDbo = new SortableDbo(SortableType.PALLET, SortableStatus.ARRIVED_DIRECT);
        sortableRepository.save(sortableDbo);

        List<SortableDbo> all = sortableRepository.findAll();
        all.forEach(s -> s.setStatus(SortableStatus.SHIPPED_DIRECT));
        sortableRepository.saveAll(all);

        all = sortableRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0))
                .extracting(SortableDbo::getType, SortableDbo::getStatus)
                .contains(SortableType.PALLET, SortableStatus.SHIPPED_DIRECT);
    }

    @Test
    void findByStatusIsNotWorking() {
        sortableRepository.save(new SortableDbo(SortableType.PALLET, SortableStatus.ARRIVED_DIRECT));
        sortableRepository.save(new SortableDbo(SortableType.PALLET, SortableStatus.SHIPPED_DIRECT));

        Assertions.assertThrows(
                InvalidDataAccessResourceUsageException.class,
                () -> sortableRepository.findByStatus(SortableStatus.ARRIVED_DIRECT)
        );
    }
}
