module co.starodubov.jmptimer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens co.starodubov.jmptimer to javafx.fxml;
    exports co.starodubov.jmptimer;
}