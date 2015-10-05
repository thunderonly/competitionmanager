package fr.csmb.competition.model.comparator;

import fr.csmb.competition.model.EpreuveBean;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by Administrateur on 04/04/15.
 */
public class EpreuveCombatComparator implements Comparator<Integer> {
    Map<Integer, EpreuveBean> base;
    public EpreuveCombatComparator(Map<Integer, EpreuveBean> baseMap) {
        base = baseMap;
    }

    public int compare(Integer a, Integer b) {

        if (a > 0) {
            return 1;
        }
        if (b > 0) {
            return -1;
        }

        if (a >= b) {
            return -1;
        } else {
            return 1;
        }

    }
}
