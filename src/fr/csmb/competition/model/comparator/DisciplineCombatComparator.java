package fr.csmb.competition.model.comparator;

import fr.csmb.competition.component.grid.globalvision.GlobalVision;
import fr.csmb.competition.model.EpreuveBean;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by Administrateur on 04/04/15.
 */
public class DisciplineCombatComparator implements Comparator<GlobalVision> {
    public DisciplineCombatComparator() {
    }

    public int compare(GlobalVision a, GlobalVision b) {
        Integer weightA = 0;
        Integer weightB = 0;
        try {
            weightA = Integer.parseInt(a.getDisciplineName());
        } catch (NumberFormatException e) {

        }
        try {
            weightB = Integer.parseInt(b.getDisciplineName());
        } catch (NumberFormatException e) {

        }

        if (weightA >= weightB) {
            return -1;
        } else {
            return 1;
        }

    }
}
