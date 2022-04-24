package GameV2MinMax;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Docs: this class is the game manager. Date: 2/11/2017 Author: Lidor
 * Malich
 */
public class View {

    // תכונות
    private JFrame win;
    private JButton[][] viewBoard;
    private JLabel lblMsg;
    private JPanel pnlButtons;
    private JPanel pnlOptions;
    private JPanel pnlShapes;
    private JMenuBar menuBar;
    private JButton btnNewGame, btnPCMove;
    private JButton btn1, btn2, btn3R, btn3L;
    private Border originalBorder;  //משמש להחזרת הלוח למצב הרגיל - ביטול הצהוב מסביב

    //פעולה בונה של התצוגה
    public View() {
        viewBoard = new JButton[Controller.BOARD_SIZE][Controller.BOARD_SIZE];

        pnlOptions = createOptionsPanel();
        pnlShapes = createShapesButtons();
        pnlButtons = createButtonsPanel();
        lblMsg = createMessageLable();
        menuBar = createMenuBar();

        win = new JFrame(Controller.GAME_TITLE);
        win.setResizable(false);
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setJMenuBar(menuBar);

        win.add(pnlOptions, BorderLayout.NORTH);
        win.add(pnlShapes, BorderLayout.EAST);
        win.add(pnlButtons, BorderLayout.CENTER);
        win.add(lblMsg, BorderLayout.SOUTH);

        win.pack();
        win.setLocationRelativeTo(null);
        win.setVisible(true);

        init();
    }

    //אתחול הלוח
    public void init() {
        for (int row = 0; row < viewBoard.length; row++) {
            for (int col = 0; col < viewBoard[row].length; col++) {
                //viewBoard[row][col].setText("");
                viewBoard[row][col].setEnabled(true);
                viewBoard[row][col].setOpaque(false);
                viewBoard[row][col].setBackground(Color.WHITE);
            }
        }

        //הגדרת הכפטור כלחיץ
        btn1.setEnabled(true);
        btn2.setEnabled(true);
        btn3R.setEnabled(true);
        btn3L.setEnabled(true);

    }

