package org.openjfx.fierydragons.render;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimerController {
    private int timeRemainingSeconds;
    private final Label timerLabel;
    private Timeline timeline;

    public TimerController(Label timerLabel) {
        this.timeRemainingSeconds = 150; // Default Game Clock
        this.timerLabel = timerLabel;
        this.timerLabel.setText(formatTime(this.timeRemainingSeconds));

        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                this.timeRemainingSeconds--;
                this.timerLabel.setText(formatTime(this.timeRemainingSeconds));
                if (this.timeRemainingSeconds <= 0) {
                    timeline.stop();
                }
            })
        );
        timeline.setCycleCount(this.timeRemainingSeconds);
    }

    public void startTimer() {
        timeline.play();
    }

    public void stopTimer() {
        timeline.stop();
    }

    private String formatTime(int timeSeconds) {
        int minutes = timeSeconds / 60;
        int remainingSeconds = timeSeconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
}
