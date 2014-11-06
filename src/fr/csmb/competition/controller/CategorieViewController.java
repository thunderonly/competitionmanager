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
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.ConfigureFightView;
import fr.csmb.competition.view.NotificationView;
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
    private NetworkSender sender = new NetworkSender("", 9878);
    private Stage currentStage;
    private File fileTmp;

    public CategorieViewController (final CompetitionBean competitionBean, final Stage currentStage) {
        this.competitionBean = competitionBean;
        this.currentStage = currentStage;
        Preferences pref = Preferences.userNodeForPackage(Main.class);
        String fileName = pref.get("filePath", null);
        fileTmp = new File(fileName);
    }

    public int validateEpreuve(String typeCategorie, String categorie, String epreuve) {
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        if (categorieBean != null) {
            EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
            if (epreuveBean != null) {
                if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                    return 1;
                } else if (EtatEpreuve.FUSION.getValue().equals(epreuveBean.getEtat())) {
                    return 2;
                } else if (EtatEpreuve.VALIDE.getValue().equals(epreuveBean.getEtat())) {
                    return 3;
                } else {
                    epreuveBean.getParticipants().addAll(extractParticipants(typeCategorie, categorie, epreuve));
                    for (int i = 0; i < 8; i++) {
                        epreuveBean.getParticipants().add(new ParticipantBean("NewNom " + i, "NewPrénom " + i));
                    }
                    if (epreuveBean.getType().equals(TypeEpreuve.COMBAT.getValue())) {
                        //Configure order of fighter
                        ConfigureFightView configureFightView = new ConfigureFightView();
                        configureFightView.showView(currentStage, epreuveBean);
                        if (!epreuveBean.getEtat().equals(EtatEpreuve.VALIDE.getValue())) {
                            epreuveBean.getParticipants().clear();
                        }
                    } else {
                        epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
                    }

                    saveCompetitionToXmlFileTmp();
                    sender.send(competitionBean, categorieBean, epreuveBean);
                    return 0;
                }
            }
        }
        return -1;
    }

    public int fusionEpreuve(TreeView<String> treeView, String typeCategorie1, String categorie1, String epreuve1, String typeCategorie2, String categorie2, String epreuve2) {

        EpreuveBean epreuveBean1 = getEpreuveBean(typeCategorie1, categorie1, epreuve1);
        EpreuveBean epreuveBean2 = getEpreuveBean(typeCategorie2, categorie2, epreuve2);
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
            String newCategorie = categorie1.concat(" - ").concat(categorie2);
            if (categorie1.equals(categorie2)) {
                newCategorie = categorie1;
            }

            String newTypeCategorie = TypeCategorie.MIXTE.getValue();
            if (typeCategorie1.equals(typeCategorie2)) {
                newTypeCategorie = typeCategorie1;
            }
            CategorieBean categorieBean = competitionBean.getCategorie(newTypeCategorie, newCategorie);
            if (categorieBean == null) {
                categorieBean = new CategorieBean(newCategorie);
                categorieBean.setType(newTypeCategorie);
                competitionBean.getCategories().add(categorieBean);
            }

            String newEpreuve = epreuve1.concat("-").concat(epreuve2);
            if (epreuve1.equals(epreuve2)) {
                newEpreuve = epreuve1;
            }
            EpreuveBean epreuveBean = new EpreuveBean(newEpreuve);
            epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
            epreuveBean.setType(epreuveBean1.getType());
            categorieBean.getEpreuves().add(epreuveBean);

            ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
            if (epreuveBean1 != null) {
                epreuveBean1.setEtat(EtatEpreuve.FUSION.getValue());
                CategorieBean categorieBean1 = competitionBean.getCategorie(typeCategorie1, categorie1);
                sender.send(competitionBean, categorieBean1, epreuveBean1);

                participantBeans.addAll(extractParticipants(typeCategorie1, categorie1, epreuve1));
            }

            if (epreuveBean2 != null) {
                epreuveBean2.setEtat(EtatEpreuve.FUSION.getValue());
                CategorieBean categorieBean2 = competitionBean.getCategorie(typeCategorie2, categorie2);
                sender.send(competitionBean, categorieBean2, epreuveBean2);
                participantBeans.addAll(extractParticipants(typeCategorie2, categorie2, epreuve2));
            }

            epreuveBean.setParticipants(participantBeans);

            saveCompetitionToXmlFileTmp();
            sender.send(competitionBean, categorieBean, epreuveBean);

            ImageView imageMixte = new ImageView(new Image(getClass().getResourceAsStream("images/mixte.png")));
            imageMixte.setFitHeight(25);
            imageMixte.setFitWidth(25);
            TreeItem<String> treeItemTypeCategorie = new TreeItem(categorieBean.getType());
            TreeItem<String> treeItemCategorie = new TreeItem(newCategorie);
            ImageView imageTypeEpreve = null;
            if (epreuveBean.getType().equals(TypeEpreuve.COMBAT.getValue())) {
                imageTypeEpreve = new ImageView(new Image(getClass().getResourceAsStream("images/combat.png")));
            } else {
                imageTypeEpreve = new ImageView(new Image(getClass().getResourceAsStream("images/technique.png")));
            }
            TreeItem<String> treeItemTypeEpreuve = new TreeItem(epreuveBean.getType());
            TreeItem<String> treeItemEpreuve = new TreeItem<String>(newEpreuve);
            treeItemTypeCategorie.getChildren().add(treeItemCategorie);
            treeItemCategorie.getChildren().add(treeItemTypeEpreuve);
            treeItemTypeEpreuve.getChildren().add(treeItemEpreuve);

            //get item correspond to categorie type
            boolean categorieAdded = false;
            for (TreeItem<String> treeItem : treeView.getRoot().getChildren()) {
                if (treeItem.getValue().equals(categorieBean.getType())) {
                    for (TreeItem<String> itemCategorie : treeItem.getChildren()) {
                        if (itemCategorie.getValue().equals(categorieBean.getNom())) {
                            for (TreeItem<String> itemTypeEpreuve : itemCategorie.getChildren()) {
                                if (itemTypeEpreuve.getValue().equals(epreuveBean.getType())) {
                                    itemTypeEpreuve.getChildren().add(treeItemEpreuve);
                                    categorieAdded = true;
                                }
                            }
                        }
                    }
                    if (!categorieAdded) {
                        treeItem.getChildren().add(treeItemCategorie);
                        categorieAdded = true;
                    }
                }
            }

            //New categorie from fusion
            if (!categorieAdded) {
                treeView.getRoot().getChildren().add(treeItemTypeCategorie);
            }
        }
        return 0;
    }

    private ObservableList<ParticipantBean> extractParticipants(String typeCategorie, String categorie, String epreuve) {
        ObservableList<ParticipantBean> participantBeans1 = FXCollections.observableArrayList();
        for (ClubBean clubBean : competitionBean.getClubs()) {
            for (EleveBean eleveBean : clubBean.getEleves()) {
                if (categorie.equals(eleveBean.getCategorie()) && typeCategorie.equals(eleveBean.getSexe())) {
                    if (eleveBean.getEpreuves().contains(epreuve)) {
                        ParticipantBean participantBean = new ParticipantBean(eleveBean.getNom(), eleveBean.getPrenom());
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
        if (categorieBean != null) {
            epreuveBean = categorieBean.getEpreuveByName(epreuve);
        }
        return epreuveBean;
    }
}
