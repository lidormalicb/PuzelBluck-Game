import java.io.Serializable;

public class Location implements Serializable
{
    //תכונות
    private int row;    //שורה על הלוח
    private int col;    //עמודה על הלוח

    /**
     * פעולה בונה למחלקה
     * @param row שורה
     * @param col  עמודה
     */
    public Location(int row, int col)
    {
        this.row = row;
        this.col = col;
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
    public String toString()
    {
        return "location{" + "row=" + row + ", col=" + col + '}';
    }

    public void setCol(int col)
    {
        this.col = col;
    }
}