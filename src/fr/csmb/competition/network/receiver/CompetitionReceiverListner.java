package fr.csmb.competition.network.receiver;

import fr.csmb.competition.Helper.EleveConverter;
import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.ClubBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EleveBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Competition;
import fr.csmb.competition.xml.model.Eleve;
import fr.csmb.competition.xml.model.Epreuve;
import fr.csmb.competition.xml.model.Participant;

/**
 * Created by Administrateur on 28/10/14.
 */
public class CompetitionReceiverListner implements DatagramListener {

    private CompetitionBean competitionBean;

    public CompetitionReceiverListner(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    @Override
    public void receive(Competition competition) {

        //Update Competition in competition bean
        Categorie categorie = competition.getCategories().get(0);
        CategorieBean categorieBean = competitionBean.getCategorie(
                categorie.getTypeCategorie(),
                categorie.getNomCategorie());
        Epreuve epreuve = categorie.getEpreuves().get(0);
        EpreuveBean epreuveBean = categorieBean.getEpreuveByName(epreuve.getNomEpreuve());
        if (epreuveBean == null) {
            epreuveBean = new EpreuveBean(epreuve.getNomEpreuve());
            categorieBean.getEpreuves().add(epreuveBean);
        }

        epreuveBean.setEtat(epreuve.getEtatEpreuve());
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
    }
}
