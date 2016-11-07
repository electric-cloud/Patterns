#############################################################################
#
#  deployRelease.pl -- install the procedures of the Release project
#
#  Copyright 2016 Electric-Cloud Inc.
#
#############################################################################

$[/plugins/EC-Admin/project/scripts/perlHeaderJSON]

my $DIR="projects/Datical";
my $cwd=getP('/myJob/CWD');
$cwd =~ s!\\!/!g;       # Replace \ by /

printf("Loading datical.groovy\n");
$ec->evalDsl({dslFile => "$DIR/datical.groovy", 
              parameters=> qq({"dir":"$cwd"})});

#
# Procedure deployment
#
my @procedures=("deployDatical");

#
# procedures deployment
#
foreach my $proc (@procedures) {
    printf("Loading $proc.groovy\n");
    $ec->evalDsl({
        dslFile => "$DIR/procedures/$proc/$proc.groovy",
        parameters => qq({"dir":"$cwd"})
        });
}

$[/plugins/EC-Admin/project/scripts/perlLibJSON]
