package GameV2MinMax;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * Docs: Controller class is the game manager. 
 * Date: 2/11/2017 
 * Author: Lidor Malich
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
    private View view;              // game GUI
    private Model model;            // GAME Logic
 
    /*תכונות עזר למחלקה*/
    public static final int NumOfDepth = 5; //max depth to minimax
    private static int whatTheChoiceInNum = 0; //מספר הביחרה שיש ללחצן
    private static Color colorToPC = Color.GREEN; //צבע עזר לצפייה במה המחשב עשה
    
    /*
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
    
        //פעולה להתחלת משחק חדש 
    public void newGame()
    {
        model.init();
        view.init();
        view.updateMessage(model.getCurrentPlayer());
        view.setVisible(true);
        view.setColorToBTN(0);//0 Is Reset code
        //model.setPCTurn(false);
        UnlockAndRelockBTN(model.getPCTurn());
    }

    // פעולה להרצה ראשונית של המשחק
    private void startTheGame()
    {
        showGameSplash();
        newGame();
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

                view.getBoardButtons()[row][col].addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseEntered(MouseEvent e)
                    {
                        
                        super.mouseEntered(e);
                        int row = (int) ((JButton) e.getSource()).getClientProperty("rowIndex");
                        int col = (int) ((JButton) e.getSource()).getClientProperty("colIndex");
                        setTMPColorToSelctedBTN(whatTheChoiceInNum,row,col);
//                        setPlayerViaNum(whatTheChoiceInNum, col, col, false);
//                        char player=model.currentPlayer;
//                        view.setBtnSelectedMinMax(row,col,whatTheChoiceInNum,model.currentPlayer);
                      
                    }
                    @Override
                    public void mouseExited(MouseEvent e)
                    {
                        super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
                        if(!model.isGameOver())
                        {
                            moveAllButtonBorderLightToOrignal();
                        }
                    }

                    @Override
                    public void mouseMoved(MouseEvent me)
                    {
                        super.mouseMoved(me); //To change body of generated methods, choose Tools | Templates.
                        if(!model.isGameOver())
                        {
                            moveAllButtonBorderLightToOrignal();
                        }
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
        
        
        //הגדרת שחקן מול מחשב
        view.getMenuBar().getMenu(1).getItem(0).addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
//                model.setTwoPlayer(false);
            }
        });
        
        //הגדרת שחקן מול שחקן
        view.getMenuBar().getMenu(1).getItem(1).addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
//                model.setTwoPlayer(true);
            }
        });

        //כפטור ליצירת משחק חדש
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
                moveAllButtonBorderLightToOrignal();                
            }
        });
        view.getBtn2Button().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                whatTheChoiceInNum = 2;
                view.setColorToBTN(whatTheChoiceInNum);
                moveAllButtonBorderLightToOrignal();
            }
        });
        view.getBtn3LButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                whatTheChoiceInNum = 3;
                view.setColorToBTN(whatTheChoiceInNum);
                moveAllButtonBorderLightToOrignal();
            }
        });

        view.getBtn3RButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                whatTheChoiceInNum = 4;
                view.setColorToBTN(whatTheChoiceInNum);
                moveAllButtonBorderLightToOrignal();
            }
        });

        //תפריט עליון של אופציות
        view.getPCPlayerButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                whatTheChoiceInNum = 0;
                view.setGrayColorToSideBTN();
//                view.setBTNLockedShaep(true,Color.YELLOW);
//                System.out.println(""+whatTheChoiceInNum);
                computerMove();
//                987654321987654321
//                view.setBTNLockedShaep(false,Color.YELLOW);
            }
        }
        );
    }
    
    //החזרת מצב הבורד למצב המקורי
    //החזרה למצב שבלי סימון מסיב למשבצת
    private void moveAllButtonBorderLightToOrignal()
    {
        
            for(int row= 0; row < Controller.BOARD_SIZE; row++)        
            {
                for(int col= 0; col < Controller.BOARD_SIZE; col++)            
                {
                   view.setButtonBorderLight(row,col,false,null,false);
                }
            }
        
    }

    //שחקן שני- המחשב
    public void computerMove()
    {
        //view.setMessage(view.getMessage()+"But now PC is Play to you.... PLZ w8");
        //char player = model.getCurrentPlayer();
        State bestState = model.doComputerMove();

        //setPlayerAtMinMaxColor(bestState);
        //System.out.println("best>>>>>>>\n"+place);
//        setPlayerViaNum(bestState.getLastShapeNum(), bestState.getLastMove().getRow(), bestState.getLastMove().getCol());
        setPlayerViaNum(bestState.getLastShapeNum(), bestState.getLastMove().getRow(), bestState.getLastMove().getCol(), false);

//        if(bestState.getLastMove()!=null && bestState.getLastShapeNum()!=0)
//                System.out.println("is ok BestState");
        //view.setBtnSelectedMinMax(place.getRow(), place.getCol(),place.getChoice(), player);
        checkGameOver();
        //model.replacePlayer();

    }

    //החל שינוי בעת לחיצה-על מנת לסמן את השחקן הנחבר
    private void makeMove(int row, int col)
    {
        view.setColorToBTN(0);//0 Is Reset code
        setPlayerViaNum(whatTheChoiceInNum, row, col, true);
        //model.printBord();
        checkGameOver();
        //פעולה לנעילת הכפטור של היריב - למשל אם תורי לשחק אז שהמחשב יהיה נעול
    }
    
    public void UnlockAndRelockBTN(boolean PCTurn)
    {
//        if(model.getTwoPlayer()==false)
//        {
//            if(PCTurn)
//            {
//                view.getPCPlayerButton().setEnabled(true);
//                view.setBTNLockedShaep(true,Color.DARK_GRAY);
//            }
//            else
//            {
//                view.getPCPlayerButton().setEnabled(false);
//                view.setBTNLockedShaep(false,Color.DARK_GRAY);
//            }
//            model.setPCTurn();
////        }
    
    }

    public void setTMPColorToSelctedBTN(int choice, int row, int col)
    {
//        Color selcted=Color.decode("55522");
//        Color selcted=Color.decode("8526139");
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
            if(col > 0 && rowtmp < BOARD_SIZE && coltmpSec < BOARD_SIZE)
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
    //הגדרת שחקן לפי מספר
    public boolean setPlayerViaNum(int choice, int row, int col, boolean isHumman)
    {
//        System.out.println(">>>>>>>>>>>>>>>>>> cho=" + choice + ", row=" + row + ", col=" + col);
        boolean chaeck = false;
        if(choice == 0)
        {
//            System.out.println("cohis 0");
            view.setMessage("נא לבחור אופציה לישום");
            return false;
        }
        else if(choice == 1)
        {
            model.setPlayerAt(row, col);
            if(!isHumman)
            {
                view.setButtonBorderLight(row, col, true, colorToPC,true);
            }
            view.setPlayerAt(model.getCurrentPlayer(), row, col);
            chaeck = true;
        }

        else if(choice == 2)
        {
            int rowtmp = row;
            int coltmp = col - 1;

            if(coltmp >= 0)
            {
                if(model.isEmpty(row, coltmp))
                {
                    model.setPlayerAt(row, col);
                    model.setPlayerAt(rowtmp, coltmp);
                    
                    view.setPlayerAt(model.getCurrentPlayer(), row, col);
                    view.setPlayerAt(model.getCurrentPlayer(), rowtmp, coltmp);
                    
                    if(!isHumman)
                    {
                        view.setButtonBorderLight(row, col, true, colorToPC,true);
                        view.setButtonBorderLight(rowtmp, coltmp, true, colorToPC,true);
                    }
                    
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
                if(rowtmp >= 0)
                {
                    if(model.isEmpty(row, col))
                    {
                        if(model.isEmpty(rowtmp, coltmp))
                        {
                            if(model.isEmpty(rowtmpSec, coltmpSec))
                            {
                                model.setPlayerAt(row, col);
                                model.setPlayerAt(rowtmp, coltmp);
                                model.setPlayerAt(rowtmpSec, coltmpSec);
                                
                                view.setPlayerAt(model.getCurrentPlayer(), row, col);
                                view.setPlayerAt(model.getCurrentPlayer(), rowtmp, coltmp);
                                view.setPlayerAt(model.getCurrentPlayer(), rowtmpSec, coltmpSec);
                                if(!isHumman)
                                {
                                    view.setButtonBorderLight(row, col, true, colorToPC,true);
                                    view.setButtonBorderLight(rowtmp, coltmp, true, colorToPC,true);
                                    view.setButtonBorderLight(rowtmpSec, coltmpSec, true, colorToPC,true);
                                }
                                chaeck = true;
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
            if(col > 0 && rowtmp < BOARD_SIZE && coltmpSec < BOARD_SIZE)
            {
                if(model.isEmpty(row, col))
                {
                    if(model.isEmpty(rowtmp, coltmp))
                    {
                        if(model.isEmpty(rowtmpSec, coltmpSec))
                        {
                            model.setPlayerAt(row, col);
                            model.setPlayerAt(rowtmp, coltmp);
                            model.setPlayerAt(rowtmpSec, coltmpSec);
                            
                            view.setPlayerAt(model.getCurrentPlayer(), row, col);
                            view.setPlayerAt(model.getCurrentPlayer(), rowtmp, coltmp);
                            view.setPlayerAt(model.getCurrentPlayer(), rowtmpSec, coltmpSec);
                            if(!isHumman)
                            {
                                 view.setButtonBorderLight(row, col, true, colorToPC,true);
                                view.setButtonBorderLight(rowtmp, coltmp, true, colorToPC,true);
                                view.setButtonBorderLight(rowtmpSec, coltmpSec, true, colorToPC,true);
                            }
                                
                            chaeck = true;
                        }
                    }
                }
            }

        }

        whatTheChoiceInNum = 0;

        if(chaeck == false)
        {
            view.setMessage("אופציה לא חוקית - חורג מגבולות הלוח");
        }
        else
        {
            model.replacePlayer();
        }
        return chaeck;
    }
    
   //פעולת עזר לבדיקת נצחון המשחק
    public void checkGameOver()
    {
        // האם יש ניצחון
        ArrayList<Location> winLock = model.isWin();
        if(winLock != null)
        {
            model.setGameOver(true);
            
            view.setBTNLocked();
            view.setBTNLockedShaep(true,Color.YELLOW);
            view.setMessage(model.getCurrentPlayerInText() + " Is Winner *Start Game");
            view.setColorToWiner(winLock);
            return;
        }

        //מצב תיקו - אפשרי בתנאי שכל הלוח מלא
        if(model.isTie())
        {
            view.setMessage("Tie!");
            return;
        }
        view.updateMessage(model.getCurrentPlayer());
        UnlockAndRelockBTN(model.getPCTurn());

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

    //הצגת הספלאש בכניסה למשחק
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
