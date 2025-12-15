package ticketbuilder.model;

import java.util.List;

public class TicketResult {

    private int ticketId;
    private List<Question> questions;

    public TicketResult(int ticketId, List<Question> questions) {
        this.ticketId = ticketId;
        this.questions = questions;
    }

    public int getTicketId() {
        return ticketId;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
