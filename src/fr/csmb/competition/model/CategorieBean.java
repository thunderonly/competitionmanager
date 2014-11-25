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
public class CategorieBean implements Cloneable {

    private SimpleStringProperty nom;
    private SimpleStringProperty type;
    private SimpleListProperty<EpreuveBean> epreuves;
    private SimpleListProperty<DisciplineBean> disciplines;

    public CategorieBean(String nom) {
        this.nom = new SimpleStringProperty(nom);
        this.type = new SimpleStringProperty();
        this.epreuves = new SimpleListProperty<EpreuveBean>();
        ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
        this.epreuves.set(epreuveBeans);
        this.disciplines = new SimpleListProperty<DisciplineBean>();
        ObservableList<DisciplineBean> disciplineBeans = FXCollections.observableArrayList();
        this.disciplines.set(disciplineBeans);
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

    public ObservableList<EpreuveBean> getEpreuves() {
        return this.epreuves.get();
    }

    public void setEpreuves(ObservableList<EpreuveBean> epreuves) {
        this.epreuves.set(epreuves);
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public SimpleListProperty<EpreuveBean> epreuvesProperty() {
        return epreuves;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieBean)) return false;

        CategorieBean that = (CategorieBean) o;

        if (!nom.get().equals(that.nom.get())) return false;
        if (!type.get().equals(that.type.get())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nom.get().hashCode();
    }

    public EpreuveBean getEpreuveByName(String name) {
        for (EpreuveBean epreuveBean : getEpreuves()) {
            if (name.equals(epreuveBean.getNom())) {
                return epreuveBean;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String toString = "";
        if (type.get()!= null && nom.get() != null) {
            toString = type.get().concat(" - ").concat(nom.get());
        }
        return toString;
    }
}
