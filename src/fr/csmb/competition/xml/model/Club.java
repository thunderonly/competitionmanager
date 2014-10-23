package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Administrateur on 13/10/14.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Club {

    @XmlAttribute(name="nomClub")
    private String nomClub;
    @XmlAttribute(name="identifiant")
    private String identifiant;
    @XmlAttribute(name="responsable")
    private String responsable;
    @XmlElementWrapper(name="eleves")
    @XmlElement(name="eleve")
    private List<Eleve> eleves;

    public String getNomClub() {
        return nomClub;
    }

    public void setNomClub(String nomClub) {
        this.nomClub = nomClub;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public List<Eleve> getEleves() {
        return eleves;
    }

    public void setEleves(List<Eleve> eleves) {
        this.eleves = eleves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Club club = (Club) o;

        if (!identifiant.equals(club.identifiant)) return false;
        if (!nomClub.equals(club.nomClub)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nomClub.hashCode();
        result = 31 * result + identifiant.hashCode();
        result = 31 * result + (responsable != null ? responsable.hashCode() : 0);
        result = 31 * result + (eleves != null ? eleves.hashCode() : 0);
        return result;
    }
}
