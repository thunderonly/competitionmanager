package fr.csmb.competition.Helper;

import fr.csmb.competition.model.*;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.ConfigureFightView;
import fr.csmb.competition.xml.model.Eleve;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 26/11/15.
 */
public class EleveHelper {

    public static void manageEpreuves(CompetitionBean competitionBean, ClubBean clubBean, EleveBean eleveBean, List<String> epreuves) {
        CategorieBean categorieBean = competitionBean.getCategorie(eleveBean.getSexe(), eleveBean.getCategorie());
        if (categorieBean != null) {
            List<String> epreuvesToBeRemove = new ArrayList<String>();
            for (String epreuve : eleveBean.getEpreuves()) {
                if (!epreuves.contains(epreuve)) {
                    epreuvesToBeRemove.add(epreuve);
                }
            }

            List<String> epreuvesToBeAdd = new ArrayList<String>();
            for (String epreuve : epreuves) {
                String newEpreuve = epreuve;
                if (epreuve.equals(TypeEpreuve.COMBAT.getValue())) {
                    newEpreuve = CategorieHelper.extractCategorieCombat(eleveBean, competitionBean);
                }

                if (!eleveBean.getEpreuves().contains(newEpreuve)) {
                    epreuvesToBeAdd.add(newEpreuve);
                }
            }

            EleveHelper.removeEpreuves(competitionBean, epreuvesToBeRemove, categorieBean, eleveBean);
            EleveHelper.addEpreuves(competitionBean, epreuvesToBeAdd, categorieBean, clubBean, eleveBean);
        }
    }

    public static void addEpreuves(CompetitionBean competitionBean, List<String> epreuvesToBeAdd,
                                   CategorieBean categorieBean, ClubBean clubBean, EleveBean eleveBean) {
        for (String epreuve : epreuvesToBeAdd) {
            String newEpreuve = epreuve;
            if (epreuve.equals(TypeEpreuve.COMBAT.getValue())) {
                newEpreuve = CategorieHelper.extractCategorieCombat(eleveBean, competitionBean);
            }

            if (!eleveBean.getEpreuves().contains(newEpreuve)) {
                eleveBean.getEpreuves().add(newEpreuve);
                DisciplineBean disciplineBean = competitionBean.getDiscipline(newEpreuve);
                if (categorieBean != null) {
                    ParticipantHelper.createParticipantFromEleveBeanCategorie(
                            competitionBean, eleveBean, clubBean, categorieBean, newEpreuve);

                    EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
                    if (disciplineBean.getType().equals(TypeEpreuve.COMBAT.getValue())) {
                        if (epreuveBean.getEtat() != null &&
                                (epreuveBean.getEtat().equals(EtatEpreuve.VALIDE.getValue()) ||
                                        epreuveBean.getEtat().equals(EtatEpreuve.DEMARRE.getValue()))) {
                            final Stage newStage = new Stage();
                            ConfigureFightView configureFightView = new ConfigureFightView();
                            configureFightView.showView(newStage, competitionBean, epreuveBean);
                        }
                    }
                }
            }
        }
    }

    public static void removeEpreuves(CompetitionBean competitionBean, List<String> epreuvesToBeRemove,
                                      CategorieBean categorieBean, EleveBean eleveBean) {
        for (String epreuve : epreuvesToBeRemove) {
            DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
            EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
            ParticipantBean participantBeanToBeRemove = null;
            eleveBean.getEpreuves().remove(epreuve);
            for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(
                    epreuveBean)) {
                if (participantBean.getNom().equals(eleveBean.getNom()) &&
                        participantBean.getPrenom().equals(eleveBean.getPrenom())) {
                    participantBeanToBeRemove = participantBean;
                }
            }
            if (participantBeanToBeRemove != null) {
                competitionBean.getParticipants().remove(participantBeanToBeRemove);
            }
            if (disciplineBean.getType().equals(TypeEpreuve.COMBAT.getValue())) {
                if (epreuveBean.getEtat() != null &&
                        (epreuveBean.getEtat().equals(EtatEpreuve.VALIDE.getValue()) ||
                                epreuveBean.getEtat().equals(EtatEpreuve.DEMARRE.getValue()))) {
                    final Stage newStage = new Stage();
                    ConfigureFightView configureFightView = new ConfigureFightView();
                    configureFightView.showView(newStage, competitionBean, epreuveBean);
                }
            }
        }
    }
}
