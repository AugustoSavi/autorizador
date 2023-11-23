package org.autorizador.useCase;

import org.autorizador.DomainEvent;
import org.autorizador.account.AccountEvent;
import org.autorizador.useCase.records.ProcessResultOutput;
import org.autorizador.violation.ViolationDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessEventsTest {

    private ProcessEvents useCase;

    @BeforeEach
    void setUp() {
        useCase = new ProcessEvents();
    }

    @DisplayName("Deve processar um evento de criação de conta")
    @Test
    void process() {
        List<DomainEvent> accountEvents = Collections.singletonList(new AccountEvent(true, 250.0));

        ProcessResultOutput output = useCase.process(accountEvents);

        assertTrue(output.violations().isEmpty());
        assertTrue(output.account().isActiveCard());
        assertEquals(250, output.account().getAvailableLimit());
    }

    @DisplayName("Deve processar um evento de criação de conta com 1 exceção")
    @Test
    void expect1Violaion() {
        List<DomainEvent> accountEvents = Collections.singletonList(new AccountEvent(true, 250.0));

        ProcessResultOutput output = useCase.process(accountEvents);
        output = useCase.process(accountEvents);

        assertEquals(1, output.violations().size());
        assertEquals(ViolationDefinition.ACCOUNT_ALREADY_CREATED.name(), output.violations().get(0));
        assertTrue(output.account().isActiveCard());
        assertEquals(250, output.account().getAvailableLimit());
    }
}
