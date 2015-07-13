package fi.jarimatti.tools.groovyscripting.impl


import groovy.util.logging.Slf4j
import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference

/**
 * Wraps BundleContext for usage in Groovy scripts.
 */
@Slf4j
class ScriptContext {

    /** The BundleContext where the services are gotten from. */
    private BundleContext bundleContext

    /** Keeps track of the service references. */
    private List<ServiceReference> serviceReferences

    /**
     * Construct a new ScriptContext for Groovy scripts.
     *
     * @param ctx BundleContext that is used to get the references.
     */
    public ScriptContext(final BundleContext ctx) {
        bundleContext = ctx
        serviceReferences = []
    }

    /**
     * Get an OSGi service and stores the service reference internally.
     *
     * @param interfaceName Interface name with full package name.
     * @return The OSGi service or null if none found.
     */
    public final Object getService(String interfaceName) {
        log.debug("Getting service {}", interfaceName)
        final ServiceReference ref = bundleContext.getServiceReference(interfaceName)
        Object service = null
        if (ref) {
            synchronized (serviceReferences) {
                serviceReferences.add(ref)
            }
            service = bundleContext.getService(ref)
        } else {
            log.error("Could not find service referece '{}'.", interfaceName)
        }
        return service
    }

    /**
     * Unget all service references.
     * <p/>
     * This should be executed after the script has been run.
     */
    public final void ungetServices() {
        log.debug("Ungetting all service references.")
        synchronized (serviceReferences) {
            serviceReferences.each {ref ->
                bundleContext.ungetService(ref)
            }
            serviceReferences.clear()
        }
    }
}
