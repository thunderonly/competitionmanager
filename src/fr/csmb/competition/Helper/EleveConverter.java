/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.xml.model.Eleve;
import javafx.collections.FXCollections;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class EleveConverter {

    public static EleveBean converEleveToEleveBean(Eleve eleve) {
        EleveBean eleveBean = new EleveBean();
        eleveBean.setNom(eleve.getNomEleve());
        eleveBean.setPrenom(eleve.getPrenomEleve());
        eleveBean.setLicence(eleve.getLicenceEleve());
        eleveBean.setAge(eleve.getAgeEleve());
        eleveBean.setCategorie(eleve.getCategorieEleve());
        eleveBean.setPoids(eleve.getPoidsEleve());
        eleveBean.setSexe(eleve.getSexeEleve());
        eleveBean.setPresence(eleve.getPresenceEleve());
        eleveBean.setEpreuves(FXCollections.observableArrayList(eleve.getEpreuvesEleves()));
        return eleveBean;
    }

    public static Eleve convertEleveBeanToEleve(EleveBean eleveBean) {
        Eleve eleve = new Eleve(eleveBean.getNom(), eleveBean.getPrenom());
        eleve.setCategorieEleve(eleveBean.getCategorie());
        eleve.setSexeEleve(eleveBean.getSexe());
        eleve.setAgeEleve(eleveBean.getAge());
        eleve.setLicenceEleve(eleveBean.getLicence());
        eleve.setPoidsEleve(eleveBean.getPoids());
        eleve.setPresenceEleve(eleveBean.getPresence());
        for (String epreuve : eleveBean.getEpreuves()) {
            eleve.getEpreuvesEleves().add(epreuve);
        }
        return eleve;
    }
}
