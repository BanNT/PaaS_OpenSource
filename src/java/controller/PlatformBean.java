/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import entity.Config;
import entity.Hosting;
import itplus.util.Common;
import itplus.util.MessageUtil;
import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.PlatformModel;

/**
 *
 * @author BanNT
 */
public class PlatformBean {

    private Config config;
    private PlatformModel pm;
    private int userid;
    Session session = null;
    boolean check = true;

    public PlatformBean() {
        config = new Config();
        pm = new PlatformModel();
    }
    //check status manager hosting
    private boolean checkManager = false;

    public String selectPlatform(String platform) {
        //add platform into session
        System.out.println("platform: " + platform);
        HttpSession session = SessionBean.newSession(false);
        if (session != null) {
            SessionBean sb = SessionBean.getInstance();
            Config config = sb.mConfig();
            config.setPlatform(platform);
            session.setAttribute("config", config);
        }
        return "appcartjava?faces-redirect=true";
    }
//back action applicationtype

    public String back() {
        return "applicationtype?faces-redirect=true";
    }

    public String backAppJava() {
        return "appcartjava?faces-redirect=true";
    }

    public String backToAppreconf() {
        return "apppreconfig?faces-redirect=true";
    }

    public String backtoDashboard() {
        return "dashboard?faces-redirect=true";
    }
//next action

    public String nextToAppreconfig() {
        //lay ve cac thong tin tren giao dien day vao session
        putToSession();
        return "apppreconfig?faces-redirect=true";
    }

    //next to myhosting
    public String managerHosting() {
        return "myhosting?faces-redirect=true";
    }

//add mysql
    public String addMysql() {
        HttpSession session = SessionBean.newSession(false);
        if (session != null) {
            SessionBean sb = SessionBean.getInstance();
            Config config = sb.mConfig();
            config.setDatabase("MySQL 5.6");
            config.setDb_name(config.getUrl());
            config.setDb_username(config.getUrl() + "_2016");
            config.setDb_pass(config.getUrl() + "_2016_" + Common.generateSessionKey(5));
            session.setAttribute("config", config);
        }
        return "apppreconfig?faces-redirect=true";
    }

    //add phpmyadmin
    public void addPhpmyadmin() {
        HttpSession session = SessionBean.newSession(false);
        if (session != null) {
            SessionBean sb = SessionBean.getInstance();
            Config config = sb.mConfig();
            config.setTools("phpMyAmin 4.6.4");
            session.setAttribute("config", config);
        }
    }

    public void putToSession() {
        HttpSession session = SessionBean.newSession(false);
        if (session != null) {
            SessionBean sb = SessionBean.getInstance();
            Config sconfig = sb.mConfig();
            sconfig.setGears(this.config.getGears());
            sconfig.setScale(this.config.getScale());
            sconfig.setUrl(this.config.getUrl());
            session.setAttribute("config", sconfig);
        }
    }

    //create platform
    public void createVMUbuntu() throws IOException {
        //execute source openrc
//        openRC();
        long startTime, elapsedTime;
        try {

            System.out.println("Call create VM");
// set up the command and parameter
//            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            String resHomeImgPath = "/home/stack/devstack/credentials.py";

            String[] cmd = new String[2];
            cmd[0] = "python"; // check version of installed python: python -V
            cmd[1] = resHomeImgPath;
            //lay ve thoi gian he thong
            startTime = System.nanoTime();
// create runtime to execute external command
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(cmd);
//            pr.waitFor();
// retrieve output from python script
            BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line = "";
            String value = "";
            while ((value = bfr.readLine()) != null) {
// display each output line form python script
                line += value;
            }
            System.out.println("Ket qua tra ve: " + line);
            //save config
            //     savingConfig();
            //get floating ip
//            String str = "<Floating fixed=none, id=sdfasf, instan=none, ip=192.168.1.3, pool=abd>begin rsa.......";
            String[] values = line.split(">");
            String[] floats_ip = values[0].split(",");
            String floating_ip = floats_ip[3].substring(4);
            /*
            String keypair = values[1];
            Hosting hosting = new Hosting();
            hosting.setExternal_ip(floating_ip);
            hosting.setKeypair(keypair);
            hosting.setUserid(userid); //lay ket qua tra ve tu python, luu vao bang tblHosting: dia chi ip va key pair
            //Luu cac thong tin vao csdl
            SessionBean sb = SessionBean.getInstance();
            String url = sb.mConfig().getUrl();
            String vmname = url + "_VM_" + Common.currentDate();
            hosting.setHostingname(vmname);
            pm.saveHosting(hosting);
             */

            System.out.println("dia chi ip: " + floating_ip);

            System.out.println("da chay qua");
            //lay ve thoi gian sau khi khoi tao thanh công
            elapsedTime = System.nanoTime() - startTime;
            System.out.println("time create VM: " + (elapsedTime / 1000000.0) + " msec");
            //thong bao thanh cong
            MessageUtil.addSuccessMessage("Your hosting created!");
            MessageUtil.addSuccessMessage("IP Address:" + floating_ip);
            //enable button manager
            checkManager = true;
            installWP(floating_ip);
        } catch (Exception ex) {
            System.out.println("Loi: " + ex.getMessage());
        }

    }

