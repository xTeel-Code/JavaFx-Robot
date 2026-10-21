package com.example.helloapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.math.MathContext;

public class HelloController {
    @FXML Label displej, historia;

    private static final String CHYBA = "Chyba";

    private double prveCislo = 0;
    private String operator = "";
    private boolean noveCislo = true;

    private double nacitajDisplej(){
        return Double.parseDouble(displej.getText().replace(",","."));
    }
    private String formatuj(double cislo){
        if (Double.isNaN(cislo) || Double.isInfinite(cislo)){
            return CHYBA;
        }
        // zaokruhlenie odstrani chyby typu 0,1 + 0,2 = 0,30000000000000004
        BigDecimal zaokruhlene = new BigDecimal(cislo).round(new MathContext(12)).stripTrailingZeros();
        if (zaokruhlene.signum() == 0){
            return "0";
        }
        return zaokruhlene.toPlainString().replace(".", ",");
    }
    private String zobrazOperator(String operator){
        //×÷ + −
        return switch (operator) {
            case "*" -> "×";
            case "/" -> "÷";
            case "-" -> "−";
            case "+" -> "+";
            default -> operator;
        };
    }
    private boolean skontrolujDelenieNulou(double cislo){
        return operator.equals("/") && cislo == 0;
    }
    private double vypocet(double a, double b, String operator){
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> b;
        };
    }
    private boolean jeChyba(){
        return displej.getText().equals(CHYBA);
    }
    private void zobrazChybu(){
        vymaz();
        displej.setText(CHYBA);
    }
    @FXML private void initialize(){
        displej.setText("0");
        historia.setText("");
    }
    @FXML private void cislo(ActionEvent event){
        String cislo = ((Button) event.getSource()).getText();
        if (noveCislo || jeChyba()){
            displej.setText(cislo);
            noveCislo = false;
        }
        else{
            if (displej.getText().equals("0")){
                displej.setText(cislo);
            }
            else {
                displej.setText(displej.getText() + cislo);
            }
        }
    }
    @FXML private void desatinnaCiarka(){
        if (noveCislo || jeChyba()){
            displej.setText("0,");
            noveCislo = false;
        }
        else if (!displej.getText().contains(",")){
            displej.setText(displej.getText() + ",");
        }
    }
    @FXML private void vymaz(){
        displej.setText("0");
        historia.setText("");
        prveCislo = 0;
        operator = "";
        noveCislo = true;
    }
    @FXML private void vymazPosledny(){
        if (noveCislo || jeChyba()) return;
        String text = displej.getText();
        text = text.substring(0, text.length() - 1);
        if (text.isEmpty() || text.equals("-")){
            text = "0";
        }
        displej.setText(text);
    }
    @FXML private void zmenZamienko(){
        if (jeChyba()) return;
        String text = displej.getText();
        if (text.equals("0")) return;
        displej.setText(text.startsWith("-") ? text.substring(1) : "-" + text);
    }
    @FXML private void percento(){
        if (jeChyba()) return;
        displej.setText(formatuj(nacitajDisplej() / 100));
        noveCislo = false;
    }
    @FXML private void operator(ActionEvent e) {
        if (jeChyba()) return;
        Button button = (Button) e.getSource();
        String novyOperator;
        //×÷ + −
        switch (button.getText()) {
            case "+":
                novyOperator = "+";
                break;
            case "×":
                novyOperator = "*";
                break;
            case "÷":
                novyOperator = "/";
                break;
            case "−":
                novyOperator = "-";
                break;
            default:
                return;
        }
        // pri retazeni (napr. 2 + 3 ×) sa najprv vypocita predchadzajuca operacia
        if (!operator.isEmpty() && !noveCislo) {
            double druheCislo = nacitajDisplej();
            if (skontrolujDelenieNulou(druheCislo)){
                zobrazChybu();
                return;
            }
            prveCislo = vypocet(prveCislo, druheCislo, operator);
            displej.setText(formatuj(prveCislo));
        }
        else if (operator.isEmpty()) {
            prveCislo = nacitajDisplej();
        }
        operator = novyOperator;
        historia.setText(formatuj(prveCislo) + " " + zobrazOperator(operator));
        noveCislo = true;
    }
    @FXML private void vypocitaj(){
        if (operator.isEmpty() || noveCislo || jeChyba()) return;
        double druheCislo = nacitajDisplej();
        if (skontrolujDelenieNulou(druheCislo)){
            zobrazChybu();
            return;
        }
        double vysledok = vypocet(prveCislo,druheCislo,operator);
        historia.setText(formatuj(prveCislo) + " " + zobrazOperator(operator) + " " + formatuj(druheCislo) + " =");
        displej.setText(formatuj(vysledok));
        prveCislo = vysledok;
        operator = "";
        noveCislo = true;
    }
}
