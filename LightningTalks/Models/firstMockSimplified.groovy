def myproj = "DOES2016-2"

project myproj, {
  description = 'DOES2016 Patterns example on modeling your processes'

  procedure 'Stub', {
    description = 'This is a stubbed procedure for a placeholder in the sample application.'
    timeLimitUnits = 'minutes'

    step 'Echo Hello World', {
      description = ''
      command = 'echo "Hello World"'
      errorHandling = 'failProcedure'
      exclusiveMode = 'none'
      releaseMode = 'none'
      timeLimitUnits = 'minutes'
    }
  }

  application 'My First Application', {
    description = '''My First Application, generated from the UX
'''

    applicationTier 'Web application Tier', {
      description = 'A tier to hold the web components'
      applicationName = 'My First Application'
      projectName = myproj

      component 'Web part', pluginName: null, {
        applicationName = 'My First Application'
        pluginKey = 'EC-Artifact'
        reference = '0'
        sourceComponentName = null
        sourceProjectName = null

        process 'Deploy web part', {
          processType = 'DEPLOY'

          processStep 'Get the artifact', {
            applicationTierName = null
            dependencyJoinType = 'and'
            errorHandling = 'failProcedure'
            instruction = null
            notificationTemplate = null
            processStepType = 'component'
            rollbackSnapshot = null
            rollbackType = null
            rollbackUndeployProcess = null
            smartRollback = null
            subcomponent = null
            subcomponentApplicationName = null
            subcomponentProcess = null
            subprocedure = 'Retrieve'
            subproject = '/plugins/EC-Artifact/project'
            timeLimitUnits = null
            workspaceName = null
            actualParameter 'artifactName', '$[/myComponent/ec_content_details/artifactName]'
            actualParameter 'artifactVersionLocationProperty', '$[/myComponent/ec_content_details/artifactVersionLocationProperty]'
            actualParameter 'filterList', '$[/myComponent/ec_content_details/filterList]'
            actualParameter 'overwrite', '$[/myComponent/ec_content_details/overwrite]'
            actualParameter 'retrieveToDirectory', '$[/myComponent/ec_content_details/retrieveToDirectory]'
            actualParameter 'versionRange', '$[/myJob/ec_Web part-version]'
          }

          processStep 'Deploy the Web Part', {
            applicationTierName = null
            dependencyJoinType = 'and'
            errorHandling = 'failProcedure'
            instruction = null
            notificationTemplate = null
            processStepType = 'procedure'
            rollbackSnapshot = null
            rollbackType = null
            rollbackUndeployProcess = null
            smartRollback = null
            subcomponent = null
            subcomponentApplicationName = null
            subcomponentProcess = null
            subprocedure = 'Stub'
            subproject = myproj
            timeLimitUnits = null
            workspaceName = null
          }

          processDependency 'Get the artifact', targetProcessStepName: 'Deploy the Web Part', {
            branchCondition = null
            branchConditionName = null
            branchConditionType = null
            branchType = 'ALWAYS'
          }
        }

        // Custom properties

        property 'ec_content_details', {

          // Custom properties
          artifactName = 'com.ec:storefront'
          artifactVersionLocationProperty = '/myJob/retrievedArtifactVersions/$[assignedResourceName]'
          filterList = ''
          overwrite = 'update'
          pluginProcedure = 'Retrieve'
          pluginProjectName = 'EC-Artifact'
          retrieveToDirectory = ''
          versionRange = ''
        }
      }
    }

    applicationTier 'Database Application Tier', {
      description = '''A tier to hold the database components in your application.
'''
      applicationName = 'My First Application'
      projectName = myproj

      component 'Database Schema', pluginName: null, {
        description = 'A sample schema, perhaps consisting of a set of SQL commands'
        applicationName = 'My First Application'
        pluginKey = 'EC-Artifact'
        reference = '0'
        sourceComponentName = null
        sourceProjectName = null

        process 'Deploy Database Schema', {
          applicationName = null
          processType = 'DEPLOY'
          timeLimitUnits = null
          workspaceName = null

          processStep 'Acquire component', {
            applicationTierName = null
            dependencyJoinType = 'and'
            errorHandling = 'failProcedure'
            instruction = null
            notificationTemplate = null
            processStepType = 'component'
            rollbackSnapshot = null
            rollbackType = null
            rollbackUndeployProcess = null
            smartRollback = null
            subcomponent = null
            subcomponentApplicationName = null
            subcomponentProcess = null
            subprocedure = 'Retrieve'
            subproject = '/plugins/EC-Artifact/project'
            timeLimitUnits = null
            workspaceName = null
            actualParameter 'artifactName', '$[/myComponent/ec_content_details/artifactName]'
            actualParameter 'artifactVersionLocationProperty', '$[/myComponent/ec_content_details/artifactVersionLocationProperty]'
            actualParameter 'filterList', '$[/myComponent/ec_content_details/filterList]'
            actualParameter 'overwrite', '$[/myComponent/ec_content_details/overwrite]'
            actualParameter 'retrieveToDirectory', '$[/myComponent/ec_content_details/retrieveToDirectory]'
            actualParameter 'versionRange', '$[/myJob/ec_Database Schema-version]'
          }

          processStep 'Deploy DB schema', {
            description = 'This could be a set of CLI SQL commands.'
            applicationTierName = null
            dependencyJoinType = 'and'
            errorHandling = 'failProcedure'
            instruction = null
            notificationTemplate = null
            processStepType = 'procedure'
            rollbackSnapshot = null
            rollbackType = null
            rollbackUndeployProcess = null
            smartRollback = null
            subcomponent = null
            subcomponentApplicationName = null
            subcomponentProcess = null
            subprocedure = 'Stub'
            subproject = myproj
            timeLimitUnits = null
            workspaceName = null
          }

          processDependency 'Acquire component', targetProcessStepName: 'Deploy DB schema', {
            branchCondition = null
            branchConditionName = null
            branchConditionType = null
            branchType = 'ALWAYS'
          }
        }

        // Custom properties

        property 'ec_content_details', {

          // Custom properties
          artifactName = 'com.ec:schema'
          artifactVersionLocationProperty = '/myJob/retrievedArtifactVersions/$[assignedResourceName]'
          filterList = ''
          overwrite = 'update'
          pluginProcedure = 'Retrieve'
          pluginProjectName = 'EC-Artifact'
          retrieveToDirectory = ''
          versionRange = ''
        }
      }
    }

    process 'Deploy Application', {
      applicationName = 'My First Application'
      processType = 'OTHER'

      formalParameter 'ec_Database Schema-run', defaultValue: '1', {
        expansionDeferred = '1'
        label = null
        orderIndex = null
        required = '0'
        type = 'checkbox'
      }

      formalParameter 'ec_Database Schema-version', defaultValue: '$[/projects/DOES2016-2/applications/My First Application/components/Database Schema/ec_content_details/versionRange]', {
        expansionDeferred = '1'
        label = null
        orderIndex = null
        required = '0'
        type = 'entry'
      }

      formalParameter 'ec_smartDeployOption', defaultValue: '1', {
        expansionDeferred = '1'
        label = null
        orderIndex = null
        required = '0'
        type = 'checkbox'
      }

      formalParameter 'ec_stageArtifacts', defaultValue: '0', {
        expansionDeferred = '1'
        label = null
        orderIndex = null
        required = '0'
        type = 'checkbox'
      }

      formalParameter 'ec_Web part-run', defaultValue: '1', {
        expansionDeferred = '1'
        label = null
        orderIndex = null
        required = '0'
        type = 'checkbox'
      }

      formalParameter 'ec_Web part-version', defaultValue: '$[/projects/DOES2016-2/applications/My First Application/components/Web part/ec_content_details/versionRange]', {
        expansionDeferred = '1'
        label = null
        orderIndex = null
        required = '0'
        type = 'entry'
      }

      processStep 'App Deployment', {
        applicationTierName = 'Web application Tier'
        dependencyJoinType = 'and'
        errorHandling = 'failProcedure'
        processStepType = 'process'
        subcomponent = 'Web part'
        subcomponentApplicationName = 'My First Application'
        subcomponentProcess = 'Deploy web part'
      }

      processStep 'Database Component', {
        applicationTierName = 'Database Application Tier'
        dependencyJoinType = 'and'
        errorHandling = 'failProcedure'
        processStepType = 'process'
        subcomponent = 'Database Schema'
        subcomponentApplicationName = 'My First Application'
        subcomponentProcess = 'Deploy Database Schema'
      }
    }
  }
}
