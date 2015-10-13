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
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.component.grid.bean.Phase;
import fr.csmb.competition.component.textbox.TextBox;
import fr.csmb.competition.component.textbox.TextBoxListner;
import fr.csmb.competition.model.comparator.ComparatorParticipantPlaceOnGrid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class GridComponentFight2 extends GridComponent {
    int spaceBetweenJoueur = 20;
    int spaceBetweenMatch = 50;
    int widthRectangle = 150;
    int heightRectangle = 30;
    Color colorBlue = Color.rgb(81, 136, 245, 0.5);
    Color colorRed = Color.rgb(255, 255, 102, 0.5);

    private SortedList<ParticipantBean> sortedJoueurs;
    private ObservableList<ParticipantBean> joueurs = FXCollections.observableArrayList();

    private TextBox[] resultats = null;
    private ParticipantClassementFinalListener participantClassementFinalListener;

    public GridComponentFight2(List<ParticipantBean> joueurList) {
        this.joueurs.addAll(joueurList);
        resultatsList = new ArrayList<ParticipantBean>();
    }

    public List<ParticipantBean> getResultatsList() {
        resultatsList.clear();
        for (TextBox textBox : resultats) {
            resultatsList.add(textBox.getParticipant());
        }
        return resultatsList;
    }

    public List<ParticipantBean> getListParticipant() {
        return this.sortedJoueurs;
    }

    public void addParticipant(ParticipantBean participantBean) {
        ObservableList newList = FXCollections.observableArrayList(this.joueurs);
        this.joueurs = newList;
        this.joueurs.add(participantBean);
        this.getChildren().clear();
    }

    public void drawGrid() {
        drawGrid(false);
    }

    public void drawGrid(boolean forConfigure) {
        Group group = this;
        resultats = drawShape(group, forConfigure);
        if (forConfigure) {

            for (TextBox textBox : resultats) {
                textBox.setDragable();
                textBox.setClickable(false);
            }
        }
    }

    /**
     * Etablish match list and draw match table
     *
     * @param group
     * @return
     */
    private TextBox[] drawShape(Group group, boolean forConfigure) {
        this.sortedJoueurs = new SortedList(joueurs);
        this.sortedJoueurs.setComparator(new ComparatorParticipantPlaceOnGrid());
        this.sortedJoueurs.sort();
        int nbJoueur = sortedJoueurs.size();
        TextBox[] result = null;
        if (nbJoueur > 4) {
            result = computeSeveralRound(nbJoueur, forConfigure);
            if (forConfigure) {
                return result;
            }
            return new TextBox[]{result[0], result[1], result[0], result[1]};
        } else {
            if (nbJoueur == 3) {
                return computeFor3Fighters(nbJoueur, forConfigure);
            } else if(nbJoueur == 4) {
                return computeFor4Fighter(nbJoueur, forConfigure);
            } else {
                return computeFor2Fighters(forConfigure);
            }
        }
    }

    /**
     * Compute for 2 fighters
     * @param forConfigure
     * @return
     */
    private TextBox[] computeFor2Fighters(boolean forConfigure) {
        int placeOfJoueur = 0;
        int i = 0;
        Match match = new Match();
        match.setJoueur1(sortedJoueurs.get(placeOfJoueur));
        placeOfJoueur++;
        match.setJoueur2(sortedJoueurs.get(placeOfJoueur));
        placeOfJoueur++;

        TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1);
        textBoxForMatch[0].setNumPlace(1);
        textBoxForMatch[1].setNumPlace(2);
        if (forConfigure) {
            return new TextBox[]{textBoxForMatch[0], textBoxForMatch[1]};
        }
        TextBox[] resultatsFinale = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, 2, Phase.FINALE);
        return new TextBox[]{resultatsFinale[0], resultatsFinale[1]};
    }

    /**
     * Compute for 3 fighters
     * @param nbJoueur
     * @param forConfigure
     * @return
     */
    private TextBox[] computeFor3Fighters(int nbJoueur, boolean forConfigure) {

        int nbMatchMaxi = 4/2;
        TextBox[] result = buildFirstRound(nbMatchMaxi, nbJoueur);

        TextBox[] firstMatchOfDemiFinale = new TextBox[]{result[0], result[1]};
        TextBox[] secondMatchOfDemiFinale = new TextBox[]{result[2], result[3]};

        TextBox[] textBoxesDemiFinale = buildDemiFinale(firstMatchOfDemiFinale, secondMatchOfDemiFinale, false, 5);

        if (forConfigure) {
            return new TextBox[]{result[0], result[1], textBoxesDemiFinale[2]};
        }
        TextBox[] resultatsFinale = drawMatch(textBoxesDemiFinale[0], textBoxesDemiFinale[2], this, 2, Phase.FINALE);
        TextBox[] resultatsPetiteFinale = drawMatch(textBoxesDemiFinale[1], textBoxesDemiFinale[3], this, 2, Phase.PETITE_FINALE);
        textBoxesDemiFinale[1].enableListForFight3Fighters(resultatsPetiteFinale[0]);

        return new TextBox[]{resultatsFinale[0], resultatsFinale[1], resultatsPetiteFinale[0], resultatsPetiteFinale[1]};
    }

    /**
     * Compute for 4 fighters
     * @param nbJoueur
     * @param forConfigure
     * @return
     */
    private TextBox[] computeFor4Fighter(int nbJoueur, boolean forConfigure) {
        int nbMatchMaxi = 4/2;
        TextBox[] result = buildFirstRound(nbMatchMaxi, nbJoueur);
        if (forConfigure) {
            return result;
        }

        TextBox[] firstMatchOfDemiFinale = new TextBox[]{result[0], result[1]};
        TextBox[] secondMatchOfDemiFinale = new TextBox[]{result[2], result[3]};

        TextBox[] textBoxesDemiFinale = buildDemiFinale(firstMatchOfDemiFinale, secondMatchOfDemiFinale, true, 5);

        TextBox[] resultatsFinale = drawMatch(textBoxesDemiFinale[0], textBoxesDemiFinale[2], this, 2, Phase.FINALE);
        TextBox[] resultatsPetiteFinale = drawMatch(textBoxesDemiFinale[1], textBoxesDemiFinale[3], this, 2, Phase.PETITE_FINALE);

        return new TextBox[]{resultatsFinale[0], resultatsFinale[1], resultatsPetiteFinale[0], resultatsPetiteFinale[1]};
    }

    /**
     * Compute for several fighters (>4)
     * @param nbJoueur
     * @param forConfigure
     * @return
     */
    private TextBox[] computeSeveralRound(int nbJoueur, boolean forConfigure) {

        TextBox[] resultForConfigure = new TextBox[nbJoueur];

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
        int indexForConfigure = 0;
        int i = 0;
        while (i < nbMatchBeforeMaxi) {

            Match match = new Match();
            match.setJoueur1(sortedJoueurs.get(placeOfJoueur));
            placeOfJoueur++;
            match.setJoueur2(sortedJoueurs.get(placeOfJoueur));
            placeOfJoueur++;

            TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1);
            textBoxForMatch[0].setNumPlace(placeOfJoueur - 1);
            textBoxForMatch[1].setNumPlace(placeOfJoueur);
            result[i] = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, i + 1);
            i++;
            resultForConfigure[indexForConfigure] = textBoxForMatch[0];
            indexForConfigure++;
            resultForConfigure[indexForConfigure] = textBoxForMatch[1];
            indexForConfigure++;
        }
        double rest = nbMatchBeforeMaxi % 2;
        if (rest > 0) {

            Match match = new Match();
            match.setJoueur1(new ParticipantBean());
            match.setJoueur2(new ParticipantBean());
            TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1, false);
            result[i] = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, i + 1, false);
            result[i].setParticipant(sortedJoueurs.get(placeOfJoueur));
            if (forConfigure) {
                result[i].setDragable();
            }
            placeOfJoueur++;
            result[i].setNumPlace(placeOfJoueur);
            i++;
            resultForConfigure[indexForConfigure] = result[i - 1];
            indexForConfigure++;
        }

        while (i < nbMatchMaxi) {
            Match match = new Match();
            match.setJoueur1(new ParticipantBean());
            match.setJoueur2(new ParticipantBean());
            TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1, false);
            result[i] = drawMatch(textBoxForMatch[0], textBoxForMatch[1], this, i + 1, false);
            result[i].setParticipant(sortedJoueurs.get(placeOfJoueur));
            placeOfJoueur++;
            result[i].setNumPlace(placeOfJoueur);
            i++;
            resultForConfigure[indexForConfigure] = result[i - 1];
            indexForConfigure++;
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

        TextBox[] resultatsDemi1 = drawMatch(result[0], result[1], this, 1, Phase.DEMI_FINALE);
        TextBox[] resultatsDemi2 =  drawMatch(result[2], result[3], this, 2, Phase.DEMI_FINALE);
        TextBox[] resultatsFinale = drawMatch(resultatsDemi1[0], resultatsDemi2[0], this, 2, Phase.FINALE);
        TextBox[] resultatsPetiteFinale = drawMatch(resultatsDemi1[1], resultatsDemi2[1], this, 2, Phase.PETITE_FINALE);

        if (forConfigure) {
            return resultForConfigure;
        }
        return new TextBox[]{resultatsFinale[0], resultatsFinale[1], resultatsPetiteFinale[0], resultatsPetiteFinale[1]};
    }

    /**
     * Build first round
     * @param nbMatchMaxi
     * @param nbJoueur
     * @return
     */
    private TextBox[] buildFirstRound(int nbMatchMaxi, int nbJoueur) {
        TextBox[] result = null;
        int placeOfJoueur = 0;
        int i = 0;
        int nbMatchBeforeMaxi = nbJoueur - nbMatchMaxi;
        int index = 0;
        result = new TextBox[4];
        while (i < nbMatchBeforeMaxi) {

            Match match = new Match();
            placeOfJoueur++;
            match.setJoueur1(this.getByNumPlace(placeOfJoueur));
            placeOfJoueur++;
            match.setJoueur2(this.getByNumPlace(placeOfJoueur));

            TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1);
            result[index] = textBoxForMatch[0];
            result[index].setNumPlace(placeOfJoueur - 1);
            index++;
            result[index] = textBoxForMatch[1];
            result[index].setNumPlace(placeOfJoueur);
            index++;
            i++;
        }

        double rest = nbMatchBeforeMaxi % 2;
        if (rest > 0) {

            Match match = new Match();
            match.setJoueur1(new ParticipantBean());
            match.setJoueur2(new ParticipantBean());
            TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1, false);
            result[index] = textBoxForMatch[0];
            index++;
            result[index] = textBoxForMatch[1];
            index++;
            i++;
        }

        return result;
    }

    /**
     * Build match for first round
     * @param beginX
     * @param beginY
     * @param group1
     * @param match
     * @param level
     * @return
     */
    private TextBox[] drawMatchFirstRound(int beginX, int beginY, Group group1, Match match, int level) {
        return drawMatchFirstRound(beginX, beginY, group1, match, level, true);
    }

    /**
     * Build match for first round and can specify if visible
     * @param beginX
     * @param beginY
     * @param group1
     * @param match
     * @param level
     * @param visible
     * @return
     */
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


        Color colorResultat = colorBlue;
        if (level % 2 == 0) {
            colorResultat = colorRed;
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

        Color colorResultat = colorBlue;
        if (level % 2 == 0) {
            colorResultat = colorRed;
        }

        Line lineBlueH = drawLine(beginXLineHBlue, beginYLineHBlue , endXLineHBlue, endYLineHBlue);
        Line lineBlueV = drawLine(beginXLineVBlue, beginYLineVBlue , endXLineVBlue, endYLineVBlue);

        Line lineRedH = drawLine(beginXLineHRed, beginYLineHRed , endXLineHRed, endYLineHRed);
        Line lineRedV = drawLine(beginXLineVRed, beginYLineVRed , endXLineVRed, endYLineVRed);

        TextBox victoryBox = createTextBox(new ParticipantBean("", ""), beginXResultat, beginYResultat, colorResultat);
        TextBox defaitBox = createTextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, colorResultat);

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
        return drawMatch(boxBlue, boxRed, group, level, phase, true);
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
    private TextBox[] drawMatch(TextBox boxBlue, TextBox boxRed, Group group, int level, Phase phase, boolean drawLine) {
        TextBox victoryBox = null;
        TextBox defaitBox = null;
        Color colorResultat = colorBlue;
        if (level % 2 == 0) {
            colorResultat = colorRed;
        }
        if (phase.ordinal() == Phase.FINALE.ordinal() || phase.ordinal() == Phase.PETITE_FINALE.ordinal()) {
            victoryBox = createTextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, Color.color(1.0, 1.0, 0.0, 0.5));
            defaitBox = createTextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, Color.color(0.0, 1.0, 1.0, 0.5));
            //Create a TextBoxListner to prepare resultat
            TextBoxListner listner = new TextBoxListner(boxBlue, boxRed, victoryBox, defaitBox, phase);
            boxBlue.setListner(listner);
            boxRed.setListner(listner);
            listner.setParticipantClassementFinalListener(participantClassementFinalListener);

            if (level % 2 == 0) {
                Label label;
                if (phase.ordinal() == Phase.FINALE.ordinal()) {
                    label= new Label("FINALE");
                } else {
                    label= new Label("PETITE FINALE");
                }

                //BoxBlue
                double beginXBlue = boxBlue.getLayoutX();
                double beginYBlue = boxBlue.getLayoutY();
                double endYBlue = beginYBlue + heightRectangle;
                //BoxRed
                double beginXRed = boxRed.getLayoutX();
                double beginYRed = boxRed.getLayoutY();

                double y = ((beginYRed - endYBlue) / 2) - (spaceBetweenJoueur/2) + endYBlue;
                label.setAlignment(Pos.CENTER);
                Font font = Font.font("Arial", FontWeight.BOLD, 16);
                label.setFont(font);
                label.setMinWidth(widthRectangle);
                label.setTextFill(Color.RED);

                label.setLayoutX(beginXBlue);
                label.setLayoutY(y);
                group.getChildren().add(label);
            }

        } else if (phase.ordinal() == Phase.DEMI_FINALE.ordinal()) {
            victoryBox = drawMatch(boxBlue, boxRed, group, level, drawLine);

            defaitBox = createTextBox(new ParticipantBean("", ""), widthRectangle, heightRectangle, colorResultat);

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

    public ParticipantBean getByNumPlace(int numPlace) {
        for (ParticipantBean participantBean : this.sortedJoueurs) {
            if (participantBean.getPlaceOnGrid().equals(numPlace)) {
                return participantBean;
            }
        }
        return new ParticipantBean("", "");
    }

    private TextBox[] buildDemiFinale(TextBox[] firstMatch, TextBox[] secondMatch, boolean drawLastLines, int firstPlace) {
        TextBox[] resultatsDemi1 = drawMatch(firstMatch[0], firstMatch[1], this, 1, Phase.DEMI_FINALE);
        resultatsDemi1[0].setNumPlace(firstPlace);
        resultatsDemi1[0].setParticipant(this.getByNumPlace(firstPlace));
        resultatsDemi1[1].setNumPlace(firstPlace+1);
        resultatsDemi1[1].setParticipant(this.getByNumPlace(firstPlace+1));

        TextBox[] resultatsDemi2 = drawMatch(secondMatch[0], secondMatch[1], this, 2, Phase.DEMI_FINALE, drawLastLines);
        resultatsDemi2[0].setNumPlace(firstPlace+2);
        resultatsDemi2[0].setParticipant(this.getByNumPlace(firstPlace+2));
        resultatsDemi2[1].setNumPlace(firstPlace+3);
        resultatsDemi2[1].setParticipant(this.getByNumPlace(firstPlace+3));
        return new TextBox[] {resultatsDemi1[0], resultatsDemi1[1], resultatsDemi2[0], resultatsDemi2[1]};
    }
}