    //עדכון בר ההודעות
    public void updateMessage(char color) {
        //<html>Text color: <font color='red'>red</font></html>
        if (color == 'R') {
            lblMsg.setText("<html> <font color='red'>Red Player your turn...</font></html>");
        } else {
            lblMsg.setText("<html> <font color='blue'>Blue Player your turn...</font></html>");
        }
    }

//    יצירת הכפטורים על  הלוח
    private JPanel createButtonsPanel() {
        JPanel pnl = new MyPanel(Controller.BUTTON_SIZE * Controller.BOARD_SIZE, Controller.BUTTON_SIZE * Controller.BOARD_SIZE);
        pnl.setOpaque(true);
        pnl.setLayout(new GridLayout(Controller.BOARD_SIZE, Controller.BOARD_SIZE));
        for (int row = 0; row < viewBoard.length; row++) {
            for (int col = 0; col < viewBoard[row].length; col++) {
                JButton btn = new JButton();
                
                btn.setFont(Controller.BUTTON_FONT);
                btn.setPreferredSize(new Dimension(Controller.BUTTON_SIZE, Controller.BUTTON_SIZE));
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

// יצירת חלונית ההודעות
    private JLabel createMessageLable() {
        JLabel lbl = new JLabel(" ");
        lbl.setForeground(Color.BLUE);
        lbl.setFont(Controller.MESSAGE_FONT);
        return lbl;
    }

    //קבלת הלוח הגרפי
    public JButton[][] getBoardButtons() {
        return viewBoard;
    }

    //הגדרת לחצן כלא לחיץ - קורה לאחר נצחון
    public void setEnabled(int row, int col) {
        viewBoard[row][col].setEnabled(false);
    }

    //קבלת התפריט העליון
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    //הגדרת הודעה
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

    public JButton getNewGameButton() {
        return btnNewGame;
    }

    public JButton getPCPlayerButton() {
        return btnPCMove;
    }

    public JButton getBtn1Button() {
        return btn1;
    }

    public JButton getBtn2Button() {
        return btn2;
    }

    public JButton getBtn3RButton() {
        return btn3R;
    }

    public JButton getBtn3LButton() {
        return btn3L;
    }

    private JMenuBar createMenuBar() {
        //יצירת בר התפריטים אותו נצרף לחלון המשחק 
        JMenuBar menuBar = new JMenuBar();

//         יצירת תפריט ראשי ראשון אותו נוסיף לבר התפריטים
        JMenu menu1 = new JMenu("Game"); // יצירת פריט ראשי בתפריט 
        menuBar.add(menu1);

        // יצירת סדרת תת תפריטים אותם נוסיף לתפריט הראשי הראשון
        JMenuItem menu1_1 = new JMenuItem("New Game");
        JMenuItem menu1_2 = new JMenuItem("About");
        JMenuItem menu1_3 = new JMenuItem("EXIT");

        //הוספת התפריט אל הסטטוס באר העליון
        menu1.add(menu1_1);
        menu1.add(menu1_2);
        menu1.add(menu1_3);
        
        JMenu menu2 = new JMenu("switch game mode"); // יצירת פריט ראשי בתפריט 
        menuBar.add(menu2);

        //BAR2
        JMenuItem menu2_1 = new JMenuItem("2 Player");
        JMenuItem menu2_2 = new JMenuItem("Humen VS PC");

        //הוספת התפריט אל הסטטוס באר העליון
        menu2.add(menu2_1);
        menu2.add(menu2_2);

        return menuBar;
    }

    public void setPlayerAt(char nowPlay, int row, int col) {
        viewBoard[row][col].setEnabled(false);

        if (nowPlay == 'R') {
            viewBoard[row][col].setOpaque(true);
            viewBoard[row][col].setBackground(Color.RED);
        } else {
            viewBoard[row][col].setOpaque(true);
            viewBoard[row][col].setBackground(Color.BLUE);
        }
    }
    
  

    public void setBTNLocked() {
        for (int row = 0; row < Controller.BOARD_SIZE; row++) {
            for (int col = 0; col < Controller.BOARD_SIZE; col++) {
                viewBoard[row][col].setEnabled(false);
            }

        }
    }

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

    public void setColorToWiner(ArrayList<Location> winLock) {
        Location winlockionTMP = new Location(0, 0);
        int col, row;
        for (int i = 0; i < winLock.size(); i++) {
            winlockionTMP = winLock.get(i);
            row = winlockionTMP.getRow();
            col = winlockionTMP.getCol();
//            viewBoard[row][col].setBackground(Color.yellow);
            setButtonBorderLight(row, col, true, Color.CYAN,true);
        }
    }

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

    private JPanel createOptionsPanel() {
        int s = 10; // num of spaces
        JPanel pnl = new JPanel(new GridLayout(1, 3, s, s));
        pnl.setLayout(new FlowLayout());
        pnl.setBorder(BorderFactory.createEmptyBorder(s, s, s, s));

        btnPCMove = new JButton("PC Move");
        btnPCMove.setFocusable(false);
        pnl.add(btnPCMove);

        btnNewGame = new JButton("New Game");
        btnNewGame.setFocusable(false);
        pnl.add(btnNewGame);

        return pnl;
    }

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

        btn1 = new JButton(Controller.icons[3]);
        btn1.setFocusable(false);
        btn1.setBackground(Color.WHITE);
        pnl.add(btn1);

        btn2 = new JButton(Controller.icons[4]);
        btn2.setFocusable(false);
        btn2.setBackground(Color.WHITE);
        pnl.add(btn2);

        btn3L = new JButton(Controller.icons[5]);
        btn3L.setFocusable(false);
        btn3L.setBackground(Color.WHITE);
        pnl.add(btn3L);

        btn3R = new JButton(Controller.icons[6]);
        btn3R.setFocusable(false);
        btn3R.setBackground(Color.WHITE);
        pnl.add(btn3R);

        return pnl;
    }

    public void setBtnSelected(int row, int col, char playerPlay) {
        viewBoard[row][col].setEnabled(false);
    }

    public void setBtnSelectedMinMax(int row, int col, int choice, char playerPlay) 
    {
        if (choice == 1) 
        {
            if(playerPlay=='R')
                {
                    viewBoard[row][col].setBackground(Color.RED);
                }
                else
                {
                    viewBoard[row][col].setBackground(Color.BLUE);
                }
            viewBoard[row][col].setEnabled(false);
             setPlayerAt(playerPlay, row, col);

            //מחזיר אמת
            //- מיכוון שהתא שבחר המחשב הוא מתוך תאים ריקים
        } else if (choice == 2) {
            int coltmp = col - 1;
            if (coltmp >= 0) {
                if(playerPlay=='R'){
                    viewBoard[row][col].setBackground(Color.RED);
                    viewBoard[row][coltmp].setBackground(Color.RED);}
                else{
                  viewBoard[row][col].setBackground(Color.BLUE);
                  viewBoard[row][coltmp].setBackground(Color.BLUE);}
                viewBoard[row][col].setEnabled(false);
                viewBoard[row][coltmp].setEnabled(false);
                setPlayerAt(playerPlay, row, col);
                setPlayerAt(playerPlay, row, coltmp);
            }

        } else if (choice == 3) {

            int rowtmp = row - 1;
            int coltmp = col;
            int rowtmpSec = row;
            int coltmpSec = col - 1;
                if(playerPlay=='R')
                {
                    viewBoard[row][col].setBackground(Color.RED);
                    viewBoard[rowtmp][coltmp].setBackground(Color.RED);
                    viewBoard[rowtmpSec][coltmpSec].setBackground(Color.RED);
                }
                else
                {
                    viewBoard[row][col].setBackground(Color.BLUE);
                    viewBoard[rowtmp][coltmp].setBackground(Color.BLUE);
                    viewBoard[rowtmpSec][coltmpSec].setBackground(Color.BLUE);
                }
            viewBoard[row][col].setEnabled(false);
            viewBoard[rowtmp][coltmp].setEnabled(false);
            viewBoard[rowtmpSec][coltmpSec].setEnabled(false);
            setPlayerAt(playerPlay, row, col);
            setPlayerAt(playerPlay, rowtmp, coltmp);
            setPlayerAt(playerPlay, rowtmpSec, coltmpSec);
        } else 
            if (choice == 4) 
            {
            int rowtmp = row + 1;
            int coltmp = col;
            int rowtmpSec = row;
            int coltmpSec = col-1;
            
            if(playerPlay=='R')
                {
                    viewBoard[row][col].setBackground(Color.RED);
                    viewBoard[rowtmp][coltmp].setBackground(Color.RED);
                    viewBoard[rowtmpSec][coltmpSec].setBackground(Color.RED);
                }
                else
                {
                    viewBoard[row][col].setBackground(Color.BLUE);
                    viewBoard[rowtmp][coltmp].setBackground(Color.BLUE);
                    viewBoard[rowtmpSec][coltmpSec].setBackground(Color.BLUE);
                }
            
            viewBoard[row][col].setEnabled(false);
            viewBoard[rowtmp][coltmp].setEnabled(false);
            viewBoard[rowtmpSec][coltmpSec].setEnabled(false);
            setPlayerAt(playerPlay, row, col);
            setPlayerAt(playerPlay, rowtmp, coltmp);
            setPlayerAt(playerPlay, rowtmpSec, coltmpSec);

        }
    }

    public void setButtonBorderLight(int row, int col, boolean status, Color color,boolean isHumen)
    {
        if(status)
        {
            
            if(isHumen)
            {
                viewBoard[row][col].setBorder(BorderFactory.createLineBorder(color, 5));
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
}
