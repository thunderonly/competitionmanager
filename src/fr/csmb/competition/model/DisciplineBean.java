/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class DisciplineBean {

    private SimpleStringProperty type;
    private SimpleStringProperty nom;

    public DisciplineBean(String nom, String type) {
        this.type = new SimpleStringProperty(type);
        this.nom = new SimpleStringProperty(nom);
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

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DisciplineBean)) return false;

        DisciplineBean that = (DisciplineBean) o;

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
