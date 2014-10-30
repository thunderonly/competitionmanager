/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * [Enter type description here].
 *
 * @author Bull SAS
 */
public class NotificationView {

    private Stage currentStage;
    private static final double ICON_WIDTH = 24;
    private static final double ICON_HEIGHT = 24;
    private double width = 300;
    private double height = 100;
    private double offsetX = 5;
    private double offsetY = 50;
    private double spacingY = 5;
    private Pos popupLocation = Pos.BOTTOM_RIGHT;
    private Stage stageRef = null;
    private Duration popupLifetime;
    private ObservableList<Popup> popups;

    public NotificationView(Stage mainStage) {
        Scene scene = new Scene(new Region());
        scene.setFill(null);
        scene.getStylesheets().add(getClass().getResource("css/notifier.css").toExternalForm());
        popupLifetime = Duration.millis(7000);
        popups = FXCollections.observableArrayList();
        currentStage = new Stage();
        currentStage.initStyle(StageStyle.TRANSPARENT);
        currentStage.setScene(scene);
        if (mainStage != null) {
            currentStage.initOwner(mainStage);
        }
    }


    /**
     * @param stageRef      The Notification will be positioned relative to the given Stage.<br>
     *                      If null then the Notification will be positioned relative to the primary Screen.
     * @param popupLocation The default is TOP_RIGHT of primary Screen.
     */
    public void setPopupLocation(final Stage stageRef, final Pos popupLocation) {
        if (stageRef != null) {
            this.stageRef = stageRef;
            this.currentStage.initOwner(stageRef);
        }
        this.popupLocation = popupLocation;
    }

    /**
     * Sets the Notification's owner stage so that when the owner
     * stage is closed Notifications will be shut down as well.<br>
     * This is only needed if <code>setPopupLocation</code> is called
     * <u>without</u> a stage reference.
     *
     * @param owner the stage that triggered the notification.
     */
    public void setNotificationOwner(final Stage owner) {
        if (owner != null && this.currentStage.getOwner() != null) {
        } else {
            this.currentStage.initOwner(owner);
        }
    }

    /**
     * @param offsetX The horizontal shift required.
     *                <br> The default is 0 px.
     */
    public void setOffsetX(final double offsetX) {
        this.offsetX = offsetX;
    }

    /**
     * @param offsetY The vertical shift required.
     *                <br> The default is 25 px.
     */
    public void setOffsetY(final double offsetY) {
        this.offsetY = offsetY;
    }

    /**
     * @param width The default is 300 px.
     */
    public void setWidth(final double width) {
        this.width = width;
    }

    /**
     * @param height The default is 80 px.
     */
    public void setHeight(final double height) {
        this.height = height;
    }

    /**
     * @param spacingY The spacing between multiple Notifications.
     *                 <br> The default is 5 px.
     */
    public void setSpacingY(final double spacingY) {
        this.spacingY = spacingY;
    }

    /**
     * Returns the Duration that the notification will stay on screen before it
     * will fade out.
     *
     * @return the Duration the popup notification will stay on screen
     */
    public Duration getPopupLifetime() {
        return popupLifetime;
    }

    /**
     * Defines the Duration that the popup notification will stay on screen before it
     * will fade out. The parameter is limited to values between 2 and 20 seconds.
     *
     * @param popupLifetime popup duration
     */
    public void setPopupLifetime(final Duration popupLifetime) {
        this.popupLifetime = Duration.millis(clamp(2000, 20000, popupLifetime.toMillis()));
    }

    /**
     * Shows a notification on the screen at the given level.
     *
     * @param level   the level of the message to show
     * @param title   the message title
     * @param message the message to show
     */
    public void notify(Level level, String title, String message) {
        preOrder();
        showPopup(level, title, message);
    }

    /**
     * Shows a notification on the screen at the info level.
     *
     * @param title   the message title
     * @param message the message to show
     */
    public void notifyInfo(String title, String message) {
        notify(Level.INFO, title, message);
    }

    /**
     * Shows a notification on the screen at the warning level.
     *
     * @param title   the message title
     * @param message the message to show
     */
    public void notifyWarning(String title, String message) {
        notify(Level.WARN, title, message);
    }

    /**
     * Shows a notification on the screen at the success level.
     *
     * @param title   the message title
     * @param message the message to show
     */
    public void notifySuccess(String title, String message) {
        notify(Level.SUCCESS, title, message);
    }

    /**
     * Shows a notification on the screen at the error level.
     *
     * @param title   the message title
     * @param message the message to show
     */
    public void notifyError(String title, String message) {
        notify(Level.ERROR, title, message);
    }

