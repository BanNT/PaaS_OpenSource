#!/bin/bash

# Install script for Latest WordPress by Johnathan Williamson - extended by Don Gilbert
# Disclaimer: It might not bloody work
# Disclaimer 2: I'm not responsible for any screwups ... :)

# DB Variables

mysqlhost="localhost"

mysqldb="wordpress"
mysqluser="wordpressuser"
mysqlpass="123456"
wptitle="Your wordpress site"
wpuser="admin"
wppass="nucewpadmin"
wpemail="thanhbanat@gmail.com"
siteurl="172.16.69.176"

# Download latest WordPress and uncompress
wget http://wordpress.org/latest.tar.gz
tar zxf latest.tar.gz
sudo apt-get update
sudo apt-get install php5-gd libssh2-php
# Grab our Salt Keys
wget -O /tmp/wp.keys https://api.wordpress.org/secret-key/1.1/salt/

# Butcher our wp-config.php file
sed -e "s/localhost/"$mysqlhost"/" -e "s/database_name_here/"$mysqldb"/" -e "s/username_here/"$mysqluser"/" -e "s/password_here/"$mysqlpass"/" wp-config-sample.php > wp-config.php
sed -i '/#@-/r /tmp/wp.keys' wp-config.php
sed -i "/#@+/,/#@-/d" wp-config.php
sudo rsync -avP ~/wordpress/ /var/www/html/
cd /var/www/html
sudo chown -R ubuntu:www-data *
mkdir /var/www/html/wp-content/uploads
sudo chown -R :www-data /var/www/html/wp-content/uploads
# Run our install ...
#wget --post-data "weblog_title=$wptitle&user_name=$wpuser&admin_password=$wppass&admin_password2=$wppass&admin_email=$wpemail" http://$siteurl/wp-admin/install.php?step=2

# Tidy up
#rmdir wordpress
#rm latest.tar.gz
#rm /tmp/wp.keys
