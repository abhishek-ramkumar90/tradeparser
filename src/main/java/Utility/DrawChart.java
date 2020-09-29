package Utility;

import Constants.Constants;
import Model.BarChart;
import Model.Trade;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Stack;

public class DrawChart {

    private BarChart chart;
    private Trade trade;
    private DataOutputStream outStream;
    private int bar_num;
    private Stack<BarChart> stack;


    public DrawChart (DrawChartBuilder builder) {
        this.chart = builder.chart;
        this.trade = builder.trade;
        this.outStream = builder.outStream;
        this.bar_num = builder.bar_num;
        this.stack = builder.stack;
    }


    public void createCloseChart ( ) throws IOException {
        BarChart pop = stack.pop();
        if (pop.getH().doubleValue() < trade.getP().doubleValue()) {
            pop.setH(trade.getP());
        }
        if (pop.getL().doubleValue() > trade.getP().doubleValue()) {
            pop.setL(trade.getP());
        }
        pop.setC(trade.getP());
        Gson gson = new Gson();
        String json = gson.toJson(pop);
        outStream.writeUTF(json);
        outStream.flush();

    }

    public void createEmptyChart ( ) throws IOException {
        chart = new BarChart();
        chart.setEvent(Constants.NOTIFY);
        chart.setSymbol(trade.getSym());
        chart.setBarNum(bar_num);
        Gson gson = new Gson();
        String json = gson.toJson(chart);
        outStream.writeUTF(json);
        outStream.flush();
    }

    public void createChartandDisplay ( ) throws IOException {
        chart = new BarChart();
        chart.setH(trade.getP());
        chart.setC(0);
        chart.setL(trade.getP());
        chart.setO(trade.getP());
        chart.setVolume(trade.getQ());
        chart.setEvent(Constants.NOTIFY);
        chart.setSymbol(trade.getSym());
        chart.setBarNum(bar_num);
        if (!stack.isEmpty()) {
            BarChart pop = stack.pop();
            if (pop.getH().doubleValue() > chart.getH().doubleValue()) {
                chart.setH(pop.getH());
            }
            if (pop.getL().doubleValue() < chart.getL().doubleValue()) {
                chart.setL(pop.getL());
            }
            chart.setO(pop.getO());
            chart.setVolume(pop.getVolume().doubleValue() + chart.getVolume().doubleValue());
            stack.clear();

        }
        stack.push(chart);
        Gson gson = new Gson();
        String json = gson.toJson(chart);
        outStream.writeUTF(json);
        outStream.flush();
    }

    public BarChart getChart ( ) {
        return chart;
    }

    public static class DrawChartBuilder {

        private BarChart chart;

        private Trade trade;

        private DataOutputStream outStream;

        private int bar_num = 1;

        private Stack stack;

        public DrawChartBuilder initializeCandle (BarChart chart) {
            this.chart = chart;
            return this;
        }

        public DrawChartBuilder initializeTradeToBeDisplayed (Trade trade) {
            this.trade = trade;
            return this;
        }

        public DrawChartBuilder initializeBarNumber (int bar_num) {
            this.bar_num = bar_num;
            return this;
        }

        public DrawChartBuilder initializeStack (Stack stack) {
            this.stack = stack;
            return this;
        }

        public DrawChartBuilder initializeOutPutStream (DataOutputStream outStream) {
            this.outStream = outStream;
            return this;
        }

        public DrawChart build ( ) {
            return new DrawChart(this);
        }

    }


}
