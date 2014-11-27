/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid;

import fr.csmb.competition.model.ParticipantBean;
import javafx.scene.control.TextField;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class ParticipantClassementFinalListener {

    private TextField firstPlaceTf = new TextField();
    private TextField secondPlaceTf = new TextField();
    private TextField thirdPlaceTf = new TextField();
    private TextField fourthPlaceTf = new TextField();

    public ParticipantClassementFinalListener(TextField firstPlaceTf, TextField secondPlaceTf, TextField thirdPlaceTf, TextField fourthPlaceTf) {
        this.firstPlaceTf = firstPlaceTf;
        this.secondPlaceTf = secondPlaceTf;
        this.thirdPlaceTf = thirdPlaceTf;
        this.fourthPlaceTf = fourthPlaceTf;
    }

    public void fireUpdateClassementFinal(ParticipantBean participantBean) {
        if (participantBean.getClassementFinal() == 1) {
            firstPlaceTf.setText(participantBean.toString());
        } else if (participantBean.getClassementFinal() == 2) {
            secondPlaceTf.setText(participantBean.toString());
        } else if (participantBean.getClassementFinal() == 3) {
            thirdPlaceTf.setText(participantBean.toString());
        } else if (participantBean.getClassementFinal() == 4) {
            fourthPlaceTf.setText(participantBean.toString());
        }
    }
}
