package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 13/10/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Competition {

    @XmlElement(name="nom")
    private String nom;
    @XmlElementWrapper(name="categories")
    @XmlElement(name="categorie")
    private List<Categorie> categories;
    @XmlElementWrapper(name="clubs")
    @XmlElement(name="club")
    private List<Club> clubs;

    public Competition(){
        this.nom = "";
        this.categories = new ArrayList<Categorie>();
    }

    public Competition(String nom) {
        this.nom = nom;
        this.categories = new ArrayList<Categorie>();
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

    public List<Club> getClubs() {
        return clubs;
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = clubs;
    }
}