package fr.csmb.competition.controller;

import fr.csmb.competition.model.*;
import fr.csmb.competition.model.comparator.EpreuveCombatComparator;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.Epreuve;

import java.util.*;

/**
 * Created by Administrateur on 11/11/14.
 */
public class EditEleveController {

    private CompetitionBean competitionBean;
    private ClubBean clubBean;

    public void addEleve(String licence, String nom, String prenom, String age, String poids, TypeCategorie sexe, List<String> epreuves) {
        EleveBean eleveBean = new EleveBean();
        setEleve(eleveBean, licence, nom, prenom, age, poids, sexe, epreuves);
        clubBean.getEleves().add(eleveBean);

        EleveBeanPresenceChangePropertyListener propertyListener = new EleveBeanPresenceChangePropertyListener();
        propertyListener.setCompetitionBean(competitionBean);
        propertyListener.setClubBean(clubBean);
        propertyListener.setEleveBean(eleveBean);
        eleveBean.presenceProperty().addListener(propertyListener);
        eleveBean.setPresence(true);

        NetworkSender.getINSTANCE().sendClub(clubBean);
    }

    public void updateEleve(EleveBean eleveBean, String licence, String nom, String prenom, String age, String poids, TypeCategorie sexe, List<String> epreuves) {
        setEleve(eleveBean, licence, nom, prenom, age, poids, sexe, epreuves);
        NetworkSender.getINSTANCE().sendClub(clubBean);
    }

    private void setEleve(EleveBean eleveBean, String licence, String nom, String prenom, String age, String poids, TypeCategorie sexe, List<String> epreuves) {
        eleveBean.setLicence(licence);
        eleveBean.setNom(nom);
        eleveBean.setPrenom(prenom);
        eleveBean.setAge(age);
        eleveBean.setPoids(poids);
        eleveBean.setSexe(sexe.getValue());
        eleveBean.setCategorie(extractCategorie(age));
        for (String epreuve : epreuves) {
            if (epreuve.equals(TypeEpreuve.COMBAT.getValue())) {
                eleveBean.getEpreuves().add(extractCategorieCombat2(eleveBean, competitionBean));
            } else {
                eleveBean.getEpreuves().add(epreuve);
            }

        }
    }

    private String extractCategorie(String age) {
        Integer ageInt = Integer.parseInt(age);
        if (ageInt >= 8 && ageInt < 10) {
            return "Pupille";
        } else if (ageInt >= 10 && ageInt < 12) {
            return "Benjamin";
        } else if (ageInt >=12 && ageInt < 14) {
            return "Minime";
        } else if (ageInt >= 14 && ageInt < 16) {
            return "Cadet";
        } else if (ageInt >= 16 && ageInt < 18) {
            return "Junior";
        } else if (ageInt >= 18 && ageInt < 35) {
            return "Sénior";
        } else if (ageInt >= 35 && ageInt < 50) {
            return "Vétérant";
        }
        return "";
    }


    private String extractCategorieCombat(EleveBean eleve, CompetitionBean competition) {
        String poids = eleve.getPoids();
        int poidsEleveInt = Integer.parseInt(poids);
        List<Epreuve> epreuvesCombat = new ArrayList<Epreuve>();
        for (EpreuveBean epreuve : competition.getEpreuves()) {
            if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getDiscipline().getType())) {
                String nomEpreuve = epreuve.getDiscipline().getNom();
                String newNomEpreuve = nomEpreuve.trim();
                String minPoids = newNomEpreuve.substring(0, newNomEpreuve.indexOf("-"));
                String maxPoids = newNomEpreuve.substring(newNomEpreuve.indexOf("-") + 1);
                int intMinPoids = Integer.parseInt(minPoids);
                int intMaxPoids = Integer.parseInt(maxPoids);

                if (epreuve.getCategorie().getNom().equals(eleve.getCategorie()) && epreuve.getCategorie().getType().equals(eleve.getSexe())) {
                    if (poidsEleveInt >=intMinPoids && poidsEleveInt < intMaxPoids ) {
                        return nomEpreuve;
                    }
                }
            }
        }
        return "";
    }


    private String extractCategorieCombat2(EleveBean eleve, CompetitionBean competition) {
        String categorieEleve = eleve.getCategorie();
        String sexeEleve = eleve.getSexe();
        String poidsEleveStr = eleve.getPoids();
        Integer poidsEleve = new Integer(0);
        if (poidsEleveStr != null && !poidsEleveStr.isEmpty()) {
            poidsEleve = Integer.parseInt(poidsEleveStr);
        }

        Map<Integer, EpreuveBean> mapEpreuves = new HashMap<Integer, EpreuveBean>();
        EpreuveCombatComparator comparator = new EpreuveCombatComparator(mapEpreuves);
        TreeMap<Integer, EpreuveBean> epreuveBeanTreeMap = new TreeMap<Integer, EpreuveBean>(comparator);

        //recup tous les poids de la categorie de l eleve
        for (EpreuveBean epreuveBean : competition.getEpreuves()) {
            if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuveBean.getDiscipline().getType())) {
                if (epreuveBean.getCategorie().getNom().equals(categorieEleve) &&
                        epreuveBean.getCategorie().getType().equals(sexeEleve)) {
                    Integer poidsEpreuve = Integer.parseInt(epreuveBean.getDiscipline().getNom());
                    mapEpreuves.put(poidsEpreuve, epreuveBean);
                }
            }
        }
        //tri par poids
        epreuveBeanTreeMap.putAll(mapEpreuves);

        //comparaison poids eleve avec valeur abs du poids de l epreuve
        Iterator<Integer> iterator = epreuveBeanTreeMap.keySet().iterator();
        while (iterator.hasNext()) {
            Integer poidsEpreuve = iterator.next();
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

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    public void setClubBean(ClubBean clubBean) {
        this.clubBean = clubBean;
    }
}
