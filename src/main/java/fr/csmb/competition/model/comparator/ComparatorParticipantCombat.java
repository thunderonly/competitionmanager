package fr.csmb.competition.model.comparator;

import fr.csmb.competition.model.ParticipantBean;

import java.util.Comparator;

/**
 * Created by Administrateur on 22/11/14.
 */
public class ComparatorParticipantCombat implements Comparator<ParticipantBean> {

    public int compare(ParticipantBean o1, ParticipantBean o2) {
        if (o1.getClassementFinal() < o2.getClassementFinal()) {
            return -1;
        } else if (o1.getClassementFinal() > o2.getClassementFinal()) {
            return 1;
        }
        return 0;
    }
}
