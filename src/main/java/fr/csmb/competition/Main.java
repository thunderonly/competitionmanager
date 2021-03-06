package fr.csmb.competition;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.listener.EleveBeanPresenceChangePropertyListener;
import fr.csmb.competition.model.*;
import fr.csmb.competition.controller.ClubController;
import fr.csmb.competition.controller.Controller;
import fr.csmb.competition.manager.InscriptionsManager;
import fr.csmb.competition.network.receiver.CompetitionReceiverListner;
import fr.csmb.competition.network.receiver.NetworkReceiver;
import fr.csmb.competition.view.CategoriesView;
import fr.csmb.competition.view.CreateCompetitionView;
import fr.csmb.competition.view.GlobalVisionView;
import fr.csmb.competition.view.NotificationView;
import fr.csmb.competition.view.ResultatsView;
import fr.csmb.competition.xml.model.Competition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Administrateur on 13/10/14.
 */
public class Main extends Application {

    private static final Logger LOGGER = LogManager.getFormatterLogger(InscriptionsManager.class);
    private Stage mainStage;
    private BorderPane borderPane;

    private ObservableList<Competition> competitionData = FXCollections.observableArrayList();
    private ObservableList<ClubBean> clubs;
    private CompetitionBean competitionBean;
    private NotificationView notificationView;
    private CategoriesView categoriesView;
    private Controller controller;
    private NetworkReceiver receiver = new NetworkReceiver("", 9878);
    private File tmpFile;

    @Override
    public void start(Stage primaryStage) throws Exception {

//        NewSender newSender = new NewSender();
//        NewReceiver newReceiver = new NewReceiver();
//        newSender.start();
//        newReceiver.start();

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

    @Override
    public void stop() {
        receiver.interrupt();

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

            Competition competition = CompetitionConverter.convertCompetitionBeanToCompetition(competitionBean);

            marshaller.marshal(competition, file);
            notificationView.notify(NotificationView.Level.SUCCESS, "Sauvegarde", "Compétition sauvegardée avec succès");

        } catch (JAXBException e) {
            LOGGER.error("Error when save competition xml File", e);
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
            competitionBean = CompetitionConverter.convertCompetitionToCompetitionBean(competition);
            clubs = competitionBean.getClubs();
            initializeListener();

            controller.setCompetitionBean(competitionBean);

            int index = file.getAbsolutePath().indexOf(".xml");
            String filePath = file.getAbsolutePath().substring(0,index);
            if (filePath.indexOf("-tmp") > 0) {
                filePath = file.getAbsolutePath().substring(0,filePath.indexOf("-tmp"));
            }
            String fileName = filePath.concat("-tmp").concat(".xml");
            Preferences pref = Preferences.userNodeForPackage(Main.class);
            pref.put("filePath", fileName);

            CompetitionReceiverListner receiverListner = new CompetitionReceiverListner(competitionBean, notificationView);
            receiver.addListener(receiverListner);
            receiver.start();

            showCompetitionView();
            notificationView.notify(NotificationView.Level.SUCCESS, "Chargement compétition",
                    "La compétition " + competition.getNom() + " a été chargée avec succès");
        } catch (JAXBException e) {

            e.printStackTrace();
            notificationView.notify(NotificationView.Level.SUCCESS, "Erreur",
                    "Erreur lors du chargement de la compétition");
        }
    }

    private void initializeListener() {

        for (final ClubBean clubBean : clubs) {
            for (final EleveBean eleveBean : clubBean.getEleves()) {
                EleveBeanPresenceChangePropertyListener changeListener = new EleveBeanPresenceChangePropertyListener();
                changeListener.setClubBean(clubBean);
                changeListener.setCompetitionBean(competitionBean);
                changeListener.setEleveBean(eleveBean);
                eleveBean.presenceProperty().addListener(changeListener);


            }
        }

    }

    public void showCompetitionView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/competitionView.fxml"));
            BorderPane tableView = (BorderPane) loader.load();
            ClubController controller = (ClubController) loader.getController();
            controller.setMainStage(mainStage);
            controller.setCompetitionBean(competitionBean);
            controller.getTableClub().setItems(clubs);
            borderPane.setCenter(tableView);
        } catch (IOException e) {
            e.printStackTrace();
            notificationView.notify(NotificationView.Level.SUCCESS, "Erreur",
                    "Erreur lors du chargement de la compétition");
        }
    }

    public void loadInscriptionFile(File file) {
        InscriptionsManager inscriptionsManager = new InscriptionsManager();
        String nameClub = inscriptionsManager.loadInscription(file, competitionBean);

        if (nameClub != null) {
            notificationView.notify(NotificationView.Level.SUCCESS, "Import",
                    "Inscription pour le club " + nameClub + " chargée avec succès");
        } else {
            notificationView.notify(NotificationView.Level.SUCCESS, "Erreur",
                    "Erreur lors de l'import du fichier d'inscription " + file.getName());
        }

    }

    public void showCategorienView() {
        if (competitionBean == null) {
            NotificationView notificationView = new NotificationView(mainStage);
            notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                    "Aucune compétition chargée. Vous devez ouvrir une compétition.");
        } else {
            if (categoriesView == null) {
                categoriesView = new CategoriesView();
            }
            categoriesView.showView(mainStage, this.competitionBean);
        }
    }

    public void showResultatsView() {
        if (competitionBean == null) {
            NotificationView notificationView = new NotificationView(mainStage);
            notificationView.notify(NotificationView.Level.ERROR, "Erreur",
                    "Aucune compétition chargée. Vous devez ouvrir une compétition.");
        } else {
            ResultatsView resultatsView = new ResultatsView();
            resultatsView.showView(mainStage, this.competitionBean);
        }
    }

    public void showCreationCompetitionView() {
        CreateCompetitionView createCompetitionView = new CreateCompetitionView();
        competitionBean = new CompetitionBean();
        createCompetitionView.showCreateCompetitionView(mainStage, competitionBean);
        if (!"".equals(competitionBean.getNom())) {
            controller.getCreateCategorie().setDisable(false);
        }
    }

    public void showCreationCategorieView() {
        CreateCompetitionView createCompetitionView = new CreateCompetitionView();
        createCompetitionView.showCreateCategorieView(mainStage, competitionBean);
        if (competitionBean.getCategories().size() > 0) {
            controller.getCreateEpreuve().setDisable(false);
        }
    }

    public void showCreationEpreuveView() {
        CreateCompetitionView createCompetitionView = new CreateCompetitionView();
        createCompetitionView.showCreateEpreuveView(mainStage, competitionBean);
    }

    public void showGlobalVisionView() {
        GlobalVisionView globalVisionView = new GlobalVisionView();
        globalVisionView.showView(mainStage, competitionBean);
    }

    public void showTechnicalGrid() {

        Stage newStage = new Stage();
        BorderPane pane = new BorderPane();
        List<ParticipantBean> participantBeans = new ArrayList<ParticipantBean>();
        participantBeans.add(new ParticipantBean("Nom", "Prenom"));
        participantBeans.add(new ParticipantBean("Nom1", "Prenom1"));
        participantBeans.add(new ParticipantBean("Nom2", "Prenom2"));
        GridComponentTechnical gridComponentTechnical = new GridComponentTechnical(participantBeans);
        gridComponentTechnical.drawGrid();
        borderPane.setCenter(gridComponentTechnical);

        newStage.setScene(new Scene(pane));
        newStage.show();
    }
}
