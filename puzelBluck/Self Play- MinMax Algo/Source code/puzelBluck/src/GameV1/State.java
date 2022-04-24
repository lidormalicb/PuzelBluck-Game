package GameV1;

import java.util.ArrayList;

/**
 * Docs: State class is the bord state.
 *
 * Author: Lidor Malich (lidormalich@gmail.com)
 */
public class State
{
    private Double score;
    private Location lastMove;
    private char[][] bord;

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

    // copy constructor
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

    // return status
    // update state-score & state-lastMove
    public void setPlayerAt(int row, int col, char player)
    {
        bord[row][col] = player;

        // record the last move
        lastMove = new Location(row, col);
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

    public char[][] getBord()
    {
        return bord;
    }

    public void setScore(double score)
    {
        this.score = score;
    }
    
    public char getPlayerAt(int row,int col)
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
        boolean check;

        // בדיקה האם יש ניצחון באחת מהשורות
        for(int row = 0; row < Controller.BOARD_SIZE; row++)
        {
            check = true;
            for(int col = 0; col < Controller.BOARD_SIZE; col++)
            {
                if(bord[row][col] != player)
                {
                    check = false;
                    break;
                }
            }
            if(check == true) //System.out.println("win for " + player + " on row #" +row);
            {
                return true;
            }
        }

        // בדיקה האם יש ניצחון באחת מהעמודות
        for(int col = 0; col < Controller.BOARD_SIZE; col++)
        {
            check = true;
            for(int row = 0; row < Controller.BOARD_SIZE; row++)
            {
                if(bord[row][col] != player)
                {
                    check = false;
                    break;
                }
            }
            if(check == true) //System.out.println("win for " + player + " on col #" +col);
            {
                return true;
            }
        }

        // בדיקה האם יש ניצחון באלכסון הראשי
        check = true;
        for(int i = 0; i < Controller.BOARD_SIZE; i++)
        {
            if(bord[i][i] != player)
            {
                check = false;
                break;
            }
        }
        if(check == true) //System.out.println("win for " + player + " on main diagonal");
        {
            return true;
        }

        // בדיקה האם יש ניצחון באלכסון המשני
        check = true;
        for(int i = 0; i < Controller.BOARD_SIZE; i++)
        {
            if(bord[i][Controller.BOARD_SIZE - i - 1] != player)
            {
                check = false;
                break;
            }
        }
        if(check == true)
        {
            return true;
        }

        //אם הגענו לכאן אז אין ניצחון
        return false;
    }

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

    // get em 
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
        str += ", Last Move: " + lastMove + "\n";
        return str;
    }
}
