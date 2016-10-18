/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import java.util.Comparator;

import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.model.comparator.ComparatorClubTotalCombat;
import fr.csmb.competition.model.comparator.ComparatorClubTotalGeneral;
import fr.csmb.competition.model.comparator.ComparatorClubTotalTechnique;
import fr.csmb.competition.model.comparator.ComparatorEpreuveParticipantTechnique;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class ClassementHelper {

    private static int pointForFirstPlace = 4;
    private static int pointForSecondPlace = 3;
    private static int pointForThirdPlace = 2;
    private static int pointForFourthPlace = 1;

    public static void computeClassementAutoEpreuveTechnique(ObservableList<ParticipantBean> data) {
        SortedList<ParticipantBean> parts = new SortedList<ParticipantBean>(data, new ComparatorEpreuveParticipantTechnique());
        int i = 0;
        ParticipantBean previousParticipant = null;
        for (ParticipantBean participant : parts) {
            if (previousParticipant != null) {
                if (previousParticipant.getNoteTotal().equals(participant.getNoteTotal())) {
                    data.get(data.indexOf(participant)).setClassementAuto(previousParticipant.getClassementAuto());
                    data.get(data.indexOf(participant)).setClassementFinal(previousParticipant.getClassementFinal());
                    data.get(data.indexOf(participant)).setClassementManuel(0);
                    previousParticipant.setClassementManuel(0);
                    i++;
                } else {
                    i++;
                    data.get(data.indexOf(participant)).setClassementAuto(i);
                    data.get(data.indexOf(participant)).setClassementFinal(i);
                }
            } else {
                i++;
                data.get(data.indexOf(participant)).setClassementAuto(i);
                data.get(data.indexOf(participant)).setClassementFinal(i);
            }
            previousParticipant = participant;
        }
    }

    public static void computeClassementClubs(CompetitionBean competitionBean) {
        for (EpreuveBean epreuveBean : competitionBean.getEpreuves()) {
            if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean)) {
                    ClubBean clubBean = CompetitionHelper.getClubById(competitionBean, participantBean.getClub());
                    Integer points = pointForParticipant(participantBean);
                    if (epreuveBean.getDiscipline().getType().equals(TypeEpreuve.COMBAT.getValue())) {
                        clubBean.setTotalCombat(clubBean.getTotalCombat() + points);
                    } else {
                        clubBean.setTotalTechnique(clubBean.getTotalTechnique() + points);
                    }
                }
            }
        }

        for (ClubBean clubBean : competitionBean.getClubs()) {
            clubBean.setTotalGeneral(clubBean.getTotalTechnique() + clubBean.getTotalCombat());
        }
    }

    public static void computeClassementCombatClub(ObservableList<ClubBean> clubs) {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(clubs, new ComparatorClubTotalCombat());
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalCombat() == club.getTotalCombat()) {
                    club.setClassementCombat(previousClub.getClassementCombat());
                    i++;
                } else {
                    i++;
                    club.setClassementCombat(i);
                }
            } else {
                i++;
                club.setClassementCombat(i);
            }
            previousClub = club;
        }
    }

    public static void computeClassementGeneralClub(ObservableList<ClubBean> clubs) {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(clubs, new ComparatorClubTotalGeneral());
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalGeneral() == club.getTotalGeneral()) {
                    club.setClassementGeneral(previousClub.getClassementGeneral());
                    i++;
                } else {
                    i++;
                    club.setClassementGeneral(i);
                }
            } else {
                i++;
                club.setClassementGeneral(i);
            }
            previousClub = club;
        }
    }

    public static void computeClassementTechniqueClub(ObservableList<ClubBean> clubs) {
        SortedList<ClubBean> sortableList = new SortedList<ClubBean>(clubs, new ComparatorClubTotalTechnique());
        int i = 0;
        ClubBean previousClub = null;
        for (ClubBean club : sortableList) {
            if (previousClub != null) {
                if (previousClub.getTotalTechnique() == club.getTotalTechnique()) {
                    club.setClassementTechnique(previousClub.getClassementTechnique());
                    i++;
                } else {
                    i++;
                    club.setClassementTechnique(i);
                }
            } else {
                i++;
                club.setClassementTechnique(i);
            }
            previousClub = club;
        }
    }

    private static Integer pointForParticipant(ParticipantBean participantBean) {
        Integer points = 0;
        switch (participantBean.getClassementFinal().intValue()) {
            case 1:
                points+=pointForFirstPlace;
                break;
            case 2:
                points+=pointForSecondPlace;
                break;
            case 3:
                points+=pointForThirdPlace;
                break;
            case 4:
                points+=pointForFourthPlace;
                break;
            default:
                break;
        }
        return points;
    }
}
