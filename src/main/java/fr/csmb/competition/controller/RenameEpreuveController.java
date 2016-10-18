package fr.csmb.competition.controller;

import fr.csmb.competition.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Administrateur on 23/03/15.
 */
public class RenameEpreuveController {

    @FXML
    private Label epreuveNameLabel;
    @FXML
    private TextField epreuveNameTf;

    private EpreuveBean epreuveBean;

    private ActionListener actionListener;

    @FXML
    private void initialize(){

    }

    public void initComponent(EpreuveBean epreuveBean) {
        this.epreuveBean = epreuveBean;
        this.epreuveNameTf.setText(epreuveBean.getDiscipline().getNom());
    }

    @FXML
    private void validate() {
        epreuveBean.setLabel(epreuveNameTf.getText());

        this.actionListener.actionPerformed(new ActionEvent(this, 0, "validate"));
    }

    @FXML
    private void cancel() {

    }

    public void setActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
    }
}
