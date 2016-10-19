/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vtca.connectionpool;

/**
 *
 * @author BanNT
 */
public class Logger {

    private String classname;

    public Logger(String classname) {
        this.classname = classname;
    }

    public void log(String msg) {
        System.out.println("[" + this.classname + "] " + msg);
    }

    public void error(String msg) {
        System.err.println("[ERROR] [" + this.classname + "] " + msg);
    }
}
