package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import javax.persistence.*;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        try {
            // create connection
            emf = Persistence.createEntityManagerFactory("JPASIBankTest");
            em = emf.createEntityManager();
            try {
                //создание списка рандомных пользователей
                List<User> usersList = User.randomUsers();
                //создание списка рандомных счетов
                List<Account> accountsList = Account.randomAccounts();
                //создание списка рандомных транзакций
                List<Transaction> transactionsList = Transaction.randomTransactions();

                //связывание рандомных списков для теста
                User.addRandomListsForTest(usersList, accountsList, transactionsList);

                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------------");
                System.out.println();

                //добавление пользователей в бд
                for (User u : usersList) {
                    addUser(u);
                }
                //вывод результата на экран
                viewUsers();

                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------------");
                System.out.println();

                //пополнение счета в нужной валюте (тестируем на User - id=1, name='US01', phone='1000 )
                refill((long) 10, 500.0, "UAH");
                refill((long) 310, 1000.0, "EUR");
                refill((long) 710, 10.0, "USD");

                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------------");
                System.out.println();

                //вывод одного пользователя на экран
                viewOneUser(1);

                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------------");
                System.out.println();

                //перевод средств между счетами в нужной валюте (тестируем на User - id=1 и User - id=11 )
                refillInOut((long) 10, (long) 110, 100.0, "UAH");
                //вывод пользователей на экран
                viewOneUser(1);
                viewOneUser(11);

                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------------");
                System.out.println();

                //конвертация валюти по курсу в рамках счетов одного пользователя (тестируем на  User - id=1, name='US01', phone='1000 )
                conversionOneUser(1, (long) 10, (long) 310, 100.0);
                conversionOneUser(1, (long) 310, (long) 710, 100.0);
                //ввывод одного пользователя на экран
                viewOneUser(1);

                System.out.println();
                System.out.println("------------------------------------------------------------------------------------------------");
                System.out.println();

                //запрос для получения суммарных средств на счету одного пользователя в UAH (расчет по курсу)
                // (тестируем на  User - id=1, name='US01', phone='1000 )
                allMoneyInAccountUAH((long) 10);
                allMoneyInAccountUAH((long) 310);
                allMoneyInAccountUAH((long) 710);

            } finally {
                em.close();
                emf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //добавление пользователя
    private static void addUser(User user) {
        em.getTransaction().begin();
        try {
            em.persist(user);
            em.getTransaction().commit();
            System.out.println(user.getId());
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }
    }

    //вывод БД на экран
    private static void viewUsers() {
        Query query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> list = (List<User>) query.getResultList();

        for (User u : list)
            System.out.println(u);
    }

    //вывод ОДНОГО пользователя на экран
    private static void viewOneUser(int userId) {
        Query query = em.createQuery("SELECT u FROM User u where u.id = " + userId, User.class);
        List<User> list = (List<User>) query.getResultList();

        for (User u : list)
            System.out.println(u);
    }

    //пополнение счета в нужной валюте
    private static void refill(Long number, Double sum, String cur) {
        Account a = null;
        try {
            Query query = em.createQuery(
                    "SELECT x FROM Account x WHERE x.accountNumber = " + number, Account.class);
            if (query != null) {
                Transaction t = new Transaction("Refill", sum, cur);
                a = (Account) query.getSingleResult();
                if (a.getCurrency().equals(cur)) {
                    a.addTransaction(t);
                    System.out.println("The account has been replenished.");
                } else {
                    System.out.println("The replenishment currency does not match the account currency.");
                }
            }
        } catch (NoResultException ex) {
            System.out.println("The specified account does not exist.");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }
    }

    //перевод с одного счета на другой в нужной валюте
    private static void refillInOut(Long numberIn, Long numberOut, Double sum, String cur) {
        Account aIn = null;
        Account aOut = null;
        try {
            Query queryIn = em.createQuery(
                    "SELECT x FROM Account x WHERE x.accountNumber = " + numberIn, Account.class);
            if (queryIn != null) {
                Transaction tIn = new Transaction("Transferring funds to an account " + numberOut, sum, cur);
                aIn = (Account) queryIn.getSingleResult();
                if (aIn.getCurrency().equals(cur)) {
                    aIn.addTransaction(tIn);
                } else {
                    System.out.println("The replenishment currency does not match the account currency.");
                }
            }
            Query queryOut = em.createQuery(
                    "SELECT x FROM Account x WHERE x.accountNumber = " + numberOut, Account.class);
            if (queryOut != null) {
                Transaction tOut = new Transaction("Refill from the account " + numberOut, sum, cur);
                aOut = (Account) queryOut.getSingleResult();
                if (aOut.getCurrency().equals(cur)) {
                    aOut.addTransaction(tOut);
                } else {
                    System.out.println("The replenishment currency does not match the account currency.");
                }
            }
        } catch (NoResultException ex) {
            System.out.println("The specified account does not exist.");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }
    }

    //конвертация валюты в рамках счетов одного пользователя
    private static void conversionOneUser(int userId, Long numberIn, Long numberOut, Double sum) {
        Account aIn = null;
        Account aOut = null;
        Double res;
        try {
            Query queryIn = em.createQuery(
                    "SELECT x FROM Account x WHERE x.accountNumber = " + numberIn, Account.class);
            if (queryIn != null) {
                aIn = (Account) queryIn.getSingleResult();
                Transaction tIn = new Transaction("Transferring funds to an account " + numberOut, sum, aIn.getCurrency());
                aIn.addTransaction(tIn);
            }
            Query queryOut = em.createQuery(
                    "SELECT x FROM Account x WHERE x.accountNumber = " + numberOut, Account.class);
            if (queryOut != null) {
                aOut = (Account) queryOut.getSingleResult();
                String check = ExchangeRates.checkMethod(aIn.getCurrency(), aOut.getCurrency());
                if (check.equals("USDinEUR")) {
                    res = ExchangeRates.USDinEUR(sum);
                } else if (check.equals("USDinUAH")) {
                    res = ExchangeRates.USDinUAH(sum);
                } else if (check.equals("EURinUSD")) {
                    res = ExchangeRates.EURinUSD(sum);
                } else if (check.equals("EURinUAH")) {
                    res = ExchangeRates.EURinUAH(sum);
                } else if (check.equals("UAHinUSD")) {
                    res = ExchangeRates.UAHinUSD(sum);
                } else if (check.equals("UAHinUSD")) {
                    res = ExchangeRates.UAHinUSD(sum);
                } else if (check.equals("UAHinEUR")) {
                    res = ExchangeRates.UAHinEUR(sum);
                } else res = 0.0;
                Transaction tOut = new Transaction("Refill with conversion from the account " + numberOut, res, aOut.getCurrency());
                aOut.addTransaction(tOut);
            }
        } catch (NoResultException ex) {
            System.out.println("The specified account does not exist.");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }
    }

    //получение суммарных средств на счету одного пользователя в UAH (расчет по курсу)
    public static void allMoneyInAccountUAH(long accountNumber) {
        Account a = null;
        try {
            Query query = em.createQuery(
                    "SELECT x FROM Account x WHERE x.accountNumber = " + accountNumber, Account.class);

            a = (Account) query.getSingleResult();
            if (a.getCurrency().equals("UAH")) {
                System.out.println("Total on account - " + a.getAllMoney() + " UAH");
            } else if (a.getCurrency().equals("USD")) {
                System.out.println("Total on account - " + ExchangeRates.USDinUAH(a.getAllMoney()) + " UAH");
            } else if (a.getCurrency().equals("EUR")) {
                System.out.println("Total on account - " + ExchangeRates.EURinUAH(a.getAllMoney()) + " UAH");
            }
        } catch (NoResultException ex) {
            System.out.println("The specified account does not exist.");
            return;
        } catch (NonUniqueResultException ex) {
            System.out.println("Non unique result!");
            return;
        }
    }
}
