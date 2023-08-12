package co.starodubov.jmptimer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.sound.sampled.AudioSystem;
import java.io.File;

public class JmpTimerApp extends Application {

    public final static File songFile =
            new File("/home/haxul/Development/jmp-timer/src/main/resources/co/starodubov/jmptimer/Whistling-Call-D2-www.fesliyanstudios.com.wav");

    public static JumpWindowSingleThreadScheduledExecutor pool = new JumpWindowSingleThreadScheduledExecutor(200, 3);

    @Override
    public void start(Stage stage) {
        var bp = new BorderPane();

        bp.setLeft(new LeftZone().getPane());
        bp.setTop(new TopZone().getPane());
        bp.setBottom(new BottomZone().getPane());
        var scene = new Scene(bp, 500, 500);
        stage.setScene(scene);
        stage.show();
    }

    public static class BottomZone {
        private final HBox box = new HBox();
        private final Button startBtn = new Button("старт");
        private final Button stopBtn = new Button("стоп");

        public BottomZone() {
            box.getChildren().add(startBtn);
            startBtn.setOnAction((ActionEvent event) -> {
                var minutes = LeftZone.minFd.getText();
                var secs = LeftZone.minFd.getText();
                TopZone.timerText.setText(minutes + ":" + secs);

                pool.exec(() -> {
                    try {
                        var as = AudioSystem.getAudioInputStream(songFile);
                        var clip = AudioSystem.getClip();
                        clip.open(as);
                        clip.start();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            });

            box.getChildren().add(stopBtn);
            stopBtn.setOnAction((ActionEvent e) -> {
                JmpTimerApp.pool.stop();
            });

            box.setAlignment(Pos.CENTER);
            box.setSpacing(10);
            box.setPadding(new Insets(10));
        }

        public Pane getPane() {
            return box;
        }
    }

    public static class LeftZone {
        private final GridPane gp = new GridPane();
        public static final TextField minFd = new TextField("0");
        public static final TextField secFd = new TextField("0");

        public LeftZone() {
            gp.addRow(0, new Text("мин"), minFd);
            gp.addRow(1, new Text("сек"), secFd);
        }

        public Pane getPane() {
            return gp;
        }
    }


    public static class TopZone {
        private final VBox pane = new VBox();
        public static Time curTime = new Time(0, 0);
        public static final Text timerText = new Text("00:00");

        public TopZone() {
            timerText.setFont(Font.font(50));

            pane.getChildren().add(timerText);
            pane.setAlignment(Pos.CENTER);
        }

        public Pane getPane() {
            return pane;
        }
    }


}