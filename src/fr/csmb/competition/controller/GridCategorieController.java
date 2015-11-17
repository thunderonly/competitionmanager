package fr.csmb.competition.controller;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.Main;
import fr.csmb.competition.component.grid.GridComponent;
import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.component.grid.fight.GridComponentFight2;
import fr.csmb.competition.component.grid.technical.GridComponentTechnical;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.DetailEpreuveBean;
import fr.csmb.competition.model.DisciplineBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.network.sender.NetworkSender;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.type.TypeEpreuve;
import fr.csmb.competition.view.CategoriesView;
import fr.csmb.competition.view.ConfigureFightView;
import fr.csmb.competition.view.GridCategorieView;
import fr.csmb.competition.xml.model.Competition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private DisciplineBean disciplineBean;
    private EpreuveBean epreuveBean;
    private GridComponent gridComponent;
    private NetworkSender sender;
    private CategoriesView categorieView;
    private GridCategorieView gridCategorieView;

    @FXML
    private void initialize(){}

    public void initGrid(CompetitionBean competitionBean, final String typeCategorie, final String categorie, final String typeEpreuve, final String epreuve) {
        this.sender = NetworkSender.getINSTANCE();
        this.competitionBean = competitionBean;
        this.categorieBean = competitionBean.getCategorie(typeCategorie, categorie);
        this.disciplineBean = competitionBean.getDiscipline(typeEpreuve, epreuve);
        this.epreuveBean = null;
        ObservableList<ParticipantBean> participants = FXCollections.observableArrayList();
        if (categorieBean != null) {
            epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean, epreuve);
            if (epreuveBean != null) {
                for (ParticipantBean participantBean : competitionBean.getParticipantByEpreuve(epreuveBean))
                    participants.add(participantBean);
            }
        }

        categorieTf.setText(categorieBean.getNom().concat(" ").concat(categorieBean.getType()));
        epreuveTf.setText(epreuveBean.getLabel());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String heure = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        heureDebutTf.setText(heure);

        nbParticipantsTf.setText(String.valueOf(competitionBean.getParticipantByEpreuve(epreuveBean).size()));

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

        gridCategorieView = new GridCategorieView();
        gridCategorieView.initView(competitionBean, epreuveBean);

        competitionBean.getParticipants().addListener(new ListChangeListener<ParticipantBean>() {
            @Override
            public void onChanged(Change<? extends ParticipantBean> c) {
                while (c.next()) {
                    if (c.wasPermutated()) {
                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
                            //permutate
                        }
                    } else if (c.wasUpdated()) {
                        //update item
                    } else {
                        for (ParticipantBean remitem : c.getRemoved()) {
                            gridComponent.drawGrid();
                        }
                        for (ParticipantBean additem : c.getAddedSubList()) {
                            gridComponent.addParticipant(additem);
                            gridComponent.drawGrid();
                        }
                    }
                }
            }
        });
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
        DetailEpreuveBean detailEpreuveBean = new DetailEpreuveBean();
        detailEpreuveBean.setAdministrateur(adminTf.getText());
        detailEpreuveBean.setChronometreur(chronoTf.getText());
        detailEpreuveBean.setJuge1(firstJugeTf.getText());
        detailEpreuveBean.setJuge2(secondJugeTf.getText());
        detailEpreuveBean.setJuge3(thirdJugeTf.getText());
        detailEpreuveBean.setJuge4(fourthJugeTf.getText());
        detailEpreuveBean.setJuge5(fifthJugeTf.getText());
        detailEpreuveBean.setTapis(tapisTf.getText());
        detailEpreuveBean.setHeureDebut(heureDebutTf.getText());
        detailEpreuveBean.setHeureFin(heureFinTf.getText());
        detailEpreuveBean.setDuree(dureeTf.getText());
        epreuveBean.setDetailEpreuve(detailEpreuveBean);
        sender.send(competitionBean, epreuveBean);
        saveCompetitionToXmlFileTmp();
    }

    @FXML
    private void cancel() {
        if (epreuveBean != null) {
            epreuveBean.setEtat(EtatEpreuve.VALIDE.getValue());
            sender.send(competitionBean, epreuveBean);
        }
        this.categorieView.handleCancelEpreuve(epreuveBean);
    }

    @FXML
    private void addPart() {
        final Stage newStage = new Stage();
        final AddParticipantController participantController = this.gridCategorieView.initAddPartView(newStage);
        participantController.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                gridComponent.addParticipant(participantController.getParticipantBean());
                //When update partipants of competition, call listener and redraw component. (for network receive)
                competitionBean.getParticipants().add(participantController.getParticipantBean());
                if (epreuveBean.getDiscipline().getType().equals(TypeEpreuve.COMBAT.getValue())) {
                    ConfigureFightView configureFightView = new ConfigureFightView();
                    configureFightView.showView(newStage, competitionBean, epreuveBean);
                    gridComponent.drawGrid();
                }
                newStage.close();

                sender.send(competitionBean, epreuveBean);
            }
        });
        newStage.show();

    }

    @FXML
    private void delPart() {
        ParticipantBean participantBean = null;
        if (gridComponent instanceof GridComponentTechnical) {
            participantBean = ((GridComponentTechnical) gridComponent).getSelectedParticipant();
            gridComponent.delParticipant(participantBean);
            competitionBean.getParticipants().remove(participantBean);
        } else {
            final Stage newStage = new Stage();
            final DelParticipantController participantController = gridCategorieView.initDelPartView(newStage);
            newStage.show();

            participantController.setActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gridComponent.delParticipant(participantController.getParticipantBean());
                    if (epreuveBean.getDiscipline().getType().equals(TypeEpreuve.COMBAT.getValue())) {
                        ConfigureFightView configureFightView = new ConfigureFightView();
                        newStage.close();
                        configureFightView.showView(newStage, competitionBean, epreuveBean);
                        gridComponent.drawGrid();
                    }
                    sender.send(competitionBean, epreuveBean);
                }
            });

        }

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
