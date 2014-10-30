package fr.csmb.competition.view;

import fr.csmb.competition.component.pane.BorderedTitledPane;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.type.TypeCategorie;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Epreuve;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Created by Administrateur on 22/10/14.
 */
public class CreateCompetitionView {

    private TextField textFieldNomCompetition = new TextField();
    private TextField nomCategorieTF = new TextField();
    private TextField nomEpreuveTF = new TextField();
    private ListView<CategorieBean> categorieListDispo = new ListView<CategorieBean>();
    private ListView<CategorieBean> categorieListChoix = new ListView<CategorieBean>();
    private ListView<String> epreuveListChoix = new ListView<String>();
    private CheckBox masculin = new CheckBox("Masculin");
    private CheckBox feminin = new CheckBox("Féminin");
    private RadioButton technique = new RadioButton("Technique");
    private RadioButton combat = new RadioButton("Combat");
    private NotificationView notificationView;
    private Stage stage;

    public void showCreateCompetitionView(Stage mainStage, final CompetitionBean competitionBean) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 400, 275);
        stage = new Stage();
        stage.setTitle("Création compétition");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        stage.setScene(scene);

        Text scenetitle = new Text("Créer une compétition");
        scenetitle.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Nom de la compétition :");
        grid.add(userName, 0, 1);

        grid.add(textFieldNomCompetition, 1, 1);
        Button createBtn = new Button("Créer");
        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                competitionBean.setNom(textFieldNomCompetition.getText());
                stage.close();
            }
        });
        grid.add(createBtn, 0, 2);
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        grid.add(cancelBtn, 1, 2);
        stage.showAndWait();
    }

    public void showCreateCategorieView(Stage mainStage, final CompetitionBean competition) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 800, 600);
        scene.getStylesheets().add(getClass().getResource("css/createCompetitionView.css").toExternalForm());
        stage = new Stage();
        stage.setTitle("Compétition ".concat(competition.getNom()).concat(" - Création catégorie"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        stage.setScene(scene);

        Text scenetitle = new Text("Créer de catégorie");
        scenetitle.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Nom de la catégorie :");
        grid.add(userName, 0, 1);
        grid.add(nomCategorieTF, 1, 1);

        HBox sexeBox = new HBox();
        sexeBox.getChildren().addAll(masculin, feminin);
        BorderedTitledPane titledPane = new BorderedTitledPane("Sexe", sexeBox);
        grid.add(titledPane, 0, 2);

        Button addBtn = new Button("Ajouter");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (masculin.isSelected()) {
                    CategorieBean categorie = new CategorieBean(nomCategorieTF.getText());
                    categorie.setType(TypeCategorie.MASCULIN.getValue());
                    categorieListDispo.getItems().add(categorie);
                }

                if (feminin.isSelected()) {
                    CategorieBean categorie = new CategorieBean(nomCategorieTF.getText());
                    categorie.setType(TypeCategorie.FEMININ.getValue());
                    categorieListDispo.getItems().add(categorie);
                }
            }
        });
        grid.add(addBtn, 1, 2);

        categorieListDispo.getItems().addAll(competition.getCategories());
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(categorieListDispo);
        BorderedTitledPane cateDispoTitlePane = new BorderedTitledPane("Catégories créées", stackPane);
        grid.add(cateDispoTitlePane, 0, 3);

        Button createBtn = new Button("Valider");
        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                competition.setCategories(categorieListDispo.getItems());
                stage.close();
            }
        });
        grid.add(createBtn, 0, 4);
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        grid.add(cancelBtn, 1, 4);
        stage.showAndWait();
    }



    public void showCreateEpreuveView(Stage mainStage, final CompetitionBean competition) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Scene scene = new Scene(grid, 800, 600);
        scene.getStylesheets().add(getClass().getResource("css/createCompetitionView.css").toExternalForm());
        stage = new Stage();
        stage.setTitle("Compétition ".concat(competition.getNom()).concat(" - Création épreuve"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(mainStage);
        stage.setScene(scene);

        Text scenetitle = new Text("Création d'épreuve");
        scenetitle.setFont(javafx.scene.text.Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Nom de l'épreuve :");
        grid.add(userName, 0, 1);
        grid.add(nomEpreuveTF, 1, 1);


        HBox typeBox = new HBox();
        ToggleGroup toggleGroup = new ToggleGroup();
        technique.setToggleGroup(toggleGroup);
        technique.setSelected(true);
        combat.setToggleGroup(toggleGroup);
        typeBox.getChildren().addAll(technique, combat);
        BorderedTitledPane titledPane = new BorderedTitledPane("Type", typeBox);
        grid.add(titledPane, 0, 2);

        GridPane gridPane = new GridPane();
        categorieListDispo.getItems().addAll(competition.getCategories());
        categorieListDispo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() >= 2) {
                    CategorieBean categorie = categorieListDispo.getSelectionModel().getSelectedItem();
//                    CategorieBean newCategorie = categorie.clone();
                    categorieListChoix.getItems().add(categorie);
                    categorieListDispo.getItems().remove(categorie);
                }
            }
        });
        gridPane.add(categorieListDispo, 0, 0);

        categorieListChoix.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() >= 2) {
                    CategorieBean categorie = categorieListChoix.getSelectionModel().getSelectedItem();
                    categorieListDispo.getItems().add(categorie);
                    categorieListChoix.getItems().remove(categorie);
                }
            }
        });
        gridPane.add(categorieListChoix, 1, 0);

        BorderedTitledPane titledPaneCategorie = new BorderedTitledPane("Catégories", gridPane);
        grid.add(titledPaneCategorie, 0, 3);

        Button addBtn = new Button("Ajouter");
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (CategorieBean categorie : categorieListChoix.getItems()) {
                    EpreuveBean epreuve = new EpreuveBean(nomEpreuveTF.getText());
                    if (technique.isSelected()) {
                        epreuve.setType(TypeEpreuve.TECHNIQUE.getValue());
                    } else if (combat.isSelected()) {
                        epreuve.setType(TypeEpreuve.COMBAT.getValue());
                    }
                    categorie.getEpreuves().add(epreuve);
                    String textEpreuve = categorie.toString().concat(" / ").concat(epreuve.toString());
                    epreuveListChoix.getItems().add(textEpreuve);
                }
            }
        });
        grid.add(addBtn, 1, 3);

        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(epreuveListChoix);
        BorderedTitledPane epreuveDispoTitlePane = new BorderedTitledPane("Epreuves créées", stackPane);
        grid.add(epreuveDispoTitlePane, 0, 4);

        Button createBtn = new Button("Valider");
        createBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (CategorieBean categorie1 : categorieListChoix.getItems()) {
                    CategorieBean categorieCompetition = competition.getCategories().get(competition.getCategories().indexOf(categorie1));
                    if (categorieCompetition != null) {
                        categorieCompetition.setEpreuves(categorie1.getEpreuves());
                    }
                }
                stage.close();
            }
        });
        grid.add(createBtn, 0, 5);
        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });
        grid.add(cancelBtn, 1, 5);
        stage.showAndWait();
    }


}
