package com.home.example.pgEnum.core.model.jpa;

import com.home.example.pgEnum.configuration.TestConfigUnspecifiedString;
import com.home.example.pgEnum.core.model.SortableDbo;
import com.home.example.pgEnum.core.model.SortableRepository;
import com.home.example.pgEnum.core.model.SortableStatus;
import com.home.example.pgEnum.core.model.SortableType;
import com.home.example.pgEnum.util.TruncateExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.event.ApplicationEventsTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(TruncateExtension.class)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        MockitoTestExecutionListener.class,
        ResetMocksTestExecutionListener.class,
        ApplicationEventsTestExecutionListener.class,
})
@SpringBootTest
@Import(TestConfigUnspecifiedString.class)
@ComponentScan("com.home.data.processing.examples")
public class PgEnumWithConnectionParameter {

    @Autowired
    SortableRepository sortableRepository;

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
    void findByStatus() {
        sortableRepository.save(new SortableDbo(SortableType.PALLET, SortableStatus.ARRIVED_DIRECT));
        sortableRepository.save(new SortableDbo(SortableType.PALLET, SortableStatus.SHIPPED_DIRECT));

        List<SortableDbo> byStatus = sortableRepository.findByStatus(SortableStatus.SHIPPED_DIRECT);
        assertThat(byStatus).hasSize(1);
        assertThat(byStatus.get(0))
                .extracting(SortableDbo::getType, SortableDbo::getStatus)
                .contains(SortableType.PALLET, SortableStatus.SHIPPED_DIRECT);
    }

}
