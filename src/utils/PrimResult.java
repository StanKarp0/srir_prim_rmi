package utils;

import java.io.Serializable;

public class PrimResult implements Serializable {

    private final int from;
    private final int to;
    private final int wage;

    public PrimResult(int from, int to, int wage) {

        this.from = from;
        this.to = to;
        this.wage = wage;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getWage() {
        return wage;
    }

    @Override
    public String toString() {
        return "PrimResult{" +
                "from=" + from +
                ", to=" + to +
                ", wage=" + wage +
                '}';
    }
}
