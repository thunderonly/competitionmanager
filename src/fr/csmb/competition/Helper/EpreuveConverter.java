package fr.csmb.competition.Helper;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
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
        EpreuveBean epreuveBean = new EpreuveBean(epreuve.getNomEpreuve());
        epreuveBean.setEtat(epreuve.getEtatEpreuve());
        epreuveBean.setType(epreuve.getTypeEpreuve());
        ObservableList<ParticipantBean> participantBeans = FXCollections.observableArrayList();
        for (Participant participant : epreuve.getParticipants()) {
            ParticipantBean participantBean = ParticipantConverter.convertParticipantToParticipantBean(participant);
            participantBeans.add(participantBean);
        }
        epreuveBean.setAdministrateur(epreuve.getAdministrateur());
        epreuveBean.setChronometreur(epreuve.getChronometreur());
        epreuveBean.setDuree(epreuve.getDuree());
        epreuveBean.setHeureDebut(epreuve.getHeureDebut());
        epreuveBean.setHeureFin(epreuve.getHeureFin());
        epreuveBean.setJuge1(epreuve.getJuge1());
        epreuveBean.setJuge2(epreuve.getJuge2());
        epreuveBean.setJuge3(epreuve.getJuge3());
        epreuveBean.setJuge4(epreuve.getJuge4());
        epreuveBean.setJuge5(epreuve.getJuge5());
        epreuveBean.setParticipants(participantBeans);

        CategorieBean categorieBean = new CategorieBean(epreuve.getCategorie().getNomCategorie());
        categorieBean.setType(epreuve.getCategorie().getTypeCategorie());
        epreuveBean.setCategorie(categorieBean);

        DisciplineBean disciplineBean = new DisciplineBean(epreuve.getDiscipline().getNom());
        disciplineBean.setType(epreuve.getDiscipline().getType());
        epreuveBean.setDiscipline(disciplineBean);

        return epreuveBean;
    }

    public static Epreuve convertEpreuveBeanToEpreuve(EpreuveBean epreuveBean) {
        Epreuve epreuve = new Epreuve(epreuveBean.getNom(), epreuveBean.getType());
        epreuve.setEtatEpreuve(epreuveBean.getEtat());
        epreuve.setAdministrateur(epreuveBean.getAdministrateur());
        epreuve.setChronometreur(epreuveBean.getChronometreur());
        epreuve.setJuge1(epreuveBean.getJuge1());
        epreuve.setJuge2(epreuveBean.getJuge2());
        epreuve.setJuge3(epreuveBean.getJuge3());
        epreuve.setJuge4(epreuveBean.getJuge4());
        epreuve.setJuge5(epreuveBean.getJuge5());
        epreuve.setTapis(epreuveBean.getTapis());
        epreuve.setHeureDebut(epreuveBean.getHeureDebut());
        epreuve.setHeureFin(epreuveBean.getHeureFin());
        epreuve.setDuree(epreuveBean.getDuree());
        for (ParticipantBean participantBean : epreuveBean.getParticipants()) {
            Participant participant = ParticipantConverter.convertParticipantBeanToParticipant(participantBean);
            epreuve.getParticipants().add(participant);
        }
        epreuve.setDiscipline(new Discipline(epreuveBean.getDiscipline().getNom(), epreuveBean.getDiscipline().getType()));
        epreuve.setCategorie(new Categorie(epreuveBean.getCategorie().getNom(), epreuveBean.getCategorie().getType()));
        return epreuve;
    }
}
