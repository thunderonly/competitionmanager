package fr.csmb.competition.controller;

import fr.csmb.competition.Main;
import fr.csmb.competition.view.CategoriesView;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by Administrateur on 13/10/14.
 */
public class Controller {

    private Main main;

    @FXML
    private MenuItem createCompetition;
    @FXML
    private MenuItem createCategorie;
    @FXML
    private MenuItem createEpreuve;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void handleCreateCompetition() {
        main.showCreationCompetitionView();
    }

    @FXML
    private void handleCreateCategorie() {
        main.showCreationCategorieView();
    }

    @FXML
    private void handleCreateEpreuve() {
        main.showCreationEpreuveView();
    }

    @FXML
    private void handleManage() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(main.getMainStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.loadCompetitionToXmlFile(file);
        }
    }

    @FXML
    private void handleLoadInscription() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(main.getMainStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xlsx")) {
                file = new File(file.getPath() + ".xlsx");
            }
            main.loadInscriptionFile(file);
        }
    }

    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(main.getMainStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.saveCompetitionToXmlFile(file);
        }
    }

    @FXML
    private void handleManageCategories() {
        main.showCompetitionView();
    }

    @FXML
    private void handleShowResultats() {
        main.showResultatsView();
    }

    public MenuItem getCreateCompetition() {
        return createCompetition;
    }

    public MenuItem getCreateCategorie() {
        return createCategorie;
    }

    public MenuItem getCreateEpreuve() {
        return createEpreuve;
    }
}
