/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class CategorieBean {

    private SimpleStringProperty nom;
    private SimpleListProperty<EpreuveBean> epreuves;

    public CategorieBean(String nom) {
        this.nom = new SimpleStringProperty(nom);
        this.epreuves = new SimpleListProperty<EpreuveBean>();
    }

    public String getNom() {
        return this.nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
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

    public SimpleListProperty<EpreuveBean> epreuvesProperty() {
        return epreuves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieBean)) return false;

        CategorieBean that = (CategorieBean) o;

        if (!nom.get().equals(that.nom.get())) return false;

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
}
