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
    @XmlAttribute(name="typeEpreuve")
    private String typeEpreuve;
    @XmlAttribute(name="nomEpreuve")
    private String nomEpreuve;
    @XmlAttribute(name="etatEpreuve")
    private String etatEpreuve;
    @XmlElement(name="administrateur")
    private String administrateur;
    @XmlElement(name="chronometreur")
    private String chronometreur;
    @XmlElement(name="juge1")
    private String juge1;
    @XmlElement(name="juge2")
    private String juge2;
    @XmlElement(name="juge3")
    private String juge3;
    @XmlElement(name="juge4")
    private String juge4;
    @XmlElement(name="juge5")
    private String juge5;
    @XmlElement(name="tapis")
    private String tapis;
    @XmlElement(name="heureDebut")
    private String heureDebut;
    @XmlElement(name="heureFin")
    private String heureFin;
    @XmlElement(name="duree")
    private String duree;
    @XmlElementWrapper(name="participants")
    @XmlElement(name="participant")
    private List<Participant> participants;

    public Epreuve(String nom, String type) {
        if (nom != null && type != null) {
            this.id = nom.concat("-").concat(type);
        }
        this.nomEpreuve = nom;
        this.typeEpreuve = type;
        this.participants = new ArrayList<Participant>();
    }

    public Epreuve() {
        this(null, null);
    }

    public String getId() {
        if (this.nomEpreuve != null && this.typeEpreuve != null) {
            this.id = this.nomEpreuve.concat("-").concat(this.typeEpreuve);
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

    public String getAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(String administrateur) {
        this.administrateur = administrateur;
    }

    public String getChronometreur() {
        return chronometreur;
    }

    public void setChronometreur(String chronometreur) {
        this.chronometreur = chronometreur;
    }

    public String getJuge1() {
        return juge1;
    }

    public void setJuge1(String juge1) {
        this.juge1 = juge1;
    }

    public String getJuge2() {
        return juge2;
    }

    public void setJuge2(String juge2) {
        this.juge2 = juge2;
    }

    public String getJuge3() {
        return juge3;
    }

    public void setJuge3(String juge3) {
        this.juge3 = juge3;
    }

    public String getJuge4() {
        return juge4;
    }

    public void setJuge4(String juge4) {
        this.juge4 = juge4;
    }

    public String getJuge5() {
        return juge5;
    }

    public void setJuge5(String juge5) {
        this.juge5 = juge5;
    }

    public String getTapis() {
        return tapis;
    }

    public void setTapis(String tapis) {
        this.tapis = tapis;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return typeEpreuve.concat(" - ").concat(nomEpreuve);
    }

    public Participant getParticipantByNomPrenom(String nom, String prenom) {
        for (Participant participant : getParticipants()) {
            if (participant.getNomParticipant().equals(nom) && participant.getPrenomParticipant().equals(prenom)) {
                return participant;
            }
        }
        return null;
    }
}
