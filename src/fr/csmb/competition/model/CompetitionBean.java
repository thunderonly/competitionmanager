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
    private SimpleListProperty<ClubBean> clubs;

    public CompetitionBean(String nom) {
        this.nom = new SimpleStringProperty(nom);
        this.categories = new SimpleListProperty<CategorieBean>();
        this.clubs = new SimpleListProperty<ClubBean>();
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

    public SimpleListProperty<ClubBean> clubsProperty() {
        return clubs;
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
}
