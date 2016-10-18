package fr.csmb.competition.view;

import fr.csmb.competition.controller.RenameEpreuveController;
import fr.csmb.competition.model.CategorieBean;
import fr.csmb.competition.model.CompetitionBean;
import fr.csmb.competition.model.EpreuveBean;
import fr.csmb.competition.network.sender.NetworkSender;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Created by Administrateur on 10/11/15.
 */
public class RenameEpreuveView {

    public int showView(CompetitionBean competitionBean, EpreuveBean epreuveBean) {
        try {
            final Stage newStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("fxml/renameEpreuveView.fxml"));
            BorderPane pane = (BorderPane) loader.load();
            final RenameEpreuveController renameEpreuveController = loader.getController();
            renameEpreuveController.initComponent(epreuveBean);
            newStage.setScene(new Scene(pane));
            final StringBuilder result = new StringBuilder();
            renameEpreuveController.setActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("validate")) {
                        newStage.close();
                        result.append(1);
                    } else {
                        result.append(0);
                    }
                }
            });
            newStage.showAndWait();
            return Integer.parseInt(result.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
