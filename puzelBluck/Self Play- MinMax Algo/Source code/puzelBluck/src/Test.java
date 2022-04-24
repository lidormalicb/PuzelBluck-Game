

/**
 * Document : Test Created on : 30/04/2018, 14:06:10 Author : G47
 */
public class Test
{
    public static void main(String[] args)
    {
        int[] a1 =
        {
            1, 1, 1, 1, 1, 0, 1, 1
        };
        int[] a2 =
        {
            0, 1, 1, 1, 1, 1, 1, 0
        };
        int[] a3 =
        {
            2, 2, 1, 1, 1, 1, 1, 2
        };
        int[] a4 =
        {
            2, 1, 1, 1, 1, 1, 0, 1
        };
        int[] a5 =
        {
            2, 1, 1, 0, 1, 1, 1, 0
        };
        int[] a6 =
        {
            2, 2, 1, 1, 0, 1, 1, 2
        };
        int[] a7 =
        {
            2, 2, 1, 1, 2, 1, 1, 1
        };
        int[] a8 =
        {
            1, 1, 1, 1, 1, 1, 1, 1
        };
        int[] a9 =
        {
            1, 1, 1, 1, 1, 1, 2, 0
        };
        int[] a10 =
        {
            1, 1, 1, 1, 1, 0, 2, 0
        };
                int[] a11 =
        {
            0,-1, 1, 1, 1, 1, 1, 1
        };

        System.out.println(check(a1));  // false
        System.out.println(check(a2));  // true
        System.out.println(check(a3));  // false
        System.out.println(check(a4));  // false
        System.out.println(check(a5));  // false
        System.out.println(check(a6));  // false
        System.out.println(check(a7));  // false
        System.out.println(check(a8));  // true
        System.out.println(check(a9));  // true
        System.out.println(check(a10));  // false
        System.out.println(check(a11));  // true
    }


    public static boolean check(int[] a)
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
                count=1;
                //הצבת המספר של הרצף
                rezefNum = a[i];
            }
            //בדיקה לשאר התאים
            else 
            {
                //אם המספר הנוכחי תואם למספר הרצף
                //והוא שווה לתא הקודם לו 
                //זה מתאים לרצף אז
                if(rezefNum == a[i] && a[i]== a[i-1])
            {
                //נגדיל את גודל הרצף באחד
                count++;
                //ARRYLIST
                //אם גודל הרצף הגיע לשש מצוין החזר אמת
                if(count == 6)
                    return true;
            }
                //אם קיים מספר שאיננו תואם למספר של הרצף
                //נתחיל לספור רצף חדש
                //ואורכו יהיה אחד בהתחלה
                else
                {
                    count = 1;
                    rezefNum = a[i];
                }
            }
            

        }
        //אם לא נמצא ערך מתאים  החזר שקר
        return false;
    }
}
