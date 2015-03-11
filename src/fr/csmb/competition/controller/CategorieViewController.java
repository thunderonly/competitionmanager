/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.controller;

import java.io.File;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.Main;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.model.*;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.CategoriesView;
import fr.csmb.competition.view.ConfigureFightView;
import fr.csmb.competition.xml.model.Competition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class CategorieViewController {

    private CompetitionBean competitionBean;
    private NetworkSender sender;
    private Stage currentStage;
    private File fileTmp;

    public CategorieViewController (final CompetitionBean competitionBean, final Stage currentStage) {
        this.competitionBean = competitionBean;
        this.currentStage = currentStage;
        Preferences pref = Preferences.userNodeForPackage(Main.class);
        String fileName = pref.get("filePath", null);
        fileTmp = new File(fileName);
        this.sender = NetworkSender.getINSTANCE();
    }

    public int validateEpreuve(String typeCategorie, String categorie, String epreuve) {
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
        if (categorieBean != null && disciplineBean != null) {
            EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);

            if (epreuveBean != null) {
                if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                    return 1;
                } else if (EtatEpreuve.FUSION.getValue().equals(epreuveBean.getEtat())) {
                    return 2;
                } else if (EtatEpreuve.VALIDE.getValue().equals(epreuveBean.getEtat())) {
                    return 3;
                } else if (EtatEpreuve.DEMARRE.getValue().equals(epreuveBean.getEtat())) {
                    return 4;
                } else {
//                    epreuveBean.getParticipants().addAll(extractParticipants(typeCategorie, categorie, epreuve));
                    if (epreuveBean.getDiscipline().getType().equals(TypeEpreuve.COMBAT.getValue())) {
                        //Configure order of fighter
                        ConfigureFightView configureFightView = new ConfigureFightView();
                        configureFightView.showView(currentStage, competitionBean, epreuveBean);
//                        if (!epreuveBean.getEtat().equals(EtatEpreuve.VALIDE.getValue())) {
//                            epreuveBean.getParticipants().clear();
//                        }
                    } else {
                        epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
                    }

                    saveCompetitionToXmlFileTmp();
                    sender.send(competitionBean, epreuveBean);
                    return 0;
                }
            }
        }
        return -1;
    }

    public int invalidateEpreuve(String typeCategorie, String categorie, String epreuve) {
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
        if (categorieBean != null && disciplineBean != null) {
            EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
            if (epreuveBean != null) {
                if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                    return 1;
                } else if (EtatEpreuve.DEMARRE.getValue().equals(epreuveBean.getEtat())) {
                    return 2;
                } else {
//                    epreuveBean.getParticipants().clear();
                    epreuveBean.setEtat("");

                    saveCompetitionToXmlFileTmp();
                    sender.send(competitionBean, epreuveBean);
                    return 0;
                }
            }
        }
        return -1;
    }

    public int deleteEpreuve(TreeView<String> treeView, String typeCategorie, String categorie, String epreuve) {
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
        if (categorieBean != null && disciplineBean != null) {
            EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
            if (epreuveBean != null) {
                if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                    return 1;
                } else if (EtatEpreuve.DEMARRE.getValue().equals(epreuveBean.getEtat())) {
                    return 2;
                } else {
                    for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean)) {
                        competitionBean.getParticipants().remove(participantBean);
                    }

                    epreuveBean.setEtat(EtatEpreuve.SUPPRIME.getValue());
                    competitionBean.getEpreuves().remove(epreuveBean);

                    TreeItem<String> itemTypeCategorieToRemove = null;
                    for (TreeItem<String> itemTypeCategorie : treeView.getRoot().getChildren()) {
                        if (itemTypeCategorie.getValue().equals(categorieBean.getType())) {
                            TreeItem<String> itemCategorieToRemove = null;
                            for (TreeItem<String> itemCategorie : itemTypeCategorie.getChildren()) {
                                if (itemCategorie.getValue().equals(categorieBean.getNom())) {
                                    TreeItem<String> itemTypeEpreuveToRemove = null;
                                    for (TreeItem<String> itemTypeEpreuve : itemCategorie.getChildren()) {
                                        if (itemTypeEpreuve.getValue().equals(disciplineBean.getType())) {
                                            TreeItem<String> itemEpreuveToRemove = null;
                                            for (TreeItem<String> treeItemEpreuve : itemTypeEpreuve.getChildren()) {
                                                if (treeItemEpreuve.getValue().equals(disciplineBean.getNom())) {
                                                    itemEpreuveToRemove = treeItemEpreuve;
                                                }
                                            }
                                            if (itemEpreuveToRemove != null) {
                                                itemTypeEpreuve.getChildren().remove(itemEpreuveToRemove);
                                                if (itemTypeEpreuve.getChildren().isEmpty()) {
                                                    itemTypeEpreuveToRemove = itemTypeEpreuve;
                                                }
                                            }
                                        }
                                    }
                                    if (itemTypeEpreuveToRemove != null) {
                                        itemCategorie.getChildren().remove(itemTypeEpreuveToRemove);
                                        if (itemCategorie.getChildren().isEmpty()) {
                                            itemCategorieToRemove = itemCategorie;
                                        }
                                    }
                                }
                            }
                            if (itemCategorieToRemove != null) {
                                itemTypeCategorie.getChildren().remove(itemCategorieToRemove);
                                if (itemTypeCategorie.getChildren().isEmpty()) {
                                    itemTypeCategorieToRemove = itemTypeCategorie;
                                }
                            }
                        }
                    }
                    if (itemTypeCategorieToRemove != null) {
                        treeView.getRoot().getChildren().remove(itemTypeCategorieToRemove);
                    }

                    saveCompetitionToXmlFileTmp();
                    sender.send(competitionBean, epreuveBean);
                    return 0;
                }
            }
        }
        return -1;
    }

    public int fusionEpreuve(TreeView<String> treeView, String typeCategorie1, String nomCategorie1, String nomDiscipline1,
                             String typeCategorie2, String nomCategorie2, String nomDiscipline2) {

        EpreuveBean epreuveBean1 = getEpreuveBean(typeCategorie1, nomCategorie1, nomDiscipline1);
        EpreuveBean epreuveBean2 = getEpreuveBean(typeCategorie2, nomCategorie2, nomDiscipline2);
        if (EtatEpreuve.FUSION.getValue().equals(epreuveBean1.getEtat()) ||
                EtatEpreuve.FUSION.getValue().equals(epreuveBean2.getEtat())) {
            return 1;
        } else if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean1.getEtat()) ||
                EtatEpreuve.TERMINE.getValue().equals(epreuveBean2.getEtat())) {
            return 2;
        } else if (EtatEpreuve.VALIDE.getValue().equals(epreuveBean1.getEtat()) ||
                EtatEpreuve.VALIDE.getValue().equals(epreuveBean2.getEtat())) {
            return 3;
        } else if (EtatEpreuve.DEMARRE.getValue().equals(epreuveBean1.getEtat()) ||
                EtatEpreuve.DEMARRE.getValue().equals(epreuveBean2.getEtat())) {
            return 4;
        } else {
            CategorieBean categorieBean1 = competitionBean.getCategorie(typeCategorie1, nomCategorie1);
            CategorieBean categorieBean2 = competitionBean.getCategorie(typeCategorie2, nomCategorie2);
            DisciplineBean disciplineBean1 = competitionBean.getDiscipline(nomDiscipline1);
            DisciplineBean disciplineBean2 = competitionBean.getDiscipline(nomDiscipline2);

            CategorieBean categorieFusion = null;
            if (categorieBean1 != null && categorieBean2 != null) {
                if (categorieBean1.equals(categorieBean2)) {
                    categorieFusion = categorieBean1;
                } else {
                    String newCategorieName = categorieBean1.getNom().concat("-").concat(categorieBean2.getNom());
                    String newTypeCategorie = TypeCategorie.MIXTE.getValue();
                    if (categorieBean1.getNom().equals(categorieBean2.getNom())) {
                        newCategorieName = categorieBean1.getNom();
                    }
                    if (categorieBean1.getType().equals(categorieBean2.getType())) {
                        newTypeCategorie = categorieBean1.getType();
                    }
                    //Create or uupdate current categorie
                    categorieFusion = competitionBean.getCategorie(newTypeCategorie, newCategorieName);
                    if (categorieFusion == null) {
                        categorieFusion = new CategorieBean(newCategorieName);
                        categorieFusion.setType(newTypeCategorie);
                        competitionBean.getCategories().add(categorieFusion);
                    }
                }
            }

            DisciplineBean disciplineFusion = null;
            if (disciplineBean1 != null && disciplineBean2 != null) {
                if (disciplineBean1.equals(disciplineBean2)) {
                    disciplineFusion = disciplineBean1;
                } else {
                    String newDisciplineName = disciplineBean1.getNom().concat("-").concat(disciplineBean2.getNom());
                    if (disciplineBean1.getNom().equals(disciplineBean2.getNom())) {
                        newDisciplineName = disciplineBean1.getNom();
                    }

                    disciplineFusion = competitionBean.getDiscipline(newDisciplineName);
                    if (disciplineFusion == null) {
                        disciplineFusion = new DisciplineBean(newDisciplineName, disciplineBean1.getType());
                        competitionBean.getDisciplines().add(disciplineFusion);
                    }
                }
            }

            EpreuveBean epreuveFusion = new EpreuveBean();
            epreuveFusion.setCategorie(categorieFusion);
            epreuveFusion.setDiscipline(disciplineFusion);
            epreuveFusion.setDetailEpreuve(new DetailEpreuveBean());
            epreuveFusion.setId(categorieFusion.getNom().concat("-").concat(categorieFusion.getType()).concat(
                    disciplineFusion.getType()).concat("-").concat(disciplineFusion.getNom()));
            epreuveBean1.setEtat(EtatEpreuve.FUSION.getValue());
            epreuveBean2.setEtat(EtatEpreuve.FUSION.getValue());
            epreuveFusion.setEtat(EtatEpreuve.REGROUPE.getValue());
            competitionBean.getEpreuves().add(epreuveFusion);

            for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean1)) {
                participantBean.setEpreuveBean(epreuveFusion);
            }

            for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean2)) {
                participantBean.setEpreuveBean(epreuveFusion);
            }


            saveCompetitionToXmlFileTmp();
