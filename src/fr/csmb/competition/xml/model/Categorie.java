package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 14/10/14.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Categorie implements Cloneable, Serializable {

    @XmlAttribute(name="nomCategorie")
    private String nomCategorie;
    @XmlAttribute(name="typeCategorie")
    private String typeCategorie;
    @XmlElementWrapper(name="epreuves")
    @XmlElement(name="epreuve")
    private List<Epreuve> epreuves;

    public Categorie() {
        this("", "");
    }

    public Categorie(String nom, String type) {
        this.nomCategorie = nom;
        this.typeCategorie = type;
        this.epreuves = new ArrayList<Epreuve>();
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public String getTypeCategorie() {
        return typeCategorie;
    }

    public void setTypeCategorie(String typeCategorie) {
        this.typeCategorie = typeCategorie;
    }

    public List<Epreuve> getEpreuves() {
        return epreuves;
    }

    public void setEpreuves(List<Epreuve> epreuves) {
        this.epreuves = epreuves;
    }

    public Epreuve getEpreuveByName(String name) {
        for (Epreuve epreuve : epreuves) {
            if (epreuve.getNomEpreuve().equals(name)) {
                return epreuve;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categorie)) return false;

        Categorie that = (Categorie) o;

        if (!nomCategorie.equals(that.nomCategorie)) return false;
        if (!typeCategorie.equals(that.typeCategorie)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nomCategorie.hashCode();
        result = 31 * result + typeCategorie.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return typeCategorie.concat(" - ").concat(nomCategorie);
    }

    @Override
    public Categorie clone() {
        Categorie categorie = null;
        try {
            categorie = (Categorie) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return categorie;
    }
}
