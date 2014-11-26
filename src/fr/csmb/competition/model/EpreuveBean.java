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

    private SimpleStringProperty id;
    private DisciplineBean discipline;
    private CategorieBean categorie;
    private DetailEpreuveBean detailEpreuve;
    private SimpleStringProperty etat;
    private SimpleListProperty<ParticipantBean> participants;

    public EpreuveBean() {
        this.id = new SimpleStringProperty();
        this.etat = new SimpleStringProperty();
        this.participants = new SimpleListProperty<ParticipantBean>();
        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        this.participants.set(participantBeans);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public DisciplineBean getDiscipline() {
        return discipline;
    }

    public void setDiscipline(DisciplineBean discipline) {
        this.discipline = discipline;
    }

    public CategorieBean getCategorie() {
        return categorie;
    }

    public void setCategorie(CategorieBean categorie) {
        this.categorie = categorie;
    }

    public DetailEpreuveBean getDetailEpreuve() {
        return detailEpreuve;
    }

    public void setDetailEpreuve(DetailEpreuveBean detailEpreuve) {
        this.detailEpreuve = detailEpreuve;
    }

    public String getEtat() {
        return etat.get();
    }

    public SimpleStringProperty etatProperty() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat.set(etat);
    }

    public ObservableList<ParticipantBean> getParticipants() {
        return participants.get();
    }

    public SimpleListProperty<ParticipantBean> participantsProperty() {
        return participants;
    }

    public void setParticipants(ObservableList<ParticipantBean> participants) {
        this.participants.set(participants);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EpreuveBean)) return false;

        EpreuveBean that = (EpreuveBean) o;

        if (!id.get().equals(that.id.get())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.get().hashCode();
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
