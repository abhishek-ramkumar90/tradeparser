package Utility;

import Constants.Constants;
import Model.BarChart;
import Model.Trade;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Stack;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DrawChartTest {

    private static final String DEFAULT_STOCk = "XXBTUSD";

    @InjectMocks
    public DrawChart drawChart = new DrawChart.DrawChartBuilder().build();
    @Mock
    public BarChart chart;
    @Mock
    public Trade trade;
    @Mock
    public DataOutputStream outStream;
    @Mock
    public Stack<BarChart> stack;
    private Number LOW = 6843.2;
    private Number HIGH = 6527.1;
    private String SYMBOL = "DEFAULT";
    private Number VOLUME = 0.2323;
    private Number PRICE = 6222.1;

    @Before
    public void init ( ) {
        chart = new BarChart();
        chart.setH(HIGH);
        chart.setL(LOW);
        chart.setSymbol(SYMBOL);
        chart.setVolume(VOLUME);
        chart.setO(PRICE);

    }

    @Test
    public void testCreateCloseChartLowerLow ( ) throws IOException {
        when(stack.pop()).thenReturn(chart);
        when(trade.getP()).thenReturn(6987.1);
        drawChart.createCloseChart();
        Assert.assertTrue(stack.pop().getL().doubleValue() == LOW.doubleValue());
        Assert.assertTrue(chart.getC().doubleValue() == trade.getP().doubleValue());
    }

    @Test
    public void testCreateCloseChartHigherLow ( ) throws IOException {
        when(stack.pop()).thenReturn(chart);
        when(trade.getP()).thenReturn(6187.1);
        drawChart.createCloseChart();
        Assert.assertFalse(stack.pop().getL().doubleValue() == LOW.doubleValue());
        Assert.assertTrue(chart.getC().doubleValue() == trade.getP().doubleValue());
    }

    @Test
    public void testCreateCloseChartHigherHigh ( ) throws IOException {
        when(stack.pop()).thenReturn(chart);
        when(trade.getP()).thenReturn(6987.1);
        drawChart.createCloseChart();
        Assert.assertTrue(stack.pop().getH().doubleValue() == trade.getP().doubleValue());
        Assert.assertTrue(chart.getC().doubleValue() == trade.getP().doubleValue());
    }

    @Test
    public void testCreateCloseChartLowerHigh ( ) throws IOException {
        when(stack.pop()).thenReturn(chart);
        when(trade.getP()).thenReturn(6187.1);
        drawChart.createCloseChart();
        Assert.assertFalse(stack.pop().getH().doubleValue() == trade.getP().doubleValue());
        Assert.assertTrue(chart.getC().doubleValue() == trade.getP().doubleValue());
    }

    @Test
    public void testcreateEmptyChart ( ) throws IOException {
        when(trade.getSym()).thenReturn(DEFAULT_STOCk);
        drawChart.createEmptyChart();
        Assert.assertTrue(drawChart.getChart().getSymbol().equals(DEFAULT_STOCk));
        Assert.assertTrue(drawChart.getChart().getEvent().equals(Constants.NOTIFY));
        Assert.assertNull(drawChart.getChart().getH());
        Assert.assertNull(drawChart.getChart().getL());
        Assert.assertNull(drawChart.getChart().getC());
        Assert.assertTrue(drawChart.getChart().getBarNum().intValue() > 0);
    }

    @Test
    public void testRunningChart ( ) throws IOException {
        when(stack.isEmpty()).thenReturn(false);
        when(stack.pop()).thenReturn(chart);
        when(trade.getQ()).thenReturn(0.111);
        when(trade.getP()).thenReturn(6987.1);
        when(trade.getSym()).thenReturn(DEFAULT_STOCk);
        drawChart.createChartandDisplay();
        Assert.assertTrue(drawChart.getChart().getL().doubleValue() == LOW.doubleValue());
        Assert.assertTrue(drawChart.getChart().getVolume().doubleValue() == 0.3433);
        Assert.assertTrue(drawChart.getChart().getH().doubleValue() == 6987.1);
        Assert.assertTrue(drawChart.getChart().getO().doubleValue() == 6222.1);
        Assert.assertTrue(drawChart.getChart().getEvent().equals(Constants.NOTIFY));
        Assert.assertTrue(drawChart.getChart().getBarNum().intValue() > 0);
        Assert.assertTrue(drawChart.getChart().getSymbol().equals(DEFAULT_STOCk));
    }


}
