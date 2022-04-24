package GameV1;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Docs: Controller class is the game manager. Date: 2/11/2017 Author: Lidor
 * Malich
 * 
 * לתקן עם הקארנט סטייט שיעבוד
 */
public class Controller
{
    // קבועים
    public static int BOARD_SIZE = 8;
    public static final int BUTTON_SIZE = 100;
    public static final Font BUTTON_FONT = new Font(Font.DIALOG, Font.BOLD, 40);
    public static final Font MESSAGE_FONT = new Font(Font.DIALOG, Font.BOLD, 20);
    public static final String GAME_TITLE = "פאזל בלוק - Lidor Malich";
    public static final ImageIcon[] icons = new ImageIcon[BOARD_SIZE * BOARD_SIZE + 1];      // collection of all images 

    // תכונות
    private View view;          // game GUI
    private Model model;        // GAME Logic

    /*
    תכונות עזר למחלקה
     */
    //private static boolean OnlyOneClick = false;
    private static int whatTheChoiceInNum = 0;

    
    /**
     * פעולה בונה ליצירת מנהל המשחק
     */
    public Controller()
    {
        loadResources();
        model = new Model();
        view = new View();
        setGameEventHandlers();
        startTheGame();
    }

    // ניהול ושליטה על כל אירועי המשחק
    private void setGameEventHandlers()
    {
        // טיפול באירועי לחיצה על כפתורי לוח המשחק
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                view.getBoardButtons()[row][col].addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        int row = (int) ((JButton) e.getSource()).getClientProperty("rowIndex");
                        int col = (int) ((JButton) e.getSource()).getClientProperty("colIndex");
                        makeMove(row, col);
                    }
                });
            }
        }

        // טיפול בתפריט Menubar
        // הוספת אירועי לחיצה לכל התת תפריטים
        // New Game בעת לחיצה על תת תפריט משחק חדש
        view.getMenuBar().getMenu(0).getItem(0).addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                newGame();
            }
        });

        // About Programmer בעת לחיצה על תת תפריט אודות המתכנת 
        view.getMenuBar().getMenu(0).getItem(1).addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(view.getWin(), "The Game creat by Lidor Malich \n lidormalich.com");
            }
        });

        //בעת לחיצה על יציאה
        view.getMenuBar().getMenu(0).getItem(2).addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(view.getWin(), "Exit?");
                view.getWin().dispose();
            }
        });

        view.getNewGameButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                newGame();
            }
        });

        //בחירת כפתור ללחיצה- ידע על ידי מספר = 
