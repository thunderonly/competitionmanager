package fr.csmb.competition.view;

import fr.csmb.competition.Main;
import fr.csmb.competition.controller.DetailClubController;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.xml.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 14/10/14.
 */
public class CategoriesView {

    private ObservableList<EleveBean> eleveBeans = FXCollections.observableArrayList();
    private Competition competition;

    public void showView(Stage mainStage, Competition competition) {
        this.competition = competition;
        SplitPane splitPane = new SplitPane();
        createTreeView(splitPane, competition);
        createTableView(splitPane);
        Stage stage = new Stage();
        stage.setTitle("Détail compétition : " + competition.getNom());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        Scene scene = new Scene(splitPane);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void createTreeView(final SplitPane splitPane, final Competition competition) {
        StackPane pane = new StackPane();
        TreeItem<String>  treeItem = new TreeItem<String>(competition.getNom());
        treeItem.setExpanded(true);

        for (Categorie categorie : competition.getCategories()) {
            TreeItem<String> item = new TreeItem<String>(categorie.getNomCategorie());
            TreeItem<String> itemEpreuveTypeTechnique = new TreeItem<String>("Technique");
            item.getChildren().add(itemEpreuveTypeTechnique);
            for (Epreuve epreuve : categorie.getEpreuves()) {
                if ("Technique".equalsIgnoreCase(epreuve.getTypeEpreuve())) {
                    TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getNomEpreuve());
                    itemEpreuveTypeTechnique.getChildren().add(itemEpreuve);
                }
            }
            TreeItem<String> itemEpreuveTypeCombat = new TreeItem<String>("Combat");
            item.getChildren().add(itemEpreuveTypeCombat);
            for (Epreuve epreuve : categorie.getEpreuves()) {
                if ("Combat".equalsIgnoreCase(epreuve.getTypeEpreuve())) {
                    TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getNomEpreuve());
                    itemEpreuveTypeCombat.getChildren().add(itemEpreuve);
                }
            }

            treeItem.getChildren().add(item);
        }
        TreeView<String> treeView = new TreeView<String>(treeItem);
        treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> stringTreeView) {
                return new ContextableTreeCell();
            }
        });

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

            @Override
            public void changed(
                    ObservableValue<? extends TreeItem<String>> observable,
                    TreeItem<String> old_val, TreeItem<String> new_val) {
                TreeItem<String> selectedItem = new_val;
                if (selectedItem.getChildren().size() == 0) {

                    updateList(competition, new_val.getParent().getParent().getValue(), new_val.getValue());
                }
            }

        });

        pane.getChildren().add(treeView);

        splitPane.getItems().add(pane);

    }

    private void createTableView(SplitPane splitPane) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("clubDetailView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            DetailClubController detailClubController = loader.getController();
            eleveBeans = detailClubController.getTableEleve().getItems();

            splitPane.getItems().add(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateList(Competition competition, String categorie, String epreuve) {
        eleveBeans.clear();
        eleveBeans.addAll(extractEpreuves(competition, categorie, epreuve));
    }

    private ObservableList<EleveBean> extractEpreuves(Competition competition,String categorie, String epreuve) {
        List<Eleve> eleves = new ArrayList<Eleve>();
        for (Club club : competition.getClubs()) {
            for (Eleve eleve : club.getEleves()) {
                if (categorie.equals(eleve.getCategorieEleve())) {
                    if (eleve.getEpreuvesEleves().contains(epreuve)) {
                        eleves.add(eleve);
                    }
                }
            }
        }
        ObservableList<EleveBean> eleveBeans = FXCollections.observableArrayList();
        for (Eleve eleve : eleves) {
            EleveBean eleveBean = new EleveBean();
            eleveBean.setLicence(eleve.getLicenceEleve());
            eleveBean.setNom(eleve.getNomEleve());
            eleveBean.setPrenom(eleve.getPrenomEleve());
            eleveBean.setAge(eleve.getAgeEleve());
            eleveBean.setCategorie(eleve.getCategorieEleve());
            eleveBean.setSexe(eleve.getSexeEleve());
            eleveBean.setPoids(eleve.getPoidsEleve());
            eleveBeans.add(eleveBean);
        }

        return eleveBeans;
    }

    public void validateEpreuve(String categorie, String epreuve) {
        for (Categorie categorie1 : competition.getCategories()) {
            if (categorie1.getNomCategorie().equals(categorie)) {
                for (Epreuve epreuve1 : categorie1.getEpreuves()) {
                    if (epreuve1.getNomEpreuve().equals(epreuve)) {
                        ObservableList<EleveBean> validateEleveBeans = FXCollections.observableArrayList();
                        validateEleveBeans.addAll(extractEpreuves(competition, categorie, epreuve));
                        List<Participant> participants = new ArrayList<Participant>();
                        for (EleveBean eleveBean : validateEleveBeans) {
                            Participant participant = new Participant();
                            participant.setNomParticipant(eleveBean.getNom());
                            participant.setPrenomParticipant(eleveBean.getPrenom());
                            participants.add(participant);
                        }
                        epreuve1.setEtatEpreuve("Valide");
                        epreuve1.setParticipants(participants);
                    }
                }
            }
        }


    }

    private class ContextableTreeCell extends TreeCell<String> {
        private ContextMenu addMenu = new ContextMenu();

        public ContextableTreeCell() {
            MenuItem addMenuItem = new MenuItem("Edit");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                }
            });
            MenuItem startMenuItem = new MenuItem("Démarrer");
            addMenu.getItems().add(startMenuItem);
            startMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                }
            });
            MenuItem validMenuItem = new MenuItem("Valider");
            addMenu.getItems().add(validMenuItem);
            validMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    validateEpreuve(getTreeItem().getParent().getParent().getValue(), getItem());
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
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    setText(null);
                } else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                    if (getTreeItem().isLeaf()){
                        setContextMenu(addMenu);
                        for (Categorie categorie1 : competition.getCategories()) {
                            if (categorie1.getNomCategorie().equals(getTreeItem().getParent().getParent().getValue())) {
                                for (Epreuve epreuve1 : categorie1.getEpreuves()) {
                                    if (epreuve1.getNomEpreuve().equals(getItem())) {
                                        if ("Valide".equals(epreuve1.getEtatEpreuve())) {
                                            setTextFill(Color.GREEN);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }
}
