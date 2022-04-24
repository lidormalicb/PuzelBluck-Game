package GameV2MinMax;

import java.util.ArrayList;

/**
 * Docs: State class is the bord state.
 *
 * Author: Lidor Malich (lidormalich@gmail.com)
 */
public class State
{
    //הגדרת משתני עזר
    private Double score;
    private Location lastMove;
    private char[][] bord;
    private int lastShapeNum;

    //יצירת STATE חדש
    //כולל איתחול שלו
    public State()
    {
        score = null;
        lastMove = null;

        bord = new char[Controller.BOARD_SIZE][Controller.BOARD_SIZE];
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
        {
            for(int col = 0; col < Controller.BOARD_SIZE; col++)
            {
                bord[row][col] = ' ';
            }
        }
    }

    //קבלת הצורה האחרונה שהתמשו בה
    //לצורך מינימקס
    public int getLastShapeNum()
    {
        return lastShapeNum;
    }

    //הגדרת הצורה שהתמשו בה -לצורך מינימקס
    public int setLastShapeNum(int shapeNum)
    {
        if(shapeNum>0 && shapeNum<5)
        {
            lastShapeNum = shapeNum;
            return shapeNum;
        }
        else{
            //System.out.println("false - State.setLastShapeNum");
            return 0;
        }
    }

    // copy constructor
    //פעולה בונה מעתיקה
    public State(State other)
    {
        score = other.score;
        lastMove = other.lastMove;
        bord = new char[Controller.BOARD_SIZE][Controller.BOARD_SIZE];
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
        {
            for(int col = 0; col < Controller.BOARD_SIZE; col++)
            {
                bord[row][col] = other.bord[row][col];
            }
        }
    }

    // update state-score & state-lastMove
    public void setPlayerAt(int row, int col, char player)
    {
        bord[row][col] = player;

        // record the last move
        lastMove = new Location(row, col);
    }

    //עדכון הצעד האחרון שעשה-לצורך שימוש
    public void setLastMove(Location lastMove)
    {
        this.lastMove = lastMove;
    }

    //can call only after setPlayerAt()
    public Location getLastMove()
    {
        return lastMove;
    }

    //can call only after setPlayerAt()
    public double getScore()
    {
        return score;
    }

    //פעולה לקבלת הלוח במצב עכשיו
    public char[][] getBord()
    {
        return bord;
    }

    //הגדרת הניקוד ללוח
    public void setScore(double score)
    {
        this.score = score;
    }

    //קבלת הניקוד של הלוח
    public char getPlayerAt(int row, int col)
    {
        return bord[row][col];
    }

    // בדיקת תיקו
    public boolean isTie()
    {
        for(int i = 0; i < Controller.BOARD_SIZE; i++)
        {
            for(int j = 0; j < Controller.BOARD_SIZE; j++)
            {
                if(bord[i][j] == ' ')
                {
                    return false;
                }
            }
        }
        return true;
    }

    // בדיקת ניצחון לשחקן מסוים
    public boolean isWinMiniMax(char player)
    {
//        boolean check;

       char space = ' ';
        //char[][] board = currentState.getBord();

        //ArrayList<Location> winLine = new ArrayList<>();
        //בדיקת השורות
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
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
//                        winLine.add(new Location(row, col));

                    } //בדיקה לשאר התאים
                    else //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    if(rezefNum == bord[row][col] && bord[row][col] == bord[row][col - 1])
                        {
                            //נגדיל את גודל הרצף באחד
                            count++;
                            //נוסיף אותו לרשימה
//                            winLine.add(new Location(row, col));
                            //אם גודל הרצף הגיע לשש מצוין החזר אמת
                            if(count == 6)
                            {
                                return true;
                            }
                        } //אם קיים מספר שאיננו תואם למספר של הרצף
                        //נתחיל לספור רצף חדש
                        //ואורכו יהיה אחד בהתחלה
                        else
                        {
                            count = 1;
                            rezefNum = bord[row][col];
//                            winLine.clear();
//                            winLine.add(new Location(row, col));
                        }

                }
            }
            rezefNum = -1;
            count = 0;
//            winLine.clear();
        }

        //בדיקת עמודות
        for(int col = 0; col < bord[0].length; col++)
        {
            //גודל הרצץ הנוכחי
            int count = 0;
            //המספר של הרצף הנוכחי
            int rezefNum = -1;
            for(int row = 0; row < Controller.BOARD_SIZE; row++)
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
//                        winLine.add(new Location(row, col));
                    } //בדיקה לשאר התאים
                    else //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    if(rezefNum == bord[row][col] && bord[row][col] == bord[row - 1][col])
                        {
                            //נגדיל את גודל הרצף באחד
                            count++;
                            //נוסיף אותו לרשימה
//                            winLine.add(new Location(row, col));
                            //אם גודל הרצף הגיע לשש מצוין החזר אמת
                            if(count == 6)
                            {
                                return true;
                            }
                        } //אם קיים מספר שאיננו תואם למספר של הרצף
                        //נתחיל לספור רצף חדש
                        //ואורכו יהיה אחד בהתחלה
                        else
                        {
                            count = 1;
                            rezefNum = bord[row][col];
//                            winLine.clear();
//                            winLine.add(new Location(row, col));
                        }
                }
            }
            rezefNum = -1;
            count = 0;