    public void installWP(String ip) throws IOException, Exception {
        JSch jsch = new JSch();
        String privateKeyPath = "/home/stack/devstack/ubuntu_prk.ppk";
        try {
            System.out.println("Ket noi den VM:" + ip);
            jsch.addIdentity(privateKeyPath);
            session = jsch.getSession("ubuntu", ip, 22);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

//            String command = "echo \"this is text called from java programming\" >> /home/ubuntu/test.out";
            String command = "/home/ubuntu/wp-shell-install.sh";
            //execute script
            Thread t1 = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(10000);
                        while (check) {
                            try {
                                session.connect();//neu ket noi thanh cong
                                check = false;
                            } catch (Exception e) {
                                System.out.println("Loi ket noi: " + e.getMessage());
                                Thread.sleep(10000);
                            }
                        }
                        System.out.println("Login success");
                        Channel channel = session.openChannel("exec");
                        ((ChannelExec) channel).setCommand("sh " + command);
                        ((ChannelExec) channel).setPty(false);
                        InputStream in = channel.getInputStream();
                        channel.connect();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        //Read each line from the buffered reader and add it to result list
                        String result = "";
                        // You can also simple print the result here
                        while ((line = reader.readLine()) != null) {

                            result += line;
                        }

                        channel.disconnect();
                        session.disconnect();
                        System.out.println("Success");
                        System.out.println("Ket qua: " + result);
                        MessageUtil.addSuccessMessage("Install Wordpress Success!");
                    } catch (Exception ex) {
                        MessageUtil.addErrorMessage(ex);
                    }
                }
            });
            t1.start();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void downloadToClient(String filekey) throws IOException {
//        String keyurl = "http://localhost:8089/keypair/" + filekey;
        String keyurl = "http://172.16.69.131:8080/keypair/" + filekey;
        // Get the FacesContext
        FacesContext facesContext = FacesContext.getCurrentInstance();

        // Get HTTP response
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        // Set response headers
        response.reset();   // Reset the response in the first place
        ServletContext servletContext = (ServletContext) FacesContext
                .getCurrentInstance().getExternalContext().getContext();
        response.setHeader("Content-Type", servletContext.getMimeType(keyurl));  // Set only the content type
        response.setHeader("Content-Disposition", "attachment; filename=" + filekey + "");
        // Open response output stream
        OutputStream responseOutputStream = response.getOutputStream();

        // Read PDF contents
        URL url = new URL(keyurl);
        InputStream pdfInputStream = url.openStream();

        // Read PDF contents and write them to the output
        byte[] bytesBuffer = new byte[10];
        int bytesRead;
        while ((bytesRead = pdfInputStream.read(bytesBuffer)) > 0) {
            responseOutputStream.write(bytesBuffer, 0, bytesRead);
        }

        // Make sure that everything is out
        responseOutputStream.flush();

        // Close both streams
        pdfInputStream.close();
        responseOutputStream.close();

        // JSF doc: 
        // Signal the JavaServer Faces implementation that the HTTP response for this request has already been generated 
        // (such as an HTTP redirect), and that the request processing lifecycle should be terminated
        // as soon as the current phase is completed.
        facesContext.responseComplete();

    }

    public void savingConfig() {
        //luu thong tin vao bang config
        SessionBean sb = SessionBean.getInstance();
        Config sconfig = sb.mConfig();
        userid = sb.userid();
        sconfig.setUserid(userid);
        try {
            pm.saveConfig(sconfig);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void downloadKeyPair() {
        try {
            /* tam thoi comment lai de test tren ubuntu toi keypair co san
            SessionBean sb = SessionBean.getInstance();
            Config sconfig = sb.mConfig();
            userid = sb.userid();
            Hosting hosting = pm.getKeypair(userid);
            String keypair = hosting.getKeypair();
            //luu xuong file va gui ve cho client
            String folder = System.getProperty("user.dir");
            folder = folder.substring(0, folder.length() - 3) + "webapps\\keypair\\";
            String hostingname = hosting.getHostingname();
            String filekey = hostingname + ".pem";
            File file = new File(folder + filekey);
// if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(keypair);
            bw.flush();
            bw.close();
             */
            //download
            //test voi keypair mac dinh mykeypair.pem
            String filekey = "mykeypair.pem";
            downloadToClient(filekey);
            MessageUtil.addSuccessMessage("Download success.");
            System.out.println("Done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    public void openRC() {
//        try {
//            String[] env = {"PATH=/bin:/usr/bin/"};
//            String cmd = "initvm.sh";
//            Process process = Runtime.getRuntime().exec(cmd, env);
//        } catch (Exception ex) {
//            System.out.println("Loi: " + ex.getMessage());
//        }
//    }
//
//    public void readFile() {
//        try {
//            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//            String resHomeImgPath = servletContext.getRealPath("resources/files/content.txt");
//            System.out.println(resHomeImgPath);
//            File fl = new File(resHomeImgPath);
//            if (fl.exists()) {
//                FileInputStream fi = new FileInputStream(fl);
//                Scanner scn = new Scanner(fi);
//                while (scn.hasNextLine()) {
//                    System.out.println(scn.nextLine());
//                }
//                scn.close();
//                fi.close();
//            } else {
//                System.out.println("File khong ton tai");
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//
//    }
//    private String executeCommand(String command) {
//
//        StringBuffer output = new StringBuffer();
//
//        Process p;
//        try {
//            p = Runtime.getRuntime().exec(command);
//            p.waitFor();
//            BufferedReader reader
//                    = new BufferedReader(new InputStreamReader(p.getInputStream()));
//
//            String line = "";
//            while ((line = reader.readLine()) != null) {
//                output.append(line + "\n");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return output.toString();
//
//    }
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public boolean isCheckManager() {
        return checkManager;
    }

    public void setCheckManager(boolean checkManager) {
        this.checkManager = checkManager;
    }

}
