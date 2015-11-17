package fr.csmb.competition.network.receiver;

import java.io.File;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.Helper.EleveConverter;
import fr.csmb.competition.Helper.EpreuveConverter;
import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.Main;
import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.DisciplineBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.view.NotificationView;
import fr.csmb.competition.xml.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Administrateur on 28/10/14.
 */
public class CompetitionReceiverListner implements DatagramListener {

    private static final Logger LOGGER = LogManager.getFormatterLogger(CompetitionReceiverListner.class);

    private CompetitionBean competitionBean;
    private File fileTmp;
    private NotificationView notificationView;

    public CompetitionReceiverListner(CompetitionBean competitionBean, NotificationView notificationView) {
        this.competitionBean = competitionBean;
        Preferences pref = Preferences.userNodeForPackage(Main.class);
        String fileName = pref.get("filePath", null);
        fileTmp = new File(fileName);
        this.notificationView = notificationView;
    }

    @Override
    public void receive(Competition competition) {

        //Update Competition in competition bean
        CategorieBean categorieBean = null;
        if (competition.getCategories() != null && competition.getCategories().size() > 0) {
            Categorie categorie = competition.getCategories().get(0);
            categorieBean = competitionBean.getCategorie(
                    categorie.getTypeCategorie(),
                    categorie.getNomCategorie());
            if (categorieBean == null) {
                categorieBean = new CategorieBean(categorie.getNomCategorie());
                categorieBean.setType(categorie.getTypeCategorie());
                competitionBean.getCategories().add(categorieBean);
            }
            LOGGER.info("Receive catégorie %s ", categorieBean.getNom());
        }

        DisciplineBean disciplineBean = null;
        if (competition.getDiscipline() != null && competition.getDiscipline().size() > 0) {
            Discipline discipline = competition.getDiscipline().get(0);
            disciplineBean = competitionBean.getDiscipline(discipline.getType(), discipline.getNom());
            if (disciplineBean == null) {
                disciplineBean = new DisciplineBean(discipline.getNom(), discipline.getType());
                competitionBean.getDisciplines().add(disciplineBean);
            }
            LOGGER.info("Receive discipline %s ", disciplineBean.getNom());
        }

        if (competition.getEpreuve() != null && competition.getEpreuve().size() > 0 ) {
            Epreuve epreuve = competition.getEpreuve().get(0);
            EpreuveBean epreuveBean = competitionBean.getEpreuve(categorieBean, disciplineBean);
            if (epreuveBean == null) {
                // May be a new Epreuve
                epreuveBean = EpreuveConverter.convertEpreuveToEpreuveBean(epreuve);
                competitionBean.getEpreuves().add(epreuveBean);
            }
            if (EtatEpreuve.SUPPRIME.getValue().equals(epreuve.getEtatEpreuve())) {
                if (epreuveBean != null) {
                    LOGGER.info("Receive epreuve %s with etat %s", epreuveBean.toString(), epreuveBean.getEtat());
                    competitionBean.getEpreuves().remove(epreuveBean);
                }
            } else {
                LOGGER.info("Receive epreuve %s with etat %s", epreuveBean.toString(), epreuveBean.getEtat());
                EpreuveConverter.convertEpreuveToEpreuveBean(epreuve, epreuveBean);
            }
        }

        if (competition.getParticipant() != null && competition.getParticipant().size() > 0) {
            for (Participant participant : competition.getParticipant()) {
                EpreuveBean epreuveBeanPart = EpreuveConverter.convertEpreuveToEpreuveBean(participant.getEpreuve());
                ParticipantBean participantBean = competitionBean.getParticipantByNomPrenomEpreuve(
                        participant.getNomParticipant(), participant.getPrenomParticipant(), epreuveBeanPart);
                if (participantBean == null) {
                    participantBean = ParticipantConverter.convertParticipantToParticipantBean(participant);

                    competitionBean.getParticipants().add(participantBean);
                } else {
                    ParticipantConverter.convertParticipantToParticipantBean(participant, participantBean);
                }
                LOGGER.info("Receive participant %s %s", participant.getNomParticipant(), participant.getPrenomParticipant());
            }
        }

        saveCompetitionToXmlFileTmp();
    }

    @Override
    public void receive(Club club) {
        ClubBean clubBean = competitionBean.getClubByIdentifiant(club.getIdentifiant());
        for (Eleve eleve : club.getEleves()) {
            EleveBean eleveBean = clubBean.getEleveByLicence(eleve.getLicenceEleve());
            if (eleveBean == null) {
                eleveBean = EleveConverter.converEleveToEleveBean(eleve);
            } else {
                eleveBean.setPresence(eleve.getPresenceEleve());
            }
        }
        saveCompetitionToXmlFileTmp();
    }


    private void saveCompetitionToXmlFileTmp() {
        try {
            JAXBContext context = JAXBContext.newInstance(Competition.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Competition competition = CompetitionConverter.convertCompetitionBeanToCompetition(competitionBean);

            marshaller.marshal(competition, this.fileTmp);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}
