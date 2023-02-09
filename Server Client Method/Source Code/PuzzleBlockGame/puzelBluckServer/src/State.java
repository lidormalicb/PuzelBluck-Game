import java.io.Serializable;
import java.util.ArrayList;

/**
 * Docs: State class is the bord state.
 * Date: 2018-2019 
 * Author: Lidor Malich
 */
public class State implements Serializable
{
    public static final int BOARD_SIZE = 8;
    
    // תכונות
    private char[][] bord;

    /**
     * פעולה בונה
     */
    public State()
    {
        bord = new char[BOARD_SIZE][BOARD_SIZE];
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                bord[row][col] = ' ';
            }
        }
    }

    /**
     * פעולה בונה מעתיקה
     * @param other 
     */
    public State(State other)
    {
        bord = new char[BOARD_SIZE][BOARD_SIZE];
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                bord[row][col] = other.bord[row][col];
            }
        }
    }

    
    public void setPlayerAt(int row, int col, char player)
    {
        bord[row][col] = player;
    }

    
    public char[][] getBord()
    {
        return bord;
    }
    
    public char getPlayerAt(int row, int col)
    {
        return bord[row][col];
    }

    /**
     * בדיקת תיקו 
     * @return אמת או שקר - בהתאמה
     */
    public boolean isTie()
    {
        for(int i = 0; i < BOARD_SIZE; i++)
        {
            for(int j = 0; j < BOARD_SIZE; j++)
            {
                if(bord[i][j] == ' ')
                {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * בדיקה האם יש נצחון על הלוח
     *  מחזירה מערך עם המיקומים של הנצחון
     * @return מערך המיקמים של הנצחון
     */
    public ArrayList chackGameOver()    
    {
        {
        char space = ' ';

        ArrayList<Location> winLine = new ArrayList<>();
        //בדיקת השורות
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            //גודל הרצץ הנוכחי
            int count = 0;
            //המספר של הרצף הנוכחי
            int rezefNum = -1;
            for(int col = 0; col < bord[0].length; col++)
            {
                if(bord[row][col] != space)
                {
                    //אם אנחנו בתא הראשון קיים רצף חדש
                    //והוא באורך של אחד
                    if(col == 0)
                    {
                        //מספר הרצף מופיע בינתיים פעם אחת
                        count = 1;
                        //הצבת המספר של הרצף
                        rezefNum = bord[row][col];
                        //הוספת המקום לרשימת הניצחון
                        winLine.add(new Location(row, col));

                    } //בדיקה לשאר התאים
                    else //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    if(rezefNum == bord[row][col] && bord[row][col] == bord[row][col - 1])
                        {
                            //נגדיל את גודל הרצף באחד
                            count++;
                            //נוסיף אותו לרשימה
                            winLine.add(new Location(row, col));
                            //אם גודל הרצף הגיע לשש מצוין החזר אמת
                            if(count == 6)
                            {
                                return winLine;
                            }
                        } //אם קיים מספר שאיננו תואם למספר של הרצף
                        //נתחיל לספור רצף חדש
                        //ואורכו יהיה אחד בהתחלה
                        else
                        {
                            count = 1;
                            rezefNum = bord[row][col];
                            winLine.clear();
                            winLine.add(new Location(row, col));
                        }

                }
            }
            rezefNum = -1;
            count = 0;
            winLine.clear();
        }

        //בדיקת עמודות
        for(int col = 0; col < bord[0].length; col++)
        {
            //גודל הרצץ הנוכחי
            int count = 0;
            //המספר של הרצף הנוכחי
            int rezefNum = -1;
            for(int row = 0; row < BOARD_SIZE; row++)
            {
                //אם אנחנו בתא הראשון קיים רצף חדש
                //והוא באורך של אחד
                if(bord[row][col] != space)
                {
                    if(row == 0)
                    {
                        //מספר הרצף מופיע בינתיים פעם אחת
                        count = 1;
                        //הצבת המספר של הרצף
                        rezefNum = bord[row][col];

                        //הוספת המקום לרשימת הניצחון
                        winLine.add(new Location(row, col));
                    } //בדיקה לשאר התאים
                    else //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    if(rezefNum == bord[row][col] && bord[row][col] == bord[row - 1][col])
                        {
                            //נגדיל את גודל הרצף באחד
                            count++;
                            //נוסיף אותו לרשימה
                            winLine.add(new Location(row, col));
                            //אם גודל הרצף הגיע לשש מצוין החזר אמת
                            if(count == 6)
                            {
                                return winLine;
                            }
                        } //אם קיים מספר שאיננו תואם למספר של הרצף
                        //נתחיל לספור רצף חדש
                        //ואורכו יהיה אחד בהתחלה
                        else
                        {
                            count = 1;
                            rezefNum = bord[row][col];
                            winLine.clear();
                            winLine.add(new Location(row, col));
                        }
                }
            }
            rezefNum = -1;
            count = 0;
            winLine.clear();
        }

        //אלכסון ראשי
        //גודל הרצץ הנוכחי
        int count = 0;
        //המספר של הרצף הנוכחי
        int rezefNum = -1;
        for(int i = 0; i < BOARD_SIZE; i++)
        {
            //אם אנחנו בתא הראשון קיים רצף חדש
            //והוא באורך של אחד
            if(bord[i][i] != space)
            {
                if(i == 0)
                {
                    //מספר הרצף מופיע בינתיים פעם אחת
                    count = 1;
                    //הצבת המספר של הרצף
                    rezefNum = bord[i][i];
                    //הוספת המקום לרשימת הניצחון
                    winLine.add(new Location(i, i));
                } //בדיקה לשאר התאים
                else //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if(rezefNum == bord[i][i] && bord[i][i] == bord[i - 1][i - 1])
                    {
                        //נגדיל את גודל הרצף באחד
                        count++;
                        //נוסיף אותו לרשימה
                        winLine.add(new Location(i, i));
                        //אם גודל הרצף הגיע לשש מצוין החזר אמת
                        if(count == 6)
                        {
                            return winLine;
                        }
                    } //אם קיים מספר שאיננו תואם למספר של הרצף
                    //נתחיל לספור רצף חדש
                    //ואורכו יהיה אחד בהתחלה
                    else
                    {
                        count = 1;
                        rezefNum = bord[i][i];
                        winLine.clear();
                        winLine.add(new Location(i, i));
                    }
            }
        }
        rezefNum = -1;
        count = 0;
        winLine.clear();
        //אלכסון משני

        //גודל הרצץ הנוכחי
        count = 0;
        //המספר של הרצף הנוכחי
        rezefNum = -1;
        int col = BOARD_SIZE - 1;
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            if(bord[row][col] != space)
            {
                //אם אנחנו בתא הראשון קיים רצף חדש
                //והוא באורך של אחד
                if(row == 0)
                {
                    //מספר הרצף מופיע בינתיים פעם אחת
                    count = 1;
                    //הצבת המספר של הרצף
                    rezefNum = bord[row][col];
                    //הוספת המקום לרשימת הניצחון
                    winLine.add(new Location(row, col));
                } //בדיקה לשאר התאים
                else //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if(rezefNum == bord[row][col] && bord[row][col] == bord[row - 1][col + 1])
                    {
                        //נגדיל את גודל הרצף באחד
                        count++;
                        //נוסיף אותו לרשימה
                        winLine.add(new Location(row, col));
                        //אם גודל הרצף הגיע לשש מצוין החזר אמת
                        if(count == 6)
                        {
                            return winLine;
                        }
                    } //אם קיים מספר שאיננו תואם למספר של הרצף
                    //נתחיל לספור רצף חדש
                    //ואורכו יהיה אחד בהתחלה
                    else
                    {
                        count = 1;
                        rezefNum = bord[row][row];
                        winLine.clear();
                        winLine.add(new Location(row, col));
                    }
                col--;
            }
        }

        //אם לא נמצא ערך מתאים  החז ר
        return null;
        }
    }
    
    public String toString()
    {
        String str = "";
        for(int row = 0; row < BOARD_SIZE; row++)
        {
            for(int col = 0; col < BOARD_SIZE; col++)
            {
                System.out.print(bord[row][col]+",");
                if(bord[row][col] == ' ')
                {
                    str += '-';
                }
                else
                {
                    str += bord[row][col];
                }
            }
            str += "\n";
            System.out.println("");
        }
        return str;
    }
}