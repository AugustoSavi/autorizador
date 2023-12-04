package org.autorizador;

import com.github.javafaker.Faker;
import org.autorizador.account.AccountEvent;
import org.autorizador.transaction.TransactionEvent;

import java.time.LocalDateTime;
import java.util.Locale;

public final class TransactionEventFactory {

    public static TransactionEvent fromNow(){
        return new TransactionEvent(
                new Faker().name().name(),
                2.00,
                LocalDateTime.now()
        );
    }

    public static TransactionEvent fromTime(LocalDateTime localDateTime){
        return new TransactionEvent(
                new Faker().name().name(),
                2.00,
                localDateTime
        );
    }

    public static TransactionEvent fromAmount(double amount){
        return new TransactionEvent(
                new Faker().name().name(),
                amount,
                LocalDateTime.now()
        );
    }

    public static AccountEvent accountEvent(){
        return new AccountEvent(
                true,
                250.0
        );
    }

    public static AccountEvent accountCardNotActiveEvent(){
        return new AccountEvent(
                false,
                250.0
        );
    }
}
