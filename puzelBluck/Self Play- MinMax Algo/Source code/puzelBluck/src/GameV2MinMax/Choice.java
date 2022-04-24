package GameV2MinMax;

/**
 * Docs: Controller class is the game manager. 
 * Date: 2/11/2017 
 * Author: Lidor Malich
 */
public class Choice
{
    private int row;
    private int col;
    private int choice;
    
    
    public Choice(int row, int col ,int choice)
    {
        this.row = row;
        this.col = col;
        this.choice = choice;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public int getRow()
    {
        return row;
    }

    public void setRow(int row)
    {
        this.row = row;
    }

    public int getCol()
    {
        return col;
    }

    @Override
    public String toString() {
        return "Choice{" + "row=" + row + ", col=" + col + ", choice=" + choice + '}';
    }

    public void setCol(int col)
    {
        this.col = col;
    }
    
    
    

}
