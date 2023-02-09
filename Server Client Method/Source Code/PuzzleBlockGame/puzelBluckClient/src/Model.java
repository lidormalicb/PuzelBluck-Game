/**
 * Docs: Controller class is the game manager.
 * Date: 2018-2019 
 * Author: Lidor Malich
 */
public class Model
{
    // תכונות
    private State state;
    private char myPlayer;
    
    /**
     * פעולה בונה
     */
    public Model()
    {
        init();
    }
    
    /**
     * הצבת איזה שחקן אני
     * @param charPlayer התו שמיצג את השחקן
     */
    public void setCurrentPlayer(char charPlayer) {
        myPlayer = charPlayer;
    }

    /**
     *  איתחול מודל המשחק
    */
    public void init()
    {
        state = new State();
    }
    
    /**
     * מחזירה את הערך השמור במקום המתקבל כפרמטר
     * @param row שורה
     * @param col עמודה
     * @return התו המוחזק כרגע במקום של השורה והעמודה
     */
    public int getValueAt(int row, int col)
    {
        return state.getBord()[row][col];
    }

    /**
     * האם ניתן לבצע מהלך חוקי במיקום שהתקבל כפרמטר עם הצורה המבוקשת
     * @param row שורה לבדיקה
     * @param col עמודה לבדיקה
     * @param shape צורה לבדיקה
     * @return אמת או שקר - בהתאם
     */
    public boolean makeMove(int row, int col, int shape)
    {
        //shape =צורה
        if(state.getBord()[row][col] == ' ')
        {
            return true;
        }
        return false;
    }

    /**
     *     בדיקה לגרפיקה האם אפשר להשים שחקן מסויים במיקום שהעכבר נמצא עליו
     * @param choice צורה שנבחרה
     * @param row שורה
     * @param col עמודה
     * @return  בהתאמה- אמת או שקר
     */
    public boolean chackIfCanPlaceShapeAt(int choice, int row, int col)
    {
        if(choice == 1)
        {
            //מחזיר אמת
            //- מיכוון שהתא שבחר המחשב הוא מתוך תאים ריקים
            return true;
        }
        else if(choice == 2)
        {
            int coltmp = col - 1;
            if(coltmp >= 0)
            {
                if(isEmpty(row, coltmp))
                {
                    return true;
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
                    if(isEmpty(row, col))
                    {
                        if(isEmpty(rowtmp, coltmp))
                        {
                            if(isEmpty(rowtmpSec, coltmpSec))
                            {
                                return true;
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
                if(isEmpty(row, col))
                {
                    if(isEmpty(rowtmp, coltmp))
                    {
                        if(isEmpty(rowtmpSec, coltmpSec))
                        {
                            return true;
                        }
                    }
                }
            }

        }
        return false;
    }
    
    /**
     * עדכון לוח המשחק שהתקבל מהשחקן היריב
     * @param s הלוח החדש
     */
    public void updateState(State s) 
    {
        state = s;
    }
    
    public char getMyPlayer()
    {
        return myPlayer;
    }

    /**
     * בדיקה עם המיקום הנדרש הוא פנוי או לא
     * @param row שורה
     * @param col עמודה
     * @return אמת או שקר - בהתאמה
     */
    public boolean isEmpty(int row, int col)
    {
        if(state.getBord()[row][col] == ' ')
        {
            return true;
        }
        return false;
    }

    public void setPlayerAt(int row, int col)
    {
        state.getBord()[row][col] = myPlayer;
    }
    
    public State getState() 
    {
        return state;
    }
}
