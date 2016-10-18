/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * [Enter sexe description here].
 *
 * @author Bull SAS
 */
public class CategorieBean implements Cloneable {

    private SimpleStringProperty nom;
    private SimpleStringProperty sexe;

    public CategorieBean(String nom) {
        this.nom = new SimpleStringProperty(nom);
        this.sexe = new SimpleStringProperty();
    }

    public String getNom() {
        return this.nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getSexe() {
        return this.sexe.get();
    }

    public void setSexe(String sexe) {
        this.sexe.set(sexe);
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public SimpleStringProperty sexeProperty() {
        return sexe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieBean)) return false;

        CategorieBean that = (CategorieBean) o;

        if (!nom.get().equals(that.nom.get())) return false;
        if (!sexe.get().equals(that.sexe.get())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nom.get().hashCode();
    }

    @Override
    public String toString() {
        String toString = "";
        if (sexe.get()!= null && nom.get() != null) {
            toString = sexe.get().concat(" - ").concat(nom.get());
        }
        return toString;
    }
}
