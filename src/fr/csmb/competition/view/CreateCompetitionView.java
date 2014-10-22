package fr.csmb.competition.view;

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

    private Competition competition;
    private Categorie categorie;

    private TextField textFieldNomCompetition = new TextField();
    private TextField nomCategorieTF = new TextField();
    private TextField nomEpreuveTF = new TextField();
    private ListView<Categorie> categorieListDispo = new ListView<Categorie>();
    private ListView<Categorie> categorieListChoix = new ListView<Categorie>();
    private ListView<String> epreuveListChoix = new ListView<String>();
    private CheckBox masculin = new CheckBox("Masculin");
    private CheckBox feminin = new CheckBox("Féminin");
    private RadioButton technique = new RadioButton("Technique");
    private RadioButton combat = new RadioButton("Combat");
    private ObservableList<Competition> competitions;
    private NotificationView notificationView;
    private Stage stage;

    public void showCreateCompetitionView(Stage mainStage, final ObservableList<Competition> competitions) {
        this.competitions = competitions;
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
                Competition competition1 = new Competition(textFieldNomCompetition.getText());
                competitions.add(competition1);
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

    public void showCreateCategorieView(Stage mainStage, final Competition competition) {
        this.competition = competition;
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
                    Categorie categorie = new Categorie();
                    categorie.setNomCategorie(nomCategorieTF.getText());
                    categorie.setTypeCategorie(TypeCategorie.MASCULIN.getValue());
                    categorieListDispo.getItems().add(categorie);
                }

                if (feminin.isSelected()) {
                    Categorie categorie = new Categorie();
                    categorie.setNomCategorie(nomCategorieTF.getText());
                    categorie.setTypeCategorie(TypeCategorie.FEMININ.getValue());
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



    public void showCreateEpreuveView(Stage mainStage, final Competition competition) {
        this.competition = competition;
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
                    Categorie categorie1 = categorieListDispo.getSelectionModel().getSelectedItem();
                    Categorie newCategorie = categorie1.clone();
                    categorieListChoix.getItems().add(newCategorie);
                    categorieListDispo.getItems().remove(categorie1);
                }
            }
        });
        gridPane.add(categorieListDispo, 0, 0);

        categorieListChoix.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() >= 2) {
                    Categorie categorie1 = categorieListChoix.getSelectionModel().getSelectedItem();
                    categorieListDispo.getItems().add(categorie1);
                    categorieListChoix.getItems().remove(categorie1);
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
                for (Categorie categorie1 : categorieListChoix.getItems()) {
                    Epreuve epreuve = new Epreuve();
                    epreuve.setNomEpreuve(nomEpreuveTF.getText());
                    if (technique.isSelected()) {
                        epreuve.setTypeEpreuve(TypeEpreuve.TECHNIQUE.getValue());
                    } else if (combat.isSelected()) {
                        epreuve.setTypeEpreuve(TypeEpreuve.COMBAT.getValue());
                    }
                    categorie1.getEpreuves().add(epreuve);
                    String textEpreuve = categorie1.toString().concat(" / ").concat(epreuve.toString());
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
                for (Categorie categorie1 : categorieListChoix.getItems()) {
                    Categorie categorieCompetition = competition.getCategories().get(competition.getCategories().indexOf(categorie1));
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

    /** Places content in a bordered pane with a title. */
    public class BorderedTitledPane extends StackPane {
        private StringProperty title = new SimpleStringProperty();
        private ObjectProperty<Node> graphic = new SimpleObjectProperty<Node>();
        private ObjectProperty<Node> content = new SimpleObjectProperty<Node>();
        private ObjectProperty<Pos>  titleAlignment = new SimpleObjectProperty<Pos>();
        // todo other than TOP_LEFT other alignments aren't really supported correctly, due to translation fudge for indentation of the title label in css => best to implement layoutChildren and handle layout there.
        // todo work out how to make content the default node for fxml so you don't need to write a <content></content> tag.

        public BorderedTitledPane() {
            this("", null);
        }

        public BorderedTitledPane(String titleString, Node contentNode) {
            final Label titleLabel = new Label();
            titleLabel.textProperty().bind(Bindings.concat(title, " "));
            titleLabel.getStyleClass().add("bordered-titled-title");
            titleLabel.graphicProperty().bind(graphic);

            titleAlignment.addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    StackPane.setAlignment(titleLabel, titleAlignment.get());
                }
            });

            final StackPane contentPane = new StackPane();

            getStyleClass().add("bordered-titled-border");
            getChildren().addAll(titleLabel, contentPane);

            content.addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (content.get() == null) {
                        contentPane.getChildren().clear();
                    } else {
                        if (!content.get().getStyleClass().contains("bordered-titled-content")) {
                            content.get().getStyleClass().add("bordered-titled-content");  // todo would be nice to remove this style class when it is no longer required.
                        }
                        contentPane.getChildren().setAll(content.get());
                    }
                }
            });

            titleAlignment.set(Pos.TOP_LEFT);
            this.title.set(titleString);
            this.content.set(contentNode);
        }

        public String getTitle() {
            return title.get();
        }

        public StringProperty getTitleStringProperty() {
            return title;
        }

        public void setTitle(String title) {
            this.title.set(title);
        }

        public Pos getTitleAlignment() {
            return titleAlignment.get();
        }

        public ObjectProperty<Pos> titleAlignmentProperty() {
            return titleAlignment;
        }

        public void setTitleAlignment(Pos titleAlignment) {
            this.titleAlignment.set(titleAlignment);
        }

        public Node getContent() {
            return content.get();
        }

        public ObjectProperty<Node> contentProperty() {
            return content;
        }

        public void setContent(Node content) {
            this.content.set(content);
        }

        public Node getGraphic() {
            return graphic.get();
        }

        public ObjectProperty<Node> graphicProperty() {
            return graphic;
        }

        public void setGraphic(Node graphic) {
            this.graphic.set(graphic);
        }
    }
}
