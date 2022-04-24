import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class ClientData 
{
    //תכונות
    private String un;
    private Socket socket;
    private ClientData partner;
    private ObjectInputStream is;
    private ObjectOutputStream os;

    /**
     * פעולה בונה
     * @param socket סוקט של הקליינט
     */
     public ClientData(Socket socket) 
     {
        this.un = null;
        this.partner = null;
        this.socket = socket;
        try 
        {
            this.os = new ObjectOutputStream(socket.getOutputStream());
            this.is = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }

    public String getUserName() 
    {
        return un;
    }
    
    public void setUserName(String un) {
        this.un = un;
    }
    
    public ClientData getPartner()
    {
        return partner;
    }

    public void setPartner(ClientData c)
    {
        this.partner = c;
    }

    @Override
    public String toString() 
    {
        return "ClientData {" + this.un +"}";
    }
    
    /**
     * כתיבת אובייקט אל הלקוח
     * @param obj אובייקט לכתיבה
     */
    public void writeObject(Object obj) 
    {
        try {
            os.writeObject(obj);
            os.flush();
        } catch (Exception ex) {
            Server.printStackTrace(ex);
        }
        
    }
     
    /**
     * קריאת אובייקט מהלקוח
     * @return החזרת אובייקט
     */
    public Object readObject() 
    {
        Object obj = null;
         try {
            obj = is.readObject();
        } catch (Exception ex) {
           Server.printStackTrace(ex);
        }
        return obj;
    }
    
    /**
     * יציאה בטוחה כאשר השרת שולח הודעת יציאה
     */
    public void exit()
    {
        try 
        { 
            is.close();
            os.close();
            socket.close();
        } catch (IOException ex) {
//            System.out.println("Dataclient exit()");
            Server.printStackTrace(ex);
        }
    }
}
