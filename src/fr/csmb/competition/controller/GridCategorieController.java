package fr.csmb.competition.controller;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.Main;
import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.component.grid.fight.GridComponentFight2;
import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.CategoriesView;
import fr.csmb.competition.xml.model.Competition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Created by Administrateur on 12/11/14.
 */
public class GridCategorieController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label categorieTf;
    @FXML
    private Label epreuveTf;
    @FXML
    private Label nbParticipantsTf;
    @FXML
    private TextField adminTf;
    @FXML
    private TextField chronoTf;
    @FXML
    private TextField firstJugeTf;
    @FXML
    private TextField secondJugeTf;
    @FXML
    private TextField thirdJugeTf;
    @FXML
    private TextField fourthJugeTf;
    @FXML
    private TextField fifthJugeTf;
    @FXML
    private TextField tapisTf;
    @FXML
    private TextField heureDebutTf;
    @FXML
    private TextField heureFinTf;
    @FXML
    private TextField dureeTf;
    @FXML
    private TextField firstPlaceTf;
    @FXML
    private TextField secondPlaceTf;
    @FXML
    private TextField thirdPlaceTf;
    @FXML
    private TextField fourthPlaceTf;

    private CompetitionBean competitionBean;
    private CategorieBean categorieBean;
    private EpreuveBean epreuveBean;
    private GridComponent gridComponent;
    private NetworkSender sender;
    private CategoriesView categorieView;

    @FXML
    private void initialize(){}

    public void initGrid(CompetitionBean competitionBean, final String typeCategorie, final String categorie, final String typeEpreuve, final String epreuve) {
        this.sender = NetworkSender.getINSTANCE();
        this.competitionBean = competitionBean;
        this.categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        this.epreuveBean = null;
        ObservableList<ParticipantBean> participants = FXCollections.observableArrayList();
        if (categorieBean != null) {
            epreuveBean = categorieBean.getEpreuveByName(epreuve);
            if (epreuveBean != null) {
                for (ParticipantBean participantBean : epreuveBean.getParticipants())
                    participants.add(participantBean);
            }
        }

        categorieTf.setText(categorieBean.getNom().concat(" ").concat(categorieBean.getType()));
        epreuveTf.setText(epreuveBean.getNom());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String heure = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        heureDebutTf.setText(heure);

        nbParticipantsTf.setText(String.valueOf(epreuveBean.getParticipants().size()));

        ParticipantClassementFinalListener participantClassementFinalListener =
                new ParticipantClassementFinalListener(firstPlaceTf, secondPlaceTf, thirdPlaceTf, fourthPlaceTf);
        if (typeEpreuve.equals(TypeEpreuve.COMBAT.getValue())) {
            gridComponent = new GridComponentFight2(participants);
        } else if (typeEpreuve.equals(TypeEpreuve.TECHNIQUE.getValue())) {
            gridComponent = new GridComponentTechnical(participants);
        }
        gridComponent.setParticipantClassementFinalListener(participantClassementFinalListener);
        gridComponent.drawGrid();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridComponent);
        borderPane.setCenter(scrollPane);

        epreuveBean.setEtat(EtatEpreuve.DEMARRE.getValue());
        sender.send(competitionBean, categorieBean, epreuveBean);
    }

    @FXML
    private void validate() {
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
    }

    @FXML
    private void cancel() {
        if (epreuveBean != null) {
            epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
            sender.send(competitionBean, categorieBean, epreuveBean);
        }
        this.categorieView.handleCancelEpreuve();
    }

    private void saveCompetitionToXmlFileTmp() {
        try {
            Preferences pref = Preferences.userNodeForPackage(Main.class);
            String fileName = pref.get("filePath", null);
            File fileTmp = new File(fileName);
            JAXBContext context = JAXBContext.newInstance(Competition.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Competition competition = CompetitionConverter.convertCompetitionBeanToCompetition(competitionBean);

            marshaller.marshal(competition, fileTmp);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public void setCategorieView(CategoriesView categorieView) {
        this.categorieView = categorieView;
    }

}
