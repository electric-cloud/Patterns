// application level rollback
processStep("application_rollback") {
    errorHandling='failProcedure'
    processName = "installAll"
    applicationTierName = null
    instruction = null
    rollbackSnapshot = null
    rollbackType = 'environment'
    processStepType = 'rollback'
    rollbackUndeployProcess='uninstallAll'    // undeploy application process
    smartRollback = '1'
    actualParameter  'releaseVersion', '$' + '[releaseVersion]'
    actualParameter  'stage', '$' + '[stage]'
}       

processDependency ("previous_step") {
    targetProcessStepName= "application_rollback"
    branchType = "ALWAYS"
    // Rollback only when error
    // and not already in a rollback
    branchCondition = '''("$' + '[/myJob/outcome]" == "error") && 
                      ("$' + '[/myJob/jobType]" != "rollback")'''
    branchConditionName =  "app_rollback"
    branchConditionType = 'CUSTOM'                
}
