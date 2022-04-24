import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Docs: this class is the Pic Behind the Button.
 * Date: 2018-2019 
 * Author: Lidor Malich
 */
public class MyPanel extends JPanel
{
    //תכונות
    private Image bgImage; //תמונת רקע
    
    /**
     * פעולה בונה ליצרת רקע מותאם אישית לפנאל
     * @param w רוחב
     * @param h אורך
     */
    public MyPanel(int w, int h) 
    {
        ImageIcon img = new ImageIcon(Client.class.getResource("resources/background.png"));
        img = new ImageIcon(img.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        bgImage = img.getImage();
    }
    
    /**
     * פעולה בונה ליצרת רקע מותאם אישית לפנאל
     * @param w רוחב
     * @param h אורך
     * @param background תמונת רקע
     */
    public MyPanel(int w, int h,String background) 
    {
        /*
        *יצירת רקע מותאם אישית מתמונה לפאנל
        */
        ImageIcon img = new ImageIcon(Client.class.getResource("resources/"+background+".png"));
        img = new ImageIcon(img.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        bgImage = img.getImage();
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g); 
        g.drawImage(bgImage, 0, 0, null);
        
    }
}

