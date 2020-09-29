package DataProcessor;

import Constants.Constants;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;


public class DataProcessor {


    public static void main (String[] args) {
       //We will first group the data by trade id
        groubBySymbol();
       //Sort the data by time series after grouping it
        sortByTimeSeries();
    }

    private static void groubBySymbol ( ) {

        JsonFactory jsonfactory = new JsonFactory();
        try (JsonParser jsonParser = jsonfactory.createJsonParser(new File(Constants.FILE_NAME))) {

            File tempFile = null;

            while (jsonParser.nextToken() != null) {

                String fileNameStore = null;
                JSONObject jsonObject = new JSONObject();

                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

                    String fieldName = jsonParser.getCurrentName();

                    switch (fieldName) {
                        case Constants.STOCKNAME:
                            jsonParser.nextToken();
                            fileNameStore = Constants.UNSORTED_PATH + jsonParser.getText() + ".json";
                            tempFile = new File(fileNameStore);
                            tempFile.createNewFile();
                            jsonObject.put(fieldName, jsonParser.getText());
                            break;

                        case Constants.PRICE:
                            jsonParser.nextToken();
                            jsonObject.put(fieldName, jsonParser.getText());
                            break;

                        case Constants.QUANTITY:
                            jsonParser.nextToken();
                            jsonObject.put(fieldName, jsonParser.getText());
                            break;

                        case Constants.TIMESTAMP:
                            jsonParser.nextToken();
                            jsonObject.put(fieldName, jsonParser.getText());
                            break;

                        default:
                            break;
                    }

                }

                if (fileNameStore != null && tempFile != null) {
                    FileWriter fr = new FileWriter(tempFile, true);
                    BufferedWriter br = new BufferedWriter(fr);
                    br.write(jsonObject.toJSONString());
                    br.write("\r\n");
                    br.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sortByTimeSeries ( ) {

        File folder = new File(Constants.UNSORTED_PATH);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {

                JsonFactory jsonfactory = new JsonFactory();
                try (JsonParser jsonParser = jsonfactory.createJsonParser(new File(Constants.UNSORTED_PATH + file.getName()))) {
                    LinkedList<Trades> tradeList = new LinkedList<Trades>();

                    while (jsonParser.nextToken() != null) {
                        Trades trade = new Trades();
                        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {

                            String fieldName = jsonParser.getCurrentName();

                            switch (fieldName) {
                                case Constants.STOCKNAME:
                                    jsonParser.nextToken();
                                    trade.setStockName(jsonParser.getText());
                                    break;

                                case Constants.PRICE:
                                    jsonParser.nextToken();
                                    trade.setPrice(jsonParser.getText());
                                    break;

                                case Constants.QUANTITY:
                                    jsonParser.nextToken();
                                    trade.setQuantity(jsonParser.getText());
                                    break;

                                case Constants.TIMESTAMP:
                                    jsonParser.nextToken();
                                    trade.setTimeSeries(Long.parseLong(jsonParser.getText()));
                                    break;

                                default:
                                    break;
                            }

                        }
                        tradeList.add(trade);

                    }
                    //Sort the collection Based on timestamp and create refurbish the data
                    Collections.sort(tradeList);

                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.SORTED_PATH + file.getName()))) {
                        for (Trades trade : tradeList) {
                            writer.write("{" + Constants.STOCKNAME + ":" + trade.getStockName());
                            writer.write("," + Constants.PRICE + ":" + trade.getPrice());
                            writer.write("," + Constants.QUANTITY + ":" + trade.getQuantity());
                            writer.write("," + Constants.TIMESTAMP + ":" + trade.getTimeSeries() + "}");
                            writer.newLine();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static class Trades implements Comparable<Trades> {

        private String stockName;
        private String price;
        private String quantity;
        private Long timeSeries;

        public String getStockName ( ) {
            return stockName;
        }

        public void setStockName (String stockName) {
            this.stockName = stockName;
        }

        public String getPrice ( ) {
            return price;
        }

        public void setPrice (String price) {
            this.price = price;
        }

        public String getQuantity ( ) {
            return quantity;
        }

        public void setQuantity (String quantity) {
            this.quantity = quantity;
        }

        public Long getTimeSeries ( ) {
            return timeSeries;
        }

        public void setTimeSeries (Long timeSeries) {
            this.timeSeries = timeSeries;
        }

        @Override
        public int compareTo (Trades trades) {
            if (timeSeries == trades.timeSeries)
                return 0;
            else if (timeSeries > trades.timeSeries)
                return 1;
            else
                return -1;
        }

    }

}

