package org.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long accountNumber;
    @Column(nullable = false)
    private Double allMoney;
    @Column(nullable = false)
    private String currency;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();

    public Account() {
    }

    public Account(Long accountNumber, double allMoney, String currency) {
        this.accountNumber = accountNumber;
        this.allMoney = allMoney;
        this.currency = currency;
    }

    // добавление транзакций в список (с одного счета может осуществляться много транзакций)
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setAccount(this);
        if (transaction.getName().startsWith("Refill")) {
            allMoney = allMoney + transaction.getSum();
        } else {
            allMoney = allMoney - transaction.getSum();
        }
    }

    //создание рандомного списка пользователей
    public static List<Account> randomAccounts() {
        List<Account> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i < 3) {
                list.add(new Account((long) (100 * i+1 * 10), 100 + i * 1000, "UAH"));
            } else if (i > 6) {
                list.add(new Account((long) (100 * i+1 * 10), 100 + i * 1000, "USD"));
            } else {
                list.add(new Account((long) (100 * i+1 * 10), 100 + i * 1000, "EUR"));
            }
        }
        return list;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(Double allMoney) {
        this.allMoney = allMoney;
    }

    public User getUser() {
        return user;
    }

    //у одного пользователя может быть много счетов
    public void setUser(User user) {
        this.user = user;
    }

    public String allTransactions() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator()).append("------Transactions------").append(System.lineSeparator());
        for (Transaction t : transactions) {
            sb.append(t.getName() + "---" + t.getSum() + "---" + t.getCurrency() + "---" + t.getRestOfMoney()
            ).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
