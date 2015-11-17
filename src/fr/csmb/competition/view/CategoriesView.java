package fr.csmb.competition.view;

import fr.csmb.competition.Main;
import fr.csmb.competition.component.treeview.ContextableTreeCell;
import fr.csmb.competition.controller.CategorieViewController;
import fr.csmb.competition.controller.DetailCategorieController;
import fr.csmb.competition.controller.GridCategorieController;
import fr.csmb.competition.listener.EtatPropertyEpreuveChangeListener;
import fr.csmb.competition.listener.LabelPropertyEpreuveChangeListener;
import fr.csmb.competition.model.*;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

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

    private CategorieViewController categorieViewController;

    private Map<EpreuveBean, BorderPane> borderPaneMap =
            new HashMap<EpreuveBean, BorderPane>();

    public void showView(Stage mainStage, CompetitionBean competition) {
        Preferences pref = Preferences.userNodeForPackage(Main.class);
        String fileName = pref.get("filePath", null);
        fileTmp = new File(fileName);

        notificationView = new NotificationView(mainStage);
        this.categorieViewController = new CategorieViewController(competition, mainStage, notificationView);

        this.competitionBean = competition;
        BorderPane root = (BorderPane)mainStage.getScene().getRoot();
        SplitPane splitPane = new SplitPane();
        root.setCenter(splitPane);
        root.setBottom(null);
        splitPane.setDividerPosition(0, 0.2);
        createTreeView(splitPane);
        createTableView(stackPane, null);
        splitPane.getItems().add(stackPane);
//        stage.setTitle("Détail compétition : " + competition.getNom());
        mainStage.getScene().getStylesheets().add(getClass().getResource("css/categoriesView.css").toExternalForm());
        mainStage.getScene().getStylesheets().add(getClass().getResource("css/fightView.css").toExternalForm());
//        stage.setScene(scene);
        currentStage = mainStage;
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
            for (EpreuveBean epreuve : competitionBean.getEpreuveByCategorie(categorie)) {

                if (competitionBean.getParticipantByEpreuve(epreuve).isEmpty()) {
                    continue;
                }
                final TreeItem<String> itemEpreuve = new TreeItem<String>(epreuve.getLabel());
                isEpreuveCreated = true;
                if (TypeEpreuve.TECHNIQUE.getValue().equalsIgnoreCase(epreuve.getDiscipline().getType())) {
                    if (!isTechniqueCreated) {
                        itemEpreuveTypeTechnique = new TreeItem<String>(TypeEpreuve.TECHNIQUE.getValue());
                        itemCategorie.getChildren().add(itemEpreuveTypeTechnique);
                        isTechniqueCreated = true;
                    }
                    itemEpreuveTypeTechnique.getChildren().add(itemEpreuve);
                } else if (TypeEpreuve.COMBAT.getValue().equalsIgnoreCase(epreuve.getDiscipline().getType())) {
                    if (!isCombatCreated) {
                        itemEpreuveTypeCombat = new TreeItem<String>(TypeEpreuve.COMBAT.getValue());
                        itemCategorie.getChildren().add(itemEpreuveTypeCombat);
                        isCombatCreated = true;
                    }
                    itemEpreuveTypeCombat.getChildren().add(itemEpreuve);
                }

                epreuve.etatProperty().addListener(new EtatPropertyEpreuveChangeListener(itemEpreuve, notificationView));
                epreuve.labelProperty().addListener(new LabelPropertyEpreuveChangeListener(itemEpreuve));
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
                    String typeEpreuve = new_val.getParent().getValue();
                    EpreuveBean epreuveBean = getEpreuveBean(typeCategorie, categorie, epreuve);
                    stackPane.getChildren().clear();
                    if (epreuveBean != null && epreuveBean.getEtat() != null &&
                            (epreuveBean.getEtat().equals(EtatEpreuve.DEMARRE.getValue()) ||
                            epreuveBean.getEtat().equals(EtatEpreuve.TERMINE.getValue()))) {
                        if (borderPaneMap.containsKey(epreuveBean)) {
                            epreuveBorderPane = borderPaneMap.get(epreuveBean);
                        } else {
                            //Epreuve is Beginned or terminated but not exist
                            createGridComponentView(typeCategorie, typeEpreuve, categorie, epreuve);
                            if (epreuveBean != null) {
                                updateList(epreuveBean);
                            }
                        }
                        stackPane.getChildren().add(epreuveBorderPane);
                    } else {
                        createTableView(stackPane, epreuveBean);
                        if (epreuveBean != null) {
                            updateList(epreuveBean);
                        }
                    }
                }
            }

        });

        pane.getChildren().add(treeView);

        splitPane.getItems().add(pane);

    }

    public void startEpreuve(final String typeCategorie, final String typeEpreuve, final String categorie, final String epreuve) {
        epreuveBorderPane = new BorderPane();
        int result = this.categorieViewController.startEpreuve(typeCategorie, typeEpreuve, categorie, epreuve);

        switch (result) {

            case 1:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de démarrer une épreuve non validée");
                break;
            case 2:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de démarrer une épreuve terminée");
                break;
            case 3:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de démarrer une épreuve fusionnée");
                break;
            case 4:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible de démarrer une épreuve déjà démarrée");
                break;
            case 0:
                createGridComponentView(typeCategorie, typeEpreuve, categorie, epreuve);
                break;
            default:
                break;
        }
    }

    public void createGridComponentView(final String typeCategorie, final String typeEpreuve, final String categorie, final String epreuve) {
        EpreuveBean epreuveBean = getEpreuveBean(typeCategorie, categorie, epreuve);

        epreuveBorderPane = new BorderPane();

        GridCategorieController gridCategorieController = null;
        if (borderPaneMap.containsKey(epreuveBean)) {
            epreuveBorderPane = borderPaneMap.get(epreuveBean);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("fxml/categorieGridView.fxml"));
                epreuveBorderPane = (BorderPane) loader.load();
                gridCategorieController = loader.getController();
                borderPaneMap.put(epreuveBean, epreuveBorderPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gridCategorieController.setCategorieView(this);
            gridCategorieController.initGrid(competitionBean, typeCategorie, categorie, typeEpreuve, epreuve);

//            competitionBean.getParticipants().addListener(new ListChangeListener<ParticipantBean>() {
//                @Override
//                public void onChanged(Change<? extends ParticipantBean> c) {
//                    while (c.next()) {
//                        if (c.wasPermutated()) {
//                            for (int i = c.getFrom(); i < c.getTo(); ++i) {
//                                //permutate
//                            }
//                        } else if (c.wasUpdated()) {
//                            //update item
//                        } else {
//                            for (ParticipantBean remitem : c.getRemoved()) {
//                                updateList(remitem.getEpreuveBean());
//                            }
//                            for (ParticipantBean additem : c.getAddedSubList()) {
//                                updateList(additem.getEpreuveBean());
//                            }
//                        }
//                    }
//                }
//            });
        }

        stackPane.getChildren().clear();
        stackPane.getChildren().add(epreuveBorderPane);
    }

    public void handleCancelEpreuve(EpreuveBean epreuveBean) {
        stackPane.getChildren().clear();
        createTableView(stackPane, epreuveBean);
        if (borderPaneMap.containsKey(epreuveBean)) {
            borderPaneMap.remove(epreuveBean);
        }
    }

    private void createTableView(StackPane stackPane, EpreuveBean epreuveBean) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/categorieDetailView.fxml"));
            BorderPane borderPane = (BorderPane) loader.load();
            DetailCategorieController detailCategorieController = loader.getController();
            detailCategorieController.setCompetitionBean(competitionBean);
            detailCategorieController.setEpreuveBean(epreuveBean);
            participantBeans = detailCategorieController.getTableParticipant().getItems();

            stackPane.getChildren().add(borderPane);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateList(EpreuveBean epreuveBean) {
        participantBeans.clear();
        participantBeans.addAll(competitionBean.getParticipantPresentByEpreuve(epreuveBean));
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

    public void renameEpreuve(String typeCategorie, String categorie, String epreuve) {
        int result = this.categorieViewController.renameEpreuve(typeCategorie, categorie, epreuve);
        switch (result) {
            case 1:
                notificationView.notify(NotificationView.Level.INFO, "Information",
                        "Epreuve renommée");
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
                        "Impossible d'invalider une épreuve démarrée");
                break;
            default:
                break;
        }
    }

    public void deleteEpreuve(TreeView<String> treeView, String typeCategorie, String categorie, String epreuve) {
        int result = this.categorieViewController.deleteEpreuve(treeView, typeCategorie, categorie, epreuve);
        switch (result) {
            case 1:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible d'invalider une épreuve terminée");
                break;
            case 2:
                notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                        "Impossible d'invalider une épreuve démarrée");
                break;
            default:
                break;
        }
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
        CategorieBean categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        DisciplineBean disciplineBean = competitionBean.getDiscipline(epreuve);
        EpreuveBean epreuveBean = null;
        if (categorieBean != null && disciplineBean != null) {
            epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
        }
        if (disciplineBean == null) {
            epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean, epreuve);
        }
        return epreuveBean;
    }

}
