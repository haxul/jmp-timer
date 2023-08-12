package co.starodubov.jmptimer;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

public class JmpTimerApp extends Application {

    @Override
    public void start(Stage stage) {
        var bp = new BorderPane();

        bp.setLeft(new LeftZone().getPane());
        bp.setTop(new TopZone().getPane());
        bp.setBottom(new BottomZone().getPane());
        var scene = new Scene(bp, 500, 200);
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
                final int timePeriod = Integer.parseInt(LeftZone.timePeriod.getText());
                final int windowSize = Integer.parseInt(LeftZone.windowSize.getText());
                final var makeSoundTask = new MakeSoundTask(
                        timePeriod,
                        windowSize,
                        new File("/home/haxul/Development/jmp-timer/src/main/resources/co/starodubov/jmptimer/Whistling-Call-D2-www.fesliyanstudios.com.wav"));
                startBtn.setDisable(true);
                makeSoundTask.startAsync();
            });

            // TODO
//            box.getChildren().add(stopBtn);
//            stopBtn.setOnAction((ActionEvent e) -> {
//                JmpTimerApp.pool.stop();
//            });

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
        public static final TextField timePeriod = new TextField("15");
        public static final TextField windowSize = new TextField("5");

        public LeftZone() {
            gp.addRow(0, new Label("размеры окна: "));
            gp.addRow(1, new Text("общее время"), timePeriod);
            gp.addRow(2, new Text("размер окна"), windowSize);
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
            //TODO
//            timerText.setFont(Font.font(50));
//            pane.getChildren().add(timerText);
//            pane.setAlignment(Pos.CENTER);
        }

        public Pane getPane() {
            return pane;
        }
    }
}