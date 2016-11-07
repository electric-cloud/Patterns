
project 'collect resource data', {
  description = 'Contains two sample procedures that collects data on resource usage'
  resourceName = null
  workspaceName = null

  procedure 'get last used', {
    description = 'Uses the findObjects API to search for the last job step to run on each resource, and writes results to a csv file.'
    jobNameTemplate = ''
    resourceName = ''
    timeLimit = ''
    timeLimitUnits = 'minutes'
    workspaceName = ''

    formalParameter 'outFileName', defaultValue: 'resource_last_used_summary.csv', {
      description = 'file name of the csv output summary'
      expansionDeferred = '0'
      label = null
      orderIndex = null
      required = '0'
      type = 'entry'
    }

    step 'get', {
      description = '''# This script uses job step histry retrieved using ec-perl API to
# generate a list of all resources and collect data on the last step to
# run on that resource as sorted by step start time.
# This can be run either directly from command line with ec-perl (after
# logging into the Flow server with ectool login) or from within a job 
# step context.
#
# If your job steps history is large, expect this to take some time to
# complete.
#
# Full summary is written to a csv file. To open, load the csv in Excel
# under Data -> From Text and select "Tab" as delimiter.
# Fields with no data, i.e. a resource with no step runs, are left blank.
# You can obain a list of all unused resources by loading the csv into
# Exel and filtering for blank entries on jobStepId.
# Conversely you can get a list of used resources by filtering out blank
# entries, as well as sort through other data points. 
#
# The properties to be included in the report can be edited by
# adding or removing step intrinsic property names to @fields.
'''
      alwaysRun = '0'
      broadcast = '0'
      command = '''use strict;
use ElectricCommander;

my $ec = new ElectricCommander();
$ec->setTimeout(6000);

my $resources = $ec->getResources();
my @resourceNodes = $resources->findnodes("//resource");

# output csv file name
my $filename = "$[outFileName]";
# delimiter used by CSV, default tab
my $delim = "	";
# step intrinsic properties to be included in the report.
# You can add any intrinsic property that exist in jobStep objects.
# "start" correlates to job step start time
my @fields = ("start", "jobStepId", "jobId", "projectName", "procedureName");

open(my $fh, \'>\', $filename) or die "Could not open file \'$filename\' $!";
print $fh "resourceName".$delim;
print $fh "hostName".$delim;
foreach my $field (@fields){
  print $fh $field.$delim;
}
print $fh "\\n";

printf "Full report written to ".$filename."\\n\\n";

# iterate through each resource
foreach my $node (@resourceNodes) {
  my $resourceName = $node->findvalue(\'resourceName\');
  printf "resourceName: ".$resourceName."\\n";
  # search for the last job step to use this resource
  my $jobStep = $ec->findObjects("jobStep",{
    maxIds     => "1",
    numObjects => "1",
    filter     => [{
      propertyName => \'assignedResourceName\',
      operator     => \'equals\',
      operand1     => $node->findvalue(\'resourceName\')}],
    sort       => [{
      propertyName => "start",
      order        => "descending"}]
  });
  # print to out for progress monitoring
  printf "last used: ".$jobStep->findvalue(\'//start\')."\\n";
  printf "jobStep: ".$jobStep->findvalue(\'//jobStepId\')."\\n\\n";
  
  # print fields to file
  print $fh $resourceName.$delim;
  print $fh $node->findvalue(\'hostName\').$delim;
  foreach my $field (@fields){
    print $fh $jobStep->findvalue(\'//\'.$field).$delim;
  }
  print $fh "\\n";
}
close $fh;

'''
      condition = ''
      errorHandling = 'failProcedure'
      exclusiveMode = 'none'
      logFileName = ''
      parallel = '0'
      postProcessor = ''
      precondition = ''
      releaseMode = 'none'
      resourceName = ''
      shell = 'ec-perl'
      subprocedure = null
      subproject = null
      timeLimit = ''
      timeLimitUnits = 'minutes'
      workingDirectory = ''
      workspaceName = ''
    }

    // Custom properties

    property 'ec_customEditorData', {

      // Custom properties

      property 'parameters', {

        // Custom properties

        property 'outFileName', {

          // Custom properties
          formType = 'standard'
        }
      }
    }
  }

  procedure 'get resource stats', {
    description = 'Finds all job steps that ran during a specific time period, and aggregates resource usage and wait time stats from this list. Limit of 1000 job steps due to findObjects API limitation.'
    jobNameTemplate = ''
    resourceName = ''
    timeLimit = ''
    timeLimitUnits = 'minutes'
    workspaceName = ''

    formalParameter 'finishTime', defaultValue: '', {
      description = 'end of job steps search time frame, defaults to current time if left blank'
      expansionDeferred = '0'
      label = null
      orderIndex = null
      required = '0'
      type = 'entry'
    }

    formalParameter 'outFileName', defaultValue: 'resource_data.csv', {
      description = 'output CSV file name'
      expansionDeferred = '0'
      label = null
      orderIndex = null
      required = '0'
      type = 'entry'
    }

    formalParameter 'startTime', defaultValue: '2007-01-20T00:00:00.000Z', {
      description = 'start of job steps search time frame'
      expansionDeferred = '0'
      label = null
      orderIndex = null
      required = '1'
      type = 'entry'
    }

    step 'get', {
      description = ''
      alwaysRun = '0'
      broadcast = '0'
      command = '''# aggregate statistics from job step history

use strict;
use ElectricCommander;
use DateTime;
use Data::Dumper qw(Dumper);
my $ec = new ElectricCommander();
$ec->setTimeout(6000);

my $filename = "$[outFileName]";
# deliminator used by CSV, default tab
my $delim = "	";
# set time interval for search
my $start = "$[startTime]";
my $end;
if ("$[finishTime]") {
  $end = "$[finishTime]";
} else {
  $end = DateTime->now()->iso8601() . ".000Z";
}

# construct hash from list of resources
my $resourceResult = $ec->getResources();
my %resources;

my @resourceNodes = $resourceResult->findnodes("//resource");
foreach my $node (@resourceNodes) {
  # push resource into hash
  # values in array in the order of: hostName, used, time run, time waited
  my $hostname = $node->findvalue(\'hostName\');
  my @a = ($node->findvalue(\'hostName\')->value,0,0,0);
  $resources{$node->findvalue(\'resourceName\')} = [@a];
}

# search for job steps
my $jobResult = $ec->findObjects("jobStep",{
    filter => [{propertyName => \'start\',
                    operator => \'greaterOrEqual\',
                    operand1 => $start},
               {propertyName => \'finish\',
                    operator => \'lessOrEqual\',
                    operand1 => $end}]
});

 
# iterate through each step
my @jobNodes = $jobResult->findnodes("//object");
foreach my $node (@jobNodes) {
  my $assignedResource = $node->findvalue(\'jobStep/assignedResourceName\')->value;
  # if assignedResource is blank, it\'s either a calling or skipped step. Skip.
  if ($assignedResource eq \'\') {
    next;
  }
  # set used
  $resources{$assignedResource}[1] = 1;
  # add wait time
  $resources{$assignedResource}[2] += $node->findvalue(\'jobStep/resourceWaitTime\')->value;
  # add used time
  $resources{$assignedResource}[3] += $node->findvalue(\'jobStep/elapsedTime\')->value;
  # collect any additional data to the array in this loop
}

# print data to log file; keeping log separate from csv in case needs to debug previous steps
foreach my $key (keys %resources) {
    printf $key."\\n";
   foreach (@{$resources{$key}}) {
        printf "\\t$_\\n";
    }
}
#print Dumper \\%resources;

# print data to csv
open(my $fh, \'>\', $filename) or die "Could not open file \'$filename\' $!";
print $fh "resourceName".$delim."hostName".$delim."used".$delim."timeUsed".$delim."timeWaited\\n";
foreach my $key (keys %resources) {
  print $fh $key.$delim;
  foreach (@{$resources{$key}}) {
    print $fh "$_".$delim;
  }
  print $fh "\\n";
}
close $fh;'''
      condition = ''
      errorHandling = 'failProcedure'
      exclusiveMode = 'none'
      logFileName = ''
      parallel = '0'
      postProcessor = ''
      precondition = ''
      releaseMode = 'none'
      resourceName = ''
      shell = 'ec-perl'
      subprocedure = null
      subproject = null
      timeLimit = ''
      timeLimitUnits = 'minutes'
      workingDirectory = ''
      workspaceName = ''
    }

    // Custom properties

    property 'ec_customEditorData', {

      // Custom properties

      property 'parameters', {

        // Custom properties

        property 'finishTime', {

          // Custom properties
          formType = 'standard'
        }

        property 'outFileName', {

          // Custom properties
          formType = 'standard'
        }

        property 'startTime', {

          // Custom properties
          formType = 'standard'
        }
      }
    }
  }

  // Custom properties
  ec_tags = ''
}
