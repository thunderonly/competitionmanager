/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.fight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.csmb.competition.component.grid.GridComponent;
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
public class GridComponentFight extends GridComponent {

    int spaceBetweenJoueur = 20;
    int spaceBetweenMatch = 50;
    int widthRectangle = 150;
    int heightRectangle = 20;

    private List<ParticipantBean> joueurs;
    private TextBox[] resultats = null;

    public GridComponentFight(List<ParticipantBean> joueurs) {
        this.joueurs = joueurs;
        resultatsList = new ArrayList<ParticipantBean>();
        Group group = this;
        resultats = drawShape(group);
    }

    public List<ParticipantBean> getResultatsList() {
        resultatsList.clear();
        for (TextBox textBox : resultats) {
            resultatsList.add(textBox.getParticipant());
        }
        return resultatsList;
    }

    /**
     * Etablish match list and draw match table
     *
     * @param group
     * @return
     */
    private TextBox[] drawShape(Group group) {

        List<Match> matchs = new ArrayList<Match>();

        int nbJoueur = joueurs.size();
        int nbTourBeforeDemi = 0;
        if (nbJoueur > 4 && nbJoueur <= 8) {
            nbTourBeforeDemi = 1;
            if (nbJoueur < 8) {
                for (int i = nbJoueur; i < 8; i++) {
                    joueurs.add(new ParticipantBean("", ""));
                }
            }
        } else if (nbJoueur > 8 && nbJoueur <= 16) {
            nbTourBeforeDemi = 2;
            if (nbJoueur < 16) {
                for (int i = nbJoueur; i < 16; i++) {
                    joueurs.add(new ParticipantBean("", ""));
                }
            }
        } else if (nbJoueur > 16 && nbJoueur <= 32) {
            nbTourBeforeDemi = 3;
            if (nbJoueur < 32) {
                for (int i = nbJoueur; i < 32; i++) {
                    joueurs.add(new ParticipantBean("", ""));
                }
            }
        }
        nbJoueur = joueurs.size();
        Iterator<ParticipantBean> iterator = joueurs.iterator();
        while (iterator.hasNext()) {
            ParticipantBean joueur = iterator.next();
            Match match = new Match();
            match.setJoueur1(joueur);
            if (iterator.hasNext()) {
                ParticipantBean joueur2 = iterator.next();
                match.setJoueur2(joueur2);
            }
            matchs.add(match);
        }


        TextBox[] matchFirstStep = null;
        while(nbTourBeforeDemi > 0) {
            if (matchFirstStep == null) {
                matchFirstStep = computeFirstRound(nbJoueur, matchs, group);
            } else {
                matchFirstStep = computeOtherRound(matchFirstStep, group);
            }
            nbTourBeforeDemi --;
        }

        TextBox[] resultatsDemi1 = drawMatch(matchFirstStep[0], matchFirstStep[1], group, 1, Phase.DEMI_FINALE);
        TextBox[] resultatsDemi2 =  drawMatch(matchFirstStep[2], matchFirstStep[3], group, 2, Phase.DEMI_FINALE);
        TextBox[] resultatsFinale = drawMatch(resultatsDemi1[0], resultatsDemi2[0], group, 2, Phase.FINALE);
        TextBox[] resultatsPetiteFinale = drawMatch(resultatsDemi1[1], resultatsDemi2[1], group, 2, Phase.PETITE_FINALE);
        return new TextBox[]{resultatsFinale[0], resultatsFinale[1], resultatsPetiteFinale[0], resultatsPetiteFinale[1]};

    }

    private TextBox[] computeFirstRound(int nbJoueur, List<Match> matchs, Group group) {
        Double nbMatch1Step =  Math.ceil(nbJoueur / 2);
        TextBox[] matchFirstStep = new TextBox[nbMatch1Step.intValue()];
        int i = 1;
        int nbMatch = 0;
        for (Match match : matchs) {
            TextBox resultatMatch = drawMatch(10, 10, group, match, i);
            matchFirstStep[nbMatch] = resultatMatch;
            nbMatch ++;
            i ++;
        }
        return matchFirstStep;
    }

