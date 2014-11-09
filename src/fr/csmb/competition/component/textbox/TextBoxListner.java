/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.textbox;

import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.component.grid.bean.Phase;
import fr.csmb.competition.view.FightView;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class TextBoxListner {

    private TextBox boxBlue;
    private TextBox boxRed;
    private TextBox boxVictory;
    private TextBox boxFail;
    private Phase phase;
    private ParticipantClassementFinalListener participantClassementFinalListener;

    public TextBoxListner(TextBox boxBlue, TextBox boxRed, TextBox boxVictory, TextBox boxFail, Phase phase) {
        this.boxBlue = boxBlue;
        this.boxRed = boxRed;
        this.boxVictory = boxVictory;
        this.boxFail = boxFail;
        this.phase = phase;
    }

    public TextBoxListner(TextBox boxBlue, TextBox boxRed, TextBox boxVictory, TextBox boxFail) {
        this(boxBlue, boxRed, boxVictory, boxFail, Phase.UNKNOWN);
    }

    public void setParticipantClassementFinalListener(ParticipantClassementFinalListener participantClassementFinalListener) {
        this.participantClassementFinalListener = participantClassementFinalListener;
    }

    public void onFireEvent(TextBox sourceBox) {
        FightView fightView = new FightView(boxBlue, boxRed, boxVictory, boxFail, phase);
        if (!boxBlue.getParticipant().getNom().equals("") && !boxRed.getParticipant().getNom().equals("")) {
            fightView.showView();
        }
//        if (sourceBox == boxBlue) {
//            boxVictory.setParticipant(boxBlue.getParticipant());
//            boxFail.setParticipant(boxRed.getParticipant());
//        } else {
//            boxVictory.setParticipant(boxRed.getParticipant());
//            boxFail.setParticipant(boxBlue.getParticipant());
//        }
//        switch (phase) {
//            case PETITE_FINALE:
//                boxVictory.getParticipant().setClassementFinal(3);
//                boxFail.getParticipant().setClassementFinal(4);
//                break;
//            case FINALE:
//                boxVictory.getParticipant().setClassementFinal(1);
//                boxFail.getParticipant().setClassementFinal(2);
//                break;
//        }
        if (participantClassementFinalListener != null) {
            participantClassementFinalListener.fireUpdateClassementFinal(boxVictory.getParticipant());
            participantClassementFinalListener.fireUpdateClassementFinal(boxFail.getParticipant());
        }
    }
}
