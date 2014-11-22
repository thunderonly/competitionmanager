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
        if (sourceBox != null) {
            FightView fightView = new FightView(boxBlue, boxRed, boxVictory, boxFail, phase);
            if (!boxBlue.getParticipant().getNom().equals("") && !boxRed.getParticipant().getNom().equals("")) {
                fightView.showView();
            }
        }

        if (participantClassementFinalListener != null) {
            participantClassementFinalListener.fireUpdateClassementFinal(boxVictory.getParticipant());
            participantClassementFinalListener.fireUpdateClassementFinal(boxFail.getParticipant());
        }
    }
}