    private TextBox[] computeOtherRound(TextBox[] matchFirstStep, Group group) {
        Double nbMatchOtherStep =  Math.ceil(matchFirstStep.length / 2);
        TextBox[] matchOtherStep = new TextBox[nbMatchOtherStep.intValue()];
        int level = 1;
        int nbMatch = 0;
        for (int index = 0; index < matchFirstStep.length; index ++) {
            TextBox boxBlue = matchFirstStep[index];
            index++;
            TextBox boxRed = matchFirstStep[index];
            TextBox resultatMatch = drawMatch(boxBlue, boxRed, group, level);
            matchOtherStep[nbMatch] = resultatMatch;
            nbMatch ++;
            level ++;
        }
        return matchOtherStep;
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
        TextBox resultat = null;
        TextBox resultatFail = null;
        Color colorResultat = Color.color(1.0, 1.0, 0.0, 0.5);
        if (level % 2 == 0) {
            colorResultat = Color.color(0.0, 1.0, 1.0, 0.5);
        }
        if (phase.ordinal() == Phase.FINALE.ordinal() || phase.ordinal() == Phase.PETITE_FINALE.ordinal()) {
            resultat = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, Color.color(1.0, 1.0, 0.0, 0.5), null);
            resultatFail = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, Color.color(0.0, 1.0, 1.0, 0.5), null);

            //Create a TextBoxListner to prepare resultat
            TextBoxListner listner = new TextBoxListner(boxBlue, boxRed, resultat, resultatFail, phase);
            boxBlue.setListner(listner);
            boxRed.setListner(listner);

            if (phase.ordinal() == Phase.PETITE_FINALE.ordinal()) {
                resultat.getText().setText("3eme Place");
//                resultat.getParticipant().setClassementFinal(3);
                resultatFail.getText().setText("4eme Place");
//                resultatFail.getParticipant().setClassementFinal(4);
                resultat.setLayoutX(boxBlue.getLayoutX() + widthRectangle + spaceBetweenMatch);
                resultat.setLayoutY(10 + heightRectangle * 2);
                resultatFail.setLayoutX(boxBlue.getLayoutX() + widthRectangle + spaceBetweenMatch);
                resultatFail.setLayoutY(10 + heightRectangle * 3);
            } else {
                resultat.getText().setText("1ere Place");
//                resultat.getParticipant().setClassementFinal(1);
                resultatFail.getText().setText("2eme Place");
//                resultatFail.getParticipant().setClassementFinal(2);
                resultat.setLayoutX(boxBlue.getLayoutX() + widthRectangle + spaceBetweenMatch + widthRectangle + spaceBetweenMatch);
                resultat.setLayoutY(10);
                resultatFail.setLayoutX(boxBlue.getLayoutX() + widthRectangle + spaceBetweenMatch + widthRectangle + spaceBetweenMatch);
                resultatFail.setLayoutY(10 + heightRectangle);
            }

