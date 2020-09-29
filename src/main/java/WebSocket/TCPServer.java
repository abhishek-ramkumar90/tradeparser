package WebSocket;

import Model.Subscription;
import Model.Trade;
import WorkerThreads.WorkerThread1;
import WorkerThreads.WorkerThread2;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import Constants.Error;


public class TCPServer {
    public static void main (String[] args) throws Exception {
        try {
            ServerSocket server = new ServerSocket(8888);
            Socket serverClient = server.accept();
            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());
            String clientMessage = "", serverMessage = "";
            LinkedBlockingQueue<Trade> queue = new LinkedBlockingQueue<>();
            while (!clientMessage.equals("bye")) {
                clientMessage = inStream.readUTF();

                Gson g = new Gson();
                try {
                    Subscription clientSubscription = g.fromJson(clientMessage, Subscription.class);
                    String fileName = clientSubscription.getSymbol() + ".json";

                    WorkerThread1 wt1 = new WorkerThread1(fileName, queue,outStream);
                    Thread t = new Thread(wt1);
                    t.start();

                    WorkerThread2 wt2 = new WorkerThread2(queue, outStream, clientSubscription.getInterVal());
                    Thread t2 = new Thread(wt2);
                    t2.start();
                } catch (JsonSyntaxException e) {
                    outStream.writeUTF(Error.INVALID_MESSAGE.getMessage());
                    outStream.flush();
                }


            }
            inStream.close();
            outStream.close();
            serverClient.close();
            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}