/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.Helper;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
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
        participant.setNote1(String.valueOf(participantBean.getNote1()));
        participant.setNote2(String.valueOf(participantBean.getNote2()));
        participant.setNote3(String.valueOf(participantBean.getNote3()));
        participant.setNote4(String.valueOf(participantBean.getNote4()));
        participant.setNote5(String.valueOf(participantBean.getNote5()));
        participant.setClassementAuto(String.valueOf(participantBean
                .getClassementAuto()));
        participant.setClassementManuel(String.valueOf(participantBean.getClassementManuel()));
        participant.setClassementFinal(String.valueOf(participantBean.getClassementFinal()));
        return participant;
    }

    public static ParticipantBean convertParticipantToParticipantBean(Participant participant) {
        ParticipantBean participantBean = new ParticipantBean(participant.getNomParticipant(), participant.getPrenomParticipant());
        participantBean.setClassementFinal(Integer.parseInt(participant.getClassementFinal()));
        participantBean.setClassementManuel(Integer.parseInt(participant.getClassementManuel()));
        participantBean.setClassementAuto(Integer.parseInt(participant.getClassementAuto()));
        participantBean.setNote1(Integer.parseInt(participant.getNote1()));
        participantBean.setNote2(Integer.parseInt(participant.getNote2()));
        participantBean.setNote3(Integer.parseInt(participant.getNote3()));
        participantBean.setNote4(Integer.parseInt(participant.getNote4()));
        participantBean.setNote5(Integer.parseInt(participant.getNote5()));
        return participantBean;
    }
}
