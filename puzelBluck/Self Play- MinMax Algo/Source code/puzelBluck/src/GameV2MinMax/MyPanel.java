package GameV2MinMax;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * נוצרה ע"י לידור מליח
 */
public class MyPanel extends JPanel
{
    private Image bgImage;
    
    public MyPanel(int w, int h) 
    {
        /*
        *יצירת רקע מותאם אישית מתמונה לפאנל
        */
        ImageIcon img = new ImageIcon(Controller.class.getResource("resources/background.png"));
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

