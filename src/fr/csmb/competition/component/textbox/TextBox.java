/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.component.textbox;

import fr.csmb.competition.Helper.ParticipantConverter;
import fr.csmb.competition.component.grid.ParticipantClassementFinalListener;
import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.xml.model.Participant;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class TextBox extends Group {
    private Text text;
    private Rectangle rectangle;
    private Rectangle clip;
    private TextBoxListner listner;
    private ParticipantBean participant;
    public StringProperty textProperty() { return text.textProperty(); }
    private Integer numPlace;

    public TextBox(ParticipantBean participant, double width, double height, Color colorRectangle) {
        this.text = new Text(participant.toString());
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFill(Color.BLACK);
        text.setTextOrigin(VPos.CENTER);
        text.setFont(Font.font("Calibri", 11));
        text.setFontSmoothingType(FontSmoothingType.LCD);
        text.setOnMouseClicked(new TextBoxEventHandler(this));

        this.rectangle = new Rectangle(width, height);
        rectangle.setFill(colorRectangle);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(0.5);

        this.clip = new Rectangle(width, height);
        text.setClip(clip);
        this.participant = participant;

        this.getChildren().addAll(rectangle, text);
        text.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                text.setBlendMode(BlendMode.DIFFERENCE);
            }
        });
        text.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {
                text.setBlendMode(null);
            }
        });
        text.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {

                dragEvent.acceptTransferModes(TransferMode.MOVE);
            }
        });
        text.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent dragEvent) {

                DataFormat format = DataFormat.lookupMimeType("fr.csmb.competition.xml.model.Participant");
                Participant participant1 = (Participant) dragEvent.getDragboard().getContent(format);
                ParticipantBean player = ParticipantConverter.convertParticipantToParticipantBean(participant1);
                player.setPlaceOnGrid(numPlace);
                setParticipant(player);
                dragEvent.setDropCompleted(true);
            }
        });
    }

    public Text getText() {
        return this.text;
    }

    public TextBoxListner getListner() {
        return listner;
    }

    public void setListner(TextBoxListner listner) {
        this.listner = listner;
    }

    public ParticipantBean getParticipant() {
        return this.participant;
    }

    public void setParticipant(ParticipantBean participant) {
        this.participant = participant;
        this.text.setText(participant.toString());
    }

    public Integer getNumPlace() {
        return numPlace;
    }

    public void setNumPlace(Integer numPlace) {
        this.numPlace = numPlace;
    }

    @Override
    protected void layoutChildren() {
        final double w = rectangle.getWidth();
        final double h = rectangle.getHeight();
        clip.setWidth(w);
        clip.setHeight(h);
        clip.setLayoutX(0);
        clip.setLayoutY(-h/2);

        text.setWrappingWidth(w * 0.9);
        text.setLayoutX(w / 2 - text.getLayoutBounds().getWidth() / 2);
        text.setLayoutY(h / 2);
    }

    /**
     * Event handler when click on participant.
     * Click give victory for the source participant.
     */
    public class TextBoxEventHandler implements EventHandler<MouseEvent> {

        private TextBox textBoxSource;

        public TextBoxEventHandler(TextBox textBoxSource) {
            this.textBoxSource = textBoxSource;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
//            // If participant source have a resultat box, update text with the source participant
//            if (textBoxSource.getVictoryBox() != null) {
//                textBoxSource.getVictoryBox().setParticipant(textBoxSource.getParticipant());
//            }
            //If source participant have a listner, fire the click event
            if (textBoxSource.getListner() != null) {
                textBoxSource.getListner().onFireEvent(textBoxSource);
            }
        }
    }
}
