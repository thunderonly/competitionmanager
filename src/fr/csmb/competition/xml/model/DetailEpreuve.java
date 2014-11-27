/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DetailEpreuve implements Serializable {

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
}
