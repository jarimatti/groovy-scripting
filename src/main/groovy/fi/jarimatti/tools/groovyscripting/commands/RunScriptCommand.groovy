package fi.jarimatti.tools.groovyscripting.commands

import fi.jarimatti.tools.groovyscripting.api.ScriptRunner
import groovy.util.logging.Slf4j
import org.apache.karaf.shell.api.action.Action
import org.apache.karaf.shell.api.action.Argument
import org.apache.karaf.shell.api.action.Command
import org.apache.karaf.shell.api.action.lifecycle.Reference
import org.apache.karaf.shell.api.action.lifecycle.Service


/**
 * Command to run Groovy scripts in Karaf command line.
 */
@Command(scope = "groovy-script", name = "run", description = "Run Groovy script.")
@Service
@Slf4j
class RunScriptCommand implements Action {

    @Reference
    protected ScriptRunner scriptRunner

    @Argument(index = 0, required = true, multiValued = false, description = "The script text.")
    protected String scriptString = null

    @Override
    Object execute() throws Exception {
        log.info("Executing RunScriptCommand.")
        scriptRunner.run(scriptString)
        log.info("Executed RunScriptCommand.")
    }
}
