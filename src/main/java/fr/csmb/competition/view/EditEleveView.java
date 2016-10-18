package fr.csmb.competition.view;

import fr.csmb.competition.component.pane.BorderedTitledPane;
import fr.csmb.competition.controller.EditEleveController;
import fr.csmb.competition.model.*;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.Competition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 11/11/14.
 */
public class EditEleveView {

    private EditEleveController editEleveController;
    private Stage currentStage;
    private ClubBean clubBean;
    private CompetitionBean competitionBean;
    private TextField licenceEleveTf;
    private TextField nomEleveTf;
    private TextField prenomEleveTf;
    private TextField poidEleveTf;
    private TextField ageEleveTf;
    private ComboBox<TypeCategorie> sexeEleveCb;
    private List<CheckBox> epreuveCheckBox;

    private EleveBean eleveBean;

    public EditEleveView(Stage mainStage, CompetitionBean competitionBean, ClubBean clubBean) {
        this.editEleveController = new EditEleveController();
        this.currentStage = mainStage;
        this.clubBean = clubBean;
        this.competitionBean = competitionBean;
        this.editEleveController.setCompetitionBean(competitionBean);
        this.editEleveController.setClubBean(clubBean);
    }

    public void showView() {
        final BorderPane root = (BorderPane) currentStage.getScene().getRoot();

        currentStage.getScene().getStylesheets().add(getClass().getResource("css/createCompetitionView.css").toExternalForm());
        currentStage.getScene().getStylesheets().add(getClass().getResource("css/global.css").toExternalForm());

        final Node oldCenter = root.getCenter();
        final Node oldBottom = root.getBottom();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 0, 0, 10));
        root.setCenter(gridPane);

        GridPane gridInfoEleve = new GridPane();
        gridInfoEleve.setHgap(10);
        gridInfoEleve.setVgap(10);
        Label licenceEleve = new Label("Licence");
        licenceEleveTf = new TextField();
        gridInfoEleve.add(licenceEleve, 0, 0);
        gridInfoEleve.add(licenceEleveTf, 1, 0);

        Label nomEleve = new Label("Nom");
        nomEleveTf = new TextField();
        gridInfoEleve.add(nomEleve, 0, 1);
        gridInfoEleve.add(nomEleveTf, 1, 1);

        Label prenomEleve = new Label("Prénom");
        prenomEleveTf = new TextField();
        gridInfoEleve.add(prenomEleve, 2, 1);
        gridInfoEleve.add(prenomEleveTf, 3, 1);

        Label poidEleve = new Label("Poids");
        poidEleveTf = new TextField();
        gridInfoEleve.add(poidEleve, 0, 2);
        gridInfoEleve.add(poidEleveTf, 1, 2);

        Label ageEleve = new Label("Age");
        ageEleveTf = new TextField();
        gridInfoEleve.add(ageEleve, 2, 2);
        gridInfoEleve.add(ageEleveTf, 3, 2);

        Label sexeEleve = new Label("Sexe");
        ObservableList<TypeCategorie> listSexe = FXCollections.observableArrayList(TypeCategorie.values());
        sexeEleveCb = new ComboBox<TypeCategorie>(listSexe);
        gridInfoEleve.add(sexeEleve, 0, 3);
        gridInfoEleve.add(sexeEleveCb, 1, 3);

        BorderedTitledPane borderedTitledPane = new BorderedTitledPane("Informations élève", gridInfoEleve);
        gridPane.add(borderedTitledPane, 0, 0);

        int i = 0;
        GridPane hBoxEpreuve = new GridPane();
        epreuveCheckBox = new ArrayList<CheckBox>();
        int indexCol = 0;
        int indexRow = 0;
        for (DisciplineBean disciplineBean : competitionBean.getDisciplines()) {
            if (disciplineBean.getType().equals(TypeEpreuve.TECHNIQUE.getValue())) {
                CheckBox checkBox = new CheckBox(disciplineBean.getNom());
                epreuveCheckBox.add(checkBox);
                if (indexCol % 4 == 0) {
                    indexRow ++;
                    indexCol = 0;
                }
                hBoxEpreuve.add(checkBox, indexCol, indexRow);
                indexCol++;
            }
        }
        CheckBox combat = new CheckBox(TypeEpreuve.COMBAT.getValue());
        epreuveCheckBox.add(combat);
        hBoxEpreuve.getChildren().add(combat);

        BorderedTitledPane borderedTitledPaneEpreuves = new BorderedTitledPane("Epreuves", hBoxEpreuve);
        gridPane.add(borderedTitledPaneEpreuves, 0, 1);

        Button validateButton = new Button("Valider");
        validateButton.getStyleClass().add("buttonCompetition");
        validateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                submit();
                root.setCenter(oldCenter);
                root.setBottom(oldBottom);
            }
        });

        Button annulerButton = new Button("Annuler");
        annulerButton.getStyleClass().add("buttonCompetition");
        annulerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                root.setCenter(oldCenter);
                root.setBottom(oldBottom);
            }
        });
        HBox hBoxButton = new HBox(20);
        hBoxButton.getChildren().addAll(validateButton, annulerButton);
        root.setBottom(hBoxButton);
    }

    public void showView(EleveBean eleveBean) {
        showView();
        this.eleveBean = eleveBean;
        this.licenceEleveTf.setText(eleveBean.getLicence());
        this.nomEleveTf.setText(eleveBean.getNom());
        this.prenomEleveTf.setText(eleveBean.getPrenom());
        this.ageEleveTf.setText(eleveBean.getAge());
        this.poidEleveTf.setText(eleveBean.getPoids());
        this.sexeEleveCb.getSelectionModel().select(TypeCategorie.getByValue(eleveBean.getSexe()));
        for (CheckBox epreuveCb : this.epreuveCheckBox) {
            for (String epreuve : eleveBean.getEpreuves()) {
                if (epreuve.contains("-") && epreuveCb.getText().equals(TypeEpreuve.COMBAT.getValue())) {
                    epreuveCb.setSelected(true);
                } else {
                    if (epreuveCb.getText().equals(epreuve)) {
                        epreuveCb.setSelected(true);
                    }
                }
            }
        }
    }

    private void submit() {
        String licence = this.licenceEleveTf.getText();
        String nom = this.nomEleveTf.getText();
        String prenom = this.prenomEleveTf.getText();
        String age = this.ageEleveTf.getText();
        String poids = this.poidEleveTf.getText();
        TypeCategorie sexe = this.sexeEleveCb.getSelectionModel().getSelectedItem();
        List<String> epreuves = new ArrayList<String>();
        for (CheckBox checkBox : this.epreuveCheckBox) {
            if (checkBox.isSelected()) {
                epreuves.add(checkBox.getText());
            }
        }
        if (eleveBean != null) {
            editEleveController.updateEleve(eleveBean, licence, nom, prenom, age, poids, sexe, epreuves);
        } else {
            editEleveController.addEleve(licence, nom, prenom, age, poids, sexe, epreuves);
        }

    }
}
