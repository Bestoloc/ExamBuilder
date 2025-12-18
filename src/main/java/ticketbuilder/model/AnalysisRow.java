package ticketbuilder.model;

import javafx.beans.property.*;

public class AnalysisRow {

    private final IntegerProperty questionId;
    private final StringProperty questionText;
    private final StringProperty topicName;
    private final IntegerProperty difficulty;
    private final LongProperty usageCount;
    private final DoubleProperty averageScore;
    private final StringProperty comment;

    public AnalysisRow(
            Integer questionId,
            String questionText,
            String topicName,
            Integer difficulty,
            Long usageCount,
            Double averageScore,
            String comment
    ) {
        this.questionId = new SimpleIntegerProperty(questionId);
        this.questionText = new SimpleStringProperty(questionText);
        this.topicName = new SimpleStringProperty(topicName);
        this.difficulty = new SimpleIntegerProperty(difficulty);
        this.usageCount = new SimpleLongProperty(usageCount);
        this.averageScore = new SimpleDoubleProperty(
                averageScore == null ? 0.0 : averageScore
        );
        this.comment = new SimpleStringProperty(
                comment == null ? "" : comment
        );
    }

    // 🔥 ОБЯЗАТЕЛЬНО property-методы

    public IntegerProperty questionIdProperty() { return questionId; }
    public StringProperty questionTextProperty() { return questionText; }
    public StringProperty topicNameProperty() { return topicName; }
    public IntegerProperty difficultyProperty() {return difficulty;}
    public LongProperty usageCountProperty() { return usageCount; }
    public DoubleProperty averageScoreProperty() { return averageScore; }
    public StringProperty commentProperty() { return comment; }
}


