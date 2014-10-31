/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class EpreuveBean {

    private SimpleStringProperty type;
    private SimpleStringProperty nom;
    private SimpleStringProperty etat;
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
    private SimpleListProperty<ParticipantBean> participants;

    public EpreuveBean(String nom) {
        this.type = new SimpleStringProperty();
        this.nom = new SimpleStringProperty(nom);
        this.etat = new SimpleStringProperty();
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
        this.participants = new SimpleListProperty<ParticipantBean>();
        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        this.participants.set(participantBeans);
    }

    public String getNom() {
        return this.nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getType() {
        return this.type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getEtat() {
        return this.etat.get();
    }

    public void setEtat(String etat) {
        this.etat.set(etat);
    }

    public String getAdministrateur() {
        return this.administrateur.get();
    }

    public void setAdministrateur(String administrateur) {
        this.administrateur.set(administrateur);
    }

    public String getChronometreur() {
        return this.chronometreur.get();
    }

    public void setChronometreur(String chronometreur) {
        this.chronometreur.set(chronometreur);
    }

    public String getJuge1() {
        return this.juge1.get();
    }

    public void setJuge1(String juge1) {
        this.juge1.set(juge1);
    }

    public String getJuge2() {
        return this.juge2.get();
    }

    public void setJuge2(String juge2) {
        this.juge2.set(juge2);
    }

    public String getJuge3() {
        return this.juge3.get();
    }

    public void setJuge3(String juge3) {
        this.juge3.set(juge3);
    }

    public String getJuge4() {
        return this.juge4.get();
    }

    public void setJuge4(String juge4) {
        this.juge4.set(juge4);
    }

    public String getJuge5() {
        return this.juge5.get();
    }

    public void setJuge5(String juge5) {
        this.juge5.set(juge5);
    }

    public String getTapis() {
        return this.tapis.get();
    }

    public void setTapis(String tapis) {
        this.tapis.set(tapis);
    }

    public String getHeureDebut() {
        return this.heureDebut.get();
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut.set(heureDebut);
    }

    public String getHeureFin() {
        return this.heureFin.get();
    }

    public void setHeureFin(String heureFin) {
        this.heureFin.set(heureFin);
    }

    public String getDuree() {
        return this.duree.get();
    }

    public void setDuree(String duree) {
        this.duree.set(duree);
    }


    public ObservableList<ParticipantBean> getParticipants() {
        return this.participants.get();
    }

    public void setParticipants(ObservableList participants) {
        this.participants.set(participants);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public SimpleStringProperty etatProperty() {
        return etat;
    }

    public SimpleStringProperty administrateurProperty() {
        return administrateur;
    }

    public SimpleStringProperty chronometreurProperty() {
        return chronometreur;
    }

    public SimpleStringProperty juge1Property() {
        return juge1;
    }

    public SimpleStringProperty juge2Property() {
        return juge2;
    }

    public SimpleStringProperty juge3Property() {
        return juge3;
    }

    public SimpleStringProperty juge4Property() {
        return juge4;
    }

    public SimpleStringProperty juge5Property() {
        return juge5;
    }

    public SimpleStringProperty tapisProperty() {
        return tapis;
    }

    public SimpleStringProperty heureDebutProperty() {
        return heureDebut;
    }

    public SimpleStringProperty heureFinProperty() {
        return heureFin;
    }

    public SimpleStringProperty dureeProperty() {
        return duree;
    }

    public SimpleListProperty<ParticipantBean> participantsProperty() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EpreuveBean)) return false;

        EpreuveBean that = (EpreuveBean) o;

        if (!nom.get().equals(that.nom.get())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nom.get().hashCode();
    }

    @Override
    public String toString() {
        String toString = "";
        if (type.get()!= null && nom.get() != null) {
            toString = type.get().concat(" - ").concat(nom.get());
        }
        return toString;
    }

    public ParticipantBean getParticipantByNomPrenom(String nom, String prenom) {
        for (ParticipantBean participantBean : getParticipants()) {
            if (participantBean.getNom().equals(nom) && participantBean.getPrenom().equals(prenom)) {
                return participantBean;
            }
        }
        return null;
    }
}