    /**
     * Makes sure that the given VALUE is within the range of MIN to MAX
     *
     * @param min
     * @param max
     * @param value
     * @return
     */
    private double clamp(final double min, final double max, final double value) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * Reorder the popup Notifications on screen so that the latest Notification will stay on top
     */
    private void preOrder() {
        if (popups == null || popups.isEmpty()) {
            return;
        }
        for (Popup popup : popups) {
            switch (popupLocation) {
                case TOP_LEFT:
                case TOP_CENTER:
                case TOP_RIGHT:
                    popup.setY(popup.getY() + height + spacingY);
                    break;
                default:
                    popup.setY(popup.getY() - height - spacingY);
            }
        }
    }

    /**
     * Creates and shows a popup with the data from the given Notification object
     *
     * @param level   the level of the message to show
     * @param title   the message title
     * @param message the message to show
     */
    private void showPopup(final Level level, final String title, final String message) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("title");

        BufferedImage image = null;
        try {
            switch (level) {
                case SUCCESS:
                    image = ImageIO.read(getClass().getResource("images/success.png"));
                    break;
                case INFO:
                    image = ImageIO.read(getClass().getResource("images/info.png"));
                    break;
                case WARN:
                    image = ImageIO.read(getClass().getResource("images/warning.png"));
                    break;
                case ERROR:
                    image = ImageIO.read(getClass().getResource("images/error.png"));
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageView icon = new ImageView(SwingFXUtils.toFXImage(image, null));
        icon.setFitWidth(ICON_WIDTH);
        icon.setFitHeight(ICON_HEIGHT);

        Label messageLabel = new Label(message, icon);
        messageLabel.getStyleClass().add("message");
        messageLabel.setWrapText(true);

        VBox popupLayout = new VBox();
        popupLayout.setSpacing(10);
        popupLayout.setPadding(new Insets(10, 10, 10, 10));
        popupLayout.getChildren().addAll(titleLabel, messageLabel);

        StackPane popupContent = new StackPane();
        popupContent.setPrefSize(width, height);
        popupContent.getStyleClass().add("notification");
        popupContent.getChildren().addAll(popupLayout);

        final Popup popup = new Popup();
        popup.setX(getX());
        popup.setY(getY());
        popup.getContent().add(popupContent);

        popups.add(popup);

        // Add a timeline for popup fade out
        KeyValue fadeOutBegin = new KeyValue(popup.opacityProperty(), 1.0);
        KeyValue fadeOutEnd = new KeyValue(popup.opacityProperty(), 0.0);

        KeyFrame kfBegin = new KeyFrame(Duration.ZERO, fadeOutBegin);
        KeyFrame kfEnd = new KeyFrame(Duration.millis(500), fadeOutEnd);

        Timeline timeline = new Timeline(kfBegin, kfEnd);
        timeline.setDelay(popupLifetime);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                popup.hide();
                popups.remove(popup);
                if (popups.isEmpty()) {
                    currentStage.hide();
                }
            }
        });

        if (currentStage.isShowing()) {
            currentStage.toFront();
        } else {
            currentStage.show();
        }

        popup.show(currentStage);
        timeline.play();
    }

    private double getX() {
        if (null == stageRef) {
            return calcX(0.0, Screen.getPrimary().getBounds().getWidth());
        }

        return calcX(stageRef.getX(), stageRef.getWidth());
    }

    private double getY() {
        if (null == stageRef) {
            return calcY(0.0, Screen.getPrimary().getBounds().getHeight());
        }

        return calcY(stageRef.getY(), stageRef.getHeight());
    }

    private double calcX(final double left, final double totalWidth) {
        switch (popupLocation) {
            case TOP_LEFT:
            case CENTER_LEFT:
            case BOTTOM_LEFT:
                return left + offsetX;
            case TOP_CENTER:
            case CENTER:
            case BOTTOM_CENTER:
                return left + (totalWidth - width) * 0.5 - offsetX;
            case TOP_RIGHT:
            case CENTER_RIGHT:
            case BOTTOM_RIGHT:
                return left + totalWidth - width - offsetX;
            default:
                return 0.0;
        }
    }

    private double calcY(final double top, final double totalHeight) {
        switch (popupLocation) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                return top + offsetY;
            case CENTER_LEFT:
            case CENTER:
            case CENTER_RIGHT:
                return top + (totalHeight - height) / 2 - offsetY;
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                return top + totalHeight - height - offsetY;
            default:
                return 0.0;
        }
    }
    /**
     * Different levels of notification
     */
    public static enum Level {
        SUCCESS(0),
        INFO(1),
        ERROR(2),
        WARN(3),;

        private int key;

        Level(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }
    }
}
