package org.openjfx.fierydragons.render;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;

public class TimerController {
    private int timeRemainingSeconds;
    private final Label timerLabel;
    private Timeline timeline;

    public TimerController(int timeRemainingSeconds, Label timerLabel) {
        this.timeRemainingSeconds = timeRemainingSeconds; // Default Game Clock
        this.timerLabel = timerLabel;
        this.timerLabel.setText(formatTime(this.timeRemainingSeconds));

        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                this.timeRemainingSeconds--;
                this.timerLabel.setText(formatTime(this.timeRemainingSeconds));
                if (this.timeRemainingSeconds <= 0) {
                    timeline.stop();
                    try {
                        BoardController.getInstance().endTurn();
                    } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
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
        if (timeSeconds < 0) {
            timeSeconds = 0;
        }
        int minutes = timeSeconds / 60;
        int remainingSeconds = timeSeconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public int getTimeRemainingSeconds() {
        return timeRemainingSeconds;
    }

    public void setTimeRemainingSeconds(int timeRemainingSeconds) {
        this.timeRemainingSeconds = timeRemainingSeconds;
    }
}
