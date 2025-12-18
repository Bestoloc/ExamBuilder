package ticketbuilder.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScoreRow {

    private final TicketHistory history;

    private final StringProperty questionText;
    private final IntegerProperty score;
    private final StringProperty comment;
    private final IntegerProperty difficulty;

    public ScoreRow(TicketHistory h) {
        this.history = h;
        this.questionText = new SimpleStringProperty(
                h.getQuestion().getText()
        );
        this.score = new SimpleIntegerProperty(
                h.getScore()
        );
        this.comment = new SimpleStringProperty(
                h.getComment() == null ? "" : h.getComment()
        );
        this.difficulty = new SimpleIntegerProperty(
                h.getQuestion().getDifficulty());
    }

    public StringProperty questionTextProperty() {
        return questionText;
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    public StringProperty commentProperty() {
        return comment;
    }

    public TicketHistory getHistory() {
        return history;
    }
    public IntegerProperty difficultyProperty() {
        return difficulty;
    }
}


