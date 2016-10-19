/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package itplus.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author BanNT
 */
public class ValidatorUtil {

    public static boolean isEmail(String Value) {
        Pattern pattern = Pattern.compile(
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");        
        Matcher matcher = pattern.matcher(Value);
        return matcher.matches();
    }
}
