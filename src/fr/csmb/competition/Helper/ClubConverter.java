/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Eleve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class ClubConverter {

    public static Club convertClubBeanToClub(ClubBean clubBean) {
        Club club = new Club();
        club.setIdentifiant(clubBean.getIdentifiant());
        club.setNomClub(clubBean.getNom());
        club.setResponsable(clubBean.getResponsable());
        for (EleveBean eleveBean : clubBean.getEleves()) {
            Eleve eleve = EleveConverter.convertEleveBeanToEleve(eleveBean);
            club.getEleves().add(eleve);
        }
        return club;
    }

    public static ClubBean convertClubToClubBean(Club club) {
        ClubBean clubBean = new ClubBean();
        clubBean.setNom(club.getNomClub());
        clubBean.setIdentifiant(club.getIdentifiant());
        clubBean.setResponsable(club.getResponsable());

        for (Eleve eleve : club.getEleves()) {
            EleveBean eleveBean = EleveConverter.converEleveToEleveBean(eleve);
            clubBean.getEleves().add(eleveBean);
        }

        return clubBean;
    }
}
