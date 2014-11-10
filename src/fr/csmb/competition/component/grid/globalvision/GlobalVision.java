/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.globalvision;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.csmb.competition.xml.model.Participant;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class GlobalVision {
    private String nomCategorie;
    private Map<String, Map<String, List<Participant>>> typeCategories;

    public GlobalVision(String name) {
        this.nomCategorie = name;
        this.typeCategories = new HashMap<String, Map<String, List<Participant>>>();
    }

    public void setNomCategorie(String nomCategorie) {
        this.nomCategorie = nomCategorie;
    }

    public void setTypeCategories(Map<String, Map<String, List<Participant>>> typeCategories) {
        this.typeCategories = typeCategories;
    }

    public String getNomCategorie() {
        return nomCategorie;
    }

    public Map<String, Map<String, List<Participant>>> getTypeCategories() {
        return typeCategories;
    }
}
