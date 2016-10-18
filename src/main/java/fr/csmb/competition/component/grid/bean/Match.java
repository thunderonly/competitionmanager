/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.bean;

import fr.csmb.competition.component.textbox.TextBox;
import fr.csmb.competition.model.ParticipantBean;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class Match {
    private ParticipantBean joueur1;
    private ParticipantBean joueur2;
    private TextBox resultat;

    public Match() {
    }

    public Match(ParticipantBean joueur1, ParticipantBean joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
    }

    public ParticipantBean getJoueur1() {
        return joueur1;
    }

    public void setJoueur1(ParticipantBean joueur1) {
        this.joueur1 = joueur1;
    }

    public ParticipantBean getJoueur2() {
        return joueur2;
    }

    public void setJoueur2(ParticipantBean joueur2) {
        this.joueur2 = joueur2;
    }

    public TextBox getResultat() {
        return resultat;
    }

    public void setResultat(TextBox resultat) {
        this.resultat = resultat;
    }
}
