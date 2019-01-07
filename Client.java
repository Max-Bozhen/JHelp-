/*
 * Client.java
 *
 */
package jhelp;

import java.awt.BorderLayout;
import static java.awt.Color.GRAY;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.LineBorder;

/**
 * Client class provides users's interface of the application.
 *
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 */
public class Client extends JFrame implements JHelp {

    /**
     * Static constant for serialization
     */
    public static final long serialVersionUID = 1234;
    /**
     * Programm properties
     */
    private Properties prop;
    /**
     * Private Data object presents informational data.
     */
    private Data data;
    private Socket clientsocket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Constructor with parameters.
     *
     * @param args Array of {@link String} objects. Each item of this array can
     * define any client's property.
     */
    public Client(String[] args) {
        JFrame client = new JFrame("JHelp");
        client.setSize(750, 600);
        client.setVisible(true);
        Container cp = getContentPane();
//        setTitle("JHelp");
        //create tabbedpane`
        JTabbedPane tabsOne = new JTabbedPane(JTabbedPane.TOP);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JPanel main_tab = new JPanel();
        JPanel settings_tab = new JPanel();
        JPanel help_tab = new JPanel();
        tabsOne.addTab("Main", main_tab);
        tabsOne.addTab("Settings", settings_tab);
        tabsOne.addTab("Help", help_tab);

//create menubar
        JMenuBar jmb = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu settings = new JMenu("Settings");
        JMenu help = new JMenu("Help");
        setJMenuBar(jmb);
        jmb.add(file);
        jmb.add(edit);
        jmb.add(settings);
        jmb.add(help);
        JMenuItem jmi = new JMenuItem("Open");
        file.add(jmi);
        JPanel top = new JPanel();
        JPanel down = new JPanel();
        JPanel buttons = new JPanel();
//create jtextarea with border
        JPanel textarea = new JPanel();
        textarea.setSize(25, 50);
        LineBorder line = new LineBorder(GRAY, 1);
        textarea.setBorder(BorderFactory.createTitledBorder(line, "Definition:"));
        JTextArea definitions_area = new JTextArea(20, 45);
        definitions_area.setLineWrap(true);
        JLabel definitions = new JLabel("Definitions:");
        JScrollPane js = new JScrollPane(definitions_area, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        textarea.add(js);

        JButton find = new JButton("Find");
        JButton add = new JButton("Add");
        JButton ed = new JButton("Edit");
        JButton del = new JButton("Delete");
        JButton next = new JButton("Next");
        JButton prev = new JButton("Previous");
        JButton exit = new JButton("Exit");
        buttons.add(add);
        buttons.add(ed);
        buttons.add(del);
        buttons.add(next);
        buttons.add(prev);
        buttons.add(exit);
        buttons.setLayout(new GridLayout(6, 1));
        JLabel term = new JLabel("Term");
        JTextField termtxt = new JTextField(30);
        JLabel term1 = new JLabel("Term");
        JTextField termtxt1 = new JTextField(30);

        //Layout managing
        top.setLayout(new FlowLayout());
        top.add(term);
        top.add(termtxt);
        top.add(find);

        down.setLayout(new BorderLayout());
        down.add(textarea, BorderLayout.WEST);
        down.add(buttons, BorderLayout.EAST);
        main_tab.setLayout(new BorderLayout());
        main_tab.add(top, BorderLayout.NORTH);
        main_tab.add(down, BorderLayout.CENTER);
        settings_tab.add(term1);
        settings_tab.add(termtxt1);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                disconnect();
                System.exit(1);
            }
        });
        find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String term = termtxt.getText();
                Data data = new Data(new Item(term));

                getData(data);
                definitions_area.setText(data.getValue(SELECT).toString());
            }
        });
//        next.addActionListener(new ActionListener(){
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    definitions_area.setText(getData(terms));
//                } catch (IOException ex) {
//                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//            
//        });
        client.add(cp);
        cp.add(tabsOne);

        pack();
        System.out.println("Client: constructor");
    }

    /**
     * Method for application start
     *
     * @param args agrgument of command string
     */
    static public void main(String[] args) {

        Client client = new Client(args);

        if (client.connect() == JHelp.OK) {
            client.run();

            client.disconnect();
        }
    }

    /**
     * Method define main job cycle
     */
    public void run() {
        System.out.println("ClientThread started");
        try {

            oos = new ObjectOutputStream(clientsocket.getOutputStream());
            System.out.println("oos ok");
            ois = new ObjectInputStream(clientsocket.getInputStream());
            System.out.println("ois ok");
            //чтение и обработка входящих данных
        } catch (IOException ex) {

        }
        System.out.println("Client: run");
        try {
            oos.writeUTF("hello");
            oos.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method set connection to default server with default parameters
     *
     * @return error code
     */
    @Override
    public int connect() {
        try {
            clientsocket = new Socket("localhost", 12345);
            System.out.println("connected to server");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return JHelp.OK;
    }

    /**
     * Method set connection to server with parameters defines by argument
     * <code>args</code>
     *
     * @return error code
     */
    public int connect(String[] args) {
        System.out.println("Client: connect");
        return JHelp.ERROR;
    }

    /**
     * Method gets data from data source
     *
     * @param data initial object (template)
     * @return new object
     */
    @Override
    public Data getData(Data data) {
        connect();
        System.out.println("ClientThread started");
        try {

            oos = new ObjectOutputStream(clientsocket.getOutputStream());
            System.out.println("oos ok");
            ois = new ObjectInputStream(clientsocket.getInputStream());
            System.out.println("ois ok");
            //чтение и обработка входящих данных
        } catch (IOException ex) {

        }
        System.out.println("Client: run");
        try {
            oos.writeObject(data);
            System.out.println("Send");
            oos.flush();
            
            ois.readObject();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Client: getData");
        return data;
    }

    /**
     * Method disconnects client and server
     *
     * @return error code
     */
    @Override
    public int disconnect() {
        try {
            clientsocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Client: disconnect");
        return JHelp.ERROR;
    }
}
