#!/usr/bin/env python
import os
import time
import novaclient.v1_1.client as nvclient
from init import get_nova_creds
try:
	creds = get_nova_creds()
	nova = nvclient.Client(**creds)
	image = nova.images.find(name="cirros")
	flavor = nova.flavors.find(name="m1.tiny")
	#nova.networks.list()
	network = nova.networks.find(label="Internal_network")
	nics = [{'net-id': network.id}]
	#keypair
	keypair_name = "MyVM_Keypair"
	keypair = nova.keypairs.create(name=keypair_name)
	instance = nova.servers.create(name="MyVM", image=image, flavor=flavor, key_name=keypair_name,nics=nics)
 
# Poll at 5 second intervals, until the status is no longer 'BUILD'
	#status = instance.status
	#while status == 'BUILD':
    	#	time.sleep(5)
    # Retrieve the instance again so the status field updates
    	#	instance = nova.servers.get(instance.id)
    	#	status = instance.status
	#print "status: %s" % status
	time.sleep(15)
	#print("list of VMs")
	#print(nova.servers.list())
	#floating ip
	nova.floating_ips.list()
        floating_ip = nova.floating_ips.create()
        instance = nova.servers.find(name="MyVM")
        instance.add_floating_ip(floating_ip)

finally:
	print(floating_ip)
	print(keypair.private_key)
	print("execution completed")
