package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 14/10/14.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Epreuve implements Serializable {

    @XmlAttribute @XmlID
    private String id;
    @XmlAttribute @XmlIDREF
    private Discipline discipline;
    @XmlAttribute @XmlIDREF
    private Categorie categorie;
    @XmlAttribute(name="etatEpreuve")
    private String etatEpreuve;
    @XmlElement
    private DetailEpreuve detailEpreuve;
    @XmlElement(name="labelEpreuve")
    private String labelEpreuve;

    public Epreuve(Categorie categorie, Discipline discipline) {
        if (categorie != null && discipline != null) {
            this.id = categorie.getId().concat("-").concat(discipline.getId());
        }
        this.categorie = categorie;
        this.discipline = discipline;
//        this.participants = new ArrayList<Participant>();
    }

    public Epreuve() {
        this(null, null);
    }

    public String getId() {
        if (categorie != null && discipline != null) {
            this.id = categorie.getId().concat(discipline.getId());
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getEtatEpreuve() {
        return etatEpreuve;
    }

    public void setEtatEpreuve(String etatEpreuve) {
        this.etatEpreuve = etatEpreuve;
    }

    public DetailEpreuve getDetailEpreuve() {
        return detailEpreuve;
    }

    public void setDetailEpreuve(DetailEpreuve detailEpreuve) {
        this.detailEpreuve = detailEpreuve;
    }

    public String getLabelEpreuve() {
        if (labelEpreuve == null || labelEpreuve.isEmpty()) {
            labelEpreuve = discipline.getNom();
        }
        return labelEpreuve;
    }

    public void setLabelEpreuve(String labelEpreuve) {
        this.labelEpreuve = labelEpreuve;
    }
}