            group.getChildren().addAll(resultat, resultatFail);

        } else if (phase.ordinal() == Phase.DEMI_FINALE.ordinal()) {
            resultat = drawMatch(boxBlue, boxRed, group, level);
            resultatFail = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, colorResultat, null);

            //Create a TextBoxListner to prepare resultat
            TextBoxListner listner = new TextBoxListner(boxBlue, boxRed, resultat, resultatFail);
            boxBlue.setListner(listner);
            boxRed.setListner(listner);
            resultatFail.setLayoutX(resultat.getLayoutX() + widthRectangle + spaceBetweenMatch);
            resultatFail.setLayoutY(resultat.getLayoutY());
            group.getChildren().add(resultatFail);
        }

        TextBox[] resultats = new TextBox[2];
        resultats[0] = resultat;
        resultats[1] = resultatFail;
        return resultats;
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

        double beginXBlue = boxBlue.getLayoutX();
        double beginYBlue = boxBlue.getLayoutY();
        double beginXRed = boxRed.getLayoutX();
        double beginYRed = boxRed.getLayoutY();

        double beginXLineBlue = beginXBlue + widthRectangle;
        double beginYLineBlue = beginYBlue + heightRectangle;
        double endXLineBlue = beginXLineBlue + spaceBetweenMatch;
        double endYLineBlue = beginYBlue + heightRectangle;
        double beginXLineRed = beginXRed + widthRectangle;
        double beginYLineRed = beginYRed;
        double endXLineRed = beginXLineRed + spaceBetweenMatch;
        double endYLineRed = beginYRed;

        double beginXResultat = endXLineBlue;
        double beginYResultat = ((endYLineRed - endYLineBlue) / 2) + endYLineBlue - (heightRectangle / 2);

        Line lineBlueH = new Line(beginXLineBlue, beginYLineBlue , endXLineBlue, endYLineBlue);
        lineBlueH.setStrokeWidth(0.5);
        lineBlueH.setStroke(Color.BLACK);
        Line lineBlueV = new Line(endXLineBlue, endYLineBlue , endXLineBlue, beginYResultat);
        lineBlueV.setStrokeWidth(0.5);
        lineBlueV.setStroke(Color.BLACK);

        Line lineRedH = new Line(beginXLineRed, beginYLineRed , endXLineRed, endYLineRed);
        lineRedH.setStrokeWidth(0.5);
        lineRedH.setStroke(Color.BLACK);
        Line lineRedV = new Line(endXLineRed, endYLineRed , endXLineRed, beginYResultat + heightRectangle);
        lineRedV.setStrokeWidth(0.5);
        lineRedV.setStroke(Color.BLACK);

        Color colorResultat = Color.color(1.0, 1.0, 0.0, 0.5);
        if (level % 2 == 0) {
            colorResultat = Color.color(0.0, 1.0, 1.0, 0.5);
        }
        TextBox resultat = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, colorResultat, null);
        //Set the resultat textBox to prepare click event on red and blue box
        boxBlue.setResultatBox(resultat);
        boxRed.setResultatBox(resultat);

        resultat.setLayoutX(beginXResultat);
        resultat.setLayoutY(beginYResultat);
        group.getChildren().addAll(resultat, lineBlueH, lineRedH, lineBlueV, lineRedV);



        return resultat;
    }

    /**
     * Draw grid for the 1st round
     * @param beginX
     * @param beginY
     * @param match
     * @param level
     * @return
     */
    private TextBox drawMatch(int beginX, int beginY, Group group1, Match match, int level) {

        int newBeginX = beginX;
        int newBeginY = beginY;

        if (level > 1) {
            int facteur = level - 1;
            newBeginY = beginY + (heightRectangle * 2) * facteur + (spaceBetweenJoueur * 2) * facteur;
        }

        int beginXBlue = newBeginX;
        int beginYBlue = newBeginY;
        int beginXLineBlue = beginXBlue + widthRectangle;
        int endXLineBlue = beginXBlue + widthRectangle + spaceBetweenMatch;
        int beginYLineBlue = beginYBlue + heightRectangle;

        int beginXRed = beginXBlue;
        int beginYRed = beginYBlue + heightRectangle + spaceBetweenJoueur;
        int beginXLineRed = beginXRed + widthRectangle;
        int endXLineRed = beginXRed + widthRectangle + spaceBetweenMatch;

        int beginXResultat = endXLineBlue;
        int beginYResultat = beginYBlue + heightRectangle;


        Color colorResultat = Color.color(1.0, 1.0, 0.0, 0.5);
        if (level % 2 == 0) {
            colorResultat = Color.color(0.0, 1.0, 1.0, 0.5);
        }
        TextBox resultat = new TextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, colorResultat, null);

        TextBox blue = new TextBox(match.getJoueur1(), widthRectangle, heightRectangle, Color.color(1.0, 1.0, 0.0, 0.5), resultat);
        blue.setLayoutX(beginXBlue);
        blue.setLayoutY(beginYBlue);
        Line lineBlue = new Line(beginXLineBlue, beginYLineBlue , endXLineBlue, beginYBlue + heightRectangle);
        lineBlue.setStrokeWidth(0.5);
        lineBlue.setStroke(Color.BLACK);

        TextBox red = new TextBox(match.getJoueur2(), widthRectangle, heightRectangle, Color.color(0.0, 1.0, 1.0, 0.5), resultat);
        red.setLayoutX(beginXRed);
        red.setLayoutY(beginYRed);
        Line lineRed = new Line(beginXLineRed, beginYRed, endXLineRed, beginYRed);
        lineRed.setStrokeWidth(0.5);
        lineRed.setStroke(Color.BLACK);

        resultat.setLayoutX(beginXResultat);
        resultat.setLayoutY(beginYResultat);

        group1.getChildren().addAll(blue, red, resultat, lineBlue, lineRed);
        match.setResultat(resultat);
        return resultat;
    }


    /**
     * Represent match between 2 participants
     */


    /**
     * Fire event on 4 TextBox.
     * Update text of boxVictory with source TextBox and text of boxFail with the other linked box.
     * This listner is use for Demi Final, Little Final and Final round
     */

}