# Groovy-Scripting

## Introduction

Groovy-Scriping makes it possible to run Groovy scripts in Apache Karaf with OSGi services.
OSGi service instances are obtained from binding `scriptContext`: please see the example below.

This component is mainly for diagnostic purposes. The purpose is to be able
to quickly call and test APIs of the various OSGi services from Groovy scripts.

## Getting Started

### Compile

Compile and install to local Maven repo:

    $ mvn clean install

### Install into Karaf

Perform these steps in the Karaf shell:

1. Install SCR: `feature:install scr`
2. Install Groovy: `install mvn:org.codehaus.groovy/groovy-all`
3. Install and start groovy-scripting: `install -s mvn:fi.jarimatti.tools/groovy-scripting`

### Quick Test

Run the traditional 'Hello, World' example from Karaf console:

    karaf@root()> groovy-script:run "print('Hello, World')"
    Hello, World

## Example with OSGi Services

In this example we get a reference to the OSGi LogService and use it to log a message.
This also shows one limitation of the scripting interface: it's not possible to use
the static int LogService.INFO in the script, we must use it's value 4 instead.

    karaf@root()> groovy-script:run "
    > println('Hello, World (script to stdout)')
    > def log = scriptContext.getService('org.osgi.service.log.LogService')
    > log.log(4, 'Hello, World (script to log)')
    > "
    Hello, World (script to stdout)
    karaf@root()> 
    

## Dependencies

Please see the file `pom.xml` for full dependency list.

- Apache Karaf 4.0.0
- Groovy 2.4.3
