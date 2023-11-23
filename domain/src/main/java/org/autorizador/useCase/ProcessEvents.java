package org.autorizador.useCase;

import org.autorizador.DomainEvent;
import org.autorizador.account.AccountEvent;
import org.autorizador.useCase.records.ProcessResultOutput;
import org.autorizador.violation.ViolationDefinition;

import java.util.ArrayList;
import java.util.List;

import static org.autorizador.violation.Violation.buildFromEnum;

public class ProcessEvents {

    private AccountEvent account;
    private List<String> violations;

    public ProcessResultOutput process(List<DomainEvent> events){
        violations = new ArrayList<>();
        events.forEach(domainEvent -> {
            if (domainEvent instanceof AccountEvent){
                initAccount((AccountEvent) domainEvent);
            }
        });

        return null;
    }

    private void initAccount(AccountEvent accountEvent) {
        if (account == null){
            account = new AccountEvent(accountEvent.isActiveCard(), accountEvent.getAvailableLimit());
        }

        violations.add(buildFromEnum(ViolationDefinition.ACCOUNT_ALREADY_CREATED));
    }
}
