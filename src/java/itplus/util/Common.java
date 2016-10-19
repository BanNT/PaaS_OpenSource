/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package itplus.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author BanNT
 */
public class Common {

    public static String generateSessionKey(int length) {
        String alphabet
                = new String("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11

        for (int i = 0; i < length; i++) //12
        {
            result = result + alphabet.charAt(r.nextInt(n)); //13
        }
        return result;
    }
//get sql current date
    public static Date currentDate() {
        Date date = new Date(Calendar.getInstance().getTimeInMillis());
        return date;
    }

}
