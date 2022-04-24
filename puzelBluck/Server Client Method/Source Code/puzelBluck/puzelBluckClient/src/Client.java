import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Docs: this class is the Client and Model.
 * Date: 2018-2019 
 * Author: Lidor Malich
 */

public class Client {

    //קבועים
    private String serverIP;
    private int serverPort;
    private Socket socket;
    private String un;

    private boolean isConnected;
    private boolean  isMyTurn;
    private int whatTheChoiceInNum = 0; //מספר הביחרה שיש ללחצן
    private Color ColorToPartner = Color.GREEN; //צבע עזר לצפייה במה המחשב עשה
    public static final ImageIcon[] icons = new ImageIcon[State.BOARD_SIZE * State.BOARD_SIZE + 1];      // collection of all images 
    public static final int BUTTON_SIZE = 70;
    public static final Font BUTTON_FONT = new Font(Font.DIALOG, Font.BOLD, 40);
    public static final Font MESSAGE_FONT = new Font(Font.DIALOG, Font.BOLD, 20);

    private ObjectInputStream is;
    private ObjectOutputStream os;

    private View view;
    private Model model;

    public Client() {
        loadResources();
    }

    /**
     * טיפול בכל אירועי לחיצה על כפתור במשחק
     */
    private void setGameEventHandlers() 
    {
        view.getWin().addWindowListener(new WindowAdapter() 
        {
            public void windowClosing(WindowEvent e) 
            {
                int res = JOptionPane.showConfirmDialog(view.getWin(), "Do you want to exit?", "Exit App", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION)
                {
                    writeObjectToServer("#exit");
                }
            }
        });
        
        // טיפול באירועי לחיצה על כפתורי לוח המשחק
        for (int row = 0; row < State.BOARD_SIZE; row++) {
            for (int col = 0; col < State.BOARD_SIZE; col++) {
                view.getBoardButtons()[row][col].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int row = (int) ((JButton) e.getSource()).getClientProperty("rowIndex");
                        int col = (int) ((JButton) e.getSource()).getClientProperty("colIndex");
                        try {
                            makeMove(row, col);
                        } catch (Exception ex) {
                            printStackTrace(ex);
                        }
                    }
                });
            
                view.getBoardButtons()[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {

                        super.mouseEntered(e);
                        if (isMyTurn) 
                        {
                            int row = (int) ((JButton) e.getSource()).getClientProperty("rowIndex");
                            int col = (int) ((JButton) e.getSource()).getClientProperty("colIndex");
                            setTMPColorToSelctedBTN(whatTheChoiceInNum,row,col);
                        }

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e); 
                        if(isMyTurn)
                            moveAllButtonBorderLightToOrignal();
                    }

