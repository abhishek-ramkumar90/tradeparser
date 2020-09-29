package WorkerThreads;

import Model.BarChart;
import Model.Trade;
import Utility.DrawChart;
import Utility.TimeUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkerThread2 implements Runnable {

    private LinkedBlockingQueue<Trade> queue;
    private DataOutputStream outStream;
    private BarChart chart;
    private long interval;
    private Trade intervalTrade;
    private int bar_num = 1;
    private Stack<BarChart> stack = new Stack<>();


    public WorkerThread2 (LinkedBlockingQueue<Trade> queue, DataOutputStream outStream, long interval) {
        this.queue = queue;
        this.outStream = outStream;
        this.interval = interval;
    }

    @Override
    public void run ( ) {
        try {
            while (queue != null) {
                //comment this line if you dont want to wait.
                Thread.sleep(1000);
                //Display First Trade as a chart and assign that as the interval Trade
                if (intervalTrade == null) {
                    intervalTrade = queue.take();
                    drawFirstCandle(intervalTrade);
                }
                Trade trade = queue.take();

                long currentTradeTime = TimeUtil.getSeconds(trade.getTS2());
                long intervalTradeTime = TimeUtil.getSeconds(intervalTrade.getTS2());
                // if the trades lie within the interval display the chart else display empty event and keep looping incrementing with the interval
                if (TimeUtil.getTimeDifference(currentTradeTime, intervalTradeTime) < interval) {

                    drawRunningCandle(trade);

                } else {

                    drawCandleWithClose(trade);
                    drawEmptyCandle(trade);

                    //check empty charts for next succesive intervals, if empty? display empty chart
                    for (long i = (intervalTradeTime + interval); !(TimeUtil.getTimeDifference(i, currentTradeTime) < interval) && queue != null; i += interval) {
                        drawEmptyCandle(trade);
                    }

                    // now we have got the next interval trade display it and then continue to the while loop
                    intervalTrade = trade;
                    drawNextIntervalCandle(trade);

                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void drawNextIntervalCandle (Trade trade) throws IOException {
        bar_num++;
        drawRunningCandle(trade);
    }

    private void drawEmptyCandle (Trade trade) throws IOException {
        bar_num++;
        getDrawChartObject(trade).createEmptyChart();
    }

    private void drawCandleWithClose (Trade trade) throws IOException {
        getDrawChartObject(trade).createCloseChart();
    }

    private void drawRunningCandle (Trade trade) throws IOException {
        getDrawChartObject(trade).createChartandDisplay();
    }

    private DrawChart getDrawChartObject (Trade trade) {

        return new DrawChart.DrawChartBuilder().initializeCandle(chart)
                .initializeTradeToBeDisplayed(trade)
                .initializeOutPutStream(outStream)
                .initializeStack(stack)
                .initializeBarNumber(bar_num)
                .build();
    }

    private void drawFirstCandle (Trade trade) throws IOException {
        drawRunningCandle(trade);
    }

}
