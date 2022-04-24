package GameV2MinMax;

import java.util.ArrayList;

/**
 * Docs: Controller class is the game manager. Date: 2/11/2017 Author: Lidor
 * Malich
 */
public class Model
{

    // תכונות
    private State currentState;
    public char currentPlayer;
    private boolean GameOver=false;
    private boolean PCTurn=false;
//    private boolean twoPlayer=false;

    public Model()
    {
        
    }

    public boolean isGameOver()
    {
        return GameOver;
    }

    public void setGameOver(boolean GameOver)
    {
        this.GameOver = GameOver;
    }

    // איתחול מודל המשחק
    public void init()
    {
        currentState = new State();
        currentPlayer = 'R';
        setGameOver(false);
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
    
    public boolean getPCTurn()
    {
        return PCTurn;
    }
    
    public boolean setPCTurn(boolean a)
    {
        return PCTurn=a;
    }

    public void setPCTurn()
    {
        if(PCTurn==true)
            {
                this.PCTurn = false;
            }
        else
            {
                this.PCTurn = true;
            }
    }
    
//    public boolean getTwoPlayer()
//    {
//        return twoPlayer;
//    }
//
//    public void setTwoPlayer(boolean twoPlayer)
//    {
//        this.twoPlayer = twoPlayer;
//    }

    

    public State doComputerMove()
    {    
        State bestState = minimax(currentState, -2.0, 2.0, 5, 10000,System.currentTimeMillis(), true);
        return bestState;
    }

    private double eval(State state, char player)
    {
        if(state.isWinMiniMax(player))
        {
            return 1;
        }

        if(state.isWinMiniMax(getOtherPlayer(player)))
        {
            return -1;
        }

        if(state.isTie())
        {
            return 0;
        }

        double first = state.getNumWinsForPlayer(player);
        double last = state.getNumWinsForPlayer(getOtherPlayer(player));
        double res = (first - last) / ((Controller.BOARD_SIZE * 2) + 2);
        
        System.out.println("first="+first+",last="+last+",res="+res);
        return res;
    }

    public char getOtherPlayer(char player)
    {
        if(player == 'R')
        {
            return 'B';
        }
        return 'R';
    }

    public ArrayList<State> getPossibleStates(State state, boolean isMax)
    {
        int row, col;
        State stateCopy;
        ArrayList<State> allPossibleStates = new ArrayList();
        ArrayList<Location> emptyLocations = state.getEmptyLocations();
        char player = isMax ? currentPlayer : getOtherPlayer(currentPlayer);

        for(int i = 0; i < emptyLocations.size(); i++)
        {
            row = emptyLocations.get(i).getRow();
            col = emptyLocations.get(i).getCol();

            if(chackIfCanPlaceShapeAt(1, row, col))
            {
                stateCopy = new State(state);
                stateCopy.setPlayerAt(row, col, player);
                stateCopy.setLastShapeNum(1); //מספר הביחרה שעשה
                stateCopy.setLastMove(new Location(row, col)); //מספר הביחרה שעשה
                allPossibleStates.add(stateCopy);
            }
            if(chackIfCanPlaceShapeAt(2, row, col))
            {
                stateCopy = new State(state);
                stateCopy.setPlayerAt(row, col - 1, player);
                stateCopy.setPlayerAt(row, col, player);
                stateCopy.setLastMove(new Location(row, col)); //מספר הביחרה שעשה
                stateCopy.setLastShapeNum(2); //מספר הביחרה שעשה
                allPossibleStates.add(stateCopy);
            }

            //boolean check3 = chackIfCanPlaceShapeAt(3, row, col);
            //System.out.println(">>>>>>>>>>>>>> check3("+row+","+col+")="+check3);
            if(chackIfCanPlaceShapeAt(3, row, col))
            {
                //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> 3");
                stateCopy = new State(state);
                int rowtmp = row - 1;
                int coltmp = col;
                int rowtmpSec = row;
                int coltmpSec = col - 1;

                stateCopy.setPlayerAt(rowtmp, coltmp, player);
                stateCopy.setPlayerAt(rowtmpSec, coltmpSec, player);
                stateCopy.setPlayerAt(row, col, player);
                stateCopy.setLastMove(new Location(row, col)); //מספר הביחרה שעשה
                stateCopy.setLastShapeNum(3); //מספר הביחרה שעשה
                allPossibleStates.add(stateCopy);

            }

            if(chackIfCanPlaceShapeAt(4, row, col))
            {
                stateCopy = new State(state);
                int rowtmp = row + 1;
                int coltmp = col;
                int rowtmpSec = row;
                int coltmpSec = col - 1;

                stateCopy.setPlayerAt(rowtmp, coltmp, player);
                stateCopy.setPlayerAt(rowtmpSec, coltmpSec, player);
                stateCopy.setPlayerAt(row, col, player);
                stateCopy.setLastMove(new Location(row, col)); //מספר הביחרה שעשה
                stateCopy.setLastShapeNum(4); //מספר הביחרה שעשה
                allPossibleStates.add(stateCopy);
            }
        }

        return allPossibleStates;
    }

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
            if(col > 0 && rowtmp < Controller.BOARD_SIZE && coltmpSec < Controller.BOARD_SIZE)
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

    private State minimax(State state, double alpha, double beta, int depth, long time,long startTime, boolean isMax)
    {
        // תנאי עצירת הרקורסיה האם הגענו למצב סופי או לעומק רצוי
        if(isTerminal(state) || depth == 0 || time <= 0)
        {
//            System.out.println("\n\n depth level"+depth);
            System.out.println("VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV");
            double evalDub = eval(state, currentPlayer);
            state.setScore(evalDub);
            System.out.println(state);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            return state;
        }

        ArrayList<State> possibleStates = getPossibleStates(state, isMax);
        State st, bestState = possibleStates.get(0);
        
            if(isMax) // MAX player
            {
                bestState.setScore(-2);
                for(int i = 0; i < possibleStates.size(); i++)
                {
                    long t2=System.currentTimeMillis()-startTime;
                    
                    st = minimax(possibleStates.get(i), alpha, beta, depth - 1, time-(t2),System.currentTimeMillis(),false);
                    if(st.getScore() > bestState.getScore())
                    {
                        bestState = possibleStates.get(i);
                        bestState.setScore(st.getScore());
                        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> score for State is" + st.getScore());
                    }
                    alpha = Math.max(alpha, bestState.getScore());
                    if(alpha >= beta)
                    {
                        break;
                    }
                }
            }
            else // MIN player
            {
                bestState.setScore(2);
                for(int i = 0; i < possibleStates.size(); i++)
                {
                    long t2=System.currentTimeMillis()-startTime;
                    st = minimax(possibleStates.get(i), alpha, beta, depth - 1,time-(t2),System.currentTimeMillis(), true);
                    if(st.getScore() < bestState.getScore())
                    {
                        bestState = possibleStates.get(i);
                        bestState.setScore(st.getScore());
                        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> score for State is" + st.getScore());
                    }

                    beta = Math.min(beta, bestState.getScore());
                    if(alpha >= beta)
                    {
                        break;
                    }
                }

            }

        return bestState;
    }

    public boolean isTerminal(State state)
    {
        if(state.isWinFor('X') || state.isWinFor('O') || state.isTie())
        {
            return true;
        }
        return false;
    }

    // האם הלוח במצב המטרה
    public ArrayList<Location> isWin()
    {
        char space = ' ';
        char[][] board = currentState.getBord();

        ArrayList<Location> winLine = new ArrayList<>();
        //בדיקת השורות
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
        {
            //גודל הרצץ הנוכחי
            int count = 0;
            //המספר של הרצף הנוכחי
            int rezefNum = -1;
            for(int col = 0; col < board[0].length; col++)
            {
                if(board[row][col] != space)
                {
                    //אם אנחנו בתא הראשון קיים רצף חדש
                    //והוא באורך של אחד
                    if(col == 0)
                    {
                        //מספר הרצף מופיע בינתיים פעם אחת
                        count = 1;
                        //הצבת המספר של הרצף
                        rezefNum = board[row][col];
                        //הוספת המקום לרשימת הניצחון
                        winLine.add(new Location(row, col));

                    } //בדיקה לשאר התאים
                    else //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    if(rezefNum == board[row][col] && board[row][col] == board[row][col - 1])
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
                            rezefNum = board[row][col];
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
        for(int col = 0; col < board[0].length; col++)
        {
            //גודל הרצץ הנוכחי
            int count = 0;
            //המספר של הרצף הנוכחי
            int rezefNum = -1;
            for(int row = 0; row < Controller.BOARD_SIZE; row++)
            {
                //אם אנחנו בתא הראשון קיים רצף חדש
                //והוא באורך של אחד
                if(board[row][col] != space)
                {
                    if(row == 0)
                    {
                        //מספר הרצף מופיע בינתיים פעם אחת
                        count = 1;
                        //הצבת המספר של הרצף
                        rezefNum = board[row][col];

                        //הוספת המקום לרשימת הניצחון
                        winLine.add(new Location(row, col));
                    } //בדיקה לשאר התאים
                    else //אם המספר הנוכחי תואם למספר הרצף
                    //והוא שווה לתא הקודם לו 
                    //זה מתאים לרצף אז
                    if(rezefNum == board[row][col] && board[row][col] == board[row - 1][col])
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
                            rezefNum = board[row][col];
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
        for(int i = 0; i < Controller.BOARD_SIZE; i++)
        {
            //אם אנחנו בתא הראשון קיים רצף חדש
            //והוא באורך של אחד
            if(board[i][i] != space)
            {
                if(i == 0)
                {
                    //מספר הרצף מופיע בינתיים פעם אחת
                    count = 1;
                    //הצבת המספר של הרצף
                    rezefNum = board[i][i];
                    //הוספת המקום לרשימת הניצחון
                    winLine.add(new Location(i, i));
                } //בדיקה לשאר התאים
                else //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if(rezefNum == board[i][i] && board[i][i] == board[i - 1][i - 1])
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
                        rezefNum = board[i][i];
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
        int col = Controller.BOARD_SIZE - 1;
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
        {
            if(board[row][col] != space)
            {
                //אם אנחנו בתא הראשון קיים רצף חדש
                //והוא באורך של אחד
                if(row == 0)
                {
                    //מספר הרצף מופיע בינתיים פעם אחת
                    count = 1;
                    //הצבת המספר של הרצף
                    rezefNum = board[row][col];
                    //הוספת המקום לרשימת הניצחון
                    winLine.add(new Location(row, col));
                } //בדיקה לשאר התאים
                else //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if(rezefNum == board[row][col] && board[row][col] == board[row - 1][col + 1])
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
                        rezefNum = board[row][row];
                        winLine.clear();
                        winLine.add(new Location(row, col));
                    }
                col--;
            }
        }

        //אם לא נמצא ערך מתאים  החז ר
        return null;
    }

    public boolean check(int[] a)
    {
        //גודל הרצץ הנוכחי
        int count = 0;
        //המספר של הרצף הנוכחי
        int rezefNum = -1;
        for(int i = 0; i < a.length; i++)
        {
            //אם אנחנו בתא הראשון קיים רצף חדש
            //והוא באורך של אחד
            if(i == 0)
            {
                //מספר הרצף מופיע בינתיים פעם אחת
                count = 1;
                //הצבת המספר של הרצף
                rezefNum = a[i];
            } //בדיקה לשאר התאים
            else //אם המספר הנוכחי תואם למספר הרצף
            //והוא שווה לתא הקודם לו 
            //זה מתאים לרצף אז
            if(rezefNum == a[i] && a[i] == a[i - 1])
                {
                    //נגדיל את גודל הרצף באחד
                    count++;
                    //ARRYLIST
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
                    rezefNum = a[i];
                }

        }
        //אם לא נמצא ערך מתאים  החזר שקר
        return false;
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
