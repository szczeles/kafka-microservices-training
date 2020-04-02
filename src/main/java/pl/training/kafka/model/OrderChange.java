package pl.training.kafka.model;

import java.time.LocalDateTime;

public class OrderChange {
    private Order before;
    private Order after;
    private LocalDateTime ts;

    public Order getBefore() {
        return before;
    }

    public void setBefore(Order before) {
        this.before = before;
    }

    public Order getAfter() {
        return after;
    }

    public void setAfter(Order after) {
        this.after = after;
    }

    public LocalDateTime getTs() {
        return ts;
    }

    public void setTs(LocalDateTime ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return "OrderChange{" +
                "before=" + before +
                ", after=" + after +
                ", ts=" + ts +
                '}';
    }
}
