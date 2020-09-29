package Model;

public class Subscription {

    private String event;
    private String symbol;
    private int interval;

    public String getEvent ( ) {
        return event;
    }

    public void setEvent (String event) {
        this.event = event;
    }

    public String getSymbol ( ) {
        return symbol;
    }

    public void setSymbol (String symbol) {
        this.symbol = symbol;
    }

    public int getInterVal ( ) {
        return interval;
    }

    public void setInterVal (int interVal) {
        this.interval = interVal;
    }


}
