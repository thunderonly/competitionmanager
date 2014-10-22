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
    UNKNONW ("inconnu"),
    VALIDE ("valide"),
    FUSION ("fusion"),
    TERMINE ("termine");

    private String value;

    private EtatEpreuve(String etat) {
        this.value = etat;
    }

    public String getValue() {
        return this.value;
    }
}
