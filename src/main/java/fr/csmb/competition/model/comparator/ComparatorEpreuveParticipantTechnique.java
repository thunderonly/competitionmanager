/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.model.comparator;

import java.util.Comparator;

import fr.csmb.competition.model.ParticipantBean;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class ComparatorEpreuveParticipantTechnique implements Comparator<ParticipantBean> {

    public int compare(ParticipantBean participantBean1, ParticipantBean participantBean2) {
        if (participantBean1.getNoteTotal() > participantBean2.getNoteTotal()) {
            return -1;
        } else if (participantBean1.getNoteTotal() < participantBean2.getNoteTotal()) {
            return 1;
        }
        return 0;
    }
}
