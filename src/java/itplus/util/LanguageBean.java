/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package itplus.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author BanNT
 */
@ManagedBean(name = "language")
@SessionScoped
public class LanguageBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String localeCode;
    public static String check = "vi";
    private static Map<String, Object> countries;
    
    static {
        countries = new HashMap<String, Object>();        
        countries.put("English", Locale.ENGLISH); //label
        countries.put("VietNam", "vi");        
    }
    
    public Map<String, Object> getCountriesInMap() {
        return countries;
    }
    
    public String getLocaleCode() {
        return localeCode;
    }
    
    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    //value change event listener
    public void countryLocaleCodeChanged(ValueChangeEvent e) {
        
        String newLocaleValue = e.getNewValue().toString();
        check = newLocaleValue;
        //loop country map to compare the locale code
//        for (Map.Entry<String, Object> entry : countries.entrySet()) {
//            if (entry.getValue().toString().equals(newLocaleValue)) {
                //thay doi locale
                FacesContext.getCurrentInstance()
                        .getViewRoot().setLocale(new Locale(newLocaleValue));                
//            }
//        }
    }
    
    public LanguageBean() {
    }
}
