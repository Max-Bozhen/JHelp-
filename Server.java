/*
 * Server.java
 *
 */
package jhelp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class sets a network connection between end client's objects
 * of {@link jhelp.Client} type and single {@link jhelp.ServerDb} object.
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 * @see jhelp.Client
 * @see jhelp.ClientThread
 * @see jhelp.ServerDb
 */
public class Server implements JHelp {

    /**
     *
     */
    private ServerSocket serverSocket;
    /**
     *
     */
    private Socket clientSocket;
    /**
     *
     */
    private DataInputStream input;
    /**
     *
     */
    private DataOutputStream output;
    private Socket serverClient;

    /** Creates a new instance of Server */
    public Server() {
        this(DEFAULT_SERVER_PORT, DEFAULT_DATABASE_PORT);
        System.out.println("SERVER: Default Server Constructed");
    }

    /**
     *
     * @param port
     * @param dbPort
     */
    public Server(int port, int dbPort) {
        port = DEFAULT_DATABASE_PORT;
        dbPort = DEFAULT_DATABASE_PORT;
        try {
            serverClient = new Socket(InetAddress.getLocalHost(), DEFAULT_DATABASE_PORT);
            clientSocket = serverSocket.accept();
            ClientThread clentThread = new ClientThread(this, clientSocket);
        } catch (UnknownHostException ex) {
           ex.printStackTrace();
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        System.out.println("SERVER: Server Constructed");
    }

    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("SERVER: main");
        Server server = new Server();
       
        if (server.connect(args) == JHelp.OK) {
            server.run();
//            server.disconnect();
        }
    }

    /**
     *
     */
    private void run() {
        try {
            output = new DataOutputStream(clientSocket.getOutputStream());

            System.out.println("Client DataOutputStream  created");
            input = new DataInputStream(clientSocket.getInputStream());
            System.out.println("ClientDataInputStream  created");
            
            
        } catch (IOException ex) {
         ex.printStackTrace();
        }
        System.out.println("SERVER: run");
    }

    /**
     * The method sets connection to database ({@link jhelp.ServerDb} object) and
     * create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests. This method uses default parameters for connection.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect() {
        System.out.println("SERVER: connect");
        return OK;
    }

    /**
     * The method sets connection to database ({@link jhelp.ServerDb} object) and
     * create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests.
     * @param args specifies properties of connection.
     * @return error code. The method returns {@link JHelp#OK} if connection are
     * openeds uccessfully, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect(String[] args) {
        System.out.println("SERVER: connect");
        return OK;
    }

    /**
     * Transports initial {@link Data} object from {@link ClientThread} object to
     * {@link ServerDb} object and returns modified {@link Data} object to
     * {@link ClientThread} object.
     * @param data Initial {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */
    @Override
    public Data getData(Data data) {
        System.out.println("SERVER:getData");
        return null;
    }

    /**
     * The method closes connection with database.
     * @return error code. The method returns {@link JHelp#OK} if a connection
     * with database ({@link ServerDb} object) closed successfully,
     * otherwise the method returns {@link JHelp#ERROR} or any error code.
     */
    @Override
    public int disconnect() {
        System.out.println("SERVER: disconnect");
        return OK;
    }
}
