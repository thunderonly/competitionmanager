/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class DetailEpreuveBean {

    private SimpleStringProperty administrateur;
    private SimpleStringProperty chronometreur;
    private SimpleStringProperty juge1;
    private SimpleStringProperty juge2;
    private SimpleStringProperty juge3;
    private SimpleStringProperty juge4;
    private SimpleStringProperty juge5;
    private SimpleStringProperty tapis;
    private SimpleStringProperty heureDebut;
    private SimpleStringProperty heureFin;
    private SimpleStringProperty duree;

    public DetailEpreuveBean() {
        this.administrateur = new SimpleStringProperty();
        this.chronometreur = new SimpleStringProperty();
        this.juge1 = new SimpleStringProperty();
        this.juge2 = new SimpleStringProperty();
        this.juge3 = new SimpleStringProperty();
        this.juge4 = new SimpleStringProperty();
        this.juge5 = new SimpleStringProperty();
        this.tapis = new SimpleStringProperty();
        this.heureDebut = new SimpleStringProperty();
        this.heureFin = new SimpleStringProperty();
        this.duree = new SimpleStringProperty();
    }

    public String getAdministrateur() {
        return administrateur.get();
    }

    public SimpleStringProperty administrateurProperty() {
        return administrateur;
    }

    public void setAdministrateur(String administrateur) {
        this.administrateur.set(administrateur);
    }

    public String getChronometreur() {
        return chronometreur.get();
    }

    public SimpleStringProperty chronometreurProperty() {
        return chronometreur;
    }

    public void setChronometreur(String chronometreur) {
        this.chronometreur.set(chronometreur);
    }

    public String getJuge1() {
        return juge1.get();
    }

    public SimpleStringProperty juge1Property() {
        return juge1;
    }

    public void setJuge1(String juge1) {
        this.juge1.set(juge1);
    }

    public String getJuge2() {
        return juge2.get();
    }

    public SimpleStringProperty juge2Property() {
        return juge2;
    }

    public void setJuge2(String juge2) {
        this.juge2.set(juge2);
    }

    public String getJuge3() {
        return juge3.get();
    }

    public SimpleStringProperty juge3Property() {
        return juge3;
    }

    public void setJuge3(String juge3) {
        this.juge3.set(juge3);
    }

    public String getJuge4() {
        return juge4.get();
    }

    public SimpleStringProperty juge4Property() {
        return juge4;
    }

    public void setJuge4(String juge4) {
        this.juge4.set(juge4);
    }

    public String getJuge5() {
        return juge5.get();
    }

    public SimpleStringProperty juge5Property() {
        return juge5;
    }

    public void setJuge5(String juge5) {
        this.juge5.set(juge5);
    }

    public String getTapis() {
        return tapis.get();
    }

    public SimpleStringProperty tapisProperty() {
        return tapis;
    }

    public void setTapis(String tapis) {
        this.tapis.set(tapis);
    }

    public String getHeureDebut() {
        return heureDebut.get();
    }

    public SimpleStringProperty heureDebutProperty() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut.set(heureDebut);
    }

    public String getHeureFin() {
        return heureFin.get();
    }

    public SimpleStringProperty heureFinProperty() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin.set(heureFin);
    }

    public String getDuree() {
        return duree.get();
    }

    public SimpleStringProperty dureeProperty() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree.set(duree);
    }
}
