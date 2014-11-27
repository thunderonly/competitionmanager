package fr.csmb.competition.Helper;

import fr.csmb.competition.model.ParticipantBean;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.DisciplineBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.xml.model.Categorie;
import fr.csmb.competition.xml.model.Discipline;
import fr.csmb.competition.xml.model.Epreuve;
import fr.csmb.competition.xml.model.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Administrateur on 25/11/14.
 */
public class EpreuveConverter {

    public static EpreuveBean convertEpreuveToEpreuveBean(Epreuve epreuve) {
        EpreuveBean epreuveBean = new EpreuveBean();
        epreuveBean.setEtat(epreuve.getEtatEpreuve());
//        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
//        for (Participant participant : epreuve.getParticipants()) {
//            ParticipantBean participantBean = ParticipantConverter.convertParticipantToParticipantBean(participant);
//            participantBeans.add(participantBean);
//        }
//        epreuveBean.setParticipants(participantBeans);

        CategorieBean categorieBean = new CategorieBean(epreuve.getCategorie().getNomCategorie());
        categorieBean.setType(epreuve.getCategorie().getTypeCategorie());
        epreuveBean.setCategorie(categorieBean);

        DisciplineBean disciplineBean = new DisciplineBean(epreuve.getDiscipline().getNom(), epreuve.getDiscipline().getType());
        disciplineBean.setType(epreuve.getDiscipline().getType());
        epreuveBean.setDiscipline(disciplineBean);

        epreuveBean.setDetailEpreuve(DetailEpreuveConverter.convertDetailEpreuveToDetailEpreuveBean(epreuve
                .getDetailEpreuve()));
        epreuveBean.setId(epreuve.getId());

        return epreuveBean;
    }

    public static Epreuve convertEpreuveBeanToEpreuve(EpreuveBean epreuveBean) {
        Discipline discipline = new Discipline(epreuveBean.getDiscipline().getNom(), epreuveBean.getDiscipline().getType());
        Categorie categorie = new Categorie(epreuveBean.getCategorie().getNom(), epreuveBean.getCategorie().getType());
        Epreuve epreuve = new Epreuve(categorie, discipline);
        epreuve.setEtatEpreuve(epreuveBean.getEtat());
//        for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
//            Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
//            epreuve.getParticipants().add(participant);
//        }
        epreuve.setDetailEpreuve(DetailEpreuveConverter.convertDetailEpreuveBeanToDetailEpreuve(epreuveBean.getDetailEpreuve()));
        epreuve.setId(epreuveBean.getId());
        return epreuve;
    }
}
