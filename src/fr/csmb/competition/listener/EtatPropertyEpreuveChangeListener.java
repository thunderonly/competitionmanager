package fr.csmb.competition.listener;

import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.view.NotificationView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

import javax.management.Notification;

/**
 * Created by Administrateur on 11/10/15.
 */
public class EtatPropertyEpreuveChangeListener implements ChangeListener<String> {

    TreeItem<String> itemEpreuve;
    NotificationView notificationView;

    public EtatPropertyEpreuveChangeListener(TreeItem<String> itemEpreuve, NotificationView notificationView) {
        this.itemEpreuve = itemEpreuve;
        this.notificationView = notificationView;
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
        TreeItem<String> parent = itemEpreuve.getParent();
        TreeItem<String> categorie = parent.getParent();
        TreeItem<String> sexe = categorie.getParent();
        TreeItem<String> root = sexe.getParent();
        if (EtatEpreuve.SUPPRIME.getValue().equals(s2)) {
            parent.getChildren().remove(itemEpreuve);
            if (parent.getChildren().isEmpty()) {
                categorie.getChildren().remove(parent);
                if (categorie.getChildren().isEmpty()) {
                    sexe.getChildren().remove(categorie);
                    if (sexe.getChildren().isEmpty()) {
                        root.getChildren().remove(sexe);
                    }
                }
            }
        } else {
            int index = parent.getChildren().indexOf(itemEpreuve);
            parent.getChildren().remove(itemEpreuve);
            parent.getChildren().add(index, itemEpreuve);
        }
        if (EtatEpreuve.VALIDE.getValue().equals(s2)) {
            notificationView.notify(NotificationView.Level.INFO, "Information",
                    "L'épreuve " + categorie.getValue().concat(" ").concat(sexe.getValue()).concat(" - ")
                            .concat(itemEpreuve.getValue()) + " a été validée");
        } else if (EtatEpreuve.DEMARRE.getValue().equals(s2)) {
            notificationView.notify(NotificationView.Level.INFO, "Information",
                    "L'épreuve " + categorie.getValue().concat(" ").concat(sexe.getValue()).concat(" - ")
                            .concat(itemEpreuve.getValue()) + " a été démarrée");
        } else if (EtatEpreuve.TERMINE.getValue().equals(s2)) {
            notificationView.notify(NotificationView.Level.INFO, "Information",
                    "L'épreuve " + categorie.getValue().concat(" ").concat(sexe.getValue()).concat(" - ")
                            .concat(itemEpreuve.getValue()) + " a été terminée");
        } else if (EtatEpreuve.SUPPRIME.getValue().equals(s2)) {
            notificationView.notify(NotificationView.Level.INFO, "Information",
                    "L'épreuve " + categorie.getValue().concat(" ").concat(sexe.getValue()).concat(" - ")
                            .concat(itemEpreuve.getValue()) + " a été supprimée");
        }
    }
}
