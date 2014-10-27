package fr.csmb.competition;

import fr.csmb.competition.controller.ClubController;
import fr.csmb.competition.controller.Controller;
import fr.csmb.competition.manager.InscriptionsManager;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.view.CategoriesView;
import fr.csmb.competition.view.CreateCompetitionView;
import fr.csmb.competition.view.NotificationView;
import fr.csmb.competition.view.ResultatsView;
import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Eleve;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrateur on 13/10/14.
 */
public class Main extends Application {

    private Stage mainStage;
    private BorderPane borderPane;

    private ObservableList<Competition> competitionData = FXCollections.observableArrayList();
    private ObservableList<ClubBean> clubs = FXCollections.observableArrayList();
    private NotificationView notificationView;
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        mainStage = primaryStage;
        notificationView = new NotificationView(mainStage);
        primaryStage.setTitle("Competition Manager");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("fxml/mainView.fxml"));
        borderPane = (BorderPane) loader.load();
        controller =((Controller) loader.getController());
        controller.setMain(this);
        controller.getCreateCategorie().setDisable(true);
        controller.getCreateEpreuve().setDisable(true);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void saveCompetitionToXmlFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(Competition.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            marshaller.marshal(competitionData.get(0), file);
            notificationView.notify(NotificationView.Level.SUCCESS, "Sauvegarde", "Compétition sauvegardée avec succès");

        } catch (JAXBException e) {
            e.printStackTrace();
            notificationView.notify(NotificationView.Level.SUCCESS, "Erreur", "Erreur lors de la sauvegarde de la compétition");
        }

    }

    public void loadCompetitionToXmlFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(Competition.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Competition competition = (Competition) unmarshaller.unmarshal(file);
            competitionData.clear();
            competitionData.add(competition);


            for (Club club : competition.getClubs()) {
                ClubBean clubBean = new ClubBean();
                clubBean.setIdentifiant(club.getIdentifiant());
                clubBean.setNom(club.getNomClub());
                clubBean.setResponsable(club.getResponsable());
                ObservableList<EleveBean> eleves = FXCollections.observableArrayList();
                for (Eleve eleve : club.getEleves()) {
                    EleveBean eleveBean = new EleveBean();
                    eleveBean.setLicence(eleve.getLicenceEleve());
                    eleveBean.setNom(eleve.getNomEleve());
                    eleveBean.setPrenom(eleve.getPrenomEleve());
                    eleveBean.setAge(eleve.getAgeEleve());
                    eleveBean.setCategorie(eleve.getCategorieEleve());
                    eleveBean.setSexe(eleve.getSexeEleve());
                    eleveBean.setPoids(eleve.getPoidsEleve());
                    eleves.add(eleveBean);
                }
                clubBean.setEleves(eleves);
                clubs.add(clubBean);
            }

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/competitionView.fxml"));
            BorderPane tableView = (BorderPane) loader.load();
            ClubController controller = (ClubController) loader.getController();
            controller.setMainStage(mainStage);
            controller.getTableClub().setItems(clubs);
            borderPane.setCenter(tableView);
            notificationView.notify(NotificationView.Level.SUCCESS, "Chargement compétition",
                    "La compétition " + competition.getNom() + " a été chargée avec succès");

        } catch (JAXBException e) {
            e.printStackTrace();
            notificationView.notify(NotificationView.Level.SUCCESS, "Erreur",
                    "Erreur lors du chargement de la compétition");
        } catch (IOException e) {
            e.printStackTrace();
            notificationView.notify(NotificationView.Level.SUCCESS, "Erreur",
                    "Erreur lors du chargement de la compétition");
        }
    }

    public void loadInscriptionFile(File file) {
        InscriptionsManager inscriptionsManager = new InscriptionsManager();
        inscriptionsManager.loadInscription(file, this.competitionData.get(0));

        clubs.clear();
        for (Club club : this.competitionData.get(0).getClubs()) {
            ClubBean clubBean = new ClubBean();
            clubBean.setIdentifiant(club.getIdentifiant());
            clubBean.setNom(club.getNomClub());
            clubBean.setResponsable(club.getResponsable());
            ObservableList<EleveBean> eleves = FXCollections.observableArrayList();
            if (club.getEleves() != null) {
                for (Eleve eleve : club.getEleves()) {
                    EleveBean eleveBean = new EleveBean();
                    eleveBean.setLicence(eleve.getLicenceEleve());
                    eleveBean.setNom(eleve.getNomEleve());
                    eleveBean.setPrenom(eleve.getPrenomEleve());
                    eleveBean.setAge(eleve.getAgeEleve());
                    eleveBean.setCategorie(eleve.getCategorieEleve());
                    eleveBean.setSexe(eleve.getSexeEleve());
                    eleveBean.setPoids(eleve.getPoidsEleve());
                    eleves.add(eleveBean);
                }
            }
            clubBean.setEleves(eleves);
            clubs.add(clubBean);
            notificationView.notify(NotificationView.Level.SUCCESS, "Import",
                    "Inscription pour le club " + club.getNomClub() + " chargée avec succès");
        }
    }

    public void generateOrganisationFile(File file) {
        InscriptionsManager inscriptionsManager = new InscriptionsManager();
        inscriptionsManager.saveInscription(file, this.competitionData.get(0));
    }

    public void generateResutlatFile(File file) {
        InscriptionsManager inscriptionsManager = new InscriptionsManager();
        inscriptionsManager.saveInscription(file, this.competitionData.get(0));
    }

    public void showCompetitionView() {
        if (competitionData == null || competitionData.size() == 0) {
            NotificationView notificationView = new NotificationView(mainStage);
            notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                    "Aucune compétition chargée. Vous devez ouvrir une compétition.");
        } else {
            CategoriesView categoriesView = new CategoriesView();
            categoriesView.showView(mainStage, this.competitionData.get(0));
        }
    }

    public void showResultatsView() {
        if (competitionData == null || competitionData.size() == 0) {
            NotificationView notificationView = new NotificationView(mainStage);
            notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                    "Aucune compétition chargée. Vous devez ouvrir une compétition.");
        } else {
            ResultatsView resultatsView = new ResultatsView();
            resultatsView.showView(mainStage, this.competitionData.get(0));
        }
    }

    public void showCreationCompetitionView() {
        CreateCompetitionView createCompetitionView = new CreateCompetitionView();
        createCompetitionView.showCreateCompetitionView(mainStage, competitionData);
        if (competitionData.size() > 0) {
            controller.getCreateCategorie().setDisable(false);
        }
    }

    public void showCreationCategorieView() {
        CreateCompetitionView createCompetitionView = new CreateCompetitionView();
        createCompetitionView.showCreateCategorieView(mainStage, competitionData.get(0));
        if (competitionData.get(0).getCategories().size() > 0) {
            controller.getCreateEpreuve().setDisable(false);
        }
    }

    public void showCreationEpreuveView() {
        CreateCompetitionView createCompetitionView = new CreateCompetitionView();
        createCompetitionView.showCreateEpreuveView(mainStage, competitionData.get(0));
    }
}
