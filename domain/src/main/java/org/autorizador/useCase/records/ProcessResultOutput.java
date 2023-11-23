package org.autorizador.useCase.records;

import org.autorizador.account.AccountEvent;

import java.util.List;

public record ProcessResultOutput(
        AccountEvent accountEvent,
        List<String> violations
) {
}
