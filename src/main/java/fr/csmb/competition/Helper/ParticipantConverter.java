/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.xml.model.Epreuve;
import fr.csmb.competition.xml.model.Participant;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class ParticipantConverter {

    public static Participant convertParticipantBeanToParticipant(ParticipantBean participantBean) {
        Participant participant = new Participant();
        participant.setNomParticipant(participantBean.getNom());
        participant.setPrenomParticipant(participantBean.getPrenom());
        participant.setClubParticipant(participantBean.getClub());
        participant.setPoidsParticipant(String.valueOf(participantBean.getPoids()));
        participant.setNote1(String.valueOf(participantBean.getNote1()));
        participant.setNote2(String.valueOf(participantBean.getNote2()));
        participant.setNote3(String.valueOf(participantBean.getNote3()));
        participant.setNote4(String.valueOf(participantBean.getNote4()));
        participant.setNote5(String.valueOf(participantBean.getNote5()));
        participant.setClassementAuto(String.valueOf(participantBean
                .getClassementAuto()));
        participant.setClassementManuel(String.valueOf(participantBean.getClassementManuel()));
        participant.setClassementFinal(String.valueOf(participantBean.getClassementFinal()));
        participant.setPlaceOnGrid(String.valueOf(participantBean.getPlaceOnGrid()));
        participant.setParticipe(participantBean.getParticipe());
        participant.setEpreuve(EpreuveConverter.convertEpreuveBeanToEpreuve(participantBean.getEpreuveBean()));
        return participant;
    }

    public static ParticipantBean convertParticipantToParticipantBean(Participant participant) {
        ParticipantBean participantBean = new ParticipantBean(participant.getNomParticipant(), participant.getPrenomParticipant());
        convertParticipantToParticipantBean(participant, participantBean);
        return participantBean;
    }

    public static void convertParticipantToParticipantBean(Participant participant, ParticipantBean participantBean) {
        participantBean.setClub(participant.getClubParticipant());
        participantBean.setPoids(Integer.parseInt(participant.getPoidsParticipant()));
        participantBean.setClassementFinal(Integer.parseInt(participant.getClassementFinal()));
        participantBean.setClassementManuel(Integer.parseInt(participant.getClassementManuel()));
        participantBean.setClassementAuto(Integer.parseInt(participant.getClassementAuto()));
        participantBean.setNote1(Double.parseDouble(participant.getNote1()));
        participantBean.setNote2(Double.parseDouble(participant.getNote2()));
        participantBean.setNote3(Double.parseDouble(participant.getNote3()));
        participantBean.setNote4(Double.parseDouble(participant.getNote4()));
        participantBean.setNote5(Double.parseDouble(participant.getNote5()));
        participantBean.setPlaceOnGrid(Integer.parseInt(participant.getPlaceOnGrid()));
        participantBean.setParticipe(participant.getParticipe());
        participantBean.setEpreuveBean(EpreuveConverter.convertEpreuveToEpreuveBean(participant.getEpreuve()));
    }
}
