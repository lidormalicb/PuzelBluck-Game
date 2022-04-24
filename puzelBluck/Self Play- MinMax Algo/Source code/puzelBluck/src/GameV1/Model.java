package GameV1;

import java.util.ArrayList;

/**
 * Docs: Controller class is the game manager. Date: 2/11/2017 Author: Lidor
 * Malich
 */
public class Model {

    // תכונות
    //private char[][] modelBoard;
    private State currentState;
    private char currentPlayer;
    //private int numOfClick;

    public Model() {

    }

    // איתחול מודל המשחק
    public void init() {
        currentState = new State();
        currentPlayer = 'R';
    }

    // מחזירה את המספר השמור במקום המתקבל כפרמטר
    public int getValueAt(int row, int col) {
        return currentState.getBord()[row][col];
    }

    public char[][] getBord() {
        return currentState.getBord();
    }

    // האם ניתן לבצע מהלך
    public boolean makeMove(int row, int col, int shape) {
        //shape =צורה
        if (currentState.getBord()[row][col] == ' ') {
            return true;
        }
        return false;
    }

    // האם הלוח במצב המטרה
    public ArrayList<Location> isWin() {
        char space = ' ';
        char[][] board = currentState.getBord();

        ArrayList<Location> winLine = new ArrayList<>();
        //בדיקת השורות
        for (int row = 0; row < Controller.BOARD_SIZE; row++) {
            //גודל הרצץ הנוכחי
            int count = 0;
            //המספר של הרצף הנוכחי
            int rezefNum = -1;
            for (int col = 0; col < board[0].length; col++) {
                if(board[row][col]!=space)
                {
                //אם אנחנו בתא הראשון קיים רצף חדש
                //והוא באורך של אחד
                if (col == 0 ) {
                    //מספר הרצף מופיע בינתיים פעם אחת
                    count = 1;
                    //הצבת המספר של הרצף
                    rezefNum = board[row][col];
                     //הוספת המקום לרשימת הניצחון
                winLine.add(new Location(row, col));
                    
                } //בדיקה לשאר התאים
                else {
                    //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    if (rezefNum == board[row][col] && board[row][col] == board[row][col - 1]) {
                        //נגדיל את גודל הרצף באחד
                        count++;
                        //נוסיף אותו לרשימה
                        winLine.add(new Location(row, col));
                        //אם גודל הרצף הגיע לשש מצוין החזר אמת
                        if (count == 6) {
                            return winLine;
                        }
                    } //אם קיים מספר שאיננו תואם למספר של הרצף
                    //נתחיל לספור רצף חדש
                    //ואורכו יהיה אחד בהתחלה
                    else {
                        count = 1;
                        rezefNum = board[row][col];
                        winLine.clear();
                        winLine.add(new Location(row, col));
                    }
                }

            }
            }
            rezefNum = -1;
            count = 0;
            winLine.clear();
        }

