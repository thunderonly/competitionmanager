package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import java.io.Serializable;

/**
 * Created by Administrateur on 25/11/14.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Discipline implements Serializable {

    @XmlAttribute @XmlID
    private String id;
    @XmlAttribute
    private String nom;
    @XmlAttribute
    private String type;

    public Discipline(String nom, String type) {
        if (nom != null && type != null) {
            this.id = type.concat("-").concat(nom);
        }
        this.nom = nom;
        this.type = type;
    }

    public Discipline() {
        this(null, null);
    }

    public String getId() {
        if (this.nom != null && this.type != null) {
            this.id = this.type.concat("-").concat(this.nom);
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
