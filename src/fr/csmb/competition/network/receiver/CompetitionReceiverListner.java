package fr.csmb.competition.network.receiver;

import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Competition;

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
        EpreuveBean epreuveBean = categorieBean.getEpreuveByName(categorie.getEpreuves().get(0).getNomEpreuve());
    }
}
