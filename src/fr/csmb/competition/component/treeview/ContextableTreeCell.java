/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.treeview;

import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.CategoriesView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class ContextableTreeCell extends TreeCell<String> {
    private ContextMenu addMenu = new ContextMenu();
    private CompetitionBean competitionBean;
    private CategoriesView categoriesView;

    public ContextableTreeCell(final CategoriesView categoriesView, final CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
        this.categoriesView = categoriesView;
        MenuItem addMenuItem = new MenuItem("Edit");
        addMenu.getItems().add(addMenuItem);
        addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String typeCategorie = getTreeItem().getParent().getParent().getParent().getValue();
                String categorie = getTreeItem().getParent().getParent().getValue();
                String epreuve = getItem();
                categoriesView.editEpreuve(typeCategorie, categorie, epreuve);
            }
        });

        MenuItem runMenuItem = new MenuItem("Démarrer");
        addMenu.getItems().add(runMenuItem);
        runMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String typeCategorie = getTreeItem().getParent().getParent().getParent().getValue();
                String categorie = getTreeItem().getParent().getParent().getValue();
                String typeEpreuve = getTreeItem().getParent().getValue();
                String epreuve = getItem();
                categoriesView.createComponentGrid(typeCategorie, typeEpreuve, categorie, epreuve);
            }
        });
        MenuItem validMenuItem = new MenuItem("Valider");
        addMenu.getItems().add(validMenuItem);
        validMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String typeCategorie = getTreeItem().getParent().getParent().getParent().getValue();
                String categorie = getTreeItem().getParent().getParent().getValue();
                String epreuve = getItem();
                categoriesView.validateEpreuve(typeCategorie, categorie, epreuve);
            }
        });

        MenuItem fusionMenuItem = new MenuItem("Fusionner");
        fusionMenuItem.setVisible(false);
        addMenu.getItems().add(fusionMenuItem);
        fusionMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (getTreeView().getSelectionModel().getSelectedItems().size() >= 2) {
                    TreeItem<String> item1 = getTreeView().getSelectionModel().getSelectedItems().get(0);
                    String typeCategorie1 = item1.getParent().getParent().getParent().getValue();
                    String categorie1 = item1.getParent().getParent().getValue();

                    TreeItem<String> item2 = getTreeView().getSelectionModel().getSelectedItems().get(1);
                    String typeCategorie2 = item2.getParent().getParent().getParent().getValue();
                    String categorie2 = item2.getParent().getParent().getValue();
                    categoriesView.fusionEpreuve(getTreeView(), typeCategorie1, categorie1, item1.getValue(), typeCategorie2, categorie2, item2.getValue());
                }
            }
        });

    }

    @Override
    public void startEdit() {
        super.startEdit();

        setText(null);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText((String) getItem());
        setGraphic(getTreeItem().getGraphic());
    }

    @Override
    public void updateSelected(boolean b) {
        super.updateSelected(b);
        if (getTreeView() != null && getTreeView().getSelectionModel() != null && getTreeView().getSelectionModel().getSelectedItems() != null) {
            if (getTreeView().getSelectionModel().getSelectedItems().size() >= 2) {
                for (MenuItem menuItem : addMenu.getItems()) {
                    if (menuItem.getText().equals("Fusionner")) {
                        menuItem.setVisible(true);
                    }
                }
            } else {
                for (MenuItem menuItem : addMenu.getItems()) {
                    if (menuItem.getText().equals("Fusionner")) {
                        menuItem.setVisible(false);
                    }
                }
            }
        }
    }



    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                setText(null);
            } else {
                ImageView imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/epreuveInconnu.png")));
                if (getTreeItem().getValue().equals(TypeCategorie.FEMININ.getValue())) {
                    imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/femme.png")));
                } else if (getTreeItem().getValue().equals(TypeCategorie.MASCULIN.getValue())) {
                    imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/homme.png")));
                } else if (getTreeItem().getValue().equals(TypeCategorie.MIXTE.getValue())) {
                    imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/mixte.png")));
                } else if (getTreeItem().getValue().equals(TypeEpreuve.COMBAT.getValue())) {
                    imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/combat.png")));
                } else if (getTreeItem().getValue().equals(TypeEpreuve.TECHNIQUE.getValue())) {
                    imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/technique.png")));
                } else if (getTreeItem().getParent() != null && getTreeItem().getParent().getValue().equals(TypeCategorie.FEMININ.getValue()))  {
                    imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/categorieFeminin.png")));
                } else if (getTreeItem().getParent() != null && getTreeItem().getParent().getValue().equals(TypeCategorie.MASCULIN.getValue()))  {
                    imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/categorieMasculin.png")));
                } else if (getTreeItem().getParent() != null && getTreeItem().getParent().getValue().equals(TypeCategorie.MIXTE.getValue()))  {
                    imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/categorieMixte.png")));
                }
                if (getTreeItem().isLeaf()){
                    setContextMenu(addMenu);

                    String typeCategorie = getTreeItem().getParent().getParent().getParent().getValue();
                    String categorie = getTreeItem().getParent().getParent().getValue();
                    CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
                    if (categorieBean != null) {
                        EpreuveBean epreuveBean = categorieBean.getEpreuveByName(getItem());
                        if (epreuveBean != null) {
                            if (EtatEpreuve.VALIDE.getValue().equals(epreuveBean.getEtat())) {
                                setTextFill(Color.GREEN);
                                imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/epreuveValide.png")));
                            } else if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                                setTextFill(Color.RED);
                                setUnderline(true);
                                imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/epreuveTermine.png")));
                            } else if (EtatEpreuve.FUSION.getValue().equals(epreuveBean.getEtat())) {
                                setTextFill(Color.DARKORANGE);
                                imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/epreuveFusion.png")));
                            } else if (EtatEpreuve.DEMARRE.getValue().equals(epreuveBean.getEtat())) {
                                setTextFill(Color.DARKRED);
                                imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/epreuveDemarre.png")));
                            } else {
                                imageView = new ImageView(new Image(this.categoriesView.getClass().getResourceAsStream("images/epreuveInconnu.png")));
                            }
                        }
                    }
                }

                setText(getString());
                imageView.setFitHeight(25);
                imageView.setFitWidth(25);
                getTreeItem().setGraphic(imageView);
                setGraphic(getTreeItem().getGraphic());
            }
        }
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}