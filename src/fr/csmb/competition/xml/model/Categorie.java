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

    @XmlAttribute @XmlID
    private String id;
    @XmlAttribute(name="nomCategorie")
    private String nomCategorie;
    @XmlAttribute(name="typeCategorie")
    private String typeCategorie;
    @XmlElement
    @XmlIDREF
    private List<Discipline> discipline;

    public Categorie() {
        this(null, null);
    }

    public Categorie(String nom, String type) {
        if (nom != null && type != null) {
            this.id = nom.concat("-").concat(type);
        }
        this.nomCategorie = nom;
        this.typeCategorie = type;
        this.discipline = new ArrayList<Discipline>();
    }

    public String getId() {
        if (this.nomCategorie != null && this.typeCategorie != null) {
            this.id = this.nomCategorie.concat("-").concat(this.typeCategorie);
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<Discipline> getDiscipline() {
        return discipline;
    }

    public void setDiscipline(List<Discipline> discipline) {
        this.discipline = discipline;
    }

    public Discipline getDisciplineByName(String name) {
        for (Discipline discipline1 : discipline) {
            if (discipline1.getNom().equals(name)) {
                return discipline1;
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
