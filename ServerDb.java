/*
 * ServerDb.java
 *
 */
package jhelp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class presents server directly working with database. The complete
 * connection string should take the form of:<br>
 * <code><pre>
 *     jdbc:subprotocol://servername:port/datasource:user=username:password=password
 * </pre></code> Sample for using MS Access data source:<br>
 * <code><pre>
 *  private static final String accessDBURLPrefix
 *      = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
 *  private static final String accessDBURLSuffix
 *      = ";DriverID=22;READONLY=false}";
 *  // Initialize the JdbcOdbc Bridge Driver
 *  try {
 *         Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
 *      } catch(ClassNotFoundException e) {
 *         System.err.println("JdbcOdbc Bridge Driver not found!");
 *      }
 *
 *  // Example: method for connection to a Access Database
 *  public Connection getAccessDBConnection(String filename)
 *                           throws SQLException {
 *       String databaseURL = accessDBURLPrefix + filename + accessDBURLSuffix;
 *       return DriverManager.getConnection(databaseURL, "", "");
 *   }
 * </pre></code>
 *
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 */
public class ServerDb implements JHelp {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private DataInputStream input;
    private DataOutputStream output;
    private final String DB_CONN_STRING = "jdbc:derby://localhost:1527/terms";
    private final String DRIVER_CLASS_NAME = "org.apache.derby.jdbc.ClientDriver";
    private final String USER_NAME = "JHelp";
    private final String PASSWORD = "1";

    /**
     * Creates a new instance of <code>ServerDb</code> with default parameters.
     * Default parameters are:<br>
     * <ol>
     * <li><code>ServerDb</code> host is &laquo;localhost&raquo;;</li>
     * <li>{@link java.net.ServerSocket} is opened on
     * {@link jhelp.JHelp#DEFAULT_DATABASE_PORT};</li>
     * </ol>
     */
    public ServerDb() {
        this(DEFAULT_DATABASE_PORT);
        try {
            serverSocket = new ServerSocket(DEFAULT_DATABASE_PORT);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("SERVERDb: default constructor");
    }

    /**
     * Constructor creates new instance of <code>ServerDb</code>.
     *
     * @param port defines port for {@link java.net.ServerSocket} object.
     */
    public ServerDb(int port) {

        System.out.println("SERVERDb: constructor");
    }

    /**
     * Constructor creates new instance of <code>ServerDb</code>.
     *
     * @param args array of {@link java.lang.String} type contains connection
     * parameters.
     */
    public ServerDb(String[] args) {
        System.out.println("SERVERDb: constructor");
    }

    /**
     * Start method for <code>ServerDb</code> application.
     *
     * @param args array of {@link java.lang.String} type contains connection
     * parameters.
     */
    public static void main(String[] args) throws IOException {
        ServerDb serverdb = new ServerDb();
        
        serverdb.run();
        System.out.println("SERVERDb: main");
    }

    /**
     * Method defines job cycle for client request processing.
     */
    private void run() throws IOException {
        connect();
        input.readUTF();

    }

    /**
     *
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * opened successfully, otherwise the method returns {@link JHelp#ERROR}.
     */
    @Override
    public int connect() {
        while (true) {

            try {
                clientSocket = serverSocket.accept();
            } catch (IOException ex) {
                Logger.getLogger(ServerDb.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                System.out.println("Client connected");
                output = new DataOutputStream(clientSocket.getOutputStream());
                input = new DataInputStream(clientSocket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServerDb.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ie) {

            }
            System.out.println("SERVERDb: run");

            System.out.println("SERVERDb: connect");
            return JHelp.READY;
        }

    }

    /**
     * Method sets connection to database and create
     * {@link java.net.ServerSocket} object for waiting of client's connection
     * requests.
     *
     * @return error code. Method returns {@link jhelp.JHelp#READY} in success
     * case. Otherwise method return {@link jhelp.JHelp#ERROR} or error code.
     */
    @Override
    public int connect(String[] args) {
        try {
            Class.forName(DRIVER_CLASS_NAME).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            System.out.println("Cannot load db driver: " + DRIVER_CLASS_NAME);
        }
        try (Connection connection = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
                Statement statement = connection.createStatement()) {

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        System.out.println("SERVERDb: connect");
        return JHelp.READY;
    }

    /**
     * Method returns result of client request to a database.
     *
     * @param data object of {@link jhelp.Data} type with request to database.
     * @return object of {@link jhelp.Data} type with results of request to a
     * database.
     * @see Data
     * @since 1.0
     */
    public Data getData(Data data) {
        System.out.println("SERVERDb: getData");
        return null;
    }

    /**
     * Method disconnects <code>ServerDb</code> object from a database and
     * closes {@link java.net.ServerSocket} object.
     *
     * @return disconnect result. Method returns {@link #DISCONNECT} value, if
     * the process ends successfully. Othewise the method returns error code,
     * for example {@link #ERROR}.
     * @see jhelp.JHelp#DISCONNECT
     * @since 1.0
     */
    public int disconnect() {
        System.out.println("SERVERDb: disconnect");
        return JHelp.DISCONNECT;
    }
}
