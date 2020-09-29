_**Upstox Trade Test**_

**`NOTE:`**  
The Initial approach to solve this problem was to load the json file and store it to a time-series
database but due to lack of time and on discussion with the panel I was adviced that I can use the 
in-memory approach.

The Project has been developed using intellij as the IDE it would be great if you can run the
same using that But Im sure the same can be run using other IDE's or even in linux environment.


 ` Step-1)`
  
  I have kept the sample dataset `trades.json` in the resources folder(src/main/resources).The Idea is to split the dataset depending on the stock name first
  and then sort it. 
  The data splitting and sorting logic is already written in the 'DataProcessor.java' file .
  You dont have to run the file externally ,you can simply type in this code while you do a maven build
  `mvn -e clean install exec:java ` .You will see two new folders created 
  
  a.`sorted` 
  
  b.`unsorted`
  
  both these files will be present in the resources folder.we will be using the json's inside the `sorted` folder for further computations.
  
  `Step-2)` 
  
  I have created a Web-Socket between the Client and the Server .
  Start these java application in order `TCPServer.java`(src/main/java/WebSocket) once successfully started ,Start `TCPClient.java`
  (src/main/java/WebSocket) if both the java applications are successfully started that means the handshake has been established.
  
  Now Provide an input or event on the client side (Paste the below input in the console of `TCPClient`) 
  
  `{"event": "subscribe", "symbol": "XXBTZUSD", "interval": 15}`
  
  you should start seeing the output in the cosole.
  
  
  
  `-------------------------Underlying Logic--------------------------------`
  
Any input provided in the TCPClient console is received by the TCPServer .
TCPserver checks if the input provided is valid or no ,If invalid the Server throws an exception with a proper message stating `Please Enter a Valid Event`
if the symbol entered is not a valid stock name you will get a message stating `Please enter a valid stock name`.


If the Event is valid, we convert the input to a Java object of class `Subsciption.java`(src/main/java/model) get the symbol name ,then append `.json` to get the filename 

This filename along with a LinkedBlocking  queue is passed to a thread named `WorkerThread1.java`(src/main/java/WorkerThreads) .`WorkerThread1` reads data line by line from the derived file name converts the line to a 
 Java object of class `Trade.java` and passes the trade object to the queue.
 
 This same queue is also shared with the `WorkerThread2.java` which picks up the trade from the queue one by one and displays the chart.
 
 I have put an initial sleep of 1000ms in the `WorkerThread2.java` at line number 35, You can increase or decrease the wait-time based on your convinience.
 
 If it's the first trade we simply mark that trade as the first interval trade and display it 
 
        if (intervalTrade == null) {
                     intervalTrade = queue.take();
                     drawFirstCandle(intervalTrade);
                 }
                 
post that we take the subsequent trades one by one and check if the trade lies in the specified interval and display that 

                Trade trade = queue.take();
                long currentTradeTime = TimeUtil.getSeconds(trade.getTS2());
                long intervalTradeTime = TimeUtil.getSeconds(intervalTrade.getTS2());
                // if the trades lie within the interval display the chart else display empty event and keep looping incrementing with the interval
                if (TimeUtil.getTimeDifference(currentTradeTime, intervalTradeTime) < interval) {

                    drawRunningCandle(trade);

                }
       
If the trade dosent lie in the interval, we compute the close of the trade and draw the close candle and the empty candle 
`                            drawCandleWithClose(trade);
                            drawEmptyCandle(trade); `   
                            
 We also iterate to check for any trade in the subsequent intervals ,if no trade is found we display the empty candle
`                  

                     for (long i = (intervalTradeTime + interval); !(TimeUtil.getTimeDifference(i, currentTradeTime) < interval) && queue != null; i += interval) {
                         drawEmptyCandle(trade);
                     }                                 `

post the interval if we get any new trade which lies in the interval time-frame we mark that trade as the interval trade,display the candle and start the loop again.

                    intervalTrade = trade;
                    drawNextIntervalCandle(trade);
                    
         
We are using two utility classes `DrawChart.java` and `TimeUtil.java` to draw the candles and calculate the time-difference respectively.

The `DrawChart.java` class has a stack which pushes each trade into it and then pop's in the methods
`createCloseChart ( ) ;
createChartandDisplay ( );`

The main purpose of the stack is to store the previous trade to evaluate the High,Low,and the Close with the current trade, the logic is written in the methods which I shared above.
                


`Step-3)`      
I have written all the test-cases in the 

`a.DrawChartTest.java`(src/test/java/utility) 

`b.TimeUtilTest.java`(src/test/java/utility)  

You can simply run them by going to the class and running them as java application       
                                                      
`Please Note:`

Due to lack of time 
I have not yet written the exception handling for invalid event so even if you enter invalid event in the input the programme will still execute.



I can be Reached on +919833648779 or abhishek90.Iyer@gmail.com


Thanks 

Abhishek Ramkumar

  
  
  
  
  
  
  
  
  
  
  
  
  
  
   
  
  
  
  
  
  
    

# tradeparser