package fr.csmb.competition.view;

import fr.csmb.competition.component.grid.bean.Phase;
import fr.csmb.competition.component.textbox.NumberTextField;
import fr.csmb.competition.component.textbox.TextBox;
import fr.csmb.competition.model.ParticipantBean;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Created by Administrateur on 08/11/14.
 */
public class FightView {
    int widthRectangle = 150;
    int heightRectangle = 30;
    int spaceBetweenJoueur = 20;
    Color colorBlue = Color.rgb(81, 136, 245, 0.5);
    Color colorRed = Color.rgb(255, 255, 102, 0.5);

    private NumberTextField noteJuge1Part1;
    private TextField noteJuge2Part1;
    private TextField nbPenaltyPart1;
    private TextField resultatPart1;

    private TextField noteJuge1Part2;
    private TextField noteJuge2Part2;
    private TextField nbPenaltyPart2;
    private TextField resultatPart2;

    private TextBox boxBlue;
    private TextBox boxRed;
    private TextBox boxBlueInit;
    private TextBox boxRedInit;
    private TextBox boxVictory;
    private TextBox boxFail;
    private Phase phase;

    public FightView(TextBox boxBlue, TextBox boxRed, TextBox boxVictory, TextBox boxFail, Phase phase) {
        this.boxBlue = new TextBox(boxBlue.getParticipant(), 150, 30, colorBlue);
        this.boxRed = new TextBox(boxRed.getParticipant(), 150, 30, colorRed);
        this.boxBlueInit = boxBlue;
        this.boxRedInit = boxRed;
        this.boxFail = boxFail;
        this.boxVictory = boxVictory;
        this.phase = phase;
    }

    public void showView() {
        final Stage currentStage = new Stage();
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 400, 400);
        currentStage.initModality(Modality.WINDOW_MODAL);
        currentStage.setScene(scene);
        currentStage.sizeToScene();
        currentStage.getScene().getStylesheets().add(getClass().getResource("css/fightView.css").toExternalForm());
        currentStage.getScene().getStylesheets().add(getClass().getResource("css/global.css").toExternalForm());
        Group group = new Group();
        this.boxBlue.setLayoutX(10);
        this.boxBlue.setLayoutY(10);
        this.boxRed.setLayoutX(10);
        int newY = 10 + heightRectangle + spaceBetweenJoueur + heightRectangle;
        this.boxRed.setLayoutY(newY);

        setPart1();
        setPart2();
        this.initializeListner();

        Button valide = new Button("Valider");
        valide.getStyleClass().add("buttonCompetition");
        valide.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Integer resultat1 = Integer.parseInt(resultatPart1.getText());
                Integer resultat2 = Integer.parseInt(resultatPart2.getText());

                if (resultat1 > resultat2) {
                    boxVictory.setParticipant(boxBlue.getParticipant());
                    boxFail.setParticipant(boxRed.getParticipant());
                    boxRedInit.drawFail();
                    switch (phase) {
                        case PETITE_FINALE:
                        case FINALE:
                            break;
                        default:
                            boxBlueInit.setParticipant(new ParticipantBean("", ""));
                            break;
                    }
                } else {
                    boxVictory.setParticipant(boxRed.getParticipant());
                    boxFail.setParticipant(boxBlue.getParticipant());
                    boxBlueInit.drawFail();
                    switch (phase) {
                        case PETITE_FINALE:
                        case FINALE:
                            break;
                        default:
                            boxRedInit.setParticipant(new ParticipantBean("", ""));
                            break;
                    }
                }
                switch (phase) {
                    case PETITE_FINALE:
                        boxVictory.getParticipant().setClassementFinal(3);
                        boxFail.getParticipant().setClassementFinal(4);
                        break;
                    case FINALE:
                        boxVictory.getParticipant().setClassementFinal(1);
                        boxFail.getParticipant().setClassementFinal(2);
                        break;
                    default:
                        break;
                }

                currentStage.close();
            }
        });
        Button annule = new Button("Annuler");
        annule.getStyleClass().add("buttonCompetition");
        annule.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                currentStage.close();
            }
        });

        group.getChildren().addAll(this.boxBlue, this.boxRed, this.noteJuge1Part1, this.noteJuge2Part1, this.nbPenaltyPart1, resultatPart1,
                this.noteJuge1Part2, this.noteJuge2Part2, this.nbPenaltyPart2, resultatPart2);
        borderPane.setCenter(group);
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.getChildren().addAll(valide, annule);
        borderPane.setBottom(hBox);
        currentStage.showAndWait();
    }

    private void setPart1() {
        this.noteJuge1Part1 = new NumberTextField();
        this.noteJuge1Part1.setLayoutX(10);
        this.noteJuge1Part1.setLayoutY(40);
        this.noteJuge1Part1.setPrefSize(50, 30);
        this.noteJuge1Part1.setMinSize(50, 30);
        this.noteJuge1Part1.setMaxSize(50, 30);
        this.noteJuge1Part1.setPromptText("Juge 1");
        this.noteJuge1Part1.getStyleClass().add("tfBlue");

        this.noteJuge2Part1 = new NumberTextField();
        this.noteJuge2Part1.setLayoutX(60);
        this.noteJuge2Part1.setLayoutY(40);
        this.noteJuge2Part1.setPrefSize(50, 30);
        this.noteJuge2Part1.setMinSize(50, 30);
        this.noteJuge2Part1.setMaxSize(50, 30);
        this.noteJuge2Part1.setPromptText("Juge 2");
        this.noteJuge2Part1.getStyleClass().add("tfBlue");

        this.nbPenaltyPart1 = new NumberTextField();
        this.nbPenaltyPart1.setLayoutX(110);
        this.nbPenaltyPart1.setLayoutY(40);
        this.nbPenaltyPart1.setPrefSize(50, 30);
        this.nbPenaltyPart1.setMinSize(50, 30);
        this.nbPenaltyPart1.setMaxSize(50, 30);
        this.nbPenaltyPart1.getStyleClass().add("tfBlue");
        this.nbPenaltyPart1.setPromptText("Pen.");

        this.resultatPart1 = new NumberTextField();
        this.resultatPart1.setLayoutX(160);
        this.resultatPart1.setLayoutY(40);
        this.resultatPart1.setPrefSize(50, 30);
        this.resultatPart1.setMinSize(50, 30);
        this.resultatPart1.setMaxSize(50, 30);
        this.resultatPart1.getStyleClass().add("tfBlue");
        this.resultatPart1.setPromptText("Total");

    }

    private void setPart2() {

        int newY = 10 + heightRectangle + spaceBetweenJoueur + heightRectangle;
        int newY2 = newY + heightRectangle;
        this.noteJuge1Part2 = new NumberTextField();
        this.noteJuge1Part2.setLayoutX(10);
        this.noteJuge1Part2.setLayoutY(newY2);
        this.noteJuge1Part2.setPrefSize(50, 30);
        this.noteJuge1Part2.setMinSize(50, 30);
        this.noteJuge1Part2.setMaxSize(50, 30);
        this.noteJuge1Part2.setPromptText("Juge 1");
        this.noteJuge1Part2.getStyleClass().add("tfRed");

        this.noteJuge2Part2 = new NumberTextField();
        this.noteJuge2Part2.setLayoutX(60);
        this.noteJuge2Part2.setLayoutY(newY2);
        this.noteJuge2Part2.setPrefSize(50, 30);
        this.noteJuge2Part2.setMinSize(50, 30);
        this.noteJuge2Part2.setMaxSize(50, 30);
        this.noteJuge2Part2.setPromptText("Juge 2");
        this.noteJuge2Part2.getStyleClass().add("tfRed");

        this.nbPenaltyPart2 = new NumberTextField();
        this.nbPenaltyPart2.setLayoutX(110);
        this.nbPenaltyPart2.setLayoutY(newY2);
        this.nbPenaltyPart2.setPrefSize(50, 30);
        this.nbPenaltyPart2.setMinSize(50, 30);
        this.nbPenaltyPart2.setMaxSize(50, 30);
        this.nbPenaltyPart2.setPromptText("Pen.");
        this.nbPenaltyPart2.getStyleClass().add("tfRed");

        this.resultatPart2 = new NumberTextField();
        this.resultatPart2.setLayoutX(160);
        this.resultatPart2.setLayoutY(newY2);
        this.resultatPart2.setPrefSize(50, 30);
        this.resultatPart2.setMinSize(50, 30);
        this.resultatPart2.setMaxSize(50, 30);
        this.resultatPart2.getStyleClass().add("tfRed");
        this.resultatPart2.setPromptText("Total");
    }

    private void initializeListner() {
        EventHandler keyPressedPart1 = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    resultatPart1.setText(String.valueOf(computeResultat(noteJuge1Part1, noteJuge2Part1, nbPenaltyPart1)));
                }
            }
        };

        ChangeListener focusListenerPart1 = new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (oldValue) {
                    resultatPart1.setText(String.valueOf(computeResultat(noteJuge1Part1, noteJuge2Part1, nbPenaltyPart1)));
                }
            }
        };

        this.noteJuge1Part1.setOnKeyReleased(keyPressedPart1);
        this.noteJuge1Part1.focusedProperty().addListener(focusListenerPart1);
        this.noteJuge2Part1.setOnKeyReleased(keyPressedPart1);
        this.noteJuge2Part1.focusedProperty().addListener(focusListenerPart1);
        this.nbPenaltyPart1.setOnKeyReleased(keyPressedPart1);
        this.nbPenaltyPart1.focusedProperty().addListener(focusListenerPart1);

        EventHandler keyPressedPart2 = new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    resultatPart2.setText(String.valueOf(computeResultat(noteJuge1Part2, noteJuge2Part2, nbPenaltyPart2)));
                }
            }
        };

        ChangeListener focusListenerPart2 = new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (oldValue) {
                    resultatPart2.setText(String.valueOf(computeResultat(noteJuge1Part2, noteJuge2Part2, nbPenaltyPart2)));
                }
            }
        };

        this.noteJuge1Part2.setOnKeyReleased(keyPressedPart2);
        this.noteJuge1Part2.focusedProperty().addListener(focusListenerPart2);
        this.noteJuge2Part2.setOnKeyReleased(keyPressedPart2);
        this.noteJuge2Part2.focusedProperty().addListener(focusListenerPart2);
        this.nbPenaltyPart2.setOnKeyReleased(keyPressedPart2);
        this.nbPenaltyPart2.focusedProperty().addListener(focusListenerPart2);
    }

    private Integer computeResultat(TextField juge1, TextField juge2, TextField pen) {
        int note1 = 0;
        if (!juge1.getText().equals("")) {
            note1 = Integer.parseInt(juge1.getText());
        }
        int note2 = 0;
        if (!juge2.getText().equals("")) {
            note2 = Integer.parseInt(juge2.getText());
        }
        int penalty = 0;
        if (!pen.getText().equals("")) {
            penalty = Integer.parseInt(pen.getText());
        }
        Integer resultat = note1 + note2 - penalty;
        return resultat;
    }
}