                    @Override
                    public void mouseMoved(MouseEvent me) {
                        super.mouseMoved(me);
                        if(isMyTurn)
                            moveAllButtonBorderLightToOrignal();
                    }
                    
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseExited(e);
                        if(isMyTurn)
                            moveAllButtonBorderLightToOrignal();
                    }
                });
            }
        }
        

        // טיפול בתפריט Menubar
         view.getMenuBar().getMenu(0).getItem(0).addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //איך לשחק
                JOptionPane.showMessageDialog(view.getWin(),"Game description\n" +
                "There is a database of quadratic forms (like a tetris game) that needs to be dragged to the board in an empty space."
                        + "\n You also need to try to reach your goal state yourself "
                        + "\n(= a sequence of 6 of your operation squares in a straight way like a diagonal, column or line)."
                        + "\n The shapes you put on the board will color the player. "
                        + "\nWhen a player has 6 cubes of the same color in a row or row or diagonally - a winner.\n" +
                "- Table and dishes\n" +
                "8X8 blank board\n" +
                "4 quadratic geometrical shapes\n" +
                "Two players to the board: one represented in blue (player A) and the other player is represented in red (player B).\n" +
                "Initial state\n" +
                "A blank board without soldiers, 8X8\n" +
                "- Game rules and moves\n" +
                "8x8 game board. At the beginning of the game the board is empty\n" +
                "A total of 4 shapes that can be used by each player individually, all of which can be used indefinitely "
                        + "\n in the game board - where the cube can be used by one player - only once in a row, when the shapes must fit properly "
                        + "\ninto the board area and not exceed the board boundaries.\n" +
                "A player who placed his dice in the board, there is no way to remove the dice, they remain in place until the end of the game.\n" +
                "\n" +
                "Two Players Blue (Player A) and Red (Play B) ...\n" +
                "\n" +
                "- The purpose of the game and determining the winner\n" +
                "The goal is to reach the panel to 6 cubes in a row of the same color (diagonal secondary / diagonal head / column / row).\n" +
                "The game is also set to draw as the whole board is filled to the ground and there is no winner.");
            }
        });

        // About Programmer בעת לחיצה על תת תפריט אודות המתכנת 
        view.getMenuBar().getMenu(0).getItem(1).addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                //אודות
                JOptionPane.showMessageDialog(view.getWin(), "The Game creat by Lidor Malich\n  © 2018-2019 ©\n lidormalich.com");
            }
        });

        //בעת לחיצה על יציאה
        view.getMenuBar().getMenu(0).getItem(2).addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int res = JOptionPane.showConfirmDialog(view.getWin(), "Do you want to exit?", "Exit App", JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION)
                {
                    writeObjectToServer("#exit");
                }
            }
        });
    }

    /**
     * הסרת סימון ההדמיה הצהוב בריחוף עם העכבר על מקום לאחר לחיצה על הצורה הרלוונטית
     */
    private void moveAllButtonBorderLightToOrignal() {

        for (int row = 0; row < State.BOARD_SIZE; row++) {
            for (int col = 0; col < State.BOARD_SIZE; col++) {
                view.setButtonBorderLight(row, col, false, null, false);
            }
        }

    }

    /**
     * ביצוע פעולת השמת שחקן על שורה ועמודה ספציפים
     * @param row עמודה
     * @param col שורה
     */
    private void makeMove(int row, int col) 
    {
        if(isMyTurn)
        {
            view.setColorToBTN(0);//0 Is Reset code
            if(setPlayerViaNum(whatTheChoiceInNum, row, col))
            {
                sendStateToServer();
            }
        }
    }
    
    /**
     * הדמיית הבחירה של המשתמש על גבי המיקום הנוכחי של העכבר
     * @param choice צורה שבחר השחקן
     * @param row שורה
     * @param col עמודה
     */
    public void setTMPColorToSelctedBTN(int choice, int row, int col)
    {
        Color selcted=Color.yellow;
        if(choice == 1)
        {
            view.setButtonBorderLight(row,col,true,selcted,false);
        }
        else if(choice == 2)
        {
            int rowtmp = row;
            int coltmp = col - 1;

            if(coltmp >= 0)
            {
                if(model.isEmpty(row, coltmp))
                {
                    view.setButtonBorderLight(row,col,true,selcted,false);
                    view.setButtonBorderLight(rowtmp,coltmp,true,selcted,false);
                }
            }

        }
        else if(choice == 3)
        {
            int rowtmp = row - 1;
            int coltmp = col;
            int rowtmpSec = row;
            int coltmpSec = col - 1;
            if(coltmpSec >= 0)
            {
                if(rowtmp >= 0)
                {
                    if(model.isEmpty(row, col))
                    {
                        if(model.isEmpty(rowtmp, coltmp))
                        {
                            if(model.isEmpty(rowtmpSec, coltmpSec))
                            {
                                view.setButtonBorderLight(row,col,true,selcted,false);
                                view.setButtonBorderLight(rowtmp,coltmp,true,selcted,false);
                                view.setButtonBorderLight(rowtmpSec,coltmpSec,true,selcted,false);
                            }
                        }
                    }
                }
            }

        }
        else if(choice == 4)
        {
            int rowtmp = row + 1;
            int coltmp = col;
            int rowtmpSec = row;
            int coltmpSec = col - 1;
            if(col > 0 && rowtmp < State.BOARD_SIZE && coltmpSec < State.BOARD_SIZE)
            {
                if(model.isEmpty(row, col))
                {
                    if(model.isEmpty(rowtmp, coltmp))
                    {
                        if(model.isEmpty(rowtmpSec, coltmpSec))
                        {
                            view.setButtonBorderLight(row,col,true,selcted,false);
                            view.setButtonBorderLight(rowtmp,coltmp,true,selcted,false);
                            view.setButtonBorderLight(rowtmpSec,coltmpSec,true,selcted,false);
                        }
                    }
                }
            }

        }

    }

    
    /**
     * פתיחת הכפתורים ללחיצה כאשר מגיע תור השחקן הפעיל לשחק
     */
    private void unlockBTN()
    {
        for (int row = 0; row < State.BOARD_SIZE; row++) 
        {
            for (int col = 0; col < State.BOARD_SIZE; col++) 
            {
                if(model.isEmpty(row, col)==true)
                {
                    view.setDisable(row, col);  
                }
            }
        }
    }
    
    /**
     * יצירת חיבור אל השרת
     * והאזנה אל הפוקודת שמגיעות כל הזמן מהשרת
     */
    private void connect()  
    {
        // יצירת חיבור שקע לשרת
        serverIPDialogg();
        try {
            
            socket = new Socket(serverIP, serverPort);

            // פתיחת ערוצי הזרמת נתונים מהלקוח לשרת וההפך
            //Need First OS and after need IS - that save on bug in connect.....
            os = new ObjectOutputStream(socket.getOutputStream()); // ערוץ שליחת נתונים לשרת
            is = new ObjectInputStream(socket.getInputStream());   // ערוץ קבלת נתונים מהשרת

            
        } catch (Exception ex) 
        {
            String error="Server does not exist\nor does not have an Internet connection.\ntry again...";
             JOptionPane.showMessageDialog(null, error, "Server Error", JOptionPane.ERROR_MESSAGE);
             System.exit(0);
        }
        // login
            loginDialogg();

        // create View & Model
        view = new View(un);
//        System.out.println(un);
        setGameEventHandlers();
        model = new Model();
        addFuncToSideBTN();
        view.setColorToBTN(0);
        
        // loop with sever
        String msgFromServer = null;
        isConnected = true;
        while (isConnected) 
        {
            msgFromServer = (String) readObjectFromServer();
            chackMSG(msgFromServer);
        }
    }

    /**
     * טופס התחברות כאורח או עם שם משתמש וסיסמא
     */
    private void loginDialogg() 
    {
        // שדות קלט עבור קליטת נתונים
        JLabel loginErrLabel = new JLabel("");
        // להצגת הודעת שגיאה
        loginErrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginErrLabel.setForeground(Color.RED);

        JTextField unField = new JTextField("ש");
        // לקליטת שם המשתמש
        unField.setForeground(Color.BLUE);

        JTextField pwField = new JTextField("1");
        // לקליטת הסיסמה
        pwField.setForeground(Color.BLUE);

        Object[] inputFields
                = {
                    loginErrLabel,
                    " ",
                    "Enter user name (email)",
                    unField,
                    " ",
                    "Enter password (4 digits)",
                    pwField,
                    " ",};

        boolean isLoginOK = false;
        int option;
        un = "";
        String pw = "";

        while (!isLoginOK) 
        {
            UIManager.put("OptionPane.okButtonText","Login");
            UIManager.put("OptionPane.cancelButtonText","Guest");
            option = JOptionPane.showConfirmDialog(null, inputFields, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            UIManager.put("OptionPane.okButtonText","OK");
            UIManager.put("OptionPane.cancelButtonText","Cancel");
            
            if (option == JOptionPane.CANCEL_OPTION)    // Guest 
            {
                writeObjectToServer("#Guest#");
                writeObjectToServer("0000");
                un = (String)readObjectFromServer();
                isLoginOK = true;
            }
            
            if (option == JOptionPane.OK_OPTION)        // Login
            {
                String answerFromServer=null;
                    un = unField.getText();
                    pw = pwField.getText();

                    writeObjectToServer(un);
                    writeObjectToServer(pw);
                     answerFromServer = (String) readObjectFromServer();
                // send un & pw to server to check in DB
                if (answerFromServer.equals("exist")) 
                {
                    isLoginOK = true;
                } else 
                {
                    loginErrLabel.setText("User Name or Password incorrct! try again...");
                }
            }
        }
    }

    /**
     * בדיקת הודעות מהשרת וביצוע פעולה מתאימה לפי המחרוזת שהגיע
     * @param msgFromServer ההודעה שהתקבלה מהשרת
     */
     private void chackMSG(String msgFromServer) 
    {
        if (msgFromServer.equals(constants.WAIT_FOR_PARTNER))
        {
            view.updateMessage("wait For Partner");
            view.setBTNLocked();
            model.setCurrentPlayer('R');
            return;
        }

        //גם פעולה למי שהפסיד
        if (msgFromServer.equals(constants.HAVE_PARTNER)) 
        {
            isMyTurn=false;
            model.setCurrentPlayer('B');
            view.UpdatePlayerLable('B');
            view.updateMessage("Your partner is playing now - please wait");
            view.setBTNLocked();
            return;
        }
        
        if (msgFromServer.equals(constants.NEW_PARTNER)) 
        {
            isMyTurn=true;
            view.updateMessage("It's your turn now to play - you can start playing");
            view.UpdatePlayerLable('R');
            unlockBTN();
            return;
        }
        
        if (msgFromServer.equals(constants.WAIT_FOR_YOURE_TURN)) 
        {
            isMyTurn=false;
            view.updateMessage("Wait for your turn to play...");
            view.setBTNLocked();
            return;
        }
       
        if(msgFromServer.equals(constants.YOU_TURN))
        {
            isMyTurn=true;
            view.updateMessage("It's your turn to play...");
            updateState((State)readObjectFromServer());
            unlockBTN();
            return;
        }
        
        if(msgFromServer.equals(constants.YOU_TURN_WITHOUT_UPDATE_BORD))
        {
            isMyTurn=true;
            view.updateMessage("It's your turn to play...");
            unlockBTN();
            return;
        }
        if(msgFromServer.equals(constants.GAME_OVER))
        {
            String UpdateMSGToclient=null;
            isMyTurn=false;
            ArrayList<Location> winLine=(ArrayList)readObjectFromServer();
//            System.out.println(winLine);
            char CharOfWinner=model.getState().getPlayerAt(winLine.get(1).getRow(), winLine.get(1).getCol());
            if(CharOfWinner==model.getMyPlayer())
            {
                UpdateMSGToclient="you are the Winner ";
                view.updateMessage(UpdateMSGToclient);
                
            }
            else
            {
                UpdateMSGToclient="you are loss, try again";
                view.updateMessage(UpdateMSGToclient);
            }
            view.setBTNLocked();
            view.setColorToWiner(winLine);
            gameOverFrame(UpdateMSGToclient);
            return;
        }
        
        if(msgFromServer.equals(constants.GAME_OVER_WITH_GET_STATE))
        {
            String UpdateMSGToclient=null;
            isMyTurn=false;
            updateState((State)readObjectFromServer());
            ArrayList<Location> winLine=(ArrayList)readObjectFromServer();
            char CharOfWinner=model.getState().getPlayerAt(winLine.get(1).getRow(), winLine.get(1).getCol());
            if(CharOfWinner==model.getMyPlayer())
            {
                UpdateMSGToclient="you are the Winner ";
                view.updateMessage(UpdateMSGToclient);
            }
            else
            {
                UpdateMSGToclient="you are loss, try again";
                view.updateMessage(UpdateMSGToclient);
            }
            view.setBTNLocked();
            view.setColorToWiner(winLine);
            gameOverFrame(UpdateMSGToclient);
            return;
        }
        if(msgFromServer.equals(constants.TIE_WITH_GET_STATE))
        {
            String UpdateMSGToclient="Game Over - that TIE...";
            isMyTurn=false;
            updateState((State)readObjectFromServer());
            view.updateMessage(UpdateMSGToclient);
            view.setBTNLocked();
            gameOverFrame(UpdateMSGToclient);
            return;
        }
        
        if(msgFromServer.equals(constants.TIE))
        {
            String UpdateMSGToclient="Game Over - that TIE...";
            isMyTurn=false;
            view.updateMessage(UpdateMSGToclient);
            view.setBTNLocked();
            gameOverFrame(UpdateMSGToclient);
            return;
        }
       
        if (msgFromServer.equals(constants.CLOSE_CLIENT)) {
            exit();
        }
        
        if (msgFromServer.equals(constants.CLIENT_WANT_EXIT)) 
        {
            writeObjectToServer(constants.EXIT_AND_CLOSE_GAME);
            JOptionPane.showMessageDialog(null, "The Game End");
        }
    }
    
     /**
     *   העברת הלוח בין לקוח אל שרת
     */
    public void sendStateToServer() 
    {
        writeObjectToServer(constants.SEND_BORD);
        writeObjectToServer(model.getState());
    }

    /**
     * עדכון הסטייט שהתקבל מהשרת
     * @param state הלוח העדכני מהשחקן היריב
     */
    public void updateState(State state) 
    {
        model.updateState(state);
        view.updateView(state);
    }

    /**
     * המרת הפרמטר המספרי של הצורה אל שדות של שורה ועמודה
     * @param choice צורה שבחר השחקן
     * @param row עמודה
     * @param col שורה
     * @return 
     */
    public boolean setPlayerViaNum(int choice, int row, int col)
    { 
        boolean chaeck = false;
        if (choice == 0) {
            view.setMessage("נא לבחור אופציה לישום");
            return false;
        } else if (choice == 1) {
            model.setPlayerAt(row, col);
            view.setPlayerAt(model.getMyPlayer(), row, col);
            chaeck = true;
        } else if (choice == 2) {
            int rowtmp = row;
            int coltmp = col - 1;

            if (coltmp >= 0) {
                if (model.isEmpty(row, coltmp)) {
                    model.setPlayerAt(row, col);
                    model.setPlayerAt(rowtmp, coltmp);

                    view.setPlayerAt(model.getMyPlayer(), row, col);
                    view.setPlayerAt(model.getMyPlayer(), rowtmp, coltmp);
                    chaeck = true;
                }
            }
        } 
        else if (choice == 3) 
        {
            int rowtmp = row - 1;
            int coltmp = col;
            int rowtmpSec = row;
            int coltmpSec = col - 1;
            if (coltmpSec >= 0) {
                if (rowtmp >= 0) {
                    if (model.isEmpty(row, col)) {
                        if (model.isEmpty(rowtmp, coltmp)) {
                            if (model.isEmpty(rowtmpSec, coltmpSec)) {
                                model.setPlayerAt(row, col);
                                model.setPlayerAt(rowtmp, coltmp);
                                model.setPlayerAt(rowtmpSec, coltmpSec);

                                view.setPlayerAt(model.getMyPlayer(), row, col);
                                view.setPlayerAt(model.getMyPlayer(), rowtmp, coltmp);
                                view.setPlayerAt(model.getMyPlayer(), rowtmpSec, coltmpSec);
                                chaeck = true;
                            }
                        }
                    }
                }
            }
        } else if (choice == 4) {
            int rowtmp = row + 1;
            int coltmp = col;
            int rowtmpSec = row;
            int coltmpSec = col - 1;
            if (col > 0 && rowtmp < State.BOARD_SIZE && coltmpSec < State.BOARD_SIZE) {
                if (model.isEmpty(row, col)) {
                    if (model.isEmpty(rowtmp, coltmp)) {
                        if (model.isEmpty(rowtmpSec, coltmpSec)) {
                            model.setPlayerAt(row, col);
                            model.setPlayerAt(rowtmp, coltmp);
                            model.setPlayerAt(rowtmpSec, coltmpSec);

                            view.setPlayerAt(model.getMyPlayer(), row, col);
                            view.setPlayerAt(model.getMyPlayer(), rowtmp, coltmp);
                            view.setPlayerAt(model.getMyPlayer(), rowtmpSec, coltmpSec);
                            chaeck = true;
                        }
                    }
                }
            }
        }
        whatTheChoiceInNum = 0;

        if (chaeck == false) {
            view.setMessage("אופציה לא חוקית -אנא נסה שוב");}
        return chaeck;
    }

    /**
     * הגדרת אריוע לחיצה של מספר צורה בעת לחיצה על צורה 
     */
    private void addFuncToSideBTN() 
    {
        view.getBtn1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) 
            {
                if(isMyTurn)
                {
                whatTheChoiceInNum = 1;
                view.setColorToBTN(whatTheChoiceInNum);
                }
            }
        });
        view.getBtn2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(isMyTurn)
                {
                whatTheChoiceInNum = 2;
                view.setColorToBTN(whatTheChoiceInNum);
                }
            }
        });
        view.getBtn3L().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(isMyTurn)
                {
                whatTheChoiceInNum = 3;
                view.setColorToBTN(whatTheChoiceInNum);
                }
            }
        });
        view.getBtn3R().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(isMyTurn)
                {
                whatTheChoiceInNum = 4;
                view.setColorToBTN(whatTheChoiceInNum);
                }
            }
        });
    }

    /**
     * טעינת קבצי מדיה למשחק
     */
    private void loadResources() {
        ImageIcon img = new ImageIcon(Client.class.getResource("resources/Splash.png"));
        icons[0] = new ImageIcon(img.getImage().getScaledInstance(1000, 800, Image.SCALE_SMOOTH));

        img = new ImageIcon(Client.class.getResource("resources/penguin_icon.png"));
        icons[1] = new ImageIcon(img.getImage().getScaledInstance(1000, 800, Image.SCALE_SMOOTH));

        img = new ImageIcon(Client.class.getResource("resources/available.png"));
        icons[2] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        img = new ImageIcon(Client.class.getResource("resources/1BTN.png"));
        icons[3] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        img = new ImageIcon(Client.class.getResource("resources/2BTN.png"));
        icons[4] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        img = new ImageIcon(Client.class.getResource("resources/3BTN_Left.png"));
        icons[5] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        img = new ImageIcon(Client.class.getResource("resources/3BTN_Right.png"));
        icons[6] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
    }

    /**
     * (IP & Port) קבלת כתובת השרת
     */
    private void getServerAddress() {
        try {
            serverIP = InetAddress.getLocalHost().getHostAddress(); //"192.168.21.223";
        } catch (UnknownHostException ex) {
            printStackTrace(ex);
        }
        serverPort = 1234;
    }
    
    /**
     * דיאלוג להזנת פרטי התחברות לשרת  ( כתובת IP)
     */
    private void serverIPDialogg()
    {
        getServerAddress();
        // שדות קלט עבור קליטת נתונים
        JLabel loginErrLabel = new JLabel("");

        //Get Server IP From User
        JTextField ipField = new JTextField(serverIP);
        ipField.setFont(new Font("arial",4 ,20));
        ipField.setForeground(Color.BLACK);

        Object[] inputFields
                = {
                    loginErrLabel,
                    " ",
                    "Enter Server IP:",
                    ipField,
                    " ",};

        int option;
            option = JOptionPane.showConfirmDialog(null, inputFields, "Login", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(option==JOptionPane.OK_OPTION)
            {
                serverIP=ipField.getText();
            }
    }

    /**
     * (streams & socket) ניתוק הלקוח מהשרת
     */
    private void disconnect() 
    {
        isConnected = false;
        try {
            is.close();
            os.close();
            socket.close();
        } catch (IOException ex) {
            printStackTrace(ex);
        }
    }

    /**
     * אתחול המשחק  אל משחק חדש
     */
    public void newGame() {
        model.init();
        view.init();
        view.setVisible(true);
        view.setColorToBTN(0);//0 Is Reset code
    }

    /**
     * סגירת הקליינט ויצאה
     */
    private void exit() 
    {
        disconnect();
        view.close();
    }
    
    /**
     * חלון שקופץ לאחר סיום המשחק ומכריז על המנצח במשחק
     * @param winner 
     */
    public void gameOverFrame(String winner)
    {
        JFrame gameEndMsg = new JFrame();
        JPanel pnl = new JPanel();

        JLabel lblTimer = new JLabel();
        JLabel lblWinner = new JLabel(winner);
        
        lblWinner.setForeground(Color.BLACK);
        lblTimer.setForeground(Color.BLACK);
        
        lblWinner.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30));
        lblTimer.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 30));
        
        lblWinner.setVerticalAlignment(SwingConstants.CENTER);
        lblTimer.setVerticalAlignment(SwingConstants.CENTER);
        
        lblWinner.setBackground(Color.cyan);
        lblTimer.setBackground(Color.cyan);
        lblWinner.setSize(new Dimension(150, 100));
        lblTimer.setSize(new Dimension(150, 100));

        pnl.add(lblWinner, BorderLayout.CENTER);
        pnl.add(lblTimer);
        gameEndMsg.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        gameEndMsg.add(pnl, BorderLayout.CENTER);
        
        gameEndMsg.pack();
        gameEndMsg.setVisible(true);
        gameEndMsg.setLocationRelativeTo(view.getWin());
        for (int i = 10; i > 0; i--)
        {
            try {
                int x = 1000;
                lblTimer.setText(" We Start At " + i + "!");
                Thread.sleep(x);
            } catch (InterruptedException ex) 
            {
                printStackTrace(ex);
            }
        }
        gameEndMsg.dispose();
        newGame();
    }

    /**
     * שליחת אובייקט מכל סוג אל השרת באמצעות הסריים של הסוקט
     * @param obj העצם לשליחה אל השרת
     */
     public void writeObjectToServer(Object obj) 
     {
        try
        {
            os.writeObject(obj);
            os.flush();
        }
        catch(Exception ex) 
        {
            printStackTrace(ex);
        }
    }
    
     /**
      * קריאת אובייקט שהתקבל מן השרת באמצעות הסריים של הסוקט
      * @return האובייקט
      */
    public Object readObjectFromServer() 
    {
        Object obj = null;
         try {
            obj = is.readObject();
        } catch (Exception ex) 
        {
            printStackTrace(ex);
            disconnect();
            String error="Server does not exist\n or does not have an Internet connection.\ntry again...";
             JOptionPane.showMessageDialog(null, error, "Server Error", JOptionPane.ERROR_MESSAGE);
             System.exit(0);
        }
        return obj;
    }
    
    /**
     * הדפסת שגיאות אל צג המשתמש
     * @param e שגיאה
     */
    public static void printStackTrace(Exception e) 
    {
        //JOptionPane.showMessageDialog(null,e.toString());
    }
    
     public static void main(String[] args) 
    {
        try {
            new Client().connect();
        } catch (Exception e) 
        {
            printStackTrace(e);
        }
    }
     
     /**
      * המרת תו שחקן אל הצבע אשר מייצג אותו בלוח
      * @param player תו המיצג את השחקן
      * @return החזרת צבע השחקן
      */
     private String convertCharToString(char player)
     {
         if(player=='R')
             return  "Red";
         return "Blue";
     }
}