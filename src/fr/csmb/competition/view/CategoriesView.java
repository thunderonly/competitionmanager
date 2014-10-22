package fr.csmb.competition.view;

import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.bean.*;
import fr.csmb.competition.component.grid.fight.GridComponentFight;
import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.controller.DetailCategorieController;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.*;
import fr.csmb.competition.xml.model.Participant;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

    private ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
    private Competition competition;
    private StackPane stackPane = new StackPane();
    private CompetitionBean competitionBean;
    private Stage mainStage;

    public void showView(Stage mainStage, Competition competition) {
        this.competition = competition;
        loadCompetitionBean(competition);
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPosition(0, 0.2);
        createTreeView(splitPane, competition);
        createTableView(stackPane);
        splitPane.getItems().add(stackPane);
        Stage stage = new Stage();
        stage.setTitle("Détail compétition : " + competition.getNom());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        Scene scene = new Scene(splitPane);
        stage.setScene(scene);
        mainStage = stage;
        stage.showAndWait();
        saveWork();

    }

    private void loadCompetitionBean(Competition competition) {
        competitionBean = new CompetitionBean(competition.getNom());
        ObservableList<CategorieBean> categorieBeans = FXCollections.observableArrayList();
        for (Categorie categorie : competition.getCategories()) {
            CategorieBean categorieBean = new CategorieBean(categorie.getNomCategorie());
            categorieBean.setType(categorie.getTypeCategorie());
            ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
            for (Epreuve epreuve : categorie.getEpreuves()) {
                EpreuveBean epreuveBean = new EpreuveBean(epreuve.getNomEpreuve());
                epreuveBean.setEtat(epreuve.getEtatEpreuve());
                epreuveBean.setType(epreuve.getTypeEpreuve());
                ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
                for (Participant participant : epreuve.getParticipants()) {
                    ParticipantBean participantBean = new ParticipantBean(participant.getNomParticipant(), participant.getPrenomParticipant());
                    participantBeans.add(participantBean);
                }
                for (Club club : competition.getClubs()) {
                    for (Eleve eleve : club.getEleves()) {
                        if (categorie.getNomCategorie().equals(eleve.getCategorieEleve()) && categorie.getTypeCategorie().equals(eleve.getSexeEleve())) {
                            if (eleve.getEpreuvesEleves().contains(epreuve.getNomEpreuve())) {
                                ParticipantBean participantBean = new ParticipantBean(eleve.getNomEleve(), eleve.getPrenomEleve());
                                if (!participantBeans.contains(participantBean)) {
                                    participantBeans.add(participantBean);
                                }
                            }
                        }
                    }
                }
                epreuveBean.setParticipants(participantBeans);
                epreuveBeans.add(epreuveBean);
            }
            categorieBean.setEpreuves(epreuveBeans);
            categorieBeans.add(categorieBean);
        }
        competitionBean.setCategories(categorieBeans);
    }

    private void saveWork() {

        for (CategorieBean categorieBean : competitionBean.getCategories()) {
            Categorie categorie = competition.getCategorie(categorieBean.getNom(), categorieBean.getType());
            if (categorie == null) {
                categorie = new Categorie();
                categorie.setNomCategorie(categorieBean.getNom());
                categorie.setTypeCategorie(categorieBean.getType());
                categorie.setEpreuves(new ArrayList<Epreuve>());
                competition.getCategories().add(categorie);
            }
            for (EpreuveBean epreuveBean : categorieBean.getEpreuves()) {
                Epreuve epreuve = categorie.getEpreuveByName(epreuveBean.getNom());
                if (epreuve == null) {
                    epreuve = new Epreuve();
                    epreuve.setNomEpreuve(epreuveBean.getNom());
                    epreuve.setTypeEpreuve(epreuveBean.getType());
                    epreuve.setParticipants(new ArrayList<Participant>());
                    categorie.getEpreuves().add(epreuve);
                }

                List<Participant> participants = new ArrayList<Participant>();
                for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                    Participant participant = new Participant();
                    participant.setNomParticipant(participantBean.getNom());
                    participant.setPrenomParticipant(participantBean.getPrenom());
                    participant.setNote1(String.valueOf(participantBean.getNote1()));
                    participant.setNote2(String.valueOf(participantBean.getNote2()));
                    participant.setNote3(String.valueOf(participantBean.getNote3()));
                    participant.setNote4(String.valueOf(participantBean.getNote4()));
                    participant.setNote5(String.valueOf(participantBean.getNote5()));
                    participant.setClassementAuto(String.valueOf(participantBean
                            .getClassementAuto()));
                    participant.setClassementManuel(String.valueOf(participantBean.getClassementManuel()));
                    participant.setClassementFinal(String.valueOf(participantBean.getClassementFinal()));
                    participants.add(participant);
                }
                epreuve.setEtatEpreuve(epreuveBean.getEtat());
                epreuve.setParticipants(participants);
            }
        }
    }

    private void createTreeView(final SplitPane splitPane, final Competition competition) {
        StackPane pane = new StackPane();
        TreeItem<String>  treeItem = new TreeItem<String>(competition.getNom());
        treeItem.setExpanded(true);

        TreeItem<String> itemTypeCategorieFeminin = new TreeItem<String>(TypeCategorie.FEMININ.getValue());
        for (Categorie categorie : competition.getCategories()) {
            if (categorie.getTypeCategorie().equals(TypeCategorie.FEMININ.getValue())) {
                TreeItem<String> itemCategorie = new TreeItem<String>(categorie.getNomCategorie());
                TreeItem<String> itemEpreuveTypeTechnique = new TreeItem<String>(TypeEpreuve.TECHNIQUE.getValue());
                itemCategorie.getChildren().add(itemEpreuveTypeTechnique);
                for (Epreuve epreuve : categorie.getEpreuves()) {
                    if (TypeEpreuve.TECHNIQUE.getValue().equalsIgnoreCase(epreuve.getTypeEpreuve())) {
                        TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getNomEpreuve());
                        itemEpreuveTypeTechnique.getChildren().add(itemEpreuve);
                    }
                }
                TreeItem<String> itemEpreuveTypeCombat = new TreeItem<String>(TypeEpreuve.COMBAT.getValue());
                itemCategorie.getChildren().add(itemEpreuveTypeCombat);
                for (Epreuve epreuve : categorie.getEpreuves()) {
                    if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getTypeEpreuve())) {
                        TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getNomEpreuve());
                        itemEpreuveTypeCombat.getChildren().add(itemEpreuve);
                    }
                }

                itemTypeCategorieFeminin.getChildren().add(itemCategorie);
            }
        }
        treeItem.getChildren().add(itemTypeCategorieFeminin);

        TreeItem<String> itemTypeCategorieMasculin = new TreeItem<String>(TypeCategorie.MASCULIN.getValue());
        for (Categorie categorie : competition.getCategories()) {
            if (categorie.getTypeCategorie().equals(TypeCategorie.MASCULIN.getValue())) {
                TreeItem<String> itemCategorie = new TreeItem<String>(categorie.getNomCategorie());
                TreeItem<String> itemEpreuveTypeTechnique = new TreeItem<String>(TypeEpreuve.TECHNIQUE.getValue());
                itemCategorie.getChildren().add(itemEpreuveTypeTechnique);
                for (Epreuve epreuve : categorie.getEpreuves()) {
                    if (TypeEpreuve.TECHNIQUE.getValue().equalsIgnoreCase(epreuve.getTypeEpreuve())) {
                        TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getNomEpreuve());
                        itemEpreuveTypeTechnique.getChildren().add(itemEpreuve);
                    }
                }
                TreeItem<String> itemEpreuveTypeCombat = new TreeItem<String>(TypeEpreuve.COMBAT.getValue());
                itemCategorie.getChildren().add(itemEpreuveTypeCombat);
                for (Epreuve epreuve : categorie.getEpreuves()) {
                    if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getTypeEpreuve())) {
                        TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getNomEpreuve());
                        itemEpreuveTypeCombat.getChildren().add(itemEpreuve);
                    }
                }

                itemTypeCategorieMasculin.getChildren().add(itemCategorie);
            }
        }
        treeItem.getChildren().add(itemTypeCategorieMasculin);

        TreeView<String> treeView = new TreeView<String>(treeItem);
        treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> stringTreeView) {
                return new ContextableTreeCell();
            }
        });

        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

            @Override
            public void changed(
                    ObservableValue<? extends TreeItem<String>> observable,
                    TreeItem<String> old_val, TreeItem<String> new_val) {
                TreeItem<String> selectedItem = new_val;
                if (selectedItem.getChildren().size() == 0) {
                    String typeCategorie = new_val.getParent().getParent().getParent().getValue();
                    String categorie = new_val.getParent().getParent().getValue();
                    String epreuve = new_val.getValue();
                    updateList(competition, typeCategorie, categorie, epreuve);
                }
            }

        });

        pane.getChildren().add(treeView);

        splitPane.getItems().add(pane);

    }

    private void createComponentGrid(final String typeCategorie, final String typeEpreuve, final String categorie, final String epreuve) {
        stackPane.getChildren().clear();
        BorderPane borderPane = new BorderPane();
        GridComponent gridComponent = null;
        if (typeEpreuve.equals(TypeEpreuve.COMBAT.getValue())) {
            List<ParticipantBean> participants = new ArrayList<ParticipantBean>();
            CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
            if (categorieBean != null) {
                EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                if (epreuveBean != null) {
                    for (ParticipantBean participantBean : epreuveBean.getParticipants())
                    participants.add(participantBean);
                }
            }

            for (int i = participants.size() + 1; i <= 8; i++) {
                participants.add(new ParticipantBean("Nom " + i, "Prénom " + i));
            }
            gridComponent = new GridComponentFight(participants);
        } else if (typeEpreuve.equals(TypeEpreuve.TECHNIQUE.getValue())) {
            List<ParticipantBean> participants = new ArrayList<ParticipantBean>();
            CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
            if (categorieBean != null) {
                EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                if (epreuveBean != null) {
                    for (ParticipantBean participantBean : epreuveBean.getParticipants())
                        participants.add(participantBean);
                }
            }
            for (int i = participants.size() + 1; i <= 8; i++) {
                participants.add(new ParticipantBean("Nom " + i, "Prénom " + i));
            }
            gridComponent = new GridComponentTechnical(participants);
        }
        borderPane.setCenter(gridComponent);

        final GridComponent gridComponent2  = gridComponent;
        Button button = new Button("Terminer");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<ParticipantBean> resutlats = gridComponent2.getResultatsList();
                System.out.println("1er : " + resutlats.get(0).getNom());
                System.out.println("2eme : " + resutlats.get(1).getNom());
                System.out.println("3eme : " + resutlats.get(2).getNom());
                System.out.println("4eme : " + resutlats.get(3).getNom());
                CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
                if (categorieBean != null) {
                    EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                    if (epreuveBean != null) {
                        for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                            System.out.println("Nom : " + participantBean.getNom() + " Classement Final : " + participantBean.getClassementFinal());
                        }
                        epreuveBean.setEtat(EtatEpreuve.TERMINE.getValue());
                    }
                }
            }
        });
        borderPane.setBottom(button);
        stackPane.getChildren().add(borderPane);
    }

    private void createTableView(StackPane stackPane) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/categorieDetailView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            DetailCategorieController detailCategorieController = loader.getController();
            participantBeans = detailCategorieController.getTableParticipant().getItems();

            stackPane.getChildren().add(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateList(Competition competition, String typeCategorie, String categorie, String epreuve) {
        participantBeans.clear();
        participantBeans.addAll(extractEpreuves(competition, typeCategorie, categorie, epreuve));
    }

    private ObservableList<ParticipantBean> extractEpreuves(Competition competition, String typeCategorie, String categorie, String epreuve) {
        ObservableList<ParticipantBean> participantBeans1 = FXCollections.observableArrayList();
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        if (categorieBean != null) {
            EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
            if (epreuveBean != null) {
                return epreuveBean.getParticipants();
            }
        }
        return participantBeans1;
    }

    public void validateEpreuve(String typeCategorie, String categorie, String epreuve) {
        CategorieBean categorieBean = competitionBean.getCategorieByName(categorie);
        if (categorieBean != null) {
            EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
            if (epreuveBean != null) {
                epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
            }
        }
    }

    public void editEpreuve(String typeCategorie, String categorie, String epreuve) {
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        if (categorieBean != null) {
            EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
            if (epreuveBean != null) {
                createEditEpreuveView(categorieBean, epreuveBean);
            }
        }
    }

    private void createEditEpreuveView(CategorieBean categorieBean, EpreuveBean epreuveBean) {
        Stage stage = new Stage();
        stage.setTitle("Edition épreuve : " + categorieBean.getNom() + " - " + epreuveBean.getNom());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);

        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        Label participantsDispo = new Label("Participants disponibles");
        participantsDispo.getStyleClass().add("biglabel");

        gridPane.add(participantsDispo, 0, 0);
        borderPane.setCenter(gridPane);

        Button validBtn = new Button("Valider");
        Button cancelBtn = new Button("Annuler");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(validBtn, cancelBtn);
        borderPane.setBottom(hBox);
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(getClass().getResource("css/categoriesView.css").toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
    }

    private class ContextableTreeCell extends TreeCell<String> {
        private ContextMenu addMenu = new ContextMenu();

        public ContextableTreeCell() {
            MenuItem addMenuItem = new MenuItem("Edit");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    String typeCategorie = getTreeItem().getParent().getParent().getParent().getValue();
                    String categorie = getTreeItem().getParent().getParent().getValue();
                    String epreuve = getItem();
                    editEpreuve(typeCategorie, categorie, epreuve);
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
                    createComponentGrid(typeCategorie, typeEpreuve, categorie, epreuve);
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
                    validateEpreuve(typeCategorie, categorie, epreuve);
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

                        String newCategorie = categorie1.concat(" - ").concat(categorie2);
                        CategorieBean categorieBean = new CategorieBean(newCategorie);

                        String newEpreuve = item1.getValue().concat("-").concat(item2.getValue());
                        EpreuveBean epreuveBean = new EpreuveBean(newEpreuve);
                        epreuveBean.setType(item1.getParent().getValue());

                        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
                        EpreuveBean epreuveBean1 = getEpreuveBean(typeCategorie1, categorie1, item1.getValue());
                        if (epreuveBean1 != null) {
                            epreuveBean1.setEtat(EtatEpreuve.FUSION.getValue());
                        }
                        for (ParticipantBean participantBean : epreuveBean1.getParticipants()) {
                            participantBeans.add(participantBean);
                        }

                        EpreuveBean epreuveBean2 = getEpreuveBean(typeCategorie2, categorie2, item2.getValue());
                        if (epreuveBean2 != null) {
                            epreuveBean2.setEtat(EtatEpreuve.FUSION.getValue());
                        }
                        for (ParticipantBean participantBean : epreuveBean2.getParticipants()) {
                            participantBeans.add(participantBean);
                        }

                        epreuveBean.setParticipants(participantBeans);
                        ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
                        epreuveBeans.add(epreuveBean);
                        categorieBean.setEpreuves(epreuveBeans);

                        competitionBean.getCategories().add(categorieBean);

                        TreeItem<String> treeItemCategorie = new TreeItem(newCategorie);
                        TreeItem<String> treeItemTypeCategorie = new TreeItem(item1.getParent().getValue());
                        TreeItem<String> treeItemEpreuve = new TreeItem<String>(newEpreuve);
                        treeItemCategorie.getChildren().add(treeItemTypeCategorie);
                        treeItemTypeCategorie.getChildren().add(treeItemEpreuve);
                        getTreeView().getRoot().getChildren().add(treeItemCategorie);
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
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
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
                                } else if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                                    setTextFill(Color.RED);
                                } else if (EtatEpreuve.FUSION.getValue().equals(epreuveBean.getEtat())) {
                                    setTextFill(Color.DARKORANGE);
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

    private EpreuveBean getEpreuveBean(String typeCategorie, String categorie, String epreuve) {
        EpreuveBean epreuveBean = null;
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        if (categorieBean != null) {
            epreuveBean = categorieBean.getEpreuveByName(epreuve);
        }
        return epreuveBean;
    }
}
