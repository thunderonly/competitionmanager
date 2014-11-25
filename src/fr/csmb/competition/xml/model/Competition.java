package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 13/10/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Competition implements Serializable{

    @XmlAttribute(name="nom")
    private String nom;
    private List<Categorie> categories;
    private List<Epreuve> epreuve;
    private List<Discipline> discipline;
    @XmlElementWrapper(name="clubs")
    @XmlElement(name="club")
    private List<Club> clubs;

    public Competition(){
        this.nom = "";
        this.categories = new ArrayList<Categorie>();
        this.epreuve = new ArrayList<Epreuve>();
        this.discipline = new ArrayList<Discipline>();
        this.clubs = new ArrayList<Club>();
    }

    public Competition(String nom) {
        this.nom = nom;
        this.categories = new ArrayList<Categorie>();
        this.epreuve = new ArrayList<Epreuve>();
        this.discipline = new ArrayList<Discipline>();
        this.clubs = new ArrayList<Club>();
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Categorie> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Categorie> categories) {
        this.categories = categories;
    }

    public List<Epreuve> getEpreuve() {
        return epreuve;
    }

    public void setEpreuve(List<Epreuve> epreuve) {
        this.epreuve = epreuve;
    }

    public List<Discipline> getDiscipline() {
        return discipline;
    }

    public void setDiscipline(List<Discipline> discipline) {
        this.discipline = discipline;
    }

    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }

    public Categorie getCategorieByName(String name) {
        for (Categorie categorie : categories) {
            if (categorie.getNomCategorie().equals(name)) {
                return categorie;
            }
        }
        return null;
    }

    public Categorie getCategorie(String name, String type) {
        for (Categorie categorie : categories) {
            if (categorie.getNomCategorie().equals(name) && categorie.getTypeCategorie().equals(type)) {
                return categorie;
            }
        }
        return null;
    }
}
