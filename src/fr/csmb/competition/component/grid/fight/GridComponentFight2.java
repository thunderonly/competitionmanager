/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.fight;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.collections.transformation.SortedList;

import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.component.grid.bean.Match;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.component.grid.bean.Phase;
import fr.csmb.competition.component.textbox.TextBox;
import fr.csmb.competition.component.textbox.TextBoxListner;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class GridComponentFight2 extends GridComponent {
    int spaceBetweenJoueur = 20;
    int spaceBetweenMatch = 50;
    int widthRectangle = 150;
    int heightRectangle = 20;

    private SortedList<ParticipantBean> sortedJoueurs;
    private List<ParticipantBean> joueurs;

    private TextBox[] resultats = null;
    private ParticipantClassementFinalListener participantClassementFinalListener;

    public GridComponentFight2(List<ParticipantBean> joueurList) {
        this.joueurs = joueurList;
        resultatsList = new ArrayList<ParticipantBean>();
    }

    public List<ParticipantBean> getResultatsList() {
        resultatsList.clear();
        for (TextBox textBox : resultats) {
            resultatsList.add(textBox.getParticipant());
        }
        return resultatsList;
    }

    public void drawGrid() {
        drawGrid(false);
    }

    public void drawGrid(boolean forConfigure) {
        Group group = this;
        resultats = drawShape(group, forConfigure);
    }

    /**
     * Etablish match list and draw match table
     *
     * @param group
     * @return
     */
    private TextBox[] drawShape(Group group, boolean forConfigure) {
        int nbJoueur = joueurs.size();
        TextBox[] result = null;
        if (nbJoueur > 4) {
            result = computeSeveralRound(nbJoueur);
        } else {
            if (nbJoueur > 2 && nbJoueur <= 4) {
                int nbMatchMaxi = 4/2;
                int placeOfJoueur = 0;
                int i = 0;
                int nbMatchBeforeMaxi = nbJoueur - nbMatchMaxi;
                while (i < nbMatchBeforeMaxi) {

                    Match match = new Match();
                    match.setJoueur1(joueurs.get(placeOfJoueur));
                    placeOfJoueur++;
                    match.setJoueur2(joueurs.get(placeOfJoueur));
                    placeOfJoueur++;

                    TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1);
                    result[i] = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, i + 1);
                    i++;
                }
                double rest = nbMatchBeforeMaxi % 2;
                if (rest > 0) {

                    Match match = new Match();
                    match.setJoueur1(new ParticipantBean());
                    match.setJoueur2(new ParticipantBean());
                    TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1, false);
                    result[i] = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, i + 1, false);
                    result[i].setParticipant(joueurs.get(placeOfJoueur));
                    placeOfJoueur++;
                    i++;
                }
            }
        }



        TextBox[] resultatsDemi1 = drawMatch(result[0], result[1], group, 1, Phase.DEMI_FINALE);
        TextBox[] resultatsDemi2 =  drawMatch(result[2], result[3], group, 2, Phase.DEMI_FINALE);
        TextBox[] resultatsFinale = drawMatch(resultatsDemi1[0], resultatsDemi2[0], group, 2, Phase.FINALE);
        TextBox[] resultatsPetiteFinale = drawMatch(resultatsDemi1[1], resultatsDemi2[1], group, 2, Phase.PETITE_FINALE);
        return new TextBox[]{resultatsFinale[0], resultatsFinale[1], resultatsPetiteFinale[0], resultatsPetiteFinale[1]};
    }

    private TextBox[] computeSeveralRound(int nbJoueur) {

        int nbMatchMaxi = 0;
        if (nbJoueur > 4 && nbJoueur <= 8) {
            nbMatchMaxi = 8/2;
        } else if (nbJoueur > 8 && nbJoueur <= 16) {
            nbMatchMaxi = 16/2;
        } else if (nbJoueur > 16 && nbJoueur <= 32) {
            nbMatchMaxi = 32/2;
        }

        int nbMatchBeforeMaxi = nbJoueur - nbMatchMaxi;
        TextBox[] result = new TextBox[nbMatchMaxi];
        int placeOfJoueur = 0;
        int i = 0;
        while (i < nbMatchBeforeMaxi) {

            Match match = new Match();
            match.setJoueur1(joueurs.get(placeOfJoueur));
            placeOfJoueur++;
            match.setJoueur2(joueurs.get(placeOfJoueur));
            placeOfJoueur++;

            TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1);
            result[i] = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, i + 1);
            i++;
        }
        double rest = nbMatchBeforeMaxi % 2;
        if (rest > 0) {

            Match match = new Match();
            match.setJoueur1(new ParticipantBean());
            match.setJoueur2(new ParticipantBean());
            TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1, false);
            result[i] = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, i + 1, false);
            result[i].setParticipant(joueurs.get(placeOfJoueur));
            placeOfJoueur++;
            i++;
        }

        while (i < nbMatchMaxi) {
            Match match = new Match();
            match.setJoueur1(new ParticipantBean());
            match.setJoueur2(new ParticipantBean());
            TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1, false);
            result[i] = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, i + 1, false);
            result[i].setParticipant(joueurs.get(placeOfJoueur));
            placeOfJoueur++;
            i++;
        }

        int nbMatchOtherRound = result.length / 2;
        while (nbMatchOtherRound >= 4) {
            TextBox[] resultCurrentRound = new TextBox[nbMatchOtherRound];
            int index = 0;
            int indexLastRound = 0;
            while (index < nbMatchOtherRound) {
                TextBox blue = result[indexLastRound];
                indexLastRound++;
                TextBox red = result[indexLastRound];
                indexLastRound++;
                resultCurrentRound[index] = drawMatch(blue, red, this, index + 1);
                index++;
            }
            result = resultCurrentRound;
            nbMatchOtherRound = result.length / 2;
        }

        return result;
    }

    private TextBox[] drawMatchFirstRound(int beginX, int beginY, Group group1, Match match, int level) {
        return drawMatchFirstRound(beginX, beginY, group1, match, level, true);
    }


    private TextBox[] drawMatchFirstRound(int beginX, int beginY, Group group1, Match match, int level, boolean visible) {

        TextBox[] matchOtherStep = new TextBox[2];

        int newBeginX = beginX;
        int newBeginY = beginY;

        if (level > 1) {
            int facteur = level - 1;
            newBeginY = beginY + (heightRectangle * 2) * facteur + (spaceBetweenJoueur * 2) * facteur;
        }

        //BoxBlue
        int beginXBlue = newBeginX;
        int beginYBlue = newBeginY;
        //BoxRed
        int beginXRed = newBeginX;
        int beginYRed = newBeginY + heightRectangle + spaceBetweenJoueur;
        Color colorBlue = Color.color(1.0, 1.0, 0.0, 0.5);
        Color colorRed = Color.color(0.0, 1.0, 1.0, 0.5);


        Color colorResultat = Color.color(1.0, 1.0, 0.0, 0.5);
        if (level % 2 == 0) {
            colorResultat = Color.color(0.0, 1.0, 1.0, 0.5);
        }

        TextBox blue = createTextBox(match.getJoueur1(), beginXBlue, beginYBlue, colorBlue);
        TextBox red = createTextBox(match.getJoueur2(), beginXRed, beginYRed, colorRed);
        matchOtherStep[0] = blue;
        matchOtherStep[1] = red;
        if (visible) {
            group1.getChildren().addAll(blue, red);
        }
        return matchOtherStep;
    }

    /**
     * Draw grid in function of 2 TextBox (participant). Is for secound and other round
     * @param boxBlue
     * @param boxRed
     * @param group
     * @param level
     * @return
     */
    private TextBox drawMatch(TextBox boxBlue, TextBox boxRed, Group group, int level) {
        return drawMatch(boxBlue, boxRed, group, level, true);
    }

    /**
     * Draw grid in function of 2 TextBox (participant). Is for secound and other round
     * @param boxBlue
     * @param boxRed
     * @param group
     * @param level
     * @return
     */
    private TextBox drawMatch(TextBox boxBlue, TextBox boxRed, Group group, int level, boolean drawLine) {

        //BoxBlue
        double beginXBlue = boxBlue.getLayoutX();
        double beginYBlue = boxBlue.getLayoutY();
        //BoxRed
        double beginXRed = boxRed.getLayoutX();
        double beginYRed = boxRed.getLayoutY();
        //LineBlue Horizontale
        double beginXLineHBlue = beginXBlue + widthRectangle;
        double beginYLineHBlue = beginYBlue + heightRectangle;
        double endXLineHBlue = beginXLineHBlue + spaceBetweenMatch;
        double endYLineHBlue = beginYBlue + heightRectangle;
        //LineBlue Verticale
        double beginXLineVBlue = endXLineHBlue;
        double beginYLineVBlue = beginYLineHBlue;
        double endXLineVBlue = endXLineHBlue;
        double endYLineVBlue = ((beginYRed - endYLineHBlue) / 2) + endYLineHBlue - (heightRectangle / 2);

        //LineRed Horizontale
        double beginXLineHRed = beginXRed + widthRectangle;
        double beginYLineHRed = beginYRed;
        double endXLineHRed = beginXLineHRed + spaceBetweenMatch;
        double endYLineHRed = beginYRed;
        //LineRed Verticale
        double beginXLineVRed = endXLineHRed;
        double beginYLineVRed = endYLineHRed;
        double endXLineVRed = endXLineHRed;
        double endYLineVRed = endYLineVBlue + heightRectangle;

        double beginXResultat = endXLineHBlue;
        double beginYResultat = endYLineVBlue;

        Color colorResultat = Color.color(1.0, 1.0, 0.0, 0.5);
        if (level % 2 == 0) {
            colorResultat = Color.color(0.0, 1.0, 1.0, 0.5);
        }

        Line lineBlueH = drawLine(beginXLineHBlue, beginYLineHBlue , endXLineHBlue, endYLineHBlue);
        Line lineBlueV = drawLine(beginXLineVBlue, beginYLineVBlue , endXLineVBlue, endYLineVBlue);

        Line lineRedH = drawLine(beginXLineHRed, beginYLineHRed , endXLineHRed, endYLineHRed);
        Line lineRedV = drawLine(beginXLineVRed, beginYLineVRed , endXLineVRed, endYLineVRed);

        TextBox victoryBox = createTextBox(new ParticipantBean("", ""), beginXResultat, beginYResultat, colorResultat);
        TextBox defaitBox = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, colorResultat);

        if (drawLine) {
            group.getChildren().addAll(victoryBox, lineBlueH, lineRedH, lineBlueV, lineRedV);
        } else {
            group.getChildren().addAll(victoryBox);
        }

        TextBoxListner textBoxListner = new TextBoxListner(boxBlue, boxRed, victoryBox, defaitBox);
        boxBlue.setListner(textBoxListner);
        boxRed.setListner(textBoxListner);

        return victoryBox;
    }

    /**
     * Draw grid in function of 2 TextBox (participant). Is for Demi final, little final and final
     *
     * @param boxBlue
     * @param boxRed
     * @param group
     * @param level
     * @param phase
     * @return
     */
    private TextBox[] drawMatch(TextBox boxBlue, TextBox boxRed, Group group, int level, Phase phase) {
        TextBox victoryBox = null;
        TextBox defaitBox = null;
        Color colorResultat = Color.color(1.0, 1.0, 0.0, 0.5);
        if (level % 2 == 0) {
            colorResultat = Color.color(0.0, 1.0, 1.0, 0.5);
        }
        if (phase.ordinal() == Phase.FINALE.ordinal() || phase.ordinal() == Phase.PETITE_FINALE.ordinal()) {
            victoryBox = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, Color.color(1.0, 1.0, 0.0, 0.5));
            defaitBox = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, Color.color(0.0, 1.0, 1.0, 0.5));

            //Create a TextBoxListner to prepare resultat
            TextBoxListner listner = new TextBoxListner(boxBlue, boxRed, victoryBox, defaitBox, phase);
            boxBlue.setListner(listner);
            boxRed.setListner(listner);
            listner.setParticipantClassementFinalListener(participantClassementFinalListener);

        } else if (phase.ordinal() == Phase.DEMI_FINALE.ordinal()) {
            victoryBox = drawMatch(boxBlue, boxRed, group, level);
            defaitBox = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, colorResultat);

            //Create a TextBoxListner to prepare resultat
            TextBoxListner listner = new TextBoxListner(boxBlue, boxRed, victoryBox, defaitBox);
            boxBlue.setListner(listner);
            boxRed.setListner(listner);
            defaitBox.setLayoutX(victoryBox.getLayoutX() + widthRectangle + spaceBetweenMatch);
            defaitBox.setLayoutY(victoryBox.getLayoutY());
            group.getChildren().add(defaitBox);
        }

        TextBox[] resultats = new TextBox[2];
        resultats[0] = victoryBox;
        resultats[1] = defaitBox;
        return resultats;
    }


    private Line drawLine(double beginX, double beginY, double endX, double endY) {
        Line line = new Line(beginX, beginY, endX, endY);
        line.setStrokeWidth(0.5);
        line.setStroke(Color.BLACK);

        return line;
    }

    private TextBox createTextBox(ParticipantBean participantBean, double beginX, double beginY, Color color) {
        TextBox textBox = new TextBox(participantBean, widthRectangle, heightRectangle, color);
        textBox.setLayoutX(beginX);
        textBox.setLayoutY(beginY);
        return textBox;
    }

    public void setParticipantClassementFinalListener(ParticipantClassementFinalListener participantClassementFinalListener) {
        this.participantClassementFinalListener = participantClassementFinalListener;
    }
}
