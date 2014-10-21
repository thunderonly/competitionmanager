package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * Created by Administrateur on 14/10/14.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Categorie {

    @XmlElement(name="nomCategorie")
    private String nomCategorie;
    @XmlElementWrapper(name="epreuves")
    @XmlElement(name="epreuve")
    private List<Epreuve> epreuves;

    public String getNomCategorie() {
        return nomCategorie;
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
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
}
