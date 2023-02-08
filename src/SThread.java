import java.io.*;
import java.net.*;


public class SThread extends Thread
{
    private Object [][] RTable; // routing table
    private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
    private BufferedReader in; // reader (for reading from the machine connected to)
    private String inputLine, outputLine, destination, addr; // communication strings
    private Socket outSocket; // socket for communicating with a destination
    private int ind; // indext in the routing table

    // Constructor
    SThread(Object [][] Table, Socket toClient, int index) throws IOException
    {
        out = new PrintWriter(toClient.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
        RTable = Table;
        addr = toClient.getInetAddress().getHostAddress();
        RTable[index][0] = addr; // IP addresses
        RTable[index][1] = toClient; // sockets for communication
        ind = index;
    }

    // Run method (will run for each machine that connects to the ServerRouter)
    public void run()
    {
        try
        {
            // Initial sends/receives
            System.out.println("Thread---Socket address: "+ addr);//
            destination = in.readLine(); // initial read (the destination for writing)
            System.out.println("Forwarding to " + destination);
            out.println("TConnected to the router."); // output in the socket this string --confirmation of connection

            // waits 10 seconds to let the routing table fill with all machines' information
            try{
                Thread.currentThread().sleep(10000);
            }
            catch(InterruptedException ie){
                System.out.println("Thread interrupted");
            }

            // loops through the routing table to find the destination
            for ( int i=0; i<10; i++)
            {
                if (destination.equals((String) RTable[i][0])){ // check the router table, if the current sent IP matches the table address. move forwrd
                    outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
                    System.out.println("Found destination: " + destination);
                    outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer to collect data from outputstream and turn to txt
                }}

            // Communication loop
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client/Server said: " + inputLine);
                // out.println(inputLine); that repeats stuff alot
                //outTo.println(outputLine);

                if (inputLine.equals("Bye.")) // exit statement // Thread recognixes bye
                    break;

                outputLine = inputLine; // passes the input from the machine to the output string for the destination

                if ( outSocket != null){
                    outTo.println(outputLine); // sends back input to the destination
                }

            }// end while

        }// end try
        catch (IOException e) {
            System.err.println("Could not listen to socket.");
            System.exit(1);
        }
    }
}
