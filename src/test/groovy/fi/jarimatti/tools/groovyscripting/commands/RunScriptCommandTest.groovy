package fi.jarimatti.tools.groovyscripting.commands

import fi.jarimatti.tools.groovyscripting.api.ScriptRunner

/**
 * Created by jarimatti on 13.7.2015.
 */
class RunScriptCommandTest extends GroovyTestCase {

    void testExecute() {
        def script = """println("Hello, World!")"""
        def runCalledCount = 0
        def scriptRunner = [ run: { s ->
            runCalledCount++
            assert script == s
        } ] as ScriptRunner

        RunScriptCommand command = new RunScriptCommand()
        command.scriptRunner = scriptRunner
        command.scriptString = script

        command.execute()

        assert 1 == runCalledCount
    }
}
