package org.autorizador.account;

import org.autorizador.DomainEvent;

public class AccountEvent implements DomainEvent {

    private boolean activeCard;
    private double availableLimit;

    public AccountEvent(boolean activeCard, double availableLimit) {
        this.activeCard = activeCard;
        this.availableLimit = availableLimit;
    }

    public boolean isActiveCard() {
        return activeCard;
    }

    public void setActiveCard(boolean activeCard) {
        this.activeCard = activeCard;
    }

    public double getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(double availableLimit) {
        this.availableLimit = availableLimit;
    }
}
