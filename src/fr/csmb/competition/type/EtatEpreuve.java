/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.type;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public enum EtatEpreuve {
    UNKNONW ("Inconnu"),
    VALIDE ("Valide"),
    FUSION ("Fusion"),
    DEMARRE ("Demarre"),
    TERMINE ("Termine"),
    SUPPRIME ("Supprime");

    private String value;

    private EtatEpreuve(String etat) {
        this.value = etat;
    }

    public String getValue() {
        return this.value;
    }
}
