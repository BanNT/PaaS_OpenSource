#!/usr/bin/env python
import os
 
def get_keystone_creds():
    d = {}
    d['username'] = os.environ['OS_USERNAME']
    d['password'] = os.environ['OS_PASSWORD']
    d['auth_url'] = os.environ['OS_AUTH_URL']
    d['tenant_name'] = os.environ['OS_TENANT_NAME']
    return d
 
def get_nova_creds():
    d = {}
    d['username'] = "admin"
    d['api_key'] = "nucestack"
    d['auth_url'] = "http://172.16.69.128:5000/v2.0"
    d['project_id'] = "admin"
    return d
