package co.starodubov.jmptimer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.AudioTrack;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;
public class JmpTimerApp extends Application {


//    public static void main(String[] args) throws IOException {



//        var executor = new JumpWindowSingleThreadScheduledExecutor(200, 10);
//        executor.exec(() -> {
//            String bip = "/home/haxul/Development/jmp-timer/src/main/resources/co/starodubov/jmptimer/mixkit-police-whistle-614.wav";
//            Media hit = new Media(new File(bip).toURI().toString());
//            MediaPlayer mediaPlayer = new MediaPlayer(hit);
//            mediaPlayer.play();
//        });

//        System.in.read();
//        System.out.println("stop");
//    }

    @Override
    public void start(Stage stage) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File file= new File("C:\\Users\\sssta\\development\\jmp-timer\\src\\main\\resources\\co\\starodubov\\jmptimer\\Whistling-Call-D2-www.fesliyanstudios.com.wav");
        var as = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(as);
        clip.start();
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

        public BottomZone() {
            box.getChildren().add(startBtn);
            startBtn.setOnAction((ActionEvent event) -> {
                var minutes = LeftZone.minFd.getText();
                var secs = LeftZone.minFd.getText();
                TopZone.timerText.setText(minutes + ":" + secs);
            });
//            box.getChildren().add(new Button("pause"));
//            box.getChildren().add(new Button("stop"));
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