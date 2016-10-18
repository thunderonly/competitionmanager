/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.fight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.component.grid.bean.Match;
import fr.csmb.competition.component.grid.bean.Phase;
import fr.csmb.competition.component.textbox.TextBox;
import fr.csmb.competition.component.textbox.TextBoxListner;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.model.comparator.ComparatorParticipantPlaceOnGrid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
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

    public void delParticipant(ParticipantBean participantBean) {
        ObservableList newList = FXCollections.observableArrayList(this.joueurs);
        this.joueurs = newList;
        //Replace last fighter on last place
        List<ParticipantBean> participantsToForward = new ArrayList<ParticipantBean>();
        List<ParticipantBean> participantsToReward = new ArrayList<ParticipantBean>();
        int nbEmptyPlace = 0;
        int indexOfLastParticipant = 0;
        switch (this.joueurs.size()) {
            case 4:
                for (int i = 1; i <= this.joueurs.size(); i++) {
                    ParticipantBean participantBean1 = this.getByNumPlace(i);
                    participantsToForward.add(participantBean1);
                }
                indexOfLastParticipant = participantsToForward.size() - 1;
                if (participantsToForward.lastIndexOf(participantBean) == indexOfLastParticipant) {
                    participantsToForward.get(indexOfLastParticipant - 1).setPlaceOnGrid(
                            participantsToForward.get(indexOfLastParticipant).getPlaceOnGrid());
                } else if (participantsToForward.lastIndexOf(participantBean) == indexOfLastParticipant - 1) {
                    //nothing to do
                } else {
                    int indexOfParticipant = participantsToForward.indexOf(participantBean);
                    for (int i = indexOfParticipant + 1; i < indexOfLastParticipant; i++) {
                        participantsToForward.get(i).setPlaceOnGrid(
                                participantsToForward.get(i).getPlaceOnGrid() - 1);
                    }
                }
                break;
            case 8:
            case 16:
            case 32:
                nbEmptyPlace = this.joueurs.size() / 2;
                for (int i = 1; i <= this.joueurs.size(); i++) {
                    ParticipantBean participantBean1 = this.getByNumPlace(i);
                    participantsToForward.add(participantBean1);
                }
                indexOfLastParticipant = participantsToForward.size() - 1;
                if (participantsToForward.lastIndexOf(participantBean) == indexOfLastParticipant) {
                    participantsToForward.get(indexOfLastParticipant - 1).setPlaceOnGrid(
                            participantsToForward.get(indexOfLastParticipant).getPlaceOnGrid() + nbEmptyPlace - 2);
                } else if (participantsToForward.lastIndexOf(participantBean) == indexOfLastParticipant - 1) {
                    participantsToForward.get(indexOfLastParticipant).setPlaceOnGrid(
                            participantsToForward.get(indexOfLastParticipant).getPlaceOnGrid() + nbEmptyPlace - 2);
                } else {
                    participantsToForward.get(indexOfLastParticipant).setPlaceOnGrid(
                            participantsToForward.get(indexOfLastParticipant).getPlaceOnGrid() + nbEmptyPlace - 2);
                    int indexOfParticipant = participantsToForward.indexOf(participantBean);
                    for (int i = indexOfParticipant + 1; i < indexOfLastParticipant; i++) {
                        participantsToForward.get(i).setPlaceOnGrid(
                                participantsToForward.get(i).getPlaceOnGrid() - 1);
                    }
                }
                break;
            default:
                for (int i = 1; i < this.joueurs.size() * 2; i++) {
                    ParticipantBean participantBean1 = this.getByNumPlace(i);
                    if (participantBean1.getNom().equals("")) {
                        if (participantsToReward.isEmpty()) {
                            nbEmptyPlace++;
                        }
                    } else {
                        if (nbEmptyPlace != 0) {
                            participantsToReward.add(participantBean1);
                        } else  {
                            participantsToForward.add(participantBean1);
                        }
                    }
                }
                // if participant is in reward, -2 on all part in reward
                if (participantsToReward.contains(participantBean)) {
                    boolean afterPartDel = false;
                    for (ParticipantBean part : participantsToReward) {
                        if (part.equals(participantBean)) {
                            afterPartDel = true;
                        } else {
                            if (afterPartDel) {
                                part.setPlaceOnGrid(part.getPlaceOnGrid() - 2);
                            } else {
                                part.setPlaceOnGrid(part.getPlaceOnGrid() - 1);
                            }
                        }
                    }
                    //get 2 last part of forward;
                    ParticipantBean part1 = participantsToForward.get(participantsToForward.size()-1);
                    ParticipantBean part2 = participantsToForward.get(participantsToForward.size()-2);
                    part1.setPlaceOnGrid(part1.getPlaceOnGrid() + nbEmptyPlace - 1);
                    part2.setPlaceOnGrid(part2.getPlaceOnGrid() + nbEmptyPlace - 1);
                } else if (participantsToForward.contains(participantBean)) {
                    for (ParticipantBean partToReward : participantsToReward) {
                        partToReward.setPlaceOnGrid(partToReward.getPlaceOnGrid() - 2);
                    }

                    indexOfLastParticipant = participantsToForward.size() - 1;
                    if (participantsToForward.lastIndexOf(participantBean) == indexOfLastParticipant) {
                        participantsToForward.get(indexOfLastParticipant - 1).setPlaceOnGrid(
                                participantsToReward.get(0).getPlaceOnGrid() - 1);
                    } else if (participantsToForward.lastIndexOf(participantBean) == indexOfLastParticipant - 1) {
                        participantsToForward.get(indexOfLastParticipant).setPlaceOnGrid(
                                participantsToReward.get(0).getPlaceOnGrid() - 1);
                    } else {
                        participantsToForward.get(indexOfLastParticipant).setPlaceOnGrid(
                                participantsToReward.get(0).getPlaceOnGrid() - 1);
                        int indexOfParticipant = participantsToForward.indexOf(participantBean);
                        for (int i = indexOfParticipant + 1; i < indexOfLastParticipant; i++) {
                            participantsToForward.get(i).setPlaceOnGrid(
                                    participantsToForward.get(i).getPlaceOnGrid() - 1);
                        }
                    }
                }
                break;
        }
        this.joueurs.remove(participantBean);
        this.getChildren().clear();

    }

    public void addParticipant(ParticipantBean participantBean) {
        ObservableList newList = FXCollections.observableArrayList(this.joueurs);
        this.joueurs = newList;
        //Replace last fighter on last place
        List<ParticipantBean> participantsToForward = new ArrayList<ParticipantBean>();
        List<ParticipantBean> participantsToReward = new ArrayList<ParticipantBean>();
        switch (this.joueurs.size()) {
            case 3:
                this.getByNumPlace(4).setPlaceOnGrid(3);
                break;
            case 4:
            case 8:
            case 16:
            case 32:
                for (int i=3; i <= this.joueurs.size(); i++) {
                    ParticipantBean participantBean1 = getByNumPlace(i);
                    participantsToForward.add(participantBean1);
                }
                for (ParticipantBean participantToForward : participantsToForward) {
                    participantToForward.setPlaceOnGrid(participantToForward.getPlaceOnGrid()+1);
                }
                break;
            default:
                int nbEmptyPlace = 0;
                int nbReviewPlace = 2;
                for (int i=1; i < this.joueurs.size()*2; i++) {
                    ParticipantBean participantBean1 = getByNumPlace(i);
                    if (participantBean1.getNom().equals("") && participantsToReward.size() == 0) {
                        nbEmptyPlace++;
                    } else {
                        //manage participant to reward place.
                        //If nbEmptyPlace > 0, then all empty place are past;
                        // else if list of participant to reward place is not empty, then all participant to reward are past
                        if (nbEmptyPlace > 0 && nbReviewPlace > 0) {
                            participantsToReward.add(participantBean1);
                            nbReviewPlace--;
                        } else if (participantsToReward.size() > 0) {
                            participantsToForward.add(participantBean1);
                        }
                    }
                }
                for (ParticipantBean participantToReward : participantsToReward) {
                    participantToReward.setPlaceOnGrid(participantToReward.getPlaceOnGrid()-nbEmptyPlace);
                }
                for (ParticipantBean participantToForward : participantsToForward) {
                    participantToForward.setPlaceOnGrid(participantToForward.getPlaceOnGrid()+1);
                }

                break;
        }


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
        this.sortedJoueurs.sort(new ComparatorParticipantPlaceOnGrid());
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

        TextBox[] textBoxesDemiFinale = buildDemiFinale(firstMatchOfDemiFinale, secondMatchOfDemiFinale, false, 3);

        TextBox[] resultatsFinale = drawMatch(textBoxesDemiFinale[0], textBoxesDemiFinale[2], this, 2, Phase.FINALE);
        TextBox[] resultatsPetiteFinale = drawMatch(textBoxesDemiFinale[1], textBoxesDemiFinale[3], this, 2, Phase.PETITE_FINALE);
        textBoxesDemiFinale[1].enableListForFight3Fighters(resultatsPetiteFinale[0]);

        if (forConfigure) {
            return new TextBox[]{result[0], result[1], textBoxesDemiFinale[2]};
        }
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

        TextBox[] firstMatchOfDemiFinale = new TextBox[]{result[0], result[1]};
        TextBox[] secondMatchOfDemiFinale = new TextBox[]{result[2], result[3]};
        TextBox[] textBoxesDemiFinale = buildDemiFinale(firstMatchOfDemiFinale, secondMatchOfDemiFinale, true, 5);

        TextBox[] resultatsFinale = drawMatch(textBoxesDemiFinale[0], textBoxesDemiFinale[2], this, 2, Phase.FINALE);
        TextBox[] resultatsPetiteFinale = drawMatch(textBoxesDemiFinale[1], textBoxesDemiFinale[3], this, 2, Phase.PETITE_FINALE);
        if (forConfigure) {
            return result;
        }
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

        TextBox[] result = buildFirstRound(nbMatchMaxi, nbJoueur);
        TextBox[] resultOtherRound = Arrays.copyOf(result, result.length/2);
        int i = 0;
        int nbMatchBuilt = 0;
        int nbMatchBeforeMaxi = nbJoueur - nbMatchMaxi;
        int placeOfJoueur = nbMatchBeforeMaxi * 2 + 1;
        System.arraycopy(result, 0, resultForConfigure, 0, nbMatchBeforeMaxi*2);
        int indexForConfigure = nbMatchBeforeMaxi * 2;

        while (nbMatchBuilt < nbMatchMaxi) {
            TextBox firstPart = result[i];
            i++;
            TextBox secondPart = result[i];
            i++;
            boolean drawLine = false;
            if (nbMatchBuilt < nbMatchBeforeMaxi) {
                drawLine = true;
            }
            resultOtherRound[nbMatchBuilt] = drawMatch(firstPart, secondPart, this, nbMatchBuilt, drawLine);
            resultOtherRound[nbMatchBuilt].setNumPlace(placeOfJoueur);
            resultOtherRound[nbMatchBuilt].setParticipant(this.getByNumPlace(placeOfJoueur));

            //For match after firstRound
            if (nbMatchBuilt >= nbMatchBeforeMaxi) {
                resultOtherRound[nbMatchBuilt].setDragable();
                resultForConfigure[indexForConfigure] = resultOtherRound[nbMatchBuilt];
                indexForConfigure++;
            }
            nbMatchBuilt++;
            placeOfJoueur++;
        }

        while (nbMatchBuilt > 4) {
            TextBox[] resultCurrentRound = new TextBox[nbMatchBuilt/2];
            int index = 0;
            int indexLastRound = 0;
            while (index < nbMatchBuilt/2) {
                TextBox blue = resultOtherRound[indexLastRound];
                indexLastRound++;
                TextBox red = resultOtherRound[indexLastRound];
                indexLastRound++;
                resultCurrentRound[index] = drawMatch(blue, red, this, index + 1);
                resultCurrentRound[index].setNumPlace(placeOfJoueur);
                resultCurrentRound[index].setParticipant(this.getByNumPlace(placeOfJoueur));
                placeOfJoueur++;
                index++;
            }
            resultOtherRound = resultCurrentRound;
            nbMatchBuilt = resultOtherRound.length;
        }

        TextBox[] firstMatchOfDemiFinale = new TextBox[]{resultOtherRound[0], resultOtherRound[1]};
        TextBox[] secondMatchOfDemiFinale = new TextBox[]{resultOtherRound[2], resultOtherRound[3]};
        TextBox[] textBoxesDemiFinale = buildDemiFinale(firstMatchOfDemiFinale, secondMatchOfDemiFinale, true, placeOfJoueur);

        TextBox[] resultatsFinale = drawMatch(textBoxesDemiFinale[0], textBoxesDemiFinale[2], this, 2, Phase.FINALE);
        TextBox[] resultatsPetiteFinale = drawMatch(textBoxesDemiFinale[1], textBoxesDemiFinale[3], this, 2, Phase.PETITE_FINALE);

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
        result = new TextBox[nbMatchMaxi*2];
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
//        if (rest > 0) {
            while (nbMatchBeforeMaxi < nbMatchMaxi) {
                Match match = new Match();
                match.setJoueur1(new ParticipantBean());
                match.setJoueur2(new ParticipantBean());
                TextBox[] textBoxForMatch = drawMatchFirstRound(10, 10, this, match, i + 1, false);
                result[index] = textBoxForMatch[0];
                index++;
                result[index] = textBoxForMatch[1];
                index++;
                i++;
                nbMatchBeforeMaxi++;
            }
//        }

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
        resultatsDemi1[1].setNumPlace(firstPlace+2);
        resultatsDemi1[1].setParticipant(this.getByNumPlace(firstPlace+2));

        TextBox[] resultatsDemi2 = drawMatch(secondMatch[0], secondMatch[1], this, 2, Phase.DEMI_FINALE, drawLastLines);
        resultatsDemi2[0].setNumPlace(firstPlace+1);
        resultatsDemi2[0].setParticipant(this.getByNumPlace(firstPlace+1));
        resultatsDemi2[1].setNumPlace(firstPlace+3);
        resultatsDemi2[1].setParticipant(this.getByNumPlace(firstPlace+3));
        return new TextBox[] {resultatsDemi1[0], resultatsDemi1[1], resultatsDemi2[0], resultatsDemi2[1]};
    }
}
