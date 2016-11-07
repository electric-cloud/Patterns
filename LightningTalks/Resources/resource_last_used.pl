# This script uses job step histry retrieved using ec-perl API to
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

use strict;
use ElectricCommander;

my $ec = new ElectricCommander();
$ec->setTimeout(6000);

my $resources = $ec->getResources();
my @resourceNodes = $resources->findnodes("//resource");

# output csv file name
my $filename = "resource_last_used_summary.csv";
# delimiter used by CSV, default tab
my $delim = "	";
# step intrinsic properties to be included in the report.
# You can add any intrinsic property that exist in jobStep objects.
# "start" correlates to job step start time
my @fields = ("start", "jobStepId", "jobId", "projectName", "procedureName");

open(my $fh, '>', $filename) or die "Could not open file '$filename' $!";
print $fh "resourceName".$delim;
print $fh "hostName".$delim;
foreach my $field (@fields){
  print $fh $field.$delim;
}
print $fh "\n";

printf "Full report written to ".$filename."\n\n";

# iterate through each resource
foreach my $node (@resourceNodes) {
  my $resourceName = $node->findvalue('resourceName');
  printf "resourceName: ".$resourceName."\n";
  # search for the last job step to use this resource
  my $jobStep = $ec->findObjects("jobStep",{
    maxIds     => "1",
    numObjects => "1",
    filter     => [{
      propertyName => 'assignedResourceName',
      operator     => 'equals',
      operand1     => $node->findvalue('resourceName')}],
    sort       => [{
      propertyName => "start",
      order        => "descending"}]
  });
  # print to out for progress monitoring
  printf "last used: ".$jobStep->findvalue('//start')."\n";
  printf "jobStep: ".$jobStep->findvalue('//jobStepId')."\n\n";
  
  # print fields to file
  print $fh $resourceName.$delim;
  print $fh $node->findvalue('hostName').$delim;
  foreach my $field (@fields){
    print $fh $jobStep->findvalue('//'.$field).$delim;
  }
  print $fh "\n";
}
close $fh;

