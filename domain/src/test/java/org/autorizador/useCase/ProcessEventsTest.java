package org.autorizador.useCase;

import org.autorizador.DomainEvent;
import org.autorizador.TransactionEventFactory;
import org.autorizador.transaction.TransactionEvent;
import org.autorizador.useCase.records.ProcessResultOutput;
import org.autorizador.violation.ViolationDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessEventsTest {

    private ProcessEvents useCase;

    @BeforeEach
    void setUp() {
        useCase = new ProcessEvents();
    }

    @DisplayName("should be process create sucesfully")
    @Test
    void process() {
        List<DomainEvent> accountEvents = Collections.singletonList(TransactionEventFactory.accountEvent());

        ProcessResultOutput output = useCase.process(accountEvents);

        assertTrue(output.violations().isEmpty());
        assertTrue(output.account().isActiveCard());
        assertEquals(250, output.account().getAvailableLimit());
    }

    @DisplayName("should be return 1 violatino account already created")
    @Test
    void expect1Violaion() {
        List<DomainEvent> accountEvents = Collections.singletonList(TransactionEventFactory.accountEvent());

        ProcessResultOutput output = useCase.process(accountEvents);
        output = useCase.process(accountEvents);

        assertEquals(1, output.violations().size());
        assertEquals(ViolationDefinition.ACCOUNT_ALREADY_CREATED.name(), output.violations().get(0));
        assertTrue(output.account().isActiveCard());
        assertEquals(250, output.account().getAvailableLimit());
    }

    @DisplayName("should be return ACCOUNT_NOT_INITIALIZED")
    @Test
    void expectACCOUNT_NOT_INITIALIZED() {
        List<DomainEvent> accountEvents = Collections.singletonList(TransactionEventFactory.fromNow());

        ProcessResultOutput output = useCase.process(accountEvents);

        assertEquals(1, output.violations().size());
        assertEquals(ViolationDefinition.ACCOUNT_NOT_INITIALIZED.name(), output.violations().get(0));
        assertNull(output.account());
    }

    @DisplayName("should be return CARD_NOT_ACTIVE")
    @Test
    void expectCARD_NOT_ACTIVE() {
        List<DomainEvent> accountEvents = Arrays.asList(
                TransactionEventFactory.accountCardNotActiveEvent(),
                TransactionEventFactory.fromNow());

        ProcessResultOutput output = useCase.process(accountEvents);

        assertEquals(1, output.violations().size());
        assertEquals(ViolationDefinition.CARD_NOT_ACTIVE.name(), output.violations().get(0));
        assertFalse(output.account().isActiveCard());
    }

    @DisplayName("should be return DOUBLED_TRANSACTION")
    @Test
    void expectDOUBLED_TRANSACTION() {
        TransactionEvent transactionEvent = TransactionEventFactory.fromNow();
        TransactionEvent transactionEventDoubled = TransactionEventFactory.fromTime(transactionEvent.getTime().plusSeconds(30));

        List<DomainEvent> accountEvents = Arrays.asList(
                TransactionEventFactory.accountEvent(),
                transactionEvent,
                transactionEventDoubled);

        ProcessResultOutput output = useCase.process(accountEvents);

        assertEquals(1, output.violations().size());
        assertEquals(ViolationDefinition.DOUBLED_TRANSACTION.name(), output.violations().get(0));
        assertTrue(output.account().isActiveCard());

        transactionEvent = TransactionEventFactory.fromNow();
        transactionEventDoubled = TransactionEventFactory.fromTime(transactionEvent.getTime().plusMinutes(1));


        accountEvents = Arrays.asList(
                transactionEvent,
                transactionEventDoubled);

        output = useCase.process(accountEvents);

        assertTrue(output.violations().isEmpty());
        assertTrue(output.account().isActiveCard());
    }

    @DisplayName("should be return HIGH_FREQUENCY_SMALL_INTERVAL")
    @Test
    void expectHIGH_FREQUENCY_SMALL_INTERVAL() {
        TransactionEvent transactionEvent = TransactionEventFactory.fromNow();

        List<DomainEvent> accountEvents = Arrays.asList(
                TransactionEventFactory.accountEvent(),
                transactionEvent,
                new TransactionEvent(
                        transactionEvent.getMerchant(),
                        3.50,
                        transactionEvent.getTime()
                ));

        ProcessResultOutput output = useCase.process(accountEvents);

        assertEquals(1, output.violations().size());
        assertEquals(ViolationDefinition.HIGH_FREQUENCY_SMALL_INTERVAL.name(), output.violations().get(0));
        assertTrue(output.account().isActiveCard());
    }

    @DisplayName("should be return INSUFFICIENT_LIMIT")
    @Test
    void expectINSUFFICIENT_LIMIT() {
        TransactionEvent transactionEvent = TransactionEventFactory.fromAmount(350);

        List<DomainEvent> accountEvents = Arrays.asList(
                TransactionEventFactory.accountEvent(),
                transactionEvent
        );

        ProcessResultOutput output = useCase.process(accountEvents);

        assertEquals(1, output.violations().size());
        assertEquals(ViolationDefinition.INSUFFICIENT_LIMIT.name(), output.violations().get(0));
        assertTrue(output.account().isActiveCard());
    }
}
