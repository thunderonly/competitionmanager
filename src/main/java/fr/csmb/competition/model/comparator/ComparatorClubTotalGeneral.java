/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model.comparator;

import java.util.Comparator;

import fr.csmb.competition.model.ClubBean;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class ComparatorClubTotalGeneral implements Comparator<ClubBean> {

    public int compare(ClubBean o1, ClubBean o2) {
        if (o1.getTotalGeneral() > o2.getTotalGeneral()) {
            return -1;
        } else if (o1.getTotalGeneral() < o2.getTotalGeneral()) {
            return 1;
        }
        return 0;
    }
}
