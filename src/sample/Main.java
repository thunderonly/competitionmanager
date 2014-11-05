package sample;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import fr.csmb.competition.component.grid.bean.ParticipantBean;
import fr.csmb.competition.component.grid.fight.GridComponentFight2;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override public void start(Stage stage) {

        List<ParticipantBean> participantBeans = new ArrayList<ParticipantBean>();
        for (int i = 0; i < 10; i++) {
            participantBeans.add(new ParticipantBean("Nom " + i, "Prenom " + i));
        }

//        GridComponentFight2 group = new GridComponentFight2(participantBeans);
//        group.drawGrid();
        HBox hBox = new HBox();
        final TextField tfDebut = new TextField();
//        final long debutL = System.currentTimeMillis();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String debut = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        tfDebut.setText(debut);

        final TextField tfFin = new TextField();
        final TextField tfDuree = new TextField();
        Button valide = new Button("valider");
        valide.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                final long finL = System.currentTimeMillis();
                String fin = simpleDateFormat.format(new Date(finL));
                tfFin.setText(fin);
                Date dateDebut = null;
                Date dateFin = null;
                try {
                    dateDebut = simpleDateFormat.parse(tfDebut.getText());
                    dateFin = simpleDateFormat.parse(fin);
                } catch (ParseException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                GregorianCalendar calendarDebut = new GregorianCalendar();
                calendarDebut.setTime(dateDebut);
                GregorianCalendar calendarFin = new GregorianCalendar();
                calendarFin.setTime(dateFin);

                calendarFin.add(Calendar.HOUR_OF_DAY, -calendarDebut.get(Calendar.HOUR_OF_DAY));
                calendarFin.add(Calendar.MINUTE, -calendarDebut.get(Calendar.MINUTE));

                String duree = simpleDateFormat.format(calendarFin.getTime());
                tfDuree.setText(duree);
            }
        });

        hBox.getChildren().addAll(tfDebut, tfFin, tfDuree, valide);
        Group group = new Group();
        group.getChildren().add(hBox);
        Scene scene = new Scene(group);

//        SplitPane sp = new SplitPane();
//        final StackPane sp1 = new StackPane();
//        sp1.getChildren().add(new Button("Button One"));
//        final StackPane sp2 = new StackPane();
//        sp2.getChildren().add(new Button("Button Two"));
//        final StackPane sp3 = new StackPane();
//        sp3.getChildren().add(new Button("Button Three"));
//        sp.getItems().addAll(sp1, sp2, sp3);
//        sp.setDividerPositions(0.3f, 0.6f, 0.9f);

//        group.getChildren().add(sp);

        stage.setTitle("Welcome to JavaFX!");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
