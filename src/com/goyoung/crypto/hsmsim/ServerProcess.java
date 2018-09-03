package com.goyoung.crypto.hsmsim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;


//this class represents the TCP/IP service process for the simulator
public class ServerProcess {


    //static properties
    public static String s_port;
    public static String s_bindIP;
    public static String s_tcp_backlog;
    public static String LMK0x01;
    public static String LMK0x00;
    public static String LMK0x09;

    public static String BDK0x00;
    public static String BDK0x01;
    public static String KEK0x00;
    public static String KEK0x01;

    public static String SerialNumber;
    public static String ProductID;
    public static String LoaderVersion;
    public static String PersonalityVersion;

    static {
        Properties prop = new Properties();
        InputStream in = ServerProcess.class.getResourceAsStream("config.properties");

        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //load the service properties
        s_port = prop.getProperty("port"); //the port listen on
        s_bindIP = prop.getProperty("bindIP"); //IP address to bind to: 0.0.0.0 indicates all IPs
        s_tcp_backlog = prop.getProperty("tcp_backlog"); //set the max queue depth


        //load the LMKs
        LMK0x01 = prop.getProperty("LMK0x01");
        LMK0x00 = prop.getProperty("LMK0x00");
        LMK0x09 = prop.getProperty("LMK0x09");

        //load stored Keys
        KEK0x00 = prop.getProperty("KEK0x00");
        KEK0x01 = prop.getProperty("KEK0x01");
        BDK0x00 = prop.getProperty("BDK0x00");
        BDK0x01 = prop.getProperty("BDK0x01");

        SerialNumber = prop.getProperty("SerialNumber");
        ProductID = prop.getProperty("ProductID");
        LoaderVersion = prop.getProperty("LoaderVersion");
        PersonalityVersion = prop.getProperty("PersonalityVersion");

        System.out.println("Network HSM starting up");
        System.out.println("Serial Number: " + SerialNumber);
        System.out.println("Product ID" + ProductID);
        System.out.println("Loader Version: " + LoaderVersion);
        System.out.println("Personality Version: " + PersonalityVersion);


        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientServiceThread extends Thread {

        Socket myClientSocket;
        boolean m_bRunThread = true;

        public ClientServiceThread() {
            super();
        }

        ClientServiceThread(Socket s) {
            myClientSocket = s;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;


            // Log details of this connection
            System.out.println("Accepted Client Address - " + myClientSocket.getInetAddress().getHostName());

            try { // setup input and output streams
                in = new BufferedReader(new InputStreamReader(
                        myClientSocket.getInputStream()));
                out = new PrintWriter(new OutputStreamWriter(myClientSocket.getOutputStream()));

                while (m_bRunThread) {  // Loop untill done

                    String clientCommand = in.readLine();

                    //TODO enable some logging
                    //System.out.println("Client sent :" + clientCommand); // Log incoming stream

                    if (!ServerOn) { // Special command. Quit this thread
                        System.out.print("Server has already stopped");
                        out.println("Server has already stopped");
                        out.flush();
                        m_bRunThread = false;

                    }

                    if (clientCommand.equalsIgnoreCase("quit")) { // can be  whatever the exit code is Special command. Quit this thread
                        m_bRunThread = false;
                        System.out.print("Stopping client thread for client : ");

                    } else if (clientCommand.equalsIgnoreCase("end")) { // Quit if receive "end" this thread and Stop the Server
                        m_bRunThread = false;
                        System.out.print("Stopping client thread : ");
                        ServerOn = false;
                    } else { // Process request
                        String s_output = "";
                        try {
                            s_output = CommandProcessor.Go(clientCommand);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (s_output != "") {
                            out.println(CommandProcessor.Go(clientCommand));
                        }
                        out.flush();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally { // Clean up
                try {
                    in.close();
                    out.close();
                    myClientSocket.close();
                    System.out.println("...Stopped");
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {

        new ServerProcess(s_port);

    }

    ServerSocket myServerSocket;

    boolean ServerOn = true;

    int i_Port = Integer.parseInt(ServerProcess.s_port);

    public ServerProcess(String s_port) {
        try {
            myServerSocket = new ServerSocket(Integer.parseInt(s_port), Integer.parseInt(s_tcp_backlog), InetAddress.getByName(s_bindIP));
        } catch (IOException ioe) {
            System.out.println("Could not create server socket on port " + i_Port + ". Quitting.");
            System.exit(-1);
        }

        Calendar now = Calendar.getInstance(); // log the start time
        SimpleDateFormat formatter = new SimpleDateFormat(
                "E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        System.out.println("It is now : " + formatter.format(now.getTime()));

        // Successfully created Server Socket. Now wait for connections.
        while (ServerOn) {
            try { // Accept incoming connections.
                Socket clientSocket = myServerSocket.accept();

                // Start a Service thread
                ClientServiceThread cliThread = new ClientServiceThread(clientSocket);
                cliThread.start();

            } catch (IOException e) {
                System.out.println("Exception encountered on accept. Ignoring. Stack Trace :");
                e.printStackTrace();
            }
        }

        try {
            myServerSocket.close();
            System.out.println("Server Stopped");
        } catch (Exception ioe) {
            System.out.println("Problem stopping server socket");
            System.exit(-1);
        }
    }
}
