package WorkerThreads;

import Constants.Constants;
import Constants.Error;
import Model.Trade;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkerThread1 implements Runnable {
    private String fileName;
    private LinkedBlockingQueue<Trade> queue;
    private DataOutputStream outStream;

    public WorkerThread1 (String fileName, LinkedBlockingQueue<Trade> queue, DataOutputStream outStream) {
        this.fileName = fileName;
        this.outStream = outStream;
        this.queue = queue;
    }


    @Override
    public void run ( ) {
        //Below Thread will read data from the file and pass it to the queue which the consumer-thread(WorkerThread2) will consume
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    Constants.SORTED_PATH + fileName));
            String line = reader.readLine();
            while (line != null) {
                Gson g = new Gson();
                Trade trade = g.fromJson(line, Trade.class);
                queue.put(trade);
                line = reader.readLine();
            }
            //End queue
            queue = null;
            reader.close();
        } catch (IOException | InterruptedException e) {
            try {
                outStream.writeUTF(Error.INVALID_STOCK_NAME.getMessage());
                outStream.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
