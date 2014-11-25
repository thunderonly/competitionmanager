/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import java.util.List;

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
    private SimpleListProperty<EpreuveBean> epreuves;
    private SimpleListProperty<DisciplineBean> disciplines;
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
            if (name.equals(categorieBean.getNom()) && type.equals(categorieBean.getType())) {
                return categorieBean;
            }
        }
        return null;
    }

    public ClubBean getClubByIdentifiant(String identifiant) {
        for (ClubBean clubBean : getClubs()) {
            if (clubBean.getIdentifiant().equals(identifiant)) {
                return clubBean;
            }
        }
        return null;
    }
}
