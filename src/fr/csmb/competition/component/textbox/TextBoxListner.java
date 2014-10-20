/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.textbox;

import fr.csmb.competition.component.grid.bean.Phase;

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

    public void onFireEvent(TextBox sourceBox) {
        if (sourceBox == boxBlue) {
            boxVictory.setParticipant(boxBlue.getParticipant());
            boxFail.setParticipant(boxRed.getParticipant());
        } else {
            boxVictory.setParticipant(boxRed.getParticipant());
            boxFail.setParticipant(boxBlue.getParticipant());
        }
        switch (phase) {
            case PETITE_FINALE:
                boxVictory.getParticipant().setClassementFinal(3);
                boxFail.getParticipant().setClassementFinal(4);
                break;
            case FINALE:
                boxVictory.getParticipant().setClassementFinal(1);
                boxFail.getParticipant().setClassementFinal(2);
                break;
        }
    }
}
