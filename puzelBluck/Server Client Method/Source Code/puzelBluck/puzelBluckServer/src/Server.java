import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * Docs: this class is the Server.
 * Date: 2018-2019 
 * Author: Lidor Malich
 */
public class Server 
{
    //קבועים
    static String SERVER_IP;
    static int SERVER_PORT;
    static int BOARD_SIZE=8;
    static JTextArea logArea;
    static ArrayList<ClientData> clientsList;
    static int guestAutoNum = 1;
    

    /**
     * פעולה ראשית
     * @param args 
     */
    public static void main(String[] args) {
        try {
            SERVER_IP = InetAddress.getLocalHost().getHostAddress(); // get thelocal computer IP
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        SERVER_PORT = 1234;
        createServerGUI();
        runTheServer();
    }
    
    /**
     * הרצת השרת
     */
    private static void runTheServer() {
        // יצירת שקע שרת להאזנה וקבלת לקוחות
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        log("SERVER run on [ " + SERVER_IP + " : " + SERVER_PORT + " ]\n");

        // שקע עבור חיבור ללקוח שיתחבר
        clientsList = new ArrayList();

        // לולאה לקבלת לקוחות
        while (true) {
            log("Wait for client...\n");
            try {
                handleClient(serverSocket.accept());
                log("new client connected\n");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *      טיפול בלקוח שיתחבר
     * @param socket הסוקט של הקליינט
     */
    private static void handleClient(Socket socket) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ClientData client = new ClientData(socket);

                    boolean isLoginOk = false;
                    while (!isLoginOk) {
                        String un = (String) client.readObject();
                        String pw = (String) client.readObject();

                        if(un.equals("#Guest#"))
                        {
                            isLoginOk = true;
                            client.setUserName("Guest #"+guestAutoNum);
                            clientsList.add(client);
                            log("Welcome "+un+ " You can Acces");
                            client.writeObject("Guest #"+guestAutoNum);
                            guestAutoNum++;
                            break;
                        }
                        
                        if (DB.isUserInDB(un, pw) == false) 
                        {
                            client.writeObject("not exist");
                            log("Error username /password for un:"+un+" pw:"+pw);
                        } else {
                            isLoginOk = true;
                            client.setUserName(un);
                            clientsList.add(client);
                            log("Welcome "+un+ " You can Acces");
                            client.writeObject("exist");
                        }
                    }

                    // נבדוק האם קיים פרטנר
                    if (isHavePartner(client) == false) 
                    {
                        // אין פרטנר
                        client.writeObject(constants.WAIT_FOR_PARTNER);
                        log(client.getUserName() + "Wait for Partner");
                    } else 
                    {
                        client.writeObject(constants.HAVE_PARTNER);
                        client.getPartner().writeObject("new partner");
                        log(client.getUserName() +" And "+client.getPartner().getUserName()+" can start game.....");
                    }

                    boolean clientWork = true;
                    while (clientWork) 
                    {
                        String dataFromClient = (String) client.readObject();
                        
                        if (dataFromClient.equals(constants.EXIT_AND_CLOSE_GAME)) 
                        {
                            if(client.getPartner() != null)
                            {
                                client.getPartner().setPartner(null);
                                client.getPartner().writeObject(constants.CLIENT_WANT_EXIT);
                            }
                             
                            log(client.getUserName() + "want to exit the game");
                            client.writeObject(constants.CLOSE_CLIENT);
                            client.exit();
                            clientsList.remove(client);
                            clientWork = false;
                            break;
                        }
                        
                        
                        if (dataFromClient.equals(constants.SEND_BORD)) 
                        {
                            log("Board sended by" + client.getUserName());
                            State newState = (State)client.readObject();
                            client.writeObject(constants.WAIT_FOR_YOURE_TURN);
                            client.getPartner().writeObject(constants.YOU_TURN);
                            client.getPartner().writeObject(newState);
                            ArrayList<Location> winLine=chackGameOver(newState);
                            if(winLine!=null)
                            {
                                log(">>Game over<<");
                                log("the winner is "+client.getUserName());
                                //Winner
                                client.writeObject(constants.GAME_OVER);
                                // שליחת מיקום הניצחון לשני השחקנים
                                client.writeObject(winLine);
                                client.writeObject(constants.YOU_TURN_WITHOUT_UPDATE_BORD);
                                
                                
                                //Losser
                                client.getPartner().writeObject(constants.GAME_OVER_WITH_GET_STATE);
                                // שליחת הלוח המעודכן לשחרן השני
                                client.getPartner().writeObject(newState);
                                // שליחת מיקום הניצחון לשני השחקנים
                                client.getPartner().writeObject(winLine);
                                client.getPartner().writeObject(constants.WAIT_FOR_YOURE_TURN);
                            }
                            else
                            if(isTie(newState))
                            {
                                log("Tie between "+client.getUserName() +" And " + client.getPartner().getUserName());
                                //Winner
                                client.writeObject(constants.TIE);
                                // שליחת מיקום הניצחון לשני השחקנים
                                client.writeObject(constants.YOU_TURN_WITHOUT_UPDATE_BORD);
                                
                                //Losser
                                client.getPartner().writeObject(constants.TIE_WITH_GET_STATE);
                                // שליחת הלוח המעודכן לשחרן השני
                                client.getPartner().writeObject(newState);
                                // שליחת מיקום הניצחון לשני השחקנים
                                client.getPartner().writeObject(constants.WAIT_FOR_YOURE_TURN);
                            }

                        }else   
                                log("New Game Start between "+client.getUserName()+" and "+client.getPartner().getUserName());
                        }
                } catch (Exception ex) {
                    printStackTrace(ex);
                }
            }
        }).start();
    }
    

    /**
     *     בדיקת זוגות - האם יש פרטנר או לא
     * @param client --- ClientData של הלקוח שכולל את הנתונים עליו
     * @return אמת או שקר בהתאמה
     */
    public static boolean isHavePartner(ClientData client) {
        /*        מבצע חישוב של האם יש מספר זוגי או אי זוגי של משתתפים
        אם יש מספר זוגי          -
                    זה אומר שיש לך פרטנר לשחק איתו
                    אם אין זה אומר שאין לך עם מי לשחק
                    ואתה צריך להמתין למישהו*/
        int x = clientsList.indexOf(client);
        if (x % 2 == 0) {
            if (x + 1 != clientsList.size()) {
                client.setPartner(clientsList.get(x - 1));
                clientsList.get(x + 1).setPartner(client);
                return true;
            }
        }
        if (x % 2 != 0) {
            client.setPartner(clientsList.get(x - 1));
            clientsList.get(x - 1).setPartner(client);
            return true;
        }
        return false;

    }


    /**
     *     יצירת הממשק הגרפי של השרת
     */
    private static void createServerGUI() {
        JFrame win = new JFrame("Server "+SERVER_IP + " : " + SERVER_PORT+" Log");
        win.setSize(360, 400);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setAlwaysOnTop(true);

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.SERIF, Font.PLAIN, 18));
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN);

        win.add(new JScrollPane(logArea), BorderLayout.CENTER);
        win.setVisible(true);
    }

    /**
     * הדפסת מחרוזת ללוג השרת
     * @param str 
     */
    public static void log(String str) {
        logArea.append(" " + str);
        logArea.append("\n");
        logArea.setCaretPosition(logArea.getText().length());
    }
    
    /**
     * בדיקה האם יש נצחון על הלוח
     *  מחזירה מערך עם המיקומים של הנצחון
     * @param s הלוח
     * @return מערך המיקמים של הנצחון
     */
    private static ArrayList chackGameOver(State s)    
    {
        return s.chackGameOver();
    }
    
    /**
     * האם יש תיקו בלוח
     * @param s הלוח העדכני
     * @return אמת או שקר -בהתאמה
     */
    private static boolean isTie(State s)
    {
        for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0; j < BOARD_SIZE; j++)
            {
                if(s.getBord()[i][j] == ' ')
                {
                    return false;
                }
            }
        }
        return true;
    }
     
    /**
     * מערכת הדפסת שגיאות לצג השרת
     * @param e שגיאה
     */
    static void printStackTrace(Exception e) 
    {
        //JOptionPane.showMessageDialog(null,"Server \n"+e.toString() +"\n"+e.getCause().toString());
    }
}
