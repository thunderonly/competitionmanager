package fr.csmb.competition.network.receiver;

import java.io.File;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import fr.csmb.competition.Helper.CompetitionConverter;
import fr.csmb.competition.Helper.EleveConverter;
import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.Main;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.view.NotificationView;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Eleve;
import fr.csmb.competition.xml.model.Epreuve;
import fr.csmb.competition.xml.model.Participant;
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
        Categorie categorie = competition.getCategories().get(0);
        CategorieBean categorieBean = competitionBean.getCategorie(
                categorie.getTypeCategorie(),
                categorie.getNomCategorie());
        if (categorieBean == null) {
            categorieBean = new CategorieBean(categorie.getNomCategorie());
            categorieBean.setType(categorie.getTypeCategorie());
            competitionBean.getCategories().add(categorieBean);
        }
        Epreuve epreuve = categorie.getEpreuves().get(0);
        EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve.getNomEpreuve());
        if (EtatEpreuve.SUPPRIME.getValue().equals(epreuve.getEtatEpreuve())) {
            if (epreuveBean != null) {
                LOGGER.info("Receive epreuve %s with etat %s", epreuveBean.toString(), epreuveBean.getEtat());
                categorieBean.getEpreuves().remove(epreuveBean);
                if (categorieBean.getEpreuves().isEmpty()) {
                    competitionBean.getCategories().remove(categorieBean);
                }
            }
        } else {
            if (epreuveBean == null) {
                epreuveBean = new EpreuveBean(epreuve.getNomEpreuve());
                categorieBean.getEpreuves().add(epreuveBean);
            }

            LOGGER.info("Receive epreuve %s with etat %s", epreuveBean.toString(), epreuveBean.getEtat());
            epreuveBean.setType(epreuve.getTypeEpreuve());

            for (Participant participant : epreuve.getParticipants()) {
                ParticipantBean participantBean = epreuveBean.getParticipantByNomPrenom(participant.getNomParticipant(), participant.getPrenomParticipant());
                if (participantBean == null) {
                    participantBean = ParticipantConverter.convertParticipantToParticipantBean(participant);
                    epreuveBean.getParticipants().add(participantBean);
                } else {
                    ParticipantConverter.convertParticipantToParticipantBean(participant, participantBean);
                }

            }
        }
//        if (epreuveBean.getEtat() != null && !epreuveBean.getEtat().equals(epreuve.getEtatEpreuve())) {
//            if (EtatEpreuve.VALIDE.getValue().equals(epreuveBean.getEtat())) {
//                notificationView.notify(NotificationView.Level.INFO, "Information",
//                        "L'épreuve " + epreuveBean.toString() + " a été validée");
//            } else if (EtatEpreuve.DEMARRE.getValue().equals(epreuveBean.getEtat())) {
//                notificationView.notify(NotificationView.Level.INFO, "Information",
//                        "L'épreuve " + epreuveBean.toString() + " a été démarrée");
//            } else if (EtatEpreuve.TERMINE.getValue().equals(epreuveBean.getEtat())) {
//                notificationView.notify(NotificationView.Level.INFO, "Information",
//                        "L'épreuve " + epreuveBean.toString() + " a été terminée");
//            } else if (EtatEpreuve.SUPPRIME.getValue().equals(epreuveBean.getEtat())) {
//                notificationView.notify(NotificationView.Level.INFO, "Information",
//                        "L'épreuve " + epreuveBean.toString() + " a été supprimée");
//            }
//        }

        epreuveBean.setEtat(epreuve.getEtatEpreuve());
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
