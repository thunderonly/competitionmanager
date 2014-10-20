/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.bean;

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
    private SimpleIntegerProperty note1;
    private SimpleIntegerProperty note2;
    private SimpleIntegerProperty note3;
    private SimpleIntegerProperty note4;
    private SimpleIntegerProperty note5;
    private SimpleIntegerProperty noteTotal;

    public ParticipantBean(String nom) {
        classementAuto = new SimpleIntegerProperty();
        classementManuel = new SimpleIntegerProperty();
        classementFinal = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty(nom);
        note1 = new SimpleIntegerProperty();
        note2 = new SimpleIntegerProperty();
        note3 = new SimpleIntegerProperty();
        note4 = new SimpleIntegerProperty();
        note5 = new SimpleIntegerProperty();
        noteTotal = new SimpleIntegerProperty();
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

        return true;
    }

    @Override
    public int hashCode() {
        return nom.get().hashCode();
    }
}
