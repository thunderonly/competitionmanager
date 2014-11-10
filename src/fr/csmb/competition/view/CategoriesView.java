package fr.csmb.competition.view;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.Main;
import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.component.grid.bean.*;
import fr.csmb.competition.component.grid.fight.GridComponentFight;
import fr.csmb.competition.component.grid.fight.GridComponentFight2;
import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.component.pane.BorderedTitledPane;
import fr.csmb.competition.component.treeview.ContextableTreeCell;
import fr.csmb.competition.controller.CategorieViewController;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

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

    private BorderPane epreuveBorderPane;
    private File fileTmp;

    private NetworkSender sender = new NetworkSender("", 9878);
    private CategorieViewController categorieViewController;

    public void showView(Stage mainStage, CompetitionBean competition) {
        Preferences pref = Preferences.userNodeForPackage(Main.class);
        String fileName = pref.get("filePath", null);
        fileTmp = new File(fileName);
        this.categorieViewController = new CategorieViewController(competition, mainStage);

        this.competitionBean = competition;
        BorderPane root = (BorderPane)mainStage.getScene().getRoot();
        SplitPane splitPane = new SplitPane();
        root.setCenter(splitPane);
        root.setBottom(null);
        splitPane.setDividerPosition(0, 0.2);
        createTreeView(splitPane);
        createTableView(stackPane);
        splitPane.getItems().add(stackPane);
//        stage.setTitle("Détail compétition : " + competition.getNom());
        mainStage.getScene().getStylesheets().add(getClass().getResource("css/categoriesView.css").toExternalForm());
        mainStage.getScene().getStylesheets().add(getClass().getResource("css/fightView.css").toExternalForm());
//        stage.setScene(scene);
        currentStage = mainStage;
        notificationView = new NotificationView(mainStage);
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

            boolean isTechniqueCreated = false;
            boolean isCombatCreated = false;
            boolean isEpreuveCreated = false;
            TreeItem<String> itemEpreuveTypeTechnique = null;
            TreeItem<String> itemEpreuveTypeCombat = null;
            for (EpreuveBean epreuve : categorie.getEpreuves()) {

                ObservableList<ParticipantBean> list = categorieViewController.extractEpreuves(categorie.getType(), categorie.getNom(), epreuve.getNom());
                if (list.size() <= 0) {
                    continue;
                }
                final TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getNom());
                isEpreuveCreated = true;
                if (TypeEpreuve.TECHNIQUE.getValue().equalsIgnoreCase(epreuve.getType())) {
                    if (!isTechniqueCreated) {
                        itemEpreuveTypeTechnique = new TreeItem<String>(TypeEpreuve.TECHNIQUE.getValue());
                        itemCategorie.getChildren().add(itemEpreuveTypeTechnique);
                        isTechniqueCreated = true;
                    }
                    itemEpreuveTypeTechnique.getChildren().add(itemEpreuve);
                } else if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getType())) {
                    if (!isCombatCreated) {
                        itemEpreuveTypeCombat = new TreeItem<String>(TypeEpreuve.COMBAT.getValue());
                        itemCategorie.getChildren().add(itemEpreuveTypeCombat);
                        isCombatCreated = true;
                    }
                    itemEpreuveTypeCombat.getChildren().add(itemEpreuve);
                }

                epreuve.etatProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                        TreeItem<String> parent = itemEpreuve.getParent();
                        int index = parent.getChildren().indexOf(itemEpreuve);
                        parent.getChildren().remove(itemEpreuve);
                        parent.getChildren().add(index, itemEpreuve);
                    }
                });
            }
            if (isEpreuveCreated) {
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
        }

        TreeView<String> treeView = new TreeView<String>(treeItem);
        final CategoriesView categoriesView = this;
        treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> stringTreeView) {
                return new ContextableTreeCell(categoriesView, competitionBean, categorieViewController);
            }
        });

        treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {

            @Override
            public void changed(
                    ObservableValue<? extends TreeItem<String>> observable,
                    TreeItem<String> old_val, TreeItem<String> new_val) {
                TreeItem<String> selectedItem = new_val;
                if (selectedItem != null && selectedItem.getChildren() != null && selectedItem.getChildren().size() == 0) {
                    String typeCategorie = new_val.getParent().getParent().getParent().getValue();
                    String categorie = new_val.getParent().getParent().getValue();
                    String epreuve = new_val.getValue();
                    EpreuveBean epreuveBean = getEpreuveBean(typeCategorie, categorie, epreuve);
                    stackPane.getChildren().clear();
                    if (epreuveBean != null && epreuveBean.getEtat() != null && epreuveBean.getEtat().equals(EtatEpreuve.DEMARRE.getValue())) {
                        stackPane.getChildren().add(epreuveBorderPane);
                    } else {
                        createTableView(stackPane);
                        updateList(typeCategorie, categorie, epreuve);
                    }
                }
            }

        });

        pane.getChildren().add(treeView);

        splitPane.getItems().add(pane);

    }

    public void createComponentGrid(final String typeCategorie, final String typeEpreuve, final String categorie, final String epreuve) {

        epreuveBorderPane = new BorderPane();
        GridComponent gridComponent = null;
        ParticipantClassementFinalListener participantClassementFinalListener =
                new ParticipantClassementFinalListener(firstPlaceTf, secondPlaceTf, thirdPlaceTf, fourthPlaceTf);
        ObservableList<ParticipantBean> participants = FXCollections.observableArrayList();
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
            } else if (epreuveBean.getEtat().equals(EtatEpreuve.DEMARRE.getValue())) {
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de démarrer une épreuve déjà démarrée");
                return;
            }
            epreuveBean.setEtat(EtatEpreuve.DEMARRE.getValue());
            sender.send(competitionBean, categorieBean, epreuveBean);
            saveCompetitionToXmlFileTmp();

            if (epreuveBean != null) {
                for (ParticipantBean participantBean : epreuveBean.getParticipants())
                    participants.add(participantBean);
            }
        }

        if (typeEpreuve.equals(TypeEpreuve.COMBAT.getValue())) {
            gridComponent = new GridComponentFight2(participants);
        } else if (typeEpreuve.equals(TypeEpreuve.TECHNIQUE.getValue())) {
            gridComponent = new GridComponentTechnical(participants);
        }
        createCartoucheForGridComponent(epreuveBorderPane, categorieBean, epreuveBean);
        gridComponent.setParticipantClassementFinalListener(participantClassementFinalListener);
        gridComponent.drawGrid();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridComponent);
        epreuveBorderPane.setCenter(scrollPane);

        final GridComponent gridComponent2  = gridComponent;
        Button button = new Button("Terminer");
        button.getStyleClass().add("buttonCompetition");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<ParticipantBean> resutlats = gridComponent2.getResultatsList();
                CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
                if (categorieBean != null) {
                    EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                    if (epreuveBean != null) {

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                        String heure = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                        heureFinTf.setText(heure);

                        try {
                            Date dateDebut = simpleDateFormat.parse(heureDebutTf.getText());
                            Date dateFin = simpleDateFormat.parse(heureFinTf.getText());
                            GregorianCalendar calendarDebut = new GregorianCalendar();
                            calendarDebut.setTime(dateDebut);
                            GregorianCalendar calendarFin = new GregorianCalendar();
                            calendarFin.setTime(dateFin);

                            calendarFin.add(Calendar.HOUR_OF_DAY, -calendarDebut.get(Calendar.HOUR_OF_DAY));
                            calendarFin.add(Calendar.MINUTE, -calendarDebut.get(Calendar.MINUTE));

                            String duree = simpleDateFormat.format(calendarFin.getTime());
                            dureeTf.setText(duree);
                        } catch (ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }


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
                        saveCompetitionToXmlFileTmp();
//                        stackPane.getChildren().clear();
//                        createTableView(stackPane);
                    }
                }
            }
        });

        Button cancelButton = new Button("Annuler");
        cancelButton.getStyleClass().add("buttonCompetition");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
                if (categorieBean != null) {
                    EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve);
                    if (epreuveBean != null) {
                        epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
                        sender.send(competitionBean, categorieBean, epreuveBean);
                    }
                }
                stackPane.getChildren().clear();
                createTableView(stackPane);
            }
        });

        HBox hBox = new HBox();
        hBox.getChildren().addAll(button, cancelButton);
        hBox.setSpacing(10);
        epreuveBorderPane.setBottom(hBox);
        stackPane.getChildren().clear();
        stackPane.getChildren().add(epreuveBorderPane);
    }

    private void createCartoucheForGridComponent(BorderPane borderPane, CategorieBean categorieBean, EpreuveBean epreuveBean) {
        adminTf.setText("");
        chronoTf.setText("");
        firstJugeTf.setText("");
        secondJugeTf.setText("");
        thirdJugeTf.setText("");
        fourthJugeTf.setText("");
        fifthJugeTf.setText("");
        tapisTf.setText("");
        heureDebutTf.setText("");
        heureFinTf.setText("");
        dureeTf.setText("");
        firstPlaceTf.setText("");
        secondPlaceTf.setText("");
        thirdPlaceTf.setText("");
        fourthPlaceTf.setText("");

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
        gridPane.add(titleClassement, 6, 0, 2, 1);
        TextField[] tfPlaces = new TextField[] {firstPlaceTf, secondPlaceTf, thirdPlaceTf, fourthPlaceTf};
        for (int i = 0; i < 4; i++) {
            String place = "";
            if (i == 0) {
                place = "1ere Place";
            } else {
                place = String.valueOf(i + 1).concat("ème Place");
            }
            Label firstPlaceLabel = new Label(place);
            firstPlaceLabel.getStyleClass().add("titleGridPane");
            gridPane.add(firstPlaceLabel, 6, 1 + i);
            tfPlaces[i].setPromptText(place);
            gridPane.add(tfPlaces[i], 7, 1 + i);
        }

        Text titleJuge = new Text("Juges");
        titleJuge.getStyleClass().add("biglabelGridPane");
        gridPane.add(titleJuge, 2, 0, 2, 1);
        TextField[] tfJuges = new TextField[]{firstJugeTf, secondJugeTf, thirdJugeTf, fourthJugeTf, fifthJugeTf};
        for (int i = 0; i < 5; i++) {
            Label firstJugeLabel = new Label("Juge ".concat(String.valueOf(i + 1)));
            firstJugeLabel.getStyleClass().add("titleGridPane");
            gridPane.add(firstJugeLabel, 2, 1 + i);
            tfJuges[i].setPromptText("Juge ".concat(String.valueOf(i + 1)));
            gridPane.add(tfJuges[i], 3, 1 + i);
        }

        Text titleEpreuve = new Text("Epreuve");
        titleEpreuve.getStyleClass().add("biglabelGridPane");
        gridPane.add(titleEpreuve, 4, 0, 2, 1);
        TextField[] tfEpreuves = new TextField[] {tapisTf, heureDebutTf, heureFinTf, dureeTf};
        String[] labelEpreuves = new String[] {"Tapis", "Début", "Fin", "Durée"};
        for (int i = 0; i < 4; i++) {
            Label tapisLabel = new Label(labelEpreuves[i]);
            tapisLabel.getStyleClass().add("titleGridPane");
            gridPane.add(tapisLabel, 4, 1 + i);
            tfEpreuves[i].setPromptText(labelEpreuves[i]);
            gridPane.add(tfEpreuves[i], 5, 1 + i);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String heure = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        heureDebutTf.setText(heure);

        Label nbParticipantsLabel = new Label("Nb Participants");
        nbParticipantsLabel.getStyleClass().add("titleGridPane");
        gridPane.add(nbParticipantsLabel, 4, 5);
        Label nbParticipantsTf = new Label(String.valueOf(epreuveBean.getParticipants().size()));
        gridPane.add(nbParticipantsTf, 5, 5);
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
        participantBeans.addAll(this.categorieViewController.extractEpreuves(typeCategorie, categorie, epreuve));
    }

    public void validateEpreuve(String typeCategorie, String categorie, String epreuve) {
        int result = this.categorieViewController.validateEpreuve(typeCategorie, categorie, epreuve);
        switch (result) {
            case 1:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de valider une épreuve terminée");
                break;
            case 2:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de valider une épreuve fusionnée");
                break;
            case 3:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de valider une épreuve validée");
                break;
            case 4:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de valider une épreuve démarrée");
                break;
            default:
                break;
        }
    }

    public void invalidateEpreuve(String typeCategorie, String categorie, String epreuve) {
        int result = this.categorieViewController.invalidateEpreuve(typeCategorie, categorie, epreuve);
        switch (result) {
            case 1:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible d'invalider une épreuve terminée");
                break;
            case 2:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible d'invalider une épreuve fusionnée");
                break;
            case 3:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible d'invalider une épreuve démarrée");
                break;
            default:
                break;
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

        int result = categorieViewController.fusionEpreuve(treeView, typeCategorie1, categorie1, epreuve1, typeCategorie2, categorie2, epreuve2);
        switch (result) {
            case 1:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de fusionner une épreuve fusionnée");
                break;
            case 2:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de fusionner une épreuve terminée");
                break;
            case 3:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de fusionner une épreuve validée");
                break;
            case 4:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de fusionner une épreuve démarrée");
                break;
            default:
                break;
        }


    }

    public EpreuveBean getEpreuveBean(String typeCategorie, String categorie, String epreuve) {
        EpreuveBean epreuveBean = null;
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        if (categorieBean != null) {
            epreuveBean = categorieBean.getEpreuveByName(epreuve);
        }
        return epreuveBean;
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
}