        //בדיקת עמודות
        for (int col = 0; col < board[0].length; col++) {
            //גודל הרצץ הנוכחי
            int count = 0;
            //המספר של הרצף הנוכחי
            int rezefNum = -1;
            for (int row = 0; row < Controller.BOARD_SIZE; row++) {
                //אם אנחנו בתא הראשון קיים רצף חדש
                //והוא באורך של אחד
                if(board[row][col]!=space)
                {
                if (row == 0) {
                    //מספר הרצף מופיע בינתיים פעם אחת
                    count = 1;
                    //הצבת המספר של הרצף
                    rezefNum = board[row][col];
                    
                     //הוספת המקום לרשימת הניצחון
                winLine.add(new Location(row, col));
                } //בדיקה לשאר התאים
                else {
                    //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    if (rezefNum == board[row][col] && board[row][col] == board[row - 1][col]) {
                        //נגדיל את גודל הרצף באחד
                        count++;
                        //נוסיף אותו לרשימה
                        winLine.add(new Location(row, col));
                        //אם גודל הרצף הגיע לשש מצוין החזר אמת
                        if (count == 6) {
                            return winLine;
                        }
                    } //אם קיים מספר שאיננו תואם למספר של הרצף
                    //נתחיל לספור רצף חדש
                    //ואורכו יהיה אחד בהתחלה
                    else {
                        count = 1;
                        rezefNum = board[row][col];
                        winLine.clear();
                        winLine.add(new Location(row, col));
                    }
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
        for (int i = 0; i < Controller.BOARD_SIZE; i++) {
            //אם אנחנו בתא הראשון קיים רצף חדש
            //והוא באורך של אחד
            if(board[i][i]!=space)
            {
            if (i == 0) {
                //מספר הרצף מופיע בינתיים פעם אחת
                count = 1;
                //הצבת המספר של הרצף
                rezefNum = board[i][i];
                 //הוספת המקום לרשימת הניצחון
                winLine.add(new Location(i, i));
            } //בדיקה לשאר התאים
            else {
                //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if (rezefNum == board[i][i] && board[i][i] == board[i - 1][i - 1]) {
                    //נגדיל את גודל הרצף באחד
                    count++;
                    //נוסיף אותו לרשימה
                    winLine.add(new Location(i, i));
                    //אם גודל הרצף הגיע לשש מצוין החזר אמת
                    if (count == 6) {
                        return winLine;
                    }
                } //אם קיים מספר שאיננו תואם למספר של הרצף
                //נתחיל לספור רצף חדש
                //ואורכו יהיה אחד בהתחלה
                else {
                    count = 1;
                    rezefNum = board[i][i];
                    winLine.clear();
                    winLine.add(new Location(i, i));
                }
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
        int col = Controller.BOARD_SIZE - 1;
        for (int row = 0; row < Controller.BOARD_SIZE; row++) {
            if(board[row][col]!=space)
            {
            //אם אנחנו בתא הראשון קיים רצף חדש
            //והוא באורך של אחד
            if (row == 0) {
                //מספר הרצף מופיע בינתיים פעם אחת
                count = 1;
                //הצבת המספר של הרצף
                rezefNum = board[row][col];
                //הוספת המקום לרשימת הניצחון
                winLine.add(new Location(row, col));
            } //בדיקה לשאר התאים
            else {
                //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if (rezefNum == board[row][col] && board[row][col] == board[row - 1][col + 1]) {
                    //נגדיל את גודל הרצף באחד
                    count++;
                    //נוסיף אותו לרשימה
                    winLine.add(new Location(row, col));
                    //אם גודל הרצף הגיע לשש מצוין החזר אמת
                    if (count == 6) {
                        return winLine;
                    }
                } //אם קיים מספר שאיננו תואם למספר של הרצף
                //נתחיל לספור רצף חדש
                //ואורכו יהיה אחד בהתחלה
                else {
                    count = 1;
                    rezefNum = board[row][row];
                    winLine.clear();
                    winLine.add(new Location(row, col));
                }
            }
            col--;
            }
        }

        //אם לא נמצא ערך מתאים  החז ר
        return null;
    }

    public boolean check(int[] a) {
        //גודל הרצץ הנוכחי
        int count = 0;
        //המספר של הרצף הנוכחי
        int rezefNum = -1;
        for (int i = 0; i < a.length; i++) {
            //אם אנחנו בתא הראשון קיים רצף חדש
            //והוא באורך של אחד
            if (i == 0) {
                //מספר הרצף מופיע בינתיים פעם אחת
                count = 1;
                //הצבת המספר של הרצף
                rezefNum = a[i];
            } //בדיקה לשאר התאים
            else {
                //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if (rezefNum == a[i] && a[i] == a[i - 1]) {
                    //נגדיל את גודל הרצף באחד
                    count++;
                    //ARRYLIST
                    //אם גודל הרצף הגיע לשש מצוין החזר אמת
                    if (count == 6) {
                        return true;
                    }
                } //אם קיים מספר שאיננו תואם למספר של הרצף
                //נתחיל לספור רצף חדש
                //ואורכו יהיה אחד בהתחלה
                else {
                    count = 1;
                    rezefNum = a[i];
                }
            }

        }
        //אם לא נמצא ערך מתאים  החזר שקר
        return false;
    }

    //קבלת מי השחקן כעת
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    //בעת לחיצה על כפטור-בדיקה עם הוא ריק או לא
    public boolean isEmpty(int row, int col) {
        if (currentState.getBord()[row][col] == ' ') {
            return true;
        }
        return false;
    }

    public void setPlayerAt(int row, int col) {
        currentState.getBord()[row][col] = currentPlayer;
    }

    //הגדרת שחקן לפי מספר
    public boolean isTie() {
        return currentState.isTie();
    }

    public void printBord() {
        currentState.printBord();
    }

    public void replacePlayer() 
    {
        if (currentPlayer == 'R') 
        {
            currentPlayer = 'B';
        } else {
            currentPlayer = 'R';
        }
    }

    public String getCurrentPlayerInText() 
    {
        char chack = getCurrentPlayer();
        String tmp;
        if (chack == 'R') {
            tmp = "Red";
        } else {
            tmp = "Blue";
        }
        return tmp;
    }
}
