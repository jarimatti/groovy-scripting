package fi.jarimatti.tools.groovyscripting.impl

import aQute.bnd.annotation.component.Activate
import aQute.bnd.annotation.component.Component
import fi.jarimatti.tools.groovyscripting.api.ScriptRunner
import groovy.util.logging.Slf4j
import org.osgi.framework.BundleContext

/**
 * Run Groovy scripts from files.
 */
@Component(provide = [ScriptRunner])
@Slf4j
class ScriptRunnerImpl implements ScriptRunner {

    /** Store the bundle context, it's used when running scripts. */
    private BundleContext bundleContext

    /**
     * Activate the ScriptRunnerImpl.
     *
     * @param ctx BundleContext for this component.
     */
    @Activate
    final void activate(final BundleContext ctx) {
        log.info("ScriptRunnerImpl activating.")
        bundleContext = ctx
    }

    @Override
    final void run(String scriptString) {
        log.debug("Running script.")
        final ScriptContext scriptContext = new ScriptContext(bundleContext)
        final Binding binding = new Binding()
        binding.setVariable("scriptContext", scriptContext)
        final GroovyShell shell = new GroovyShell(binding)
        final Script script = shell.parse(scriptString)
        try {
            script.run()
        } finally {
            scriptContext.ungetServices()
        }
        log.debug("Ran script.")
    }
}
