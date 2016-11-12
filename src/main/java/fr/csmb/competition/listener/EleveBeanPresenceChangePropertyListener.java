package fr.csmb.competition.listener;

import fr.csmb.competition.Helper.ParticipantHelper;
import fr.csmb.competition.model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Created by Administrateur on 10/03/15.
 */
public class EleveBeanPresenceChangePropertyListener implements ChangeListener<Boolean> {

    private CompetitionBean competitionBean;
    private ClubBean clubBean;
    private EleveBean eleveBean;

    public void changed(ObservableValue<? extends java.lang.Boolean> observableValue, Boolean o, Boolean t1) {
        if (t1) {
            ParticipantHelper.createParticipantsFromEleveBean(competitionBean, eleveBean, clubBean);
            for (ParticipantBean participantBean : competitionBean.getParticipantByNomPrenom(
                    eleveBean.getNom(), eleveBean.getPrenom())) {
                participantBean.setParticipe(true);
            }


        } else {
            for (ParticipantBean participantBean : competitionBean.getParticipantByNomPrenom(
                    eleveBean.getNom(), eleveBean.getPrenom())) {
                participantBean.setParticipe(false);
            }
        }
    }

    public void setCompetitionBean(CompetitionBean competitionBean) {
        this.competitionBean = competitionBean;
    }

    public void setClubBean(ClubBean clubBean) {
        this.clubBean = clubBean;
    }

    public void setEleveBean(EleveBean eleveBean) {
        this.eleveBean = eleveBean;
    }
}
