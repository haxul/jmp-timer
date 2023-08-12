module co.starodubov.jmptimer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens co.starodubov.jmptimer to javafx.fxml;
    exports co.starodubov.jmptimer;
}