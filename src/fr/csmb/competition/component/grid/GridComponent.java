/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid;

import java.util.List;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import javafx.scene.Group;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public abstract class GridComponent extends Group {


    protected List<ParticipantBean> resultatsList;

    public abstract List<ParticipantBean> getResultatsList();

    public abstract void drawGrid();

    public abstract void setParticipantClassementFinalListener(ParticipantClassementFinalListener participantClassementFinalListener);

}
