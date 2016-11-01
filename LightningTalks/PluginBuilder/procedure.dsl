procedure 'Perform Checks Using MyScript', {

	step 'runChecks',
    	  command: new File(pluginDir, 'dsl/procedures/Perform Checks Using MyScript/myScript').text
}