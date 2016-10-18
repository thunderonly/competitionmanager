/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import fr.csmb.competition.model.DetailEpreuveBean;
import fr.csmb.competition.xml.model.DetailEpreuve;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class DetailEpreuveConverter {

    public static DetailEpreuveBean convertDetailEpreuveToDetailEpreuveBean(DetailEpreuve detailEpreuve) {
        DetailEpreuveBean detailEpreuveBean = new DetailEpreuveBean();
        detailEpreuveBean.setAdministrateur(detailEpreuve.getAdministrateur());
        detailEpreuveBean.setChronometreur(detailEpreuve.getChronometreur());
        detailEpreuveBean.setDuree(detailEpreuve.getDuree());
        detailEpreuveBean.setHeureDebut(detailEpreuve.getHeureDebut());
        detailEpreuveBean.setHeureFin(detailEpreuve.getHeureFin());
        detailEpreuveBean.setJuge1(detailEpreuve.getJuge1());
        detailEpreuveBean.setJuge2(detailEpreuve.getJuge2());
        detailEpreuveBean.setJuge3(detailEpreuve.getJuge3());
        detailEpreuveBean.setJuge4(detailEpreuve.getJuge4());
        detailEpreuveBean.setJuge5(detailEpreuve.getJuge5());
        detailEpreuveBean.setTapis(detailEpreuve.getTapis());

        return detailEpreuveBean;
    }

    public static DetailEpreuve convertDetailEpreuveBeanToDetailEpreuve(DetailEpreuveBean detailEpreuveBean) {
        DetailEpreuve detailEpreuve = new DetailEpreuve();
        detailEpreuve.setAdministrateur(detailEpreuveBean.getAdministrateur());
        detailEpreuve.setChronometreur(detailEpreuveBean.getChronometreur());
        detailEpreuve.setJuge1(detailEpreuveBean.getJuge1());
        detailEpreuve.setJuge2(detailEpreuveBean.getJuge2());
        detailEpreuve.setJuge3(detailEpreuveBean.getJuge3());
        detailEpreuve.setJuge4(detailEpreuveBean.getJuge4());
        detailEpreuve.setJuge5(detailEpreuveBean.getJuge5());
        detailEpreuve.setTapis(detailEpreuveBean.getTapis());
        detailEpreuve.setHeureDebut(detailEpreuveBean.getHeureDebut());
        detailEpreuve.setHeureFin(detailEpreuveBean.getHeureFin());
        detailEpreuve.setDuree(detailEpreuveBean.getDuree());

        return detailEpreuve;
    }
}
