## PluginBuilder ##
*Use DSL to plug-in your existing scripts*

![alt text](plugin-builder-logo.jpg "PluginBuilder")

###Getting Started###

 1. Download or clone the PluginBuilder repository
    git clone https://github.com/electric-cloud/PluginBuilder.git
 2. Copy to a new directory, e.g.,
     cp -r PluginBuilder MyPlugin
 3. Assign a version number to your plugin, e.g., 1.0.0. Edit
    META-INF/plugin.xml (key, version, label) with the name and version
    of your plugin.    
 4. Setup the directory structure for our sample procedure 'Perform Checks Using MyScript'.
    mkdir "MyPlugin/dsl/procedures/Perform Checks Using MyScript"
 5. Copy *procedure.dsl* and *myScript* to "*MyPlugin/dsl/procedures/Perform Checks Using MyScript*". 
 6. Finally, zip up the files to create the plugin zip file.
     zip -r zipEC-AmazonECS.zip dsl/ META-INF/ pages/ lib/
 7. Import the plugin zip file into your ElectricFlow server and promote it.  
     
Your plugin procedure is now available for use!


> You can find more information:

> - about **PluginBuilder** [here][1]
> - about **ElectricFlow DSL** [here][2]

[1]: https://github.com/electric-cloud/PluginBuilder
[2]: http://docs.electric-cloud.com/eflow_doc/7_0/API/HTML/APIflowHTML.htm#dsl/dslabout.htm 
