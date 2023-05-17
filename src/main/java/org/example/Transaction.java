package org.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double sum;

    private Double restOfMoney;
    @Column(nullable = false)
    private String currency;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction() {
    }

    public Transaction(String name, double sum, String currency) {
        this.name = name;
        this.sum = sum;
        this.currency = currency;
    }

    //создание рандомного списка транзакций
    public static List<Transaction> randomTransactions() {
        List<Transaction> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            if (i < 5) {
                list.add(new Transaction("Payment for goods", 10 * (i + 2), "UAH"));
            } else if (i >= 5 && i < 15) {
                list.add(new Transaction("Phone recharge", 10 * (i + 2), "USD"));
            } else {
                list.add(new Transaction("Transfer between accounts", 10 * (i + 2), "EUR"));
            }
        }
        return list;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public Account getAccount() {
        return account;
    }

    //много транзакций может осуществляться с одного счета
    public void setAccount(Account account) {
        this.account = account;
        if (this.name.startsWith("Refill")) {
            restOfMoney = account.getAllMoney() + sum;
        } else {
            restOfMoney = account.getAllMoney() - sum;
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getRestOfMoney() {
        return restOfMoney;
    }

}
