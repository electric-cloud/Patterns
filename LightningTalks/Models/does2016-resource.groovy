def resList = ["does-dev-web1", "does-dev-db1", 
    "does-qa-web1", "does-qa-web2", "does-qa-web3",
    "does-qa-db1", "does-qa-db2"]


resList.each { resourceName -> 
	      resource resourceName, {
	      description = 'Sample resource for DOES2016.'
	      hostName = '127.0.0.1'
	      hostType = 'CONCURRENT'
	      port = '7800'
	      zoneName = 'default'
	      }
}