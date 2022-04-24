import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Document : DB - מחלקת שירות שקבלת חיבור למסד נתונים Date : 21/11/2018 Author
 */
public class DB {

    /**
     * הפעולה מחזירה חיבור למסד נתנונים רצוי
     *
     * @param dbPath הנתיב המלא למסד הנתונים עבורו רוצים חיבור
     * @return nullמחזיר חיבור אם קיים מסד נתונים ואין בעיות אחרת מחזיר
     */
    public static Connection getConnection(String dbPath) 
    {
        //FOR NetBeans
        String dbFilePath = DB.class.getResource(dbPath).getPath().replaceAll("%20", " ").replace("build/classes", "src");
        String dbURL = "jdbc:ucanaccess://" + dbFilePath;
        String dbUserName = "";
        String dbPassword = "";

        Connection con = null;
        try {
            con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
        
        //For Jar
//        String dbFilePath = new File("mydb.accdb").getPath();
//        
//        String dbURL = "jdbc:ucanaccess://" + dbFilePath;
//        String dbUserName = "";
//        String dbPassword = "";
//
//        Connection con = null;
//        try
//        {
//            con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
//        } catch(SQLException e) {Server.printStackTrace(e);}
//        return con;
    }
    

    /**
     * בדיקה - האם היוזר והסיסמא נמצאים במאגר הנתונים של המתמשים
     * @param un שם משתמש
     * @param pw סיסמא
     * @return אמת או שקר בהתאמה
     * @throws SQLException 
     */
    public static boolean isUserInDB(String un, String pw) throws SQLException  
    {
        Connection con=null;
        Statement st=null;
        ResultSet rs=null;

        con = DB.getConnection("/assets/mydb.accdb");
        st = con.createStatement();

        String sqlQuery = "select * from users where un='" + un + "' AND pw='" + pw + "'";
        System.out.println(sqlQuery);
        try {
            rs = st.executeQuery(sqlQuery);

            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
