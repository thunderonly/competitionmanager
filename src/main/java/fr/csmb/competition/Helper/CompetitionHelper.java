/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class CompetitionHelper {

    public static ClubBean getClubById(CompetitionBean competitionBean, String id) {
        for (ClubBean clubBean : competitionBean.getClubs()) {
            if (clubBean.getIdentifiant().equals(id)) {
                return clubBean;
            }
        }
        return null;
    }
}
