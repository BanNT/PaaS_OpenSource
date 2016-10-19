# PaaS_OpenSource
Nền tảng đám mây mã nguồn mở, hỗ trợ cấu hình phần mềm như một dịch vụ trên các IaaS khác nhau.

1. Mô tả dự án:
Đối tượng sử dụng: 
Các nhà phát triển phần mềm, các lập trình viên, sinh viên muốn tự xây dựng nền tảng đám mây, triển khai triên hạ tầng IaaS tại một hoặc nhiều node. Mục đích tạo private và public cloud cho tổ chức doanh nghiệp hoặc nghiên cứu.
Tầng PaaS:
-	Code lập trình với Java Server Face Framework
-	Tool sử dụng Netbean, phiên bản Java EE: https://netbeans.org/downloads/
-	Thư viện sử dụng: JDK 8, Java Secure Channel (jsch-0.1.54.jar), mysql-connector-java-5.1.39.jar…
-	Webserver: Apache Tomcat 7.
Tầng SaaS:
-	Thử nghiệm triển khai trên hạ tầng IaaS cài đặt OpenStack.
-	Sử dụng OpenStack Python APIs: http://docs.openstack.org/user-guide/sdk.html
-	Cơ sở dữ liệu: MySQL
2. Hướng dẫn cài đặt và triển khai:
a. Tầng IaaS:
-	Triển khai trên 1 hoặc nhiều node. Cài đặt OpenStack sử dụng Devstack, tham khảo hướng dẫn cài đặt tại: https://github.com/vietstacker/devstack-note
-	Đăng nhập vào Dashboard của OpenStack và cấu hình các thành phần sau:
Cấu hình network gồm:
-	 Public: cung cấp dải địa chỉ IP cho các instances kết nối ra môi trường internet cho các máy ảo, thông số cấu hình như sau:
o	Subnet name: public-subnet
o	Network Address: 172.16.69.0/24
o	Gateway IP: 172.16.69.1
o	Allocation Pools: 172.16.69.171, 172.16.69.179
-	 Internal-network: cung cấp dải ip tĩnh cho các instances liên lạc nội bộ với nhau, thông số cấu hình như sau:
o	Subnet name: sub-internal
o	Network Address: 192.168.10.0/24
o	Gateway IP: 192.168.10.1
o	Allocation Pools: 192.168.10.100, 192.168.10.200
o	DNS Name Servers: 8.8.8.8
Cấu hình router:
Bộ định tuyến/thiết bị định tuyến, giúp chia sẻ internet từ các internal-network, thông số cấu hình:
o	Tên router: Router_nuce
o	Interfaces: router_interface (192.168.10.1), router_gateway (172.16.69.172)
 
Khởi tạo các Flavors và upload các Images hệ điều hành.
Cấp quyền cho các instances:
-	 Cấp quyền cho phép SSH bằng port 22, giúp người dùng đăng nhập vào hệ điều hành máy ảo bằng keypair sau khi khởi tạo.
-	 Quyền cho phép ping tới địa chỉ ip máy ảo
Chi tiết sử dụng dashboard tham khảo tại: http://docs.openstack.org/user-guide/dashboard.html
- Thiết lập tài khoản FTP cho ubuntu server, tham khảo tại: http://www.krizna.com/ubuntu/setup-ftp-server-on-ubuntu-14-04-vsftpd/
b. Triển khai PaaS trên hạ tầng IaaS đã cài đặt
Sau khi lập trình build ứng dụng chúng ta được gói .war
- Cài đặt JDK và Apache Tomcat 7 trên ubuntu server
- Upload gói war vào thư mục /var/bin/tomcat7/webapps/ (sử dụng FTP để upload file)
- Restart Tomcat, dùng lệnh: service tomcat7 restart
Truy cập ứng dụng tại địa chỉ: http://ip-address:port/Nucestack
Một vài lưu ý:
- Gọi và thực thi python file (credentials.py) để khởi tạo máy ảo:
Sử dụng phương thức createVMUbuntu() tại file PlatformBean.java trong project
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
- Triệu gọi shell script (wp-shell-install.sh) cài đặt cms wordpress sau khi khởi tạo máy ảo thành công:
Sử dụng phương thức installWP(String ip) tại file PlatformBean.java
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
- 2  file credentials.py, init.py đặt tại máy chủ cài đặt OpenStack, wp-shell-install.sh đặt tại một máy ảo đã được khởi tạo thành công, dùng để tạo snapshot sử dụng dashboard.
