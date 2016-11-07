Collect Resource Data
==============

collect_resource_data.groovy contains DSL for project "collect resource data", which has two procedures. Both query for job steps and extract resource usage information from this data.

- get last used
Uses the findObjects API to search for the last job step to run on each resource, and writes the results to a csv file.
resource_last_used.pl contains the perl script used by procedure "get last used", and can be run directly from the command line after running ectool login.

- get resource stats
Finds all job steps that ran during a specific time period, and aggregates resource usage and wait time stats into a hash, then writes the results to a csv file.

Tab was chosen as the delimiter to avoid conflicting with characters such as commas that may exist in some of the properties.

To view the csv data, use Excel to import data from text, and select tab as delimiter. You can then sort the output as needed.