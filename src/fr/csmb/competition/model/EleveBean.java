package fr.csmb.competition.model;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Administrateur on 13/10/14.
 */
public class EleveBean {

    private StringProperty licence;
    private StringProperty nom;
    private StringProperty prenom;
    private StringProperty age;
    private StringProperty categorie;
    private StringProperty sexe;
    private StringProperty poids;
    private ListProperty<String> epreuves;

    public EleveBean() {
        this.licence = new SimpleStringProperty();
        this.nom = new SimpleStringProperty();
        this.prenom = new SimpleStringProperty();
        this.age = new SimpleStringProperty();
        this.categorie = new SimpleStringProperty();
        this.sexe = new SimpleStringProperty();
        this.poids = new SimpleStringProperty();
        this.epreuves = new SimpleListProperty<String>();
        ObservableList<String> strings = FXCollections.observableArrayList();
        this.epreuves.set(strings);
    }

    public String getLicence() {
        return this.licence.get();
    }

    public void setLicence(String licence) {
        this.licence.set(licence);
    }

    public String getNom() {
        return this.nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPrenom() {
        return this.prenom.get();
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public String getAge() {
        return this.age.get();
    }

    public void setAge(String age) {
        this.age.set(age);
    }

    public String getCategorie() {
        return this.categorie.get();
    }

    public void setCategorie(String categorie) {
        this.categorie.set(categorie);
    }

    public String getSexe() {
        return this.sexe.get();
    }

    public void setSexe(String sexe) {
        this.sexe.set(sexe);
    }

    public String getPoids() {
        return this.poids.get();
    }

    public void setPoids(String poids) {
        this.poids.set(poids);
    }

    public ObservableList<String> getEpreuves() {
        return this.epreuves.get();
    }

    public void setEpreuves(ObservableList<String> epreuves) {
        this.epreuves.set(epreuves);
    }

    public StringProperty licenceProperty() {
        return licence;
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public StringProperty prenomProperty() {
        return prenom;
    }

    public StringProperty ageProperty() {
        return age;
    }

    public StringProperty categorieProperty() {
        return categorie;
    }

    public StringProperty sexeProperty() {
        return sexe;
    }

    public StringProperty poidsProperty() {
        return poids;
    }

    public ListProperty<String> epreuvesProperty() {
        return epreuves;
    }
}
