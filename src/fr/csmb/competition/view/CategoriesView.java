package fr.csmb.competition.view;

import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.bean.*;
import fr.csmb.competition.component.grid.fight.GridComponentFight;
import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.controller.DetailClubController;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
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
    private StackPane stackPane = new StackPane();
    private CompetitionBean competitionBean;

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
        stage.showAndWait();
        saveWork();

    }

    private void loadCompetitionBean(Competition competition) {
        competitionBean = new CompetitionBean(competition.getNom());
        ObservableList<CategorieBean> categorieBeans = FXCollections.observableArrayList();
        for (Categorie categorie : competition.getCategories()) {
            CategorieBean categorieBean = new CategorieBean(categorie.getNomCategorie());
            ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
            for (Epreuve epreuve : categorie.getEpreuves()) {
                EpreuveBean epreuveBean = new EpreuveBean(epreuve.getNomEpreuve());
                epreuveBean.setEtat(epreuve.getEtatEpreuve());
                epreuveBean.setType(epreuve.getTypeEpreuve());
                ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
                for (Club club : competition.getClubs()) {
                    for (Eleve eleve : club.getEleves()) {
                        if (categorie.getNomCategorie().equals(eleve.getCategorieEleve())) {
                            if (eleve.getEpreuvesEleves().contains(epreuve.getNomEpreuve())) {
                                ParticipantBean participantBean = new ParticipantBean(eleve.getNomEleve());
                                participantBeans.add(participantBean);
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
        for (Categorie categorie : competition.getCategories()) {
            CategorieBean categorieBean = competitionBean.getCategorieByName(categorie.getNomCategorie());
            for (Epreuve epreuve : categorie.getEpreuves()) {
                EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve.getNomEpreuve());
                List<Participant> participants = new ArrayList<Participant>();
                for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                    Participant participant = new Participant();
                    participant.setNomParticipant(participantBean.getNom());
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

    private void createComponentGrid(String typeEpreuve, final String categorie, final String epreuve) {
        stackPane.getChildren().clear();
        BorderPane borderPane = new BorderPane();
        GridComponent gridComponent = null;
        if (typeEpreuve.equals("Combat")) {
            List<ParticipantBean> participants = new ArrayList<ParticipantBean>();
            CategorieBean categorieBean = competitionBean.getCategorieByName(categorie);
            if (categorieBean != null) {
                EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                if (epreuveBean != null) {
                    for (ParticipantBean participantBean : epreuveBean.getParticipants())
                    participants.add(participantBean);
                }
            }

            for (int i = participants.size() + 1; i <= 8; i++) {
                participants.add(new ParticipantBean("Joueur " + i));
            }
            gridComponent = new GridComponentFight(participants);
        } else if (typeEpreuve.equals("Technique")) {
            List<ParticipantBean> participants = new ArrayList<ParticipantBean>();
            CategorieBean categorieBean = competitionBean.getCategorieByName(categorie);
            if (categorieBean != null) {
                EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                if (epreuveBean != null) {
                    for (ParticipantBean participantBean : epreuveBean.getParticipants())
                        participants.add(participantBean);
                }
            }
            for (int i = participants.size() + 1; i <= 8; i++) {
                participants.add(new ParticipantBean("Joueur " + i));
            }
            gridComponent = new GridComponentTechnical(participants);
        }
        borderPane.setCenter(gridComponent);

        final GridComponent gridComponent2  = gridComponent;
        Button button = new Button("Valider");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<ParticipantBean> resutlats = gridComponent2.getResultatsList();
                System.out.println("1er : " + resutlats.get(0).getNom());
                System.out.println("2eme : " + resutlats.get(1).getNom());
                System.out.println("3eme : " + resutlats.get(2).getNom());
                System.out.println("4eme : " + resutlats.get(3).getNom());
                CategorieBean categorieBean = competitionBean.getCategorieByName(categorie);
                if (categorieBean != null) {
                    EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                    if (epreuveBean != null) {
                        for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                            System.out.println("Nom : " + participantBean.getNom() + " Classement Final : " + participantBean.getClassementFinal());
                        }
                        epreuveBean.setEtat("Termine");
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
            loader.setLocation(getClass().getResource("clubDetailView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            DetailClubController detailClubController = loader.getController();
            eleveBeans = detailClubController.getTableEleve().getItems();

            stackPane.getChildren().add(borderPane);
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
        CategorieBean categorieBean = competitionBean.getCategorieByName(categorie);
        if (categorieBean != null) {
            EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
            if (epreuveBean != null) {
                epreuveBean.setType("Valide");
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

            MenuItem runMenuItem = new MenuItem("Démarrer");
            addMenu.getItems().add(runMenuItem);
            runMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createComponentGrid(getTreeItem().getParent().getValue(), getTreeItem().getParent().getParent().getValue(), getItem());
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
                        CategorieBean categorieBean = competitionBean.getCategorieByName(getTreeItem().getParent().getParent().getValue());
                        if (categorieBean != null) {
                            EpreuveBean epreuveBean = categorieBean.getEpreuveByName(getItem());
                            if (epreuveBean != null) {
                                if ("Valide".equals(epreuveBean.getEtat())) {
                                    setTextFill(Color.GREEN);
                                } else if ("Termine".equals(epreuveBean.getEtat())) {
                                    setTextFill(Color.RED);
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
