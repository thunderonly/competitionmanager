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

    public CategorieBean(String nom) {
        this.nom = new SimpleStringProperty(nom);
        this.type = new SimpleStringProperty();
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

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public SimpleStringProperty typeProperty() {
        return type;
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

    @Override
    public String toString() {
        String toString = "";
        if (type.get()!= null && nom.get() != null) {
            toString = type.get().concat(" - ").concat(nom.get());
        }
        return toString;
    }
}
