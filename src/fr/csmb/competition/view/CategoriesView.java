package fr.csmb.competition.view;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.component.grid.bean.*;
import fr.csmb.competition.component.grid.fight.GridComponentFight;
import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.component.pane.BorderedTitledPane;
import fr.csmb.competition.controller.DetailCategorieController;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.network.sender.NetworkSender;
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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
    private StackPane stackPane = new StackPane();
    private CompetitionBean competitionBean;
    private Stage currentStage;
    private NotificationView notificationView;

    private TextField adminTf = new TextField();
    private TextField chronoTf = new TextField();
    private TextField firstJugeTf = new TextField();
    private TextField secondJugeTf = new TextField();
    private TextField thirdJugeTf = new TextField();
    private TextField fourthJugeTf = new TextField();
    private TextField fifthJugeTf = new TextField();
    private TextField tapisTf = new TextField();
    private TextField heureDebutTf = new TextField();
    private TextField heureFinTf = new TextField();
    private TextField dureeTf = new TextField();
    private TextField firstPlaceTf = new TextField();
    private TextField secondPlaceTf = new TextField();
    private TextField thirdPlaceTf = new TextField();
    private TextField fourthPlaceTf = new TextField();

    private NetworkSender sender = new NetworkSender("", 9878);

    public void showView(Stage mainStage, CompetitionBean competition) {
        this.competitionBean = competition;
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPosition(0, 0.2);
        createTreeView(splitPane);
        createTableView(stackPane);
        splitPane.getItems().add(stackPane);
        Stage stage = new Stage();
        stage.setTitle("Détail compétition : " + competition.getNom());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        Scene scene = new Scene(splitPane);
        scene.getStylesheets().add(getClass().getResource("css/categoriesView.css").toExternalForm());
        stage.setScene(scene);
        currentStage = stage;
        notificationView = new NotificationView(currentStage);
        stage.showAndWait();

    }

    private void createTreeView(final SplitPane splitPane) {
        StackPane pane = new StackPane();
        TreeItem<String>  treeItem = new TreeItem<String>(competitionBean.getNom());
        treeItem.setExpanded(true);

        TreeItem<String> itemTypeCategorieFeminin = new TreeItem<String>(TypeCategorie.FEMININ.getValue());
        treeItem.getChildren().add(itemTypeCategorieFeminin);
        TreeItem<String> itemTypeCategorieMasculin = new TreeItem<String>(TypeCategorie.MASCULIN.getValue());
        treeItem.getChildren().add(itemTypeCategorieMasculin);
        TreeItem<String> itemTypeCategorieMixte = null;

        for (CategorieBean categorie : competitionBean.getCategories()) {
            TreeItem<String> itemCategorie = new TreeItem<String>(categorie.getNom());
            TreeItem<String> itemEpreuveTypeTechnique = new TreeItem<String>(TypeEpreuve.TECHNIQUE.getValue());
            itemCategorie.getChildren().add(itemEpreuveTypeTechnique);
            TreeItem<String> itemEpreuveTypeCombat = new TreeItem<String>(TypeEpreuve.COMBAT.getValue());
            itemCategorie.getChildren().add(itemEpreuveTypeCombat);
            for (EpreuveBean epreuve : categorie.getEpreuves()) {
                TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getNom());
                if (TypeEpreuve.TECHNIQUE.getValue().equalsIgnoreCase(epreuve.getType())) {
                    itemEpreuveTypeTechnique.getChildren().add(itemEpreuve);
                } else if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getType())) {
                    itemEpreuveTypeCombat.getChildren().add(itemEpreuve);
                }
            }
            if (categorie.getType().equals(TypeCategorie.FEMININ.getValue())) {
                itemTypeCategorieFeminin.getChildren().add(itemCategorie);
            } else if (categorie.getType().equals(TypeCategorie.MASCULIN.getValue())) {
                itemTypeCategorieMasculin.getChildren().add(itemCategorie);
            } else if (categorie.getType().equals(TypeCategorie.MIXTE.getValue())) {
                if (itemTypeCategorieMixte == null) {
                    itemTypeCategorieMixte = new TreeItem<String>(TypeCategorie.MIXTE.getValue());
                    treeItem.getChildren().add(itemTypeCategorieMixte);
                }
                itemTypeCategorieMixte.getChildren().add(itemCategorie);
            }
        }

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
                    updateList(typeCategorie, categorie, epreuve);
                }
            }

        });

        pane.getChildren().add(treeView);

        splitPane.getItems().add(pane);

    }

    private void createComponentGrid(final String typeCategorie, final String typeEpreuve, final String categorie, final String epreuve) {

        BorderPane borderPane = new BorderPane();
        GridComponent gridComponent = null;
        ParticipantClassementFinalListener participantClassementFinalListener =
                new ParticipantClassementFinalListener(firstPlaceTf, secondPlaceTf, thirdPlaceTf, fourthPlaceTf);
        List<ParticipantBean> participants = new ArrayList<ParticipantBean>();
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        EpreuveBean epreuveBean = null;
        if (categorieBean != null) {
            epreuveBean = categorieBean.getEpreuveByName(epreuve);

            if (epreuveBean.getEtat() == null || "".equals(epreuveBean.getEtat())) {
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de démarrer une épreuve non validée");
                return;
            } else if (epreuveBean.getEtat().equals(EtatEpreuve.TERMINE.getValue())) {
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de démarrer une épreuve terminée");
                return;
            } else if (epreuveBean.getEtat().equals(EtatEpreuve.FUSION.getValue())) {
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de démarrer une épreuve fusionnée");
                return;
            }


            if (epreuveBean != null) {
                for (ParticipantBean participantBean : epreuveBean.getParticipants())
                    participants.add(participantBean);
            }
        }

        for (int i = participants.size() + 1; i <= 3; i++) {
            participants.add(new ParticipantBean("Nom " + i, "Prénom " + i));
        }

        if (typeEpreuve.equals(TypeEpreuve.COMBAT.getValue())) {
            gridComponent = new GridComponentFight(participants);
        } else if (typeEpreuve.equals(TypeEpreuve.TECHNIQUE.getValue())) {
            gridComponent = new GridComponentTechnical(participants);
        }
        createCartoucheForGridComponent(borderPane, categorieBean, epreuveBean);
        gridComponent.setParticipantClassementFinalListener(participantClassementFinalListener);
        gridComponent.drawGrid();
        borderPane.setCenter(gridComponent);

        final GridComponent gridComponent2  = gridComponent;
        Button button = new Button("Terminer");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<ParticipantBean> resutlats = gridComponent2.getResultatsList();
                CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
                if (categorieBean != null) {
                    EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                    if (epreuveBean != null) {
                        for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
                            System.out.println("Nom : " + participantBean.getNom() + " Classement Final : " +
                                    participantBean.getClassementFinal());
                        }
                        epreuveBean.setEtat(EtatEpreuve.TERMINE.getValue());
                        epreuveBean.setAdministrateur(adminTf.getText());
                        epreuveBean.setChronometreur(chronoTf.getText());
                        epreuveBean.setJuge1(firstJugeTf.getText());
                        epreuveBean.setJuge2(secondJugeTf.getText());
                        epreuveBean.setJuge3(thirdJugeTf.getText());
                        epreuveBean.setJuge4(fourthJugeTf.getText());
                        epreuveBean.setJuge5(fifthJugeTf.getText());
                        epreuveBean.setTapis(tapisTf.getText());
                        epreuveBean.setHeureDebut(heureDebutTf.getText());
                        epreuveBean.setHeureFin(heureFinTf.getText());
                        epreuveBean.setDuree(dureeTf.getText());
                        sender.send(competitionBean, categorieBean, epreuveBean);
                    }
                }
            }
        });
        borderPane.setBottom(button);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(borderPane);
    }

    private void createCartoucheForGridComponent(BorderPane borderPane, CategorieBean categorieBean, EpreuveBean epreuveBean) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(20);
        Text titleFiche = new Text("Fiche Administrateur");
        titleFiche.getStyleClass().add("biglabelGridPane");
        gridPane.add(titleFiche, 0, 0, 2, 1);

        Label categorieLabel = new Label("Catégorie");
        categorieLabel.getStyleClass().add("titleGridPane");
        gridPane.add(categorieLabel, 0, 1);
        Label categorieTf = new Label(categorieBean.getNom().concat(" ").concat(categorieBean.getType()));
        categorieTf.getStyleClass().add("labelGridPane");
        gridPane.add(categorieTf, 1, 1);

        Label epreuveLabel = new Label("Epreuve");
        epreuveLabel.getStyleClass().add("titleGridPane");
        gridPane.add(epreuveLabel, 0, 2);
        Label epreuveTf = new Label(epreuveBean.getNom());
        epreuveTf.getStyleClass().add("labelGridPane");
        gridPane.add(epreuveTf, 1, 2);

        Label adminLabel = new Label("Administrateur");
        adminLabel.getStyleClass().add("titleGridPane");
        gridPane.add(adminLabel, 0, 3);
        adminTf.setPromptText("Nom administrateur");
        gridPane.add(adminTf, 1, 3);

        Label chronoLabel = new Label("Chronométreur");
        chronoLabel.getStyleClass().add("titleGridPane");
        gridPane.add(chronoLabel, 0, 4);
        chronoTf.setPromptText("Nom chronométreur");
        gridPane.add(chronoTf, 1, 4);


        Text titleClassement = new Text("Classement");
        titleClassement.getStyleClass().add("biglabelGridPane");
        gridPane.add(titleClassement, 2, 0, 2, 1);

        Label firstPlaceLabel = new Label("1ere Place");
        firstPlaceLabel.getStyleClass().add("titleGridPane");
        gridPane.add(firstPlaceLabel, 2, 1);
        firstPlaceTf.setPromptText("1ere Place");
        gridPane.add(firstPlaceTf, 3, 1);

        Label secondPlaceLabel = new Label("2eme Place");
        secondPlaceLabel.getStyleClass().add("titleGridPane");
        gridPane.add(secondPlaceLabel, 2, 2);
        secondPlaceTf.setPromptText("2eme Place");
        gridPane.add(secondPlaceTf, 3, 2);

        Label thirdPlaceLabel = new Label("3eme Place");
        thirdPlaceLabel.getStyleClass().add("titleGridPane");
        gridPane.add(thirdPlaceLabel, 2, 3);
        thirdPlaceTf.setPromptText("3eme Place");
        gridPane.add(thirdPlaceTf, 3, 3);

        Label fourthPlaceLabel = new Label("4eme Place");
        fourthPlaceLabel.getStyleClass().add("titleGridPane");
        gridPane.add(fourthPlaceLabel, 2, 4);
        fourthPlaceTf.setPromptText("4eme Place");
        gridPane.add(fourthPlaceTf, 3, 4);


        Text titleJuge = new Text("Juges");
        titleJuge.getStyleClass().add("biglabelGridPane");
        gridPane.add(titleJuge, 0, 5, 2, 1);

        Label firstJugeLabel = new Label("Juge 1");
        firstJugeLabel.getStyleClass().add("titleGridPane");
        gridPane.add(firstJugeLabel, 0, 6);
        firstJugeTf.setPromptText("Juge 1");
        gridPane.add(firstJugeTf, 1, 6);

        Label secondJugeLabel = new Label("Juge 2");
        secondJugeLabel.getStyleClass().add("titleGridPane");
        gridPane.add(secondJugeLabel, 0, 7);
        secondJugeTf.setPromptText("Juge 2");
        gridPane.add(secondJugeTf, 1, 7);

        Label thirdJugeLabel = new Label("Juge 3");
        thirdJugeLabel.getStyleClass().add("titleGridPane");
        gridPane.add(thirdJugeLabel, 0, 8);
        thirdJugeTf.setPromptText("Juge 3");
        gridPane.add(thirdJugeTf, 1, 8);

        Label fourthJugeLabel = new Label("Juge 4");
        fourthJugeLabel.getStyleClass().add("titleGridPane");
        gridPane.add(fourthJugeLabel, 0, 9);
        fourthJugeTf.setPromptText("Juge 4");
        gridPane.add(fourthJugeTf, 1, 9);

        Label fifthJugeLabel = new Label("Juge 5");
        fifthJugeLabel.getStyleClass().add("titleGridPane");
        gridPane.add(fifthJugeLabel, 0, 10);
        fifthJugeTf.setPromptText("Juge 5");
        gridPane.add(fifthJugeTf, 1, 10);


        Text titleEpreuve = new Text("Epreuve");
        titleEpreuve.getStyleClass().add("biglabelGridPane");
        gridPane.add(titleEpreuve, 2, 5, 2, 1);

        Label tapisLabel = new Label("Tapis");
        tapisLabel.getStyleClass().add("titleGridPane");
        gridPane.add(tapisLabel, 2, 6);
        tapisTf.setPromptText("Tapis");
        gridPane.add(tapisTf, 3, 6);

        Label heureDebutLabel = new Label("Début");
        heureDebutLabel.getStyleClass().add("titleGridPane");
        gridPane.add(heureDebutLabel, 2, 7);
        heureDebutTf.setPromptText("Début");
        gridPane.add(heureDebutTf, 3, 7);

        Label heureFinLabel = new Label("Fin");
        heureFinLabel.getStyleClass().add("titleGridPane");
        gridPane.add(heureFinLabel, 2, 8);
        heureFinTf.setPromptText("Fin");
        gridPane.add(heureFinTf, 3, 8);

        Label dureeLabel = new Label("Durée");
        dureeLabel.getStyleClass().add("titleGridPane");
        gridPane.add(dureeLabel, 2, 9);
        dureeTf.setPromptText("Durée");
        gridPane.add(dureeTf, 3, 9);

        Label nbParticipantsLabel = new Label("Juge 5");
        nbParticipantsLabel.getStyleClass().add("titleGridPane");
        gridPane.add(nbParticipantsLabel, 2, 10);
        Label nbParticipantsTf = new Label(String.valueOf(epreuveBean.getParticipants().size()));
        gridPane.add(nbParticipantsTf, 3, 10);
        nbParticipantsLabel.getStyleClass().add("titleGridPane");

        borderPane.setTop(gridPane);
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

    private void updateList(String typeCategorie, String categorie, String epreuve) {
        participantBeans.clear();
        participantBeans.addAll(extractEpreuves(typeCategorie, categorie, epreuve));
    }

    private ObservableList<ParticipantBean> extractEpreuves(String typeCategorie, String categorie, String epreuve) {
        ObservableList<ParticipantBean> participantBeans1 = FXCollections.observableArrayList();
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        if (categorieBean != null) {
            EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
            if (epreuveBean != null) {
                if (EtatEpreuve.VALIDE.getValue().equals(epreuveBean.getEtat()) || EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                    participantBeans1.addAll(epreuveBean.getParticipants());
                } else if (EtatEpreuve.FUSION.getValue().equals(epreuveBean.getEtat()) || "".equals(epreuveBean.getEtat()) || epreuveBean.getEtat() == null) {
                    for (ClubBean club : competitionBean.getClubs()) {
                        for (EleveBean eleve : club.getEleves()) {
                            if (categorie.equals(eleve.getCategorie()) && typeCategorie.equals(eleve.getSexe()) && eleve.getPresence()) {
                                if (eleve.getEpreuves().contains(epreuve)) {
                                    ParticipantBean participantBean = new ParticipantBean(eleve.getNom(), eleve.getPrenom());
                                    participantBeans1.add(participantBean);
                                }
                            }
                        }
                    }
                }
            }

        }
        return participantBeans1;
    }

    public void validateEpreuve(TreeItem<String> treeItem, String typeCategorie, String categorie, String epreuve) {
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        if (categorieBean != null) {
            EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
            if (epreuveBean != null) {
                if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
                    notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                            "Impossible de valider une épreuve terminée");
                } else if (EtatEpreuve.FUSION.getValue().equals(epreuveBean.getEtat())) {
                    notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                            "Impossible de valider une épreuve fusionnée");
                } else {
                    epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
                    epreuveBean.getParticipants().addAll(extractParticipants(typeCategorie, categorie, epreuve));
                    TreeItem<String> parent = treeItem.getParent();
                    int index = parent.getChildren().indexOf(treeItem);
                    parent.getChildren().remove(treeItem);
                    parent.getChildren().add(index, treeItem);

                    sender.send(competitionBean, categorieBean, epreuveBean);
                }
            }
        }
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

    public void fusionEpreuve(TreeView<String> treeView, String typeCategorie1, String categorie1, String epreuve1, String typeCategorie2, String categorie2, String epreuve2) {

        EpreuveBean epreuveBean1 = getEpreuveBean(typeCategorie1, categorie1, epreuve1);
        EpreuveBean epreuveBean2 = getEpreuveBean(typeCategorie2, categorie2, epreuve2);
        if (EtatEpreuve.FUSION.getValue().equals(epreuveBean1.getEtat()) ||
                EtatEpreuve.FUSION.getValue().equals(epreuveBean2.getEtat())) {
            notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                    "Impossible de fusionner une épreuve fusionnée");
        } else if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean1.getEtat()) ||
                EtatEpreuve.TERMINE.getValue().equals(epreuveBean2.getEtat())) {
            notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                    "Impossible de fusionner une épreuve terminée");
        } else if (EtatEpreuve.VALIDE.getValue().equals(epreuveBean1.getEtat()) ||
                EtatEpreuve.VALIDE.getValue().equals(epreuveBean2.getEtat())) {
            notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                    "Impossible de fusionner une épreuve validée");
        } else {
            String newCategorie = categorie1.concat(" - ").concat(categorie2);
            if (categorie1.equals(categorie2)) {
                newCategorie = categorie1;
            }

            CategorieBean categorieBean = new CategorieBean(newCategorie);
            String newTypeCategorie = typeCategorie1.concat(" - ").concat(typeCategorie2);

            if (typeCategorie1.equals(typeCategorie2)) {
                newTypeCategorie = typeCategorie1;
            }
            categorieBean.setType(newTypeCategorie);

            String newEpreuve = epreuve1.concat("-").concat(epreuve2);
            if (epreuve1.equals(epreuve2)) {
                newEpreuve = epreuve1;
            }
            EpreuveBean epreuveBean = new EpreuveBean(newEpreuve);
            epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
            epreuveBean.setType(epreuveBean1.getType());

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
            ObservableList<EpreuveBean> epreuveBeans = FXCollections.observableArrayList();
            epreuveBeans.add(epreuveBean);
            categorieBean.setEpreuves(epreuveBeans);

            competitionBean.getCategories().add(categorieBean);
            sender.send(competitionBean, categorieBean, epreuveBean);

            TreeItem<String> treeItemTypeCategorie = new TreeItem(categorieBean.getType());
            TreeItem<String> treeItemCategorie = new TreeItem(newCategorie);
            TreeItem<String> treeItemTypeEpreuve = new TreeItem(epreuveBean.getType());
            TreeItem<String> treeItemEpreuve = new TreeItem<String>(newEpreuve);
            treeItemTypeCategorie.getChildren().add(treeItemCategorie);
            treeItemCategorie.getChildren().add(treeItemTypeEpreuve);
            treeItemTypeEpreuve.getChildren().add(treeItemEpreuve);

            //get item correspond to categorie type
            boolean categorieAdded = false;
            for (TreeItem<String> treeItem : treeView.getRoot().getChildren()) {
                if (treeItem.getValue().equals(categorieBean.getType())) {
                    treeItem.getChildren().add(treeItemCategorie);
                    categorieAdded = true;
                }
            }

            //New categorie from fusion
            if (!categorieAdded) {
                treeView.getRoot().getChildren().add(treeItemTypeCategorie);
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
        stage.initOwner(currentStage);

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
                    validateEpreuve(getTreeItem(), typeCategorie, categorie, epreuve);
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
                        fusionEpreuve(getTreeView(), typeCategorie1, categorie1, item1.getValue(), typeCategorie2, categorie2, item2.getValue());
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
