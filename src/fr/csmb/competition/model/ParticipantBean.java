/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class ParticipantBean implements Comparable<ParticipantBean> {

    private SimpleIntegerProperty classementAuto;
    private SimpleIntegerProperty classementManuel;
    private SimpleIntegerProperty classementFinal;
    private SimpleStringProperty nom;
    private SimpleStringProperty prenom;
    private SimpleStringProperty club;
    private SimpleIntegerProperty poids;
    private SimpleIntegerProperty note1;
    private SimpleIntegerProperty note2;
    private SimpleIntegerProperty note3;
    private SimpleIntegerProperty note4;
    private SimpleIntegerProperty note5;
    private SimpleIntegerProperty noteTotal;
    private SimpleIntegerProperty placeOnGrid;
    private SimpleBooleanProperty participe;
    private EpreuveBean epreuveBean;

    public ParticipantBean() {
        classementAuto = new SimpleIntegerProperty(0);
        classementManuel = new SimpleIntegerProperty(0);
        classementFinal = new SimpleIntegerProperty(0);
        this.nom = new SimpleStringProperty("");
        this.prenom = new SimpleStringProperty("");
        this.club = new SimpleStringProperty("");
        this.poids = new SimpleIntegerProperty(0);
        note1 = new SimpleIntegerProperty(0);
        note2 = new SimpleIntegerProperty(0);
        note3 = new SimpleIntegerProperty(0);
        note4 = new SimpleIntegerProperty(0);
        note5 = new SimpleIntegerProperty(0);
        noteTotal = new SimpleIntegerProperty(0);
        placeOnGrid = new SimpleIntegerProperty(0);
        participe = new SimpleBooleanProperty(true);
    }

    public ParticipantBean(String nom, String prenom) {
        classementAuto = new SimpleIntegerProperty(0);
        classementManuel = new SimpleIntegerProperty(0);
        classementFinal = new SimpleIntegerProperty(0);
        this.nom = new SimpleStringProperty(nom);
        this.prenom = new SimpleStringProperty(prenom);
        this.club = new SimpleStringProperty("");
        this.poids = new SimpleIntegerProperty(0);
        note1 = new SimpleIntegerProperty(0);
        note2 = new SimpleIntegerProperty(0);
        note3 = new SimpleIntegerProperty(0);
        note4 = new SimpleIntegerProperty(0);
        note5 = new SimpleIntegerProperty(0);
        noteTotal = new SimpleIntegerProperty(0);
        placeOnGrid = new SimpleIntegerProperty(0);
        participe = new SimpleBooleanProperty(true);
    }

    public Integer getClassementAuto() {
        return this.classementAuto.get();
    }

    public Integer getClassementManuel() {
        return this.classementManuel.get();
    }

    public Integer getClassementFinal() {
        return this.classementFinal.get();
    }

    public String getNom() {
        return this.nom.get();
    }

    public String getPrenom() {
        return this.prenom.get();
    }

    public String getClub() { return this.club.get(); }

    public Integer getPoids() { return this.poids.get(); }

    public Integer getNote1() {
        return this.note1.get();
    }

    public Integer getNote2() {
        return this.note2.get();
    }

    public Integer getNote3() {
        return this.note3.get();
    }

    public Integer getNote4() {
        return this.note4.get();
    }

    public Integer getNote5() {
        return this.note5.get();
    }

    public Integer getNoteTotal() {
        return this.noteTotal.get();
    }

    public Integer getPlaceOnGrid() {
        return this.placeOnGrid.get();
    }

    public void setClassementAuto(Integer classementAuto) {
        this.classementAuto.set(classementAuto);
    }

    public void setClassementManuel(Integer classementManuel) {
        this.classementManuel.set(classementManuel);
    }

    public void setClassementFinal(Integer classementFinal) {
        this.classementFinal.set(classementFinal);
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public void setClub(String club) { this.club.set(club); }

    public void setPoids(Integer poids) { this.poids.set(poids);}

    public void setNote1(Integer note1) {
        this.note1.set(note1);
        this.computeTotal();
    }

    public void setNote2(Integer note2) {
        this.note2.set(note2);
        this.computeTotal();
    }

    public void setNote3(Integer note3) {
        this.note3.set(note3);
        this.computeTotal();
    }

    public void setNote4(Integer note4) {
        this.note4.set(note4);
        this.computeTotal();
    }

    public void setNote5(Integer note5) {
        this.note5.set(note5);
        this.computeTotal();
    }

    public void setNoteTotal(Integer noteTotal) {
        this.noteTotal.set(noteTotal);
    }

    public void setPlaceOnGrid(Integer placeOnGrid) {
        this.placeOnGrid.set(placeOnGrid);
    }

    public SimpleIntegerProperty classementAutoProperty() {
        return classementAuto;
    }

    public SimpleIntegerProperty classementManuelProperty() {
        return classementManuel;
    }

    public SimpleIntegerProperty classementFinalProperty() {
        return classementFinal;
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public SimpleStringProperty prenomProperty() {
        return prenom;
    }

    public SimpleStringProperty clubProperty() {
        return club;
    }

    public SimpleIntegerProperty poidsProperty() {
        return poids;
    }

    public SimpleIntegerProperty note1Property() {
        return note1;
    }

    public SimpleIntegerProperty note2Property() {
        return note2;
    }

    public SimpleIntegerProperty note3Property() {
        return note3;
    }

    public SimpleIntegerProperty note4Property() {
        return note4;
    }

    public SimpleIntegerProperty note5Property() {
        return note5;
    }

    public SimpleIntegerProperty noteTotalProperty() {
        return noteTotal;
    }

    public SimpleIntegerProperty placeOnGridProperty() {
        return placeOnGrid;
    }

    public boolean getParticipe() {
        return participe.get();
    }

    public SimpleBooleanProperty participeProperty() {
        return participe;
    }

    public void setParticipe(boolean participe) {
        this.participe.set(participe);
    }

    public EpreuveBean getEpreuveBean() {
        return epreuveBean;
    }

    public void setEpreuveBean(EpreuveBean epreuveBean) {
        this.epreuveBean = epreuveBean;
    }

    public void computeTotal() {
        setNoteTotal(getNote1() + getNote2() + getNote3() + getNote4() + getNote5());
    }

    @Override
    public int compareTo(ParticipantBean participant) {
        if (this.getNoteTotal() > participant.getNoteTotal()) {
            return -1;
        } else if (this.getNoteTotal() < participant.getNoteTotal()) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticipantBean)) return false;

        ParticipantBean that = (ParticipantBean) o;

        if (!nom.get().equals(that.nom.get())) return false;
        if (!prenom.get().equals(that.prenom.get())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nom.get().hashCode();
        result = 31 * result + prenom.get().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return nom.get().toUpperCase() + " " + prenom.get();
    }
}
