package com.example.helloapp;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Robot extends Group {

    public enum Smer { HORE, DOLE, VLAVO, VPRAVO }

    // rozmery tela: dlhsia strana je vzdy v smere pohybu
    private static final double DLZKA = 60;
    private static final double SIRKA = 40;
    // hrubka cerveneho pasika, ktory oznacuje predok robota
    private static final double PREDOK = 10;
    private static final double KROK = 10;
    // rychlost v automatickom rezime (pixely za sekundu)
    private static final double RYCHLOST = 150;

    private final Rectangle telo = new Rectangle();
    private final Rectangle predok = new Rectangle();
    private final Pane plocha;

    private double x, y;
    private Smer smer = Smer.VPRAVO;
    private final ReadOnlyBooleanWrapper automat = new ReadOnlyBooleanWrapper(false);
    private final AnimationTimer casovac;

    public Robot(Pane plocha, double x, double y) {
        this.plocha = plocha;
        this.x = x;
        this.y = y;

        telo.setStroke(Color.BLACK);
        predok.setFill(Color.RED);
        getChildren().addAll(telo, predok);

        // kliknutim na robota sa prepina manualny / automaticky rezim
        setOnMouseClicked(e -> prepniRezim());

        casovac = new AnimationTimer() {
            private long posledny = 0;

            @Override
            public void start() {
                posledny = 0;
                super.start();
            }

            @Override
            public void handle(long teraz) {
                if (posledny != 0) {
                    double sekundy = (teraz - posledny) / 1_000_000_000.0;
                    krokAutomat(RYCHLOST * sekundy);
                }
                posledny = teraz;
            }
        };

        // pri zmenseni okna robota vratime spat do plochy
        plocha.widthProperty().addListener((o, s, n) -> prekresli());
        plocha.heightProperty().addListener((o, s, n) -> prekresli());

        prekresli();
    }

    /** Manualny pohyb o jeden krok v zadanom smere (klavesy WSAD). */
    public void pohni(Smer novySmer) {
        if (automat.get()) return;
        smer = novySmer;
        posun(KROK);
    }

    public void zapniAutomat() {
        automat.set(true);
        casovac.start();
        prekresli();
    }

    public void vypniAutomat() {
        automat.set(false);
        casovac.stop();
        prekresli();
    }

    public void prepniRezim() {
        if (automat.get()) vypniAutomat();
        else zapniAutomat();
    }

    public boolean jeAutomat() {
        return automat.get();
    }

    public ReadOnlyBooleanProperty automatProperty() {
        return automat.getReadOnlyProperty();
    }

    public Smer getSmer() {
        return smer;
    }

    private void krokAutomat(double vzdialenost) {
        if (!posun(vzdialenost)) {
            smer = opacny(smer);
            prekresli();
        }
    }

    /**
     * Posunie robota v aktualnom smere, ale nie za okraj plochy.
     * Vrati false, ak robot narazil na okraj.
     */
    private boolean posun(double vzdialenost) {
        double novyX = x, novyY = y;
        switch (smer) {
            case HORE -> novyY -= vzdialenost;
            case DOLE -> novyY += vzdialenost;
            case VLAVO -> novyX -= vzdialenost;
            case VPRAVO -> novyX += vzdialenost;
        }
        double maxX = Math.max(0, plocha.getWidth() - sirkaTela());
        double maxY = Math.max(0, plocha.getHeight() - vyskaTela());
        x = Math.clamp(novyX, 0, maxX);
        y = Math.clamp(novyY, 0, maxY);
        prekresli();
        return x == novyX && y == novyY;
    }

    private boolean vodorovne() {
        return smer == Smer.VLAVO || smer == Smer.VPRAVO;
    }

    private double sirkaTela() {
        return vodorovne() ? DLZKA : SIRKA;
    }

    private double vyskaTela() {
        return vodorovne() ? SIRKA : DLZKA;
    }

    private static Smer opacny(Smer s) {
        return switch (s) {
            case HORE -> Smer.DOLE;
            case DOLE -> Smer.HORE;
            case VLAVO -> Smer.VPRAVO;
            case VPRAVO -> Smer.VLAVO;
        };
    }

    private void prekresli() {
        double w = sirkaTela();
        double h = vyskaTela();

        // po otoceni moze robot presahovat okraj, preto ho vratime dnu
        x = Math.clamp(x, 0, Math.max(0, plocha.getWidth() - w));
        y = Math.clamp(y, 0, Math.max(0, plocha.getHeight() - h));

        telo.setX(x);
        telo.setY(y);
        telo.setWidth(w);
        telo.setHeight(h);
        telo.setFill(automat.get() ? Color.LIMEGREEN : Color.STEELBLUE);

        switch (smer) {
            case HORE -> nastavPredok(x, y, w, PREDOK);
            case DOLE -> nastavPredok(x, y + h - PREDOK, w, PREDOK);
            case VLAVO -> nastavPredok(x, y, PREDOK, h);
            case VPRAVO -> nastavPredok(x + w - PREDOK, y, PREDOK, h);
        }
    }

    private void nastavPredok(double px, double py, double pw, double ph) {
        predok.setX(px);
        predok.setY(py);
        predok.setWidth(pw);
        predok.setHeight(ph);
    }
}
