package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Administrateur on 19/10/14.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Participant implements Serializable {
    private static final long serialVersionUID = 5950169519310163575L;

    @XmlAttribute(name="nomParticipant")
    private String nomParticipant;
    @XmlAttribute(name="prenomParticipant")
    private String prenomParticipant;
    @XmlAttribute(name="clubParticipant")
    private String clubParticipant;
    @XmlAttribute(name="poidsParticipant")
    private String poidsParticipant;
    @XmlAttribute @XmlIDREF
    private Epreuve epreuve;
    @XmlElement(name="classementAuto")
    private String classementAuto;
    @XmlElement(name="classementManuel")
    private String classementManuel;
    @XmlElement(name="classementFinal")
    private String classementFinal;
    @XmlElement(name="note1")
    private String note1;
    @XmlElement(name="note2")
    private String note2;
    @XmlElement(name="note3")
    private String note3;
    @XmlElement(name="note4")
    private String note4;
    @XmlElement(name="note5")
    private String note5;
    @XmlElement(name="noteTotal")
    private String noteTotal;
    @XmlElement(name="placeOnGrid")
    private String placeOnGrid;
    @XmlElement(name="participe")
    private Boolean participe;


    public String getNomParticipant() {
        return nomParticipant;
    }

    public void setNomParticipant(String nomParticipant) {
        this.nomParticipant = nomParticipant;
    }

    public String getPrenomParticipant() {
        return prenomParticipant;
    }

    public void setPrenomParticipant(String prenomParticipant) {
        this.prenomParticipant = prenomParticipant;
    }

    public String getClubParticipant() {
        return clubParticipant;
    }

    public void setClubParticipant(String clubParticipant) {
        this.clubParticipant = clubParticipant;
    }

    public String getPoidsParticipant() {
        return poidsParticipant;
    }

    public void setPoidsParticipant(String poidsParticipant) {
        this.poidsParticipant = poidsParticipant;
    }

    public String getClassementAuto() {
        return classementAuto;
    }

    public void setClassementAuto(String classementAuto) {
        this.classementAuto = classementAuto;
    }

    public String getClassementManuel() {
        return classementManuel;
    }

    public void setClassementManuel(String classementManuel) {
        this.classementManuel = classementManuel;
    }

    public String getClassementFinal() {
        return classementFinal;
    }

    public void setClassementFinal(String classementFinal) {
        this.classementFinal = classementFinal;
    }

    public String getNote1() {
        return note1;
    }

    public void setNote1(String note1) {
        this.note1 = note1;
    }

    public String getNote2() {
        return note2;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }

    public String getNote3() {
        return note3;
    }

    public void setNote3(String note3) {
        this.note3 = note3;
    }

    public String getNote4() {
        return note4;
    }

    public void setNote4(String note4) {
        this.note4 = note4;
    }

    public String getNote5() {
        return note5;
    }

    public void setNote5(String note5) {
        this.note5 = note5;
    }

    public String getNoteTotal() {
        return noteTotal;
    }

    public void setNoteTotal(String noteTotal) {
        this.noteTotal = noteTotal;
    }

    public String getPlaceOnGrid() {
        return placeOnGrid;
    }

    public void setPlaceOnGrid(String placeOnGrid) {
        this.placeOnGrid = placeOnGrid;
    }

    public Boolean getParticipe() {
        return participe;
    }

    public void setParticipe(Boolean participe) {
        this.participe = participe;
    }

    public Epreuve getEpreuve() {
        return epreuve;
    }

    public void setEpreuve(Epreuve epreuve) {
        this.epreuve = epreuve;
    }
}
