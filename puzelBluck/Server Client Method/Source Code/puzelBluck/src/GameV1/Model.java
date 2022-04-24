package GameV1;

import java.util.ArrayList;

/**
 * Docs: Controller class is the game manager. Date: 2/11/2017 Author: Lidor
 * Malich
 */
public class Model
{
    // תכונות
    //private char[][] modelBoard;
    private State currentState;
    private char currentPlayer;
    //private int numOfClick;

    public Model()
    {

    }

    // איתחול מודל המשחק
    public void init()
    {
        currentState = new State();
        currentPlayer = 'R';
    }

    // מחזירה את המספר השמור במקום המתקבל כפרמטר
    public int getValueAt(int row, int col)
    {
        return currentState.getBord()[row][col];
    }

    public char[][] getBord()
    {
        return currentState.getBord();
    }

    // האם ניתן לבצע מהלך
    public boolean makeMove(int row, int col, int shape)
    {
        //shape =צורה
        if(currentState.getBord()[row][col] == ' ')
        {
            return true;
        }
        return false;
    }

    public Location[] checkwinFull(Location winlock[])
    {
        if(isWin() == null)
        {
            return null;
        }
        
        //לעשות בדיקת רווחים ולראות שהכל תקין בין השורות
        9999999999999999999999999999
                545445ק54א54קא
                        אק5א5ק5א5'54א54'
                        
        return winlock;
    }

    // האם הלוח במצב המטרה
    public Location[] isWin()
    {
        boolean win;
        int fail;
        //ArrayList<Location> List=new ArrayList<Location>();
        // בודק את השורות
            for(int row = 0; row < Controller.BOARD_SIZE; row++)
            {
                fail = 0;
                win = true;
                for(int col = 0; col < Controller.BOARD_SIZE; col++)
                {
                    if(currentState.getBord()[row][col] != currentPlayer)
                    {
                        win = false;
                        fail++;
                    }
                }
                if(win == true || fail < 3)
                {
                    Location[] winLock = new Location[Controller.BOARD_SIZE+1];
                    int TMP;
                    for(TMP = 0; TMP < Controller.BOARD_SIZE; TMP++)
                    {
                        if(currentState.getBord()[row][TMP]==currentPlayer)
                            winLock[TMP] = new Location(row, TMP);
                    }
                    return winLock;
                }
            }
        

        // בודק את העמודות
//        if(finish = false)
//        {
            for(int row = 0; row < Controller.BOARD_SIZE; row++)
            {
                fail = 0;
                win = true;
                for(int col = 0; col < Controller.BOARD_SIZE; col++)
                {
                    if(currentState.getBord()[col][row] != currentPlayer)
                    {
                        win = false;
                        fail++;
                    }
                }
                if(win == true || fail < 3)
                {
                    Location[] winLock = new Location[Controller.BOARD_SIZE+1];
                    int TMP;
                    for(TMP = 0; TMP < Controller.BOARD_SIZE; TMP++)
                    {
                        if(currentState.getBord()[TMP][row]==currentPlayer)
                            winLock[TMP] = new Location(TMP, row);
                    }
                return winLock;
                }
            }
        

        /*באמת בודק*/
        // בודק את אלכסון מהפינה השמאלית למעלה לפינה הימנית
//        if(!finish)
//        {
            win = true;
            fail = 0;
            for(int row = 0; row < Controller.BOARD_SIZE; row++)
            {

                if(currentPlayer != currentState.getBord()[row][row])
                {
                    win = false;
                    fail++;
                }
            }
            if(win == true || fail < 3)
            {
                Location[] winLock = new Location[Controller.BOARD_SIZE+1];
                int TMP;
                for(TMP = 0; TMP < Controller.BOARD_SIZE; TMP++)
                {
                    if(currentState.getBord()[TMP][TMP]==currentPlayer)
                        winLock[TMP] = new Location(TMP, TMP);
                }
            return winLock;  

            }
        

        /*באמת בודק*/
        // בודק את אלכסון מהפינה השמאלית למעלה לפינה הימנית
            win = true;
            fail = 0;
            int col = Controller.BOARD_SIZE - 1;
            for(int row = 0; row < Controller.BOARD_SIZE; row++)
            {
                if(currentPlayer != currentState.getBord()[row][col])
                {
                    win = false;
                    fail++;
                }
                col--;
            }
            if(win == true || fail < 3)
            {
                Location[] winLock = new Location[Controller.BOARD_SIZE+1];
                col = Controller.BOARD_SIZE - 1;;
                int TMP;
                for(TMP = 0; TMP < Controller.BOARD_SIZE; TMP++)
                {
                    if(currentState.getBord()[TMP][col]==currentPlayer)
                        winLock[TMP] = new Location(TMP, col);
                    col--;
                }
            return winLock; 
            }
//        }


        return null;
    }

    //קבלת מי השחקן כעת
    public char getCurrentPlayer()
    {
        return currentPlayer;
    }

    //בעת לחיצה על כפטור-בדיקה עם הוא ריק או לא
    public boolean isEmpty(int row, int col)
    {
        if(currentState.getBord()[row][col] == ' ')
        {
            return true;
        }
        return false;
    }

    public void setPlayerAt(int row, int col)
    {
        currentState.getBord()[row][col] = currentPlayer;
    }

    //הגדרת שחקן לפי מספר
    public boolean isTie()
    {
        return currentState.isTie();
    }

    public void printBord()
    {
        currentState.printBord();
    }

    public void replacePlayer()
    {
        if(currentPlayer == 'R')
        {
            currentPlayer = 'B';
        }
        else
        {
            currentPlayer = 'R';
        }
    }

    public String getCurrentPlayerInText()
    {
        char chack = getCurrentPlayer();
        String tmp;
        if(chack == 'R')
        {
            tmp = "Red";
        }
        else
        {
            tmp = "Blue";
        }
        return tmp;
    }
}
