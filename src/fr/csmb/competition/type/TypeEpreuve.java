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
public enum TypeEpreuve {
    TECHNIQUE ("technique"),
    COMBAT ("combat");

    private String value;

    private TypeEpreuve(String etat) {
        this.value = etat;
    }

    public String getValue() {
        return this.value;
    }
}
