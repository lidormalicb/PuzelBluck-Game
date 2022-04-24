import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Docs: this class is the GUI.
 * Date: 2018-2019 
 * Author: Lidor Malich
 */
public class View 
{
    // קבועים
    public static  String GAME_TITLE = "פאזל בלוק - Lidor Malich";
    
    // תכונות
    private JFrame win;
    private JButton[][] viewBoard;
    private JLabel lblMsg;
    private JPanel pnlButtons;
    private JPanel pnlOptions;
    private JPanel pnlShapes;
    private JMenuBar menuBar;
    private JButton myPlayerColor, myUserName;
    private Label myColor;
    private JButton btn1, btn2, btn3R, btn3L;
    private Border originalBorder;  //משמש להחזרת הלוח למצב הרגיל - ביטול הצהוב מסביב

    /**
     * פעולה בונה
     * @param userName שם משתמש
     */
    public View(String userName) 
    {
        viewBoard = new JButton[State.BOARD_SIZE][State.BOARD_SIZE];

        pnlOptions = createOptionsPanel();
        pnlShapes = createShapesButtons();
        pnlButtons = createButtonsPanel();
        lblMsg = createMessageLable();
        menuBar = createMenuBar();

        win = new JFrame(GAME_TITLE + " - " + userName);
        win.setResizable(false);
        win.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        win.setJMenuBar(menuBar);

        win.add(pnlOptions, BorderLayout.NORTH);
        win.add(pnlShapes, BorderLayout.EAST);
        win.add(pnlButtons, BorderLayout.CENTER);
        win.add(lblMsg, BorderLayout.SOUTH);

        win.pack();
        win.setLocationRelativeTo(null);
        win.setVisible(true);

        win.setIconImage(new ImageIcon(getClass().getResource("resources/icon.png")).getImage());
        
        init();
    }

    /**
     * אתחול לוח המשחק
     */
    public void init() {
        for (int row = 0; row < viewBoard.length; row++) {
            for (int col = 0; col < viewBoard[row].length; col++) {
                viewBoard[row][col].setEnabled(true);
                viewBoard[row][col].setOpaque(false);
                viewBoard[row][col].setBackground(Color.GREEN);
                viewBoard[row][col].setBorder(originalBorder);
            }
        }

        //הגדרת הכפטור כלחיץ
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3R.setEnabled(true);
        btn3L.setEnabled(true);

    }

    /**
     * עדכון בר ההודעות
     * @param s מחרוזת של ההודעה
     */
    public void updateMessage(String s) {
            lblMsg.setText("<html> <font color='green'>"+s+"</font></html>");
        
    }

    /**
     * פעולה ליצירת כל הכפתורים על לוח המשחק
     * @return JPanel של המשחק
     */
    private JPanel createButtonsPanel() {
        JPanel pnl = new MyPanel(Client.BUTTON_SIZE * State.BOARD_SIZE, Client.BUTTON_SIZE * State.BOARD_SIZE);
        pnl.setOpaque(true);
        pnl.setLayout(new GridLayout(State.BOARD_SIZE, State.BOARD_SIZE));
        for (int row = 0; row < viewBoard.length; row++) {
            for (int col = 0; col < viewBoard[row].length; col++) {
                JButton btn = new JButton();
                
                btn.setFont(Client.BUTTON_FONT);
                btn.setPreferredSize(new Dimension(Client.BUTTON_SIZE, Client.BUTTON_SIZE));
                btn.setFocusable(false);
                btn.setOpaque(false);
                btn.putClientProperty("rowIndex", row);
                btn.putClientProperty("colIndex", col);
                viewBoard[row][col] = btn;
                originalBorder = btn.getBorder();
                pnl.add(btn);
            }
        }
        return pnl;
    }

    /**
     *  יצירת חלונית ההודעות
     * @return לייבל ההודעה
     */
    private JLabel createMessageLable() {
        JLabel lbl = new JLabel(" ");
        lbl.setForeground(Color.BLUE);
        lbl.setFont(Client.MESSAGE_FONT);
        return lbl;
    }

    /**
     * קבלת הלוח הכפטורים
     * @return 
     */
    public JButton[][] getBoardButtons() {
        return viewBoard;
    }

    /**
     * הגדרת כפתור כלא לחיץ
     * @param row שורה
     * @param col  עמודה
     */
    public void setEnabled(int row, int col) 
    {
        viewBoard[row][col].setEnabled(false);
    }
    
    /**
     * הגדרת כפתור כלחיץ
     * @param row
     * @param col 
     */
    public void setDisable(int row, int col) 
    {
        viewBoard[row][col].setEnabled(true);
    }

    
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    
    public void setMessage(String msg) {
        lblMsg.setText(msg);
    }

    public String getMessage() {
        return lblMsg.getText();
    }
    
    public JFrame getWin() {
        return win;
    }

    public void setVisible(boolean status) {
        win.setVisible(true);
    }

    public JButton getMyPlayerColor() {
        return myPlayerColor;
    }

    public JButton getMyUserName() {
        return myUserName;
    }

    /**
     * יצירת התפריט הראשי של המשחק
     * @return התפריט
     */
    private JMenuBar createMenuBar() 
    {
        //יצירת בר התפריטים אותו נצרף לחלון המשחק 
        JMenuBar menuBar = new JMenuBar();

//         יצירת תפריט ראשי ראשון אותו נוסיף לבר התפריטים
        JMenu menu1 = new JMenu("Info"); // יצירת פריט ראשי בתפריט 
        menuBar.add(menu1);

        // יצירת סדרת תת תפריטים אותם נוסיף לתפריט הראשי הראשון
        JMenuItem menu1_1 = new JMenuItem("How to play");
        JMenuItem menu1_2 = new JMenuItem("About");
        JMenuItem menu1_3 = new JMenuItem("Exit");
        

        //הוספת התפריט אל הסטטוס באר העליון
        menu1.add(menu1_1);
        menu1.add(menu1_2);
        menu1.add(menu1_3);
        
        return menuBar;
    }

    /**
     * הצבת התו המיצג את השחקן במיקום מסוים
     * @param nowPlay תו השחקן
     * @param row שורה
     * @param col  עמודה
     */
    public void setPlayerAt(char nowPlay, int row, int col)
    {
        viewBoard[row][col].setEnabled(false);

        if (nowPlay == 'R') {
            viewBoard[row][col].setOpaque(true);
            viewBoard[row][col].setBackground(Color.RED);
        } else 
         if (nowPlay == 'B'){
            
            viewBoard[row][col].setOpaque(true);
            viewBoard[row][col].setBackground(Color.BLUE);
        }
    }
    
    /**
     * נעילת כל כפתורי הלוח
     */
    public void setBTNLocked() 
    {
        for (int row = 0; row < State.BOARD_SIZE; row++) {
            for (int col = 0; col < State.BOARD_SIZE; col++) {
                viewBoard[row][col].setEnabled(false);
            }
        }
    }

    /**
     *     נעילת כפתורי הצד של בחירת אופציה ליישום
     * @param isLocked אם לנעול את הפכתור
     * @param color צבע לכפתור
     */
    public void setBTNLockedShaep(boolean isLocked,Color color)
    { 
    if(isLocked)
        {
            btn1.setBackground(color);
            btn2.setBackground(color);
            btn3R.setBackground(color);
            btn3L.setBackground(color);

            btn1.setEnabled(false);
            btn2.setEnabled(false);
            btn3R.setEnabled(false);
            btn3L.setEnabled(false);
        }
    else
        {
            setColorToBTN(0);
            btn1.setEnabled(true);
            btn2.setEnabled(true);
            btn3R.setEnabled(true);
            btn3L.setEnabled(true); 
        }
    }

    
    /**
     * צביעת הנצחון של השחקן המנצח
     * @param winLock מערך הנצחון של השחקן
     */
    public void setColorToWiner(ArrayList<Location> winLock) {
        Location winlockionTMP = new Location(0, 0);
        int col, row;
        for (int i = 0; i < winLock.size(); i++) {
            winlockionTMP = winLock.get(i);
            row = winlockionTMP.getRow();
            col = winlockionTMP.getCol();
            setButtonBorderLight(row, col, true, Color.CYAN,true);
        }
    }

   /**
    * צבעית כפתורי הצורות בצבע אפור - כאשר הם לא פעילים
    */
    public void setGrayColorToSideBTN() 
    {
        btn1.setBackground(Color.LIGHT_GRAY);
        btn2.setBackground(Color.LIGHT_GRAY);
        btn3L.setBackground(Color.LIGHT_GRAY);
        btn3R.setBackground(Color.LIGHT_GRAY);
    }
    
    public void setColorToBTN(int select) {

        setGrayColorToSideBTN();

        if (select == 0)//Reset code
        {
            return;
        } else if (select == 1) {
            btn1.setBackground(Color.ORANGE);
        } else if (select == 2) {
            btn2.setBackground(Color.ORANGE);
        } else if (select == 3) {
            btn3L.setBackground(Color.ORANGE);
        } else if (select == 4) {
            btn3R.setBackground(Color.ORANGE);
        }
    }

    /**
     *     יצירת פאנל עם פרטי השחקן
     * @return פאנל
     */
    private JPanel createOptionsPanel() {
        int s = 10; // num of spaces
        JPanel pnl = new JPanel(new GridLayout(1, 3, s, s));
        pnl.setLayout(new FlowLayout());
        pnl.setBorder(BorderFactory.createEmptyBorder(s, s, s, s));

        myColor =new Label();
        myColor.setFont(new Font("Calibri", Font.ROMAN_BASELINE, 24));
        myColor.setBackground(Color.yellow);
        myColor.setText("Please wait for another player");
        myColor.setFocusable(false);
        pnl.add(myColor);

        return pnl;
    }

    public JButton getBtn1()
    {
        return btn1;
    }

    public JButton getBtn2() 
    {
        return btn2;
    }

    public JButton getBtn3R() 
    {
        return btn3R;
    }

    public JButton getBtn3L()
    {
        return btn3L;
    }
    

    /**
     * יצרת פאנל צורות שנמצא בצד
     * @return פאנל צורות
     */
    private JPanel createShapesButtons() {
        int s = 2; // num of spaces
        JPanel pnl = new JPanel(new GridLayout(5, 1, s, s));
        //pnl.setLayout(new FlowLayout());
        pnl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); //Border(s, s, s, s));

        JLabel lbl = new JLabel("צורות");
        lbl.setBackground(Color.yellow);
        lbl.setHorizontalAlignment(lbl.CENTER);
        lbl.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        pnl.add(lbl);

        btn1 = new JButton(Client.icons[3]);
        btn1.setFocusable(false);
        btn1.setBackground(Color.WHITE);
        pnl.add(btn1);

        btn2 = new JButton(Client.icons[4]);
        btn2.setFocusable(false);
        btn2.setBackground(Color.WHITE);
        pnl.add(btn2);

        btn3L = new JButton(Client.icons[5]);
        btn3L.setFocusable(false);
        btn3L.setBackground(Color.WHITE);
        pnl.add(btn3L);

        btn3R = new JButton(Client.icons[6]);
        btn3R.setFocusable(false);
        btn3R.setBackground(Color.WHITE);
        pnl.add(btn3R);

        return pnl;
    }

    public void setBtnSelected(int row, int col, char playerPlay) {
        viewBoard[row][col].setEnabled(false);
    }

    /**
     * הגדרה של הריחוף
     * @param row שורה
     * @param col עמודה
     * @param status להחזיר למקור או לצבוע
     * @param color צבע לצביעה
     * @param myTurn  התור שלי או היריב
     */
    public void setButtonBorderLight(int row, int col, boolean status, Color color,boolean myTurn)
    {
        if(status)
        {
            
            if(myTurn)
            {
                viewBoard[row][col].setBorder(BorderFactory.createLineBorder(color, 7));
            }
            else
            {
                viewBoard[row][col].setBorder(BorderFactory.createLineBorder(color));
            }
        }
        else
        {
            viewBoard[row][col].setBorder(originalBorder);
        }
    }

    /**
     * בדגירת חלון הגרפי של המשחק
     */
    public void close() {
        win.dispose();
    }

    /**
     * עדכון הלוח הגרפי - VIEW
     * @param s הלוח המעודכן - שנשלח לעדכון הלוח הגרפי
     */
    public void updateView(State s) 
    {
       
        for (int row = 0; row < State.BOARD_SIZE; row++) 
        {
            for (int col = 0; col < State.BOARD_SIZE; col++) 
            {
                setPlayerAt(s.getPlayerAt(row, col), row, col);
                
            }
        }
    }
    
    /**
     * הגדרת לייבל השחקן וצבע שלו
     * @param player 
     */
    public void UpdatePlayerLable(char player) 
    {
        myColor.setFont(new Font(Font.MONOSPACED, Font.BOLD, 25));
        myColor.setBackground(null);
        if(player=='R')
        {
            myColor.setText("My Player Color Is Red");
        myColor.setForeground(Color.RED);
        }
        else if(player=='B')
                {
                myColor.setText("My Player Color Is Blue");
                myColor.setForeground(Color.BLUE);
                }         
    }   
}
