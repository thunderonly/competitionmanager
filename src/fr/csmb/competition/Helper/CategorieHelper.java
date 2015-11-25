package fr.csmb.competition.Helper;

import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.comparator.EpreuveCombatComparator;
import fr.csmb.competition.type.TypeEpreuve;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrateur on 24/11/15.
 */
public class CategorieHelper {

    public static String getCategorieFromAge(String ageString) {
        Integer age = Integer.parseInt(ageString);
        switch (age) {
            case 8:
            case 9:
                return "Pupilles";
            case 10:
            case 11:
                return "Benjamins";
            case 12:
            case 13:
                return "Minimes";
            case 14:
            case 15:
                return "Cadets";
            case 16:
            case 17:
                return "Juniors";
            default:
                if (age >= 18 && age < 35) {
                    return "Séniors";
                } else if (age >=35) {
                    return "Vétérans";
                }
        }
        return "";
    }

    public static String extractCategorieCombat(EleveBean eleve, CompetitionBean competition) {
        String categorieEleve = eleve.getCategorie();
        String sexeEleve = eleve.getSexe();
        String poidsEleveStr = eleve.getPoids();
        Double poidsEleve = new Double(0);
        if (poidsEleveStr != null && !poidsEleveStr.isEmpty()) {
            poidsEleve = Double.parseDouble(poidsEleveStr);
        }

        Map<Double, EpreuveBean> mapEpreuves = new HashMap<Double, EpreuveBean>();
        EpreuveCombatComparator comparator = new EpreuveCombatComparator(mapEpreuves);
        TreeMap<Double, EpreuveBean> epreuveBeanTreeMap = new TreeMap<Double, EpreuveBean>(comparator);

        //recup tous les poids de la categorie de l eleve
        for (EpreuveBean epreuveBean : competition.getEpreuves()) {
            if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuveBean.getDiscipline().getType())) {
                if (epreuveBean.getCategorie().getNom().equals(categorieEleve) &&
                        epreuveBean.getCategorie().getSexe().equals(sexeEleve)) {
                    Double poidsEpreuve = Double.parseDouble(epreuveBean.getDiscipline().getNom());
                    mapEpreuves.put(poidsEpreuve, epreuveBean);
                }
            }
        }
        //tri par poids
        epreuveBeanTreeMap.putAll(mapEpreuves);

        //comparaison poids eleve avec valeur abs du poids de l epreuve
        Iterator<Double> iterator = epreuveBeanTreeMap.keySet().iterator();
        while (iterator.hasNext()) {
            Double poidsEpreuve = iterator.next();
            if (poidsEleve <= Math.abs(poidsEpreuve)) {
                return mapEpreuves.get(poidsEpreuve).getDiscipline().getNom();
            } else {
                if (!iterator.hasNext()) {
                    //c'est le dernier donc plus lourd
                    return mapEpreuves.get(poidsEpreuve).getDiscipline().getNom();
                }
            }
        }

        return "";
    }
}
