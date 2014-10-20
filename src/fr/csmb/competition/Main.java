package fr.csmb.competition;

import fr.csmb.competition.manager.InscriptionsManager;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.view.CategoriesView;
import fr.csmb.competition.view.ResultatsView;
import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Eleve;
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

/**
 * Created by Administrateur on 13/10/14.
 */
public class Main extends Application {

    private Stage mainStage;
    private BorderPane borderPane;

    private ObservableList<Competition> competitionData = FXCollections.observableArrayList();
    private ObservableList<ClubBean> clubs = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {

        Competition stMax = initializeCompetition();
        competitionData.add(stMax);

        mainStage = primaryStage;
        primaryStage.setTitle("Competition Manager");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mainView.fxml"));
        borderPane = (BorderPane) loader.load();
        ((Controller) loader.getController()).setMain(this);


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

        } catch (JAXBException e) {
            e.printStackTrace();
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
            loader.setLocation(getClass().getResource("competitionView.fxml"));
            BorderPane tableView = (BorderPane) loader.load();
            ClubController controller = (ClubController) loader.getController();
            controller.setMainStage(mainStage);
            controller.getTableClub().setItems(clubs);
            borderPane.setCenter(tableView);

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        }
    }

    private Competition initializeCompetition() {
        Competition stMax = new Competition("St Maximin");
        List<String> categories = new ArrayList<String>();
        categories.add("Benjamin");
        categories.add("Minime");
        categories.add("Cadet");
//        stMax.setCategories(categories);

        List<String> epreuves = new ArrayList<String>();
        epreuves.add("Mains Nues");
        epreuves.add("Armes");
        epreuves.add("Doi Luyen");
//        stMax.setEpreuves(epreuves);

        Club club = new Club();
        club.setIdentifiant("13001");
        club.setNomClub("club 13001");
        club.setResponsable("responsable 13001");
        List<Eleve> elevesClub1 = new ArrayList<Eleve>();
        Eleve eleve1Club1 = new Eleve("Nom Eleve 1", "Prenom Eleve 1");
        Eleve eleve2Club1 = new Eleve("Nom Eleve 2", "Prenom Eleve 2");
        elevesClub1.add(eleve1Club1);
        elevesClub1.add(eleve2Club1);
        club.setEleves(elevesClub1);

        Club club2 = new Club();
        club2.setIdentifiant("13002");
        club2.setNomClub("club 13002");
        club2.setResponsable("responsable 13002");
        List<Eleve> elevesClub2 = new ArrayList<Eleve>();
        Eleve eleve3Club2 = new Eleve("Nom Eleve 3", "Prenom Eleve 3");
        Eleve eleve4Club2 = new Eleve("Nom Eleve 4", "Prenom Eleve 4");
        elevesClub2.add(eleve3Club2);
        elevesClub2.add(eleve4Club2);
        club2.setEleves(elevesClub2);

        List<Club> clubs = new ArrayList<Club>();
        clubs.add(club);
        clubs.add(club2);
        stMax.setClubs(clubs);

        return stMax;
    }

    public void generateOrganisationFile(File file) {
        InscriptionsManager inscriptionsManager = new InscriptionsManager();
        inscriptionsManager.saveInscription(file, this.competitionData.get(0));
    }

    public void showCompetitionView() {
        CategoriesView categoriesView = new CategoriesView();
        categoriesView.showView(mainStage, this.competitionData.get(0));
    }

    public void showResultatsView() {
        ResultatsView resultatsView = new ResultatsView();
        resultatsView.showView(mainStage, this.competitionData.get(0));
    }
}
