package org.example;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phone;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    public User() {
    }

    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    //добавление счета в список (один пользователь может иметь много счетов)
    public void addAccount(Account account) {
        accounts.add(account);
        account.setUser(this);
    }

    //создание рандомного списка пользователей
    public static List<User> randomUsers() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new User("US" + i + 1, "100" + i * 100));
        }
        return list;
    }

    //связывание рандомно созданых списков для теста
    public static void addRandomListsForTest(List<User> users, List<Account> accounts, List<Transaction> transactions) {
        for (int i = 0; i < accounts.size(); i++) {
            if (i == 0) {
                accounts.get(0).addTransaction(transactions.get(0));
                accounts.get(0).addTransaction(transactions.get(1));
            } else if (i == 1) {
                accounts.get(1).addTransaction(transactions.get(2));
                accounts.get(1).addTransaction(transactions.get(3));
            } else if (i == 2) {
                accounts.get(2).addTransaction(transactions.get(4));
            } else if (i == 3) {
                accounts.get(3).addTransaction(transactions.get(15));
            } else if (i == 4) {
                accounts.get(4).addTransaction(transactions.get(16));
            } else if (i == 5) {
                accounts.get(5).addTransaction(transactions.get(17));
            } else if (i == 6) {
                accounts.get(6).addTransaction(transactions.get(18));
                accounts.get(6).addTransaction(transactions.get(19));
            } else if (i == 7) {
                accounts.get(7).addTransaction(transactions.get(5));
                accounts.get(7).addTransaction(transactions.get(6));
                accounts.get(7).addTransaction(transactions.get(7));
            } else if (i == 8) {
                accounts.get(8).addTransaction(transactions.get(8));
                accounts.get(8).addTransaction(transactions.get(9));
                accounts.get(8).addTransaction(transactions.get(10));
            } else if (i == 9) {
                accounts.get(9).addTransaction(transactions.get(11));
                accounts.get(9).addTransaction(transactions.get(12));
                accounts.get(9).addTransaction(transactions.get(13));
                accounts.get(9).addTransaction(transactions.get(14));
            }
        }
        for (int i = 0; i < users.size(); i++) {
            if (i == 0) {
                users.get(0).addAccount(accounts.get(0));
                users.get(0).addAccount(accounts.get(3));
                users.get(0).addAccount(accounts.get(7));
            } else if (i == 1) {
                users.get(1).addAccount(accounts.get(1));
                users.get(1).addAccount(accounts.get(4));
                users.get(1).addAccount(accounts.get(8));
            } else if (i == 2) {
                users.get(2).addAccount(accounts.get(2));
            } else if (i == 3) {
                users.get(3).addAccount(accounts.get(5));
                users.get(3).addAccount(accounts.get(9));
            } else if (i == 4) {
                users.get(4).addAccount(accounts.get(6));
            }
        }
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getBankAccounts() {
        return accounts;
    }

    public void setBankAccounts(List<Account> bankAccounts) {
        this.accounts = bankAccounts;
    }

    private String accountsId() {
        StringBuilder sb = new StringBuilder();
        for (Account a : accounts) {
            sb.append(a.getId()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String nameAndCurrency() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator());
        for (Account a : accounts) {
            sb.append(a.getAccountNumber()).append(" || ").append(a.getAllMoney()).append(" || ")
                    .append(a.getCurrency()).append(a.allTransactions());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' + "," + System.lineSeparator() +
                "********ACCOUNTS********" + nameAndCurrency() +
                '}'+System.lineSeparator();
    }
}
