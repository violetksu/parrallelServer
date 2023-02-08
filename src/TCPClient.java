import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {

        // Variables for setting up connection and communication
        Socket Socket = null; // socket to connect with ServerRouter
        PrintWriter out = null; // for writing to ServerRouter
        BufferedReader in = null; // for reading form ServerRouter
        InetAddress addr = InetAddress.getLocalHost();// get local hostname and address
        String host = addr.getHostAddress(); // Client machine's IP in string form
        String routerName = "LAPTOP-I12D85L6"; // ServerRouter host name
        int SockNum = 5555; // port number

        // Tries to connect to the ServerRouter
        try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
        }
        catch (UnknownHostException e) {
            System.err.println("Don't know about router: " + routerName);
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: " + routerName);
            e.printStackTrace();
            System.exit(1);
        }

        // Variables for message passing

        Reader reader = new FileReader("hiBitch.txt");
        BufferedReader fromFile =  new BufferedReader(reader); // reader for the string file


        String fromServer; // messages received from ServerRouter
        String fromUser; // messages sent to ServerRouter
        //String address ="10.0.0.226"; // destination IP (Server)
        String address = "10.100.69.49";
        long t0, t1, t;
        long transTime=0;


        // Communication process (initial sends/receives
        out.println(address);// initial sending of IP (IP of the destination Server)
        fromServer = in.readLine();//initial receive from router (verification of connection)
        System.out.println("ServerRouter sends back verif: " + fromServer);
        System.out.println("ServerRouter: " + fromServer);
        out.println(host); // Client sends the IP of its machine as initial send
        //System.out.println("sending CLEINT socket address (.out):"+ host);
        t0 = System.currentTimeMillis();

        // Communication while loop

        while ((fromServer = in.readLine()) != null) {
            //System.out.println("while loop started");
            System.out.println("ServerRouter: " + fromServer);
            t1 = System.currentTimeMillis();

            if (fromServer.equals("BYE")){ // exit statement work if you remove the period
                //System.out.println("we found bye");
                break;}

            t = t1 - t0;
            transTime+=t;
            System.out.println("Cycle time: " + t);

            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                out.println(fromUser); // sending the strings to the Server via ServerRouter
                t0 = System.currentTimeMillis();
                //cycle time reset for next line
            }
        }
        // Note: Find a way to get final transmission time, in double to know how fast its going
        //maybe get transmission rate
        //get message size added
        //not recognizing bye statement to get out of loop
        //System.out.println("Got out of while loop and in process of closing");
        System.out.println("Final Transmission time for full message: "+ transTime);

        // in order to connect multiple servers wrap this in a foor loop with i=how mnay devices they want connected
        //the port can only be used one at a time. ??

        // closing connections
        //This doesn't close????????????????????????
        out.close();
        in.close();
        Socket.close();

    }
}

