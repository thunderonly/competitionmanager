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
public enum TypeCategorie {
    MASCULIN ("Masculin"),
    FEMININ ("Féminin");

    private String value;

    private TypeCategorie(String etat) {
        this.value = etat;
    }

    public String getValue() {
        return this.value;
    }
}