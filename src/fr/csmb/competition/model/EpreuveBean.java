/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
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
    private SimpleListProperty<ParticipantBean> participants;

    public EpreuveBean(String nom) {
        this.type = new SimpleStringProperty();
        this.nom = new SimpleStringProperty(nom);
        this.etat = new SimpleStringProperty();
        this.participants = new SimpleListProperty<ParticipantBean>();
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
}
