package fr.csmb.competition.listener;

import fr.csmb.competition.type.EtatEpreuve;
import fr.csmb.competition.view.NotificationView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

/**
 * Created by Administrateur on 11/10/15.
 */
public class LabelPropertyEpreuveChangeListener implements ChangeListener<String> {

    TreeItem<String> itemEpreuve;

    public LabelPropertyEpreuveChangeListener(TreeItem<String> itemEpreuve) {
        this.itemEpreuve = itemEpreuve;
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
        TreeItem<String> parent = itemEpreuve.getParent();
        TreeItem<String> categorie = parent.getParent();
        TreeItem<String> sexe = categorie.getParent();
        TreeItem<String> root = sexe.getParent();

        int index = parent.getChildren().indexOf(itemEpreuve);
        parent.getChildren().remove(itemEpreuve);
        itemEpreuve.setValue(s2);
        parent.getChildren().add(index, itemEpreuve);

    }
}