//            winLine.clear();
        }

        //אלכסון ראשי
        //גודל הרצץ הנוכחי
        int count = 0;
        //המספר של הרצף הנוכחי
        int rezefNum = -1;
        for(int i = 0; i < Controller.BOARD_SIZE; i++)
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
//                    winLine.add(new Location(i, i));
                } //בדיקה לשאר התאים
                else //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if(rezefNum == bord[i][i] && bord[i][i] == bord[i - 1][i - 1])
                    {
                        //נגדיל את גודל הרצף באחד
                        count++;
                        //נוסיף אותו לרשימה
//                        winLine.add(new Location(i, i));
                        //אם גודל הרצף הגיע לשש מצוין החזר אמת
                        if(count == 6)
                        {
                            return true;
                        }
                    } //אם קיים מספר שאיננו תואם למספר של הרצף
                    //נתחיל לספור רצף חדש
                    //ואורכו יהיה אחד בהתחלה
                    else
                    {
                        count = 1;
                        rezefNum = bord[i][i];
//                        winLine.clear();
//                        winLine.add(new Location(i, i));
                    }
            }
        }
        
        rezefNum = -1;
        count = 0;
//        winLine.clear();
        //אלכסון משני

        //גודל הרצץ הנוכחי
        count = 0;
        //המספר של הרצף הנוכחי
        rezefNum = -1;
        int col = Controller.BOARD_SIZE - 1;
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
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
//                    winLine.add(new Location(row, col));
                } //בדיקה לשאר התאים
                else //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if(rezefNum == bord[row][col] && bord[row][col] == bord[row - 1][col + 1])
                    {
                        //נגדיל את גודל הרצף באחד
                        count++;
                        //נוסיף אותו לרשימה
//                        winLine.add(new Location(row, col));
                        //אם גודל הרצף הגיע לשש מצוין החזר אמת
                        if(count == 6)
                        {
                            return true;
                        }
                    } //אם קיים מספר שאיננו תואם למספר של הרצף
                    //נתחיל לספור רצף חדש
                    //ואורכו יהיה אחד בהתחלה
                    else
                    {
                        count = 1;
                        rezefNum = bord[row][row];
//                        winLine.clear();
//                        winLine.add(new Location(row, col));
                    }
                col--;
            }
        }

        //אם לא נמצא ערך מתאים  החז ר
        return false;
    }

    //בדיקת ניצחון עבור שחקן מסויים-לצורך מינימקס
    public boolean isWinFor(char player)
    {
        888888888888888888888888888888888888888888888888
                //לבדוק למה מחזיר ערך 1 ולא 0 שאין כלום
        boolean check;
        char space = ' ';
//        char[][] board = getBord();

        //ArrayList<Location> winLine = new ArrayList<>();
        //בדיקת השורות
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
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

                    } //בדיקה לשאר התאים
                    else //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    {
                        if(rezefNum == bord[row][col] && bord[row][col] == bord[row][col - 1])
                        {
                            //נגדיל את גודל הרצף באחד
                            count++;
                            //אם גודל הרצף הגיע לשש מצוין החזר אמת
                            if(count == 6)
                            {
                                return true;
                            }
                        } //אם קיים מספר שאיננו תואם למספר של הרצף
                        //נתחיל לספור רצף חדש
                        //ואורכו יהיה אחד בהתחלה
                        else
                        {
                            count = 1;
                            rezefNum = bord[row][col];
                        }
                    }

                }
            }
            rezefNum = -1;
            count = 0;
        }

        //בדיקת עמודות
        for(int col = 0; col < bord[0].length; col++)
        {
            //גודל הרצץ הנוכחי
            int count = 0;
            //המספר של הרצף הנוכחי
            int rezefNum = -1;
            for(int row = 0; row < Controller.BOARD_SIZE; row++)
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
                    } //בדיקה לשאר התאים
                    else //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    {
                        if(rezefNum == bord[row][col] && bord[row][col] == bord[row - 1][col])
                        {
                            //נגדיל את גודל הרצף באחד
                            count++;
                            //אם גודל הרצף הגיע לשש מצוין החזר אמת
                            if(count == 6)
                            {
                                return true;
                            }
                        } //אם קיים מספר שאיננו תואם למספר של הרצף
                        //נתחיל לספור רצף חדש
                        //ואורכו יהיה אחד בהתחלה
                        else
                        {
                            count = 1;
                            rezefNum = bord[row][col];
                        }
                    }
                }
            }
            rezefNum = -1;
            count = 0;
        }

        //אלכסון ראשי
        //גודל הרצץ הנוכחי
        int count = 0;
        //המספר של הרצף הנוכחי
        int rezefNum = -1;
        for(int i = 0; i < Controller.BOARD_SIZE; i++)
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
                } //בדיקה לשאר התאים
                else //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                {
                    if(rezefNum == bord[i][i] && bord[i][i] == bord[i - 1][i - 1])
                    {
                        //נגדיל את גודל הרצף באחד
                        count++;
                        //אם גודל הרצף הגיע לשש מצוין החזר אמת
                        if(count == 6)
                        {
                            return true;
                        }
                    } //אם קיים מספר שאיננו תואם למספר של הרצף
                    //נתחיל לספור רצף חדש
                    //ואורכו יהיה אחד בהתחלה
                    else
                    {
                        count = 1;
                        rezefNum = bord[i][i];
                    }
                }
            }
        }
        rezefNum = -1;
        count = 0;
        //אלכסון משני

        //גודל הרצץ הנוכחי
        count = 0;
        //המספר של הרצף הנוכחי
        rezefNum = -1;
        int col = Controller.BOARD_SIZE - 1;
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
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
                } //בדיקה לשאר התאים
                else //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                {
                    if(rezefNum == bord[row][col] && bord[row][col] == bord[row - 1][col + 1])
                    {
                        //נגדיל את גודל הרצף באחד
                        count++;
                        //אם גודל הרצף הגיע לשש מצוין החזר אמת
                        if(count == 6)
                        {
                            return true;
                        }
                    } //אם קיים מספר שאיננו תואם למספר של הרצף
                    //נתחיל לספור רצף חדש
                    //ואורכו יהיה אחד בהתחלה
                    else
                    {
                        count = 1;
                        rezefNum = bord[row][row];
                    }
                }
                col--;
            }
        }
        //אם הגענו לכאן אז אין ניצחון
        //אם לא נמצא ערך מתאים  החז ר
        return false;
    }

    //קבלת השחקן השני-לא מצבע החלפה רק החזרה
    public char getOtherPlayer(char player)
    {
        if(player == 'R')
        {
            return 'B';
        }
        return 'R';
    }

    //קבלת מספר הניתחונות לשחקן בלוח - עבור מינימקס- פעולת EVAL 
    public int getNumWinsForPlayer(char player)
    {
        int count = 0;
        int winCount = 0;
        for(int i = 0; i < 8; i++)
        {
            count = 0;
            for(int j = 2; j < 6; j++)
            {
                if(getOtherPlayer(player) == bord[i][j])
                {
                    count++;
                }
            }
            if(count == 0)
            {
                winCount++;
            }
        }

        for(int i = 0; i < 8; i++)
        {
            count = 0;
            for(int j = 2; j < 6; j++)
            {
                if(getOtherPlayer(player) == bord[j][i])
                {
                    count++;
                }
            }
            if(count == 0)
            {
                winCount++;
            }
        }
        count = 0;
        for(int i = 2; i < 6; i++)
        {
            if(getOtherPlayer(player) == bord[i][i])
            {
                count++;
            }
        }
        if(count == 0)
        {
            winCount++;
        }
        count = 0;
            int i=6;
            for(int j=2;j<6;j++)
            {
                if(getOtherPlayer(player)==bord[j][i])
                {
                    count ++;
                }
                i--;
            }
            if(count==0)
                winCount++;
        

        //אם הגענו לכאן אז אין ניצחון
        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>print count"+winCount);
        return winCount;
    }

    //פעולה להדפסת הלוח הנוכחי
    public void printBord()
    {
        for(int i = 0; i < Controller.BOARD_SIZE; i++)
        {
            for(int j = 0; j < Controller.BOARD_SIZE; j++)
            {
                if(this.bord[i][j] == ' ')
                {
                    System.out.print("-");
                }
                else
                {
                    System.out.print(this.bord[i][j]);
                }

            }
            System.out.println();

        }
        System.out.println();
    }

    // קבלת המיקומים הריקים בלוח 
    public ArrayList<Location> getEmptyLocations()
    {
        ArrayList<Location> lst = new ArrayList();
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
        {
            for(int col = 0; col < Controller.BOARD_SIZE; col++)
            {
                if(bord[row][col] == ' ')
                {
                    lst.add(new Location(row, col));
                }
            }
        }
        return lst;
    }

    public String toString()
    {
        String str = "";
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
        {
            for(int col = 0; col < Controller.BOARD_SIZE; col++)
            {
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
        }
        str += "Score: " + score;
        str += ", Last Move: " + lastMove;
        str += ", shaep: " + lastShapeNum + "\n";
        return str;
    }
}
