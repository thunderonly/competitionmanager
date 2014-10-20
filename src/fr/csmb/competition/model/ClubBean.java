package fr.csmb.competition.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Administrateur on 13/10/14.
 */
public class ClubBean {

    private StringProperty identifiant;
    private StringProperty nom;
    private StringProperty responsable;
    private ListProperty<EleveBean> eleves;

    public ClubBean() {
        this.identifiant = new SimpleStringProperty();
        this.nom = new SimpleStringProperty();
        this.responsable = new SimpleStringProperty();
        this.eleves = new SimpleListProperty<EleveBean>();
    }

    public String getIdentifiant() {
        return this.identifiant.get();
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant.set(identifiant);
    }

    public String getResponsable() {
        return this.responsable.get();
    }

    public void setResponsable(String responsable) {
        this.responsable.set(responsable);
    }

    public String getNom() {
        return this.nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty identifiantProperty() {
        return identifiant;
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty responsableProperty() {
        return responsable;
    }

    public ObservableList<EleveBean> getEleves() {
        return this.eleves.get();
    }

    public void setEleves(ObservableList<EleveBean> eleves) {
        this.eleves.set(eleves);
    }

    public ListProperty<EleveBean> elevesProperty() {
        return eleves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClubBean)) return false;

        ClubBean clubBean = (ClubBean) o;

        if (!identifiant.get().equals(clubBean.identifiant.get())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifiant.get().hashCode();
    }
}