//            sender.send(competitionBean, epreuveBean);

            ImageView imageMixte = new ImageView(new Image(CategoriesView.class.getResourceAsStream("images/mixte.png")));
            imageMixte.setFitHeight(25);
            imageMixte.setFitWidth(25);
            TreeItem<String> treeItemTypeCategorie = new TreeItem(categorieFusion.getType());
            TreeItem<String> treeItemCategorie = new TreeItem(categorieFusion.getNom());
            ImageView imageTypeEpreve = null;
            if (epreuveFusion.getDiscipline().getType().equals(TypeEpreuve.COMBAT.getValue())) {
                imageTypeEpreve = new ImageView(new Image(CategoriesView.class.getResourceAsStream("images/combat.png")));
            } else {
                imageTypeEpreve = new ImageView(new Image(CategoriesView.class.getResourceAsStream("images/technique.png")));
            }
            TreeItem<String> treeItemTypeEpreuve = new TreeItem(epreuveFusion.getDiscipline().getType());
            TreeItem<String> treeItemEpreuve = new TreeItem<String>(epreuveFusion.getDiscipline().getNom());
            treeItemTypeCategorie.getChildren().add(treeItemCategorie);
            treeItemCategorie.getChildren().add(treeItemTypeEpreuve);
            treeItemTypeEpreuve.getChildren().add(treeItemEpreuve);

            //get item correspond to categorie type
            boolean epreuveAdded = false;
            boolean categorieExist = false;
            boolean typeCategorieExist = false;
            boolean typeEpreuveExist = false;
            for (TreeItem<String> treeItem : treeView.getRoot().getChildren()) {
                if (treeItem.getValue().equals(categorieFusion.getType())) {
                    for (TreeItem<String> itemCategorie : treeItem.getChildren()) {
                        if (itemCategorie.getValue().equals(categorieFusion.getNom())) {
                            for (TreeItem<String> itemTypeEpreuve : itemCategorie.getChildren()) {
                                if (itemTypeEpreuve.getValue().equals(epreuveFusion.getDiscipline().getType())) {
                                    itemTypeEpreuve.getChildren().add(treeItemEpreuve);
                                    epreuveAdded = true;
                                    typeEpreuveExist = true;
                                }
                            }
                            if (!typeEpreuveExist) {
                                itemCategorie.getChildren().add(treeItemTypeEpreuve);
                            }
                            categorieExist = true;
                        }
                    }
                    if (!categorieExist) {
                        treeItem.getChildren().add(treeItemCategorie);
                    }
                    typeCategorieExist = true;
                }
            }

            //New categorie from fusion
            if (!typeCategorieExist) {
                treeView.getRoot().getChildren().add(treeItemTypeCategorie);
            }
        }
        return 0;
    }

    public ObservableList<ParticipantBean> extractParticipants(String typeCategorie, String categorie, String epreuve) {
        ObservableList<ParticipantBean> participantBeans1 = FXCollections.observableArrayList();
        for (ClubBean clubBean : competitionBean.getClubs()) {
            for (EleveBean eleveBean : clubBean.getEleves()) {
                if (categorie.equals(eleveBean.getCategorie()) && typeCategorie.equals(eleveBean.getSexe())) {
                    if (eleveBean.getEpreuves().contains(epreuve)) {
                        ParticipantBean participantBean = new ParticipantBean(eleveBean.getNom(), eleveBean.getPrenom());
                        participantBean.setClub(clubBean.getIdentifiant());
                        participantBean.setPoids(Integer.parseInt(eleveBean.getPoids()));
                        participantBeans1.add(participantBean);
                    }
                }
            }
        }

        return participantBeans1;
    }

    private void saveCompetitionToXmlFileTmp() {
        try {
            JAXBContext context = JAXBContext.newInstance(Competition.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Competition competition = CompetitionConverter.convertCompetitionBeanToCompetition(competitionBean);

            marshaller.marshal(competition, this.fileTmp);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private EpreuveBean getEpreuveBean(String typeCategorie, String categorie, String epreuve) {
        EpreuveBean epreuveBean = null;
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
        if (categorieBean != null && disciplineBean != null) {
            epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
        }
        return epreuveBean;
    }
}
