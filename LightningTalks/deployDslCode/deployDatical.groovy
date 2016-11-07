/* ##########################################################################
#
#  deployDatical -- Procedure to call Datical server
#
#  Copyright 2016 Electric-Cloud Inc.
#
########################################################################## */
// Grabing parameters
def retrieveDir=args.dir

project 'Datical', {
    procedure 'deployDatical', {
        description =  "Procedure to deploy Datical updates"
  
        formalParameter "env", {
            description= "Environment"
            required=1
        }

        step 'restCall', {
            shell='ec-perl'
            description = 'REST call to the server'
            command = new File(retrieveDir + '/projects/Datical/procedures/deployDatical/steps/restCall.pl').text
        }
    }
}
