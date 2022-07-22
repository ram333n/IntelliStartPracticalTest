package org.example;

import java.math.BigDecimal;

public class User {
    private String firstName;
    private String lastName;
    private BigDecimal moneyAmount;

    public User(String firstName, String lastName, BigDecimal moneyAmount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.moneyAmount = moneyAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "First name : " + firstName +  "\n" +
               "Second name : " +  lastName + "\n" +
               "Money amount : " + moneyAmount;
    }
}
