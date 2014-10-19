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
public class Epreuve {

    @XmlElement(name="typeEpreuve")
    private String typeEpreuve;
    @XmlElement(name="nomEpreuve")
    private String nomEpreuve;
    @XmlElement(name="etatEpreuve")
    private String etatEpreuve;
    @XmlElementWrapper(name="participants")
    @XmlElement(name="participant")
    private List<Participant> participants;

    public String getTypeEpreuve() {
        return typeEpreuve;
    }

    public void setTypeEpreuve(String typeEpreuve) {
        this.typeEpreuve = typeEpreuve;
    }

    public String getNomEpreuve() {
        return nomEpreuve;
    }

    public void setNomEpreuve(String nomEpreuve) {
        this.nomEpreuve = nomEpreuve;
    }

    public String getEtatEpreuve() {
        return etatEpreuve;
    }

    public void setEtatEpreuve(String etatEpreuve) {
        this.etatEpreuve = etatEpreuve;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
