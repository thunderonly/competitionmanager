/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class CompetitionBean {

    private SimpleStringProperty nom;
    private SimpleListProperty<CategorieBean> categories;
    private SimpleListProperty<DisciplineBean> disciplines;
    private SimpleListProperty<EpreuveBean> epreuves;
    private SimpleListProperty<ParticipantBean> participants;
    private SimpleListProperty<ClubBean> clubs;

    public CompetitionBean() {
        this("");
    }

    public CompetitionBean(String nom) {
        this.nom = new SimpleStringProperty(nom);
        this.categories = new SimpleListProperty<CategorieBean>();
        ObservableList<CategorieBean> categorieBeans = FXCollections.observableArrayList();
        this.categories.set(categorieBeans);

        this.epreuves = new SimpleListProperty<EpreuveBean>();
        ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
        this.epreuves.set(epreuveBeans);

        this.disciplines = new SimpleListProperty<DisciplineBean>();
        ObservableList<DisciplineBean> disciplineBeans = FXCollections.observableArrayList();
        this.disciplines.set(disciplineBeans);

        this.participants = new SimpleListProperty<ParticipantBean>();
        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        this.participants.set(participantBeans);

        ObservableList<ClubBean> clubBeans = FXCollections.observableArrayList();
        this.clubs = new SimpleListProperty<ClubBean>();
        this.clubs.set(clubBeans);
    }

    public String getNom() {
        return this.nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public ObservableList<CategorieBean> getCategories() {
        return this.categories.get();
    }

    public void setCategories(ObservableList<CategorieBean> categories) {
        this.categories.set(categories);
    }

    public ObservableList<EpreuveBean> getEpreuves() {
        return this.epreuves.get();
    }

    public void setEpreuves(ObservableList<EpreuveBean> epreuves) {
        this.epreuves.set(epreuves);
    }

    public ObservableList<ClubBean> getClubs() {
        return this.clubs.get();
    }

    public void setClubs(ObservableList<ClubBean> clubs) {
        this.clubs.set(clubs);
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public SimpleListProperty<CategorieBean> categoriesProperty() {
        return categories;
    }

    public SimpleListProperty<EpreuveBean> epreuvesProperty() {
        return epreuves;
    }

    public SimpleListProperty<ClubBean> clubsProperty() {
        return clubs;
    }

    public ObservableList<DisciplineBean> getDisciplines() {
        return disciplines.get();
    }

    public SimpleListProperty<DisciplineBean> disciplinesProperty() {
        return disciplines;
    }

    public void setDisciplines(ObservableList<DisciplineBean> disciplines) {
        this.disciplines.set(disciplines);
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

    public CategorieBean getCategorieByName(String name) {
        for (CategorieBean categorieBean : getCategories()) {
            if (name.equals(categorieBean.getNom())) {
                return categorieBean;
            }
        }
        return null;
    }

    public CategorieBean getCategorie(String type, String name) {
        for (CategorieBean categorieBean : getCategories()) {
            if (name.equals(categorieBean.getNom()) && type.equals(categorieBean.getSexe())) {
                return categorieBean;
            }
        }
        return null;
    }

    public DisciplineBean getDiscipline(String type, String name) {
        for (DisciplineBean disciplineBean : getDisciplines()) {
            if (name.equals(disciplineBean.getNom()) && type.equals(disciplineBean.getType())) {
                return disciplineBean;
            }
        }
        return null;
    }

    public DisciplineBean getDiscipline(String name) {
        for (DisciplineBean disciplineBean : getDisciplines()) {
            if (name.equals(disciplineBean.getNom())) {
                return disciplineBean;
            }
        }
        return null;
    }

    public EpreuveBean getEpreuve(CategorieBean categorieBean, DisciplineBean disciplineBean) {
        for (EpreuveBean epreuveBean : getEpreuves()) {
            if (epreuveBean.getCategorie().equals(categorieBean) && epreuveBean.getDiscipline().equals(disciplineBean)) {
                return epreuveBean;
            }
        }
        return null;
    }

    public EpreuveBean getEpreuve(CategorieBean categorieBean, DisciplineBean disciplineBean, String label) {
        for (EpreuveBean epreuveBean : getEpreuves()) {
            if (epreuveBean.getCategorie().equals(categorieBean)) {
                if (epreuveBean.getDiscipline().equals(disciplineBean) || epreuveBean.getLabel().equals(label)) {
                    return epreuveBean;
                }
            }
        }
        return null;
    }

    public ObservableList<EpreuveBean> getEpreuveByDiscipline(DisciplineBean disciplineBean) {
        ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
        for (EpreuveBean epreuveBean : getEpreuves()) {
            if (epreuveBean.getDiscipline().equals(disciplineBean)) {
                epreuveBeans.add(epreuveBean);
            }
        }
        return epreuveBeans;
    }

    public ObservableList<EpreuveBean> getEpreuveByCategorie(CategorieBean categorieBean) {
        ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
        for (EpreuveBean epreuveBean : getEpreuves()) {
            if (epreuveBean.getCategorie().equals(categorieBean)) {
                epreuveBeans.add(epreuveBean);
            }
        }
        return epreuveBeans;
    }

    public ObservableList<ParticipantBean> getParticipantByNomPrenom(String nom, String prenom) {
        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        for (ParticipantBean participantBean : getParticipants()) {
            if (participantBean.getNom().equals(nom) && participantBean.getPrenom().equals(prenom)) {
                participantBeans.add(participantBean);
            }
        }
        return participantBeans;
    }

    public ParticipantBean getParticipantByNomPrenomEpreuve(String nom, String prenom, EpreuveBean epreuveBean) {
        for (ParticipantBean participantBean : getParticipants()) {
            if (participantBean.getNom().equals(nom) && participantBean.getPrenom().equals(prenom) &&
                    participantBean.getEpreuveBean().equals(epreuveBean)) {
                return participantBean;
            }
        }
        return null;
    }

    public ObservableList<ParticipantBean> getParticipantByEpreuve(EpreuveBean epreuveBean) {
        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        for (ParticipantBean participantBean : getParticipants()) {
            if (participantBean.getEpreuveBean().equals(epreuveBean)) {
                participantBeans.add(participantBean);
            }
        }
        return participantBeans;
    }

    public ObservableList<ParticipantBean> getParticipantPresentByEpreuve(EpreuveBean epreuveBean) {
        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        for (ParticipantBean participantBean : getParticipants()) {
            if (participantBean.getEpreuveBean().equals(epreuveBean) && participantBean.getParticipe()) {
                participantBeans.add(participantBean);
            }
        }
        return participantBeans;
    }

    public ClubBean getClubByIdentifiant(String identifiant) {
        for (ClubBean clubBean : getClubs()) {
            if (clubBean.getIdentifiant().equals(identifiant)) {
                return clubBean;
            }
        }
        return null;
    }

    public ClubBean getClubByEleve(EleveBean eleveBean) {
        for (ClubBean clubBean : getClubs()) {
            for (EleveBean eleveBean1 : clubBean.getEleves()) {
                if (eleveBean1.equals(eleveBean)) {
                    return clubBean;
                }
            }
        }
        return null;
    }
}