//                יהיה כפטור של הצורות שיקבל מספר קבוע 
//        למשל הכפטור של לחיצה אחת יקבל את הקוד 1
//        ועכשיו כאשר ילחץ בלוח מספר 1
//        אז הוא ישלח הכפתור הנלחץ לפונקציה שתקבל את המספר 1
//        והיא תקבל גם את השורה ועמודה
//        ומשם תצבע אם זה אפשרי ותחזיר אמת
//        אחרת תחזיר שקר
//        ובסוף היא תשים במשנה עזר של מה נבחר 0
//        ואז יודעים שלא זוכר את האופציה הקודמת
        view.getBtn1Button().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                whatTheChoiceInNum = 1;
                view.setColorToBTN(whatTheChoiceInNum);
            }
        });
        view.getBtn2Button().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                whatTheChoiceInNum = 2;
                view.setColorToBTN(whatTheChoiceInNum);
            }
        });
        view.getBtn3LButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                whatTheChoiceInNum = 3;
                view.setColorToBTN(whatTheChoiceInNum);
            }
        });

        view.getBtn3RButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                whatTheChoiceInNum = 4;
                view.setColorToBTN(whatTheChoiceInNum);
            }
        });

        //תפריט עליון של אופציות
        view.getChangePlayerButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                    String strNotWork = "עדיין המחשב לא כזה חכם כמו שנראה - שחק לבד";
                    JOptionPane.showMessageDialog(view.getWin(), strNotWork);
            }
        }
        );
    }

    //החל שינוי בעת לחיצה-על מנת לסמן את השחקן הנחבר
    private void makeMove(int row, int col)
    {
        view.setColorToBTN(0);//0 Is Reset code
        setPlayerAtViaNum(whatTheChoiceInNum, row, col);
        model.printBord();
        checkGameOver();
    }

    //הגדרת שחקן לפי מספר
    public boolean setPlayerAtViaNum(int choice, int row, int col)
    {
        boolean chaeck = false;
        if(choice == 0)
        {
            view.setMessage("נא לבחור אופציה לישום");
            return false;
        }
        else if(choice == 1)
        {
            model.setPlayerAt(row, col);
            view.setPlayerAt(model.getCurrentPlayer(), row, col);
            //איפוס בחירה- שלא יהיה זיכרון מהבחירה הקודמת
            
            chaeck = true;
        }

        else if(choice == 2)
        {
            //System.out.println(""+model.getCurrentPlayer()+coltmp+"col" + rowtmp + "row");
            int rowtmp = row;
            int coltmp = col-1;
            
            if(coltmp >= 0)
            {
                if(model.isEmpty(row, coltmp))
                {
                    
                    model.setPlayerAt(row, col);
                    view.setPlayerAt(model.getCurrentPlayer(), row, col);
                    model.setPlayerAt(rowtmp, coltmp);
                    view.setPlayerAt(model.getCurrentPlayer(), rowtmp, coltmp);
                    
                    chaeck = true;
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
                if(model.isEmpty(row, col))
                {
                    if(model.isEmpty(rowtmp, coltmp))
                    {
                        if(model.isEmpty(rowtmpSec, coltmpSec))
                        {

                            model.setPlayerAt(row, col);
                            view.setPlayerAt(model.getCurrentPlayer(), row, col);
                            model.setPlayerAt(rowtmp, coltmp);
                            view.setPlayerAt(model.getCurrentPlayer(), rowtmp, coltmp);
                            model.setPlayerAt(rowtmpSec, coltmpSec);
                            view.setPlayerAt(model.getCurrentPlayer(), rowtmpSec, coltmpSec);
                            
                            chaeck = true;
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
            if(col > 0 && rowtmp<=BOARD_SIZE &&coltmpSec<=BOARD_SIZE)
            {
                if(model.isEmpty(row, col))
                {
                    if(model.isEmpty(rowtmp, coltmp))
                    {
                        if(model.isEmpty(rowtmpSec, coltmpSec))
                        {
                            model.setPlayerAt(row, col);
                            view.setPlayerAt(model.getCurrentPlayer(), row, col);
                            model.setPlayerAt(rowtmp, coltmp);
                            view.setPlayerAt(model.getCurrentPlayer(), rowtmp, coltmp);
                            model.setPlayerAt(rowtmpSec, coltmpSec);
                            view.setPlayerAt(model.getCurrentPlayer(), rowtmpSec, coltmpSec);
                            
                            chaeck = true;
                        }
                    }
                }
            }

        }
        
        whatTheChoiceInNum = 0;

        if(chaeck = false)
        {
            view.setMessage("אופציה לא חוקית - חורג מגבולות הלוח");
            //System.out.println("לא חוקי");
        }
        else
        {
            model.replacePlayer();
        }
        return chaeck;
    }


    public void checkGameOver()
    {
        // האם יש ניצחון
        if(model.checkwinFull(model.isWin()) != null)
        {
            view.setBTNLocked();
            view.setBTNLockedShaep();
            view.setMessage(model.getCurrentPlayerInText() + " Is Winner *Start Game");
            view.setColorToWiner(model.isWin());
            return;
        }

        if(model.isTie())
        {
            view.setMessage("Tie!");
            return;
        }
        view.updateMessage(model.getCurrentPlayer());

    }

    //פעולה להתחלת משחק חדש 
    public void newGame()
    {
        model.init();
        view.init();
        view.updateMessage(model.getCurrentPlayer());
        view.setVisible(true);
        view.setColorToBTN(0);//99 Is Reset code
        //System.out.println("done!");
    }

    // פעולה להרצה ראשונית של המשחק
    private void startTheGame()
    {
        showGameSplash();
        newGame();
    }

    // טעינת משאבי המשחק כגון תמונות קבצי קול ועוד
    private void loadResources()
    {
        ImageIcon img = new ImageIcon(Controller.class.getResource("resources/Splash.png"));
        icons[0] = new ImageIcon(img.getImage().getScaledInstance(1000, 800, Image.SCALE_SMOOTH));

        img = new ImageIcon(Controller.class.getResource("resources/penguin_icon.png"));
        icons[1] = new ImageIcon(img.getImage().getScaledInstance(1000, 800, Image.SCALE_SMOOTH));

        img = new ImageIcon(Controller.class.getResource("resources/available.png"));
        icons[2] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        img = new ImageIcon(Controller.class.getResource("resources/1BTN.png"));
        icons[3] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        img = new ImageIcon(Controller.class.getResource("resources/2BTN.png"));
        icons[4] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        img = new ImageIcon(Controller.class.getResource("resources/3BTN_Left.png"));
        icons[5] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));

        img = new ImageIcon(Controller.class.getResource("resources/3BTN_Right.png"));
        icons[6] = new ImageIcon(img.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH));
        // load more ressources...
    }

    public void showGameSplash()
    {
        JLabel lblSplah = new JLabel(icons[0]);
        //lblSplah.setBorder(BorderFactory.createLineBorder(Color.BLUE, 4));
        JOptionPane.showOptionDialog(null, lblSplah, "Welcome", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]
        {
            "התחל משחק"
        }, null);
    }

    public static void main(String[] args)
    {
        new Controller();
    }
}
