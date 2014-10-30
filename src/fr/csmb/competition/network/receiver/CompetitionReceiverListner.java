package fr.csmb.competition.network.receiver;

import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Competition;
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
            ParticipantBean participantBean = ParticipantConverter.convertParticipantToParticipantBean(participant);
            epreuveBean.getParticipants().add(participantBean);
        }
        
    }
}
