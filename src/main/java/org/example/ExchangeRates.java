package org.example;

import java.util.List;

//класс Курс Валют с методами перевода валют в UAH, USD, EUR
public class ExchangeRates {
    private static final ExchangeRates ER = new ExchangeRates();

    private final List<String> testER = List.of("USD", "EUR", "UAH");

    public static ExchangeRates getInstance() {
        return ER;
    }


    //проверка правильности ввода имени валюты
    public synchronized boolean checkCurrencyName(String name) throws InvalidInputFormatException {
        for (String t : testER) {
            if (t.equals(name)) {
                return true;
            }
        }
        throw new InvalidInputFormatException("Invalid currency name");
    }

    //перевод по курсу с доллара в евро
    public static synchronized double USDinEUR(double moneyUSD) {
        double result = moneyUSD * 0.91;
        return result;
    }

    //перевод по курсу с доллара в гривну
    public static synchronized double USDinUAH(double moneyUSD) {
        double result = moneyUSD * 36.51;
        return result;
    }

    //перевод по курсу с евро в доллар
    public static synchronized double EURinUSD(double moneyEUR) {
        double result = moneyEUR * 1.1;
        return result;
    }

    //перевод по курсу с евро в гривну
    public static synchronized double EURinUAH(double moneyEUR) {
        double result = moneyEUR * 40.37;
        return result;
    }

    //перевод по курсу с гривны в доллар
    public static synchronized double UAHinUSD(double moneyUAH) {
        double result = moneyUAH * 0.03;
        return result;
    }

    //перевод по курсу с гривны в евро
    public static synchronized double UAHinEUR(double moneyUAH) {
        double result = moneyUAH * 0.02;
        return result;
    }

    //определение нужного метода по переводу валюты
    public static String checkMethod(String curIn, String curOut) {
        if (curIn.equals("USD") && curOut.equals("EUR")) {
            return "USDinEUR";
        } else if (curIn.equals("USD") && curOut.equals("UAH")) {
            return "USDinUAH";
        } else if (curIn.equals("EUR") && curOut.equals("USD")) {
            return "EURinUSD";
        } else if (curIn.equals("EUR") && curOut.equals("UAH")) {
            return "EURinUAH";
        } else if (curIn.equals("UAH") && curOut.equals("USD")) {
            return "UAHinUSD";
        } else if (curIn.equals("UAH") && curOut.equals("EUR")) {
            return "UAHinEUR";
        } else
            System.out.println("Translation without conversion. " +
                    "Use the method to transfer funds in the desired currency or check the spelling " +
                    "of the currencies for conversion.");
        return null;
    }
}
