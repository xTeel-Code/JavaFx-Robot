package com.example.helloapp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
public class HelloController {
    @FXML Label displej, historia;

    private double nacitajDisplej(){
        return Double.parseDouble(displej.getText().replace(",","."));
    }
    private String formatuj(double cislo){
        if (cislo == (long) cislo){
            return String.valueOf((long)cislo);
        }
        return String.valueOf(cislo).replace(".", ",");
    }
    private String zobrazOperator(String operator){
        //×÷ + −
        return switch (operator) {
            case "*" -> "×";
            case "/" -> "÷";
            case "-" -> "-";
            case "+" -> "+";
            default -> operator;
        };
    }
    private double prveCislo = 0;
    private String operator = "";
    private boolean noveCislo = true;
    private boolean skontrolujDelenieNulou(double cislo){
        return cislo == 0;
    }
    private double vypocet(double a, double b, String operator){
        return switch (operator) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> a / b;
            default -> 0;
        };
    }
    @FXML private void initialize(){
        displej.setText("0");
        historia.setText("");
    }
    @FXML private void getCislo(ActionEvent event){
        String cislo = ((Button) event.getSource()).getText();
        if (noveCislo){
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
    @FXML private void operator(ActionEvent e) {
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
        if (!operator.isEmpty()) {
            double druheCislo = nacitajDisplej();
            if (!skontrolujDelenieNulou(druheCislo)){
                return;
            }

        }
    }
    @FXML private void vypocitaj(){
        if (operator.isEmpty() || noveCislo) return;
        double druheCislo = nacitajDisplej();
        if (!skontrolujDelenieNulou(druheCislo)) return;
        double vysledok = vypocet(prveCislo,druheCislo,operator);
        historia.setText(formatuj(prveCislo) + " + " + zobrazOperator(operator) + " " + formatuj(druheCislo) + " =");
        displej.setText(formatuj(vysledok));
        prveCislo = vysledok;
        operator = "";
        noveCislo = true;
    }
}
