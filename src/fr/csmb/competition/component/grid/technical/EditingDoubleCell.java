/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.grid.technical;

import fr.csmb.competition.model.ParticipantBean;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class EditingDoubleCell extends EditingCell<Double> {

    @Override
    public void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);
        if (getTableColumn().getText().equals("Final")) {
            if (item != null) {
                if (item == 1) {
                    setStyle("-fx-background-color: green");
                } else if (item == 2) {
                    setStyle("-fx-background-color: yellow");
                } else if (item == 3) {
                    setStyle("-fx-background-color: orange");
                } else if (item == 4) {
                    setStyle("-fx-background-color: red");
                } else {
                    setStyle("");
                }
            }
            if (getTableView().getItems().size() > getIndex()) {
                System.out.println(getTableView().getItems().get(getIndex()).getNom());
            }
        }
    }
    protected void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    commitEdit(Double.parseDouble(textField.getText()));
                } else if (t.getCode() == KeyCode.ESCAPE) {
                    cancelEdit();
                } else if (t.getCode() == KeyCode.TAB) {
                    commitEdit(Double.parseDouble(textField.getText()));
                    TableColumn nextColumn = getNextColumn(!t.isShiftDown());
                    if (nextColumn != null) {
                        getTableView().edit(getTableRow().getIndex(), nextColumn);
                    }
                }
            }
        });
        textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue && textField != null) {
                    commitEdit(Double.parseDouble(textField.getText()));
                }
            }
        });
    }
    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
    /**
     *
     * @param forward true gets the column to the right, false the column to the left of the current column
     * @return
     */
    private TableColumn<ParticipantBean, ?> getNextColumn(boolean forward) {
        List<TableColumn<ParticipantBean, ?>> columns = new ArrayList<TableColumn<ParticipantBean, ?>>();
        for (TableColumn<ParticipantBean, ?> column : getTableView().getColumns()) {
            columns.addAll(getLeaves(column));
        }
        //There is no other column that supports editing.
        if (columns.size() < 2) {
            return null;
        }
        int currentIndex = columns.indexOf(getTableColumn());
        int nextIndex = currentIndex;
        if (forward) {
            nextIndex++;
            if (nextIndex > columns.size() - 1) {
                nextIndex = 0;
            }
        } else {
            nextIndex--;
            if (nextIndex < 0) {
                nextIndex = columns.size() - 1;
            }
        }
        return columns.get(nextIndex);
    }

    private List<TableColumn<ParticipantBean, ?>> getLeaves(TableColumn<ParticipantBean, ?> root) {
        List<TableColumn<ParticipantBean, ?>> columns = new ArrayList<TableColumn<ParticipantBean, ?>>();
        if (root.getColumns().isEmpty()) {
            //We only want the leaves that are editable.
            if (root.isEditable()) {
                columns.add(root);
            }
            return columns;
        } else {
            for (TableColumn<ParticipantBean, ?> column : root.getColumns()) {
                columns.addAll(getLeaves(column));
            }
            return columns;
        }
    }
}
