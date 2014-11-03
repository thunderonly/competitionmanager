package fr.csmb.competition.model.comparator;

import fr.csmb.competition.component.grid.bean.ParticipantBean;

import java.util.Comparator;

/**
 * Created by Administrateur on 03/11/14.
 */
public class ComparatorParticipantPlaceOnGrid implements Comparator<ParticipantBean> {

    @Override
    public int compare(ParticipantBean o1, ParticipantBean o2) {
        if (o1.getPlaceOnGrid() > o2.getPlaceOnGrid()) {
            return 1;
        } else if (o1.getPlaceOnGrid() < o2.getPlaceOnGrid()) {
            return -1;
        }
        return 0;
    }
}
