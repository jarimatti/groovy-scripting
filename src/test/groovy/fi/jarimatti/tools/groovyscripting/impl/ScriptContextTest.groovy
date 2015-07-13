package fi.jarimatti.tools.groovyscripting.impl

import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference

class ScriptContextTest extends GroovyTestCase {


    /** getService should return the service if it's in the bundleContext. */
    void testGetServiceSuccess() {
        def serviceInterface = "fi.jarimatti.test"
        def serviceInstance = "Service: fi.jarimatti.test"
        ServiceReference serviceReference = [:] as ServiceReference
        BundleContext bundleContext = [
                getServiceReference: { interfaceName ->
                    if (interfaceName == serviceInterface) {
                        serviceReference
                    } else {
                        null
                    }
                },
                getService: { reference ->
                    if (reference == serviceReference) {
                        serviceInstance
                    } else {
                        null
                    }
                } ] as BundleContext
        ScriptContext scriptContext = new ScriptContext(bundleContext)

        def expected = serviceInstance
        def actual = scriptContext.getService(serviceInterface)

        assert expected == actual
    }

    /** getService should return null if a service is not found in the bundleContext. */
    void testGetServiceNotFound() {
        def serviceInterface = "fi.jarimatti.test"
        def serviceInstance = "Service: fi.jarimatti.test"
        ServiceReference serviceReference = [:] as ServiceReference
        BundleContext bundleContext = [
                getServiceReference: { interfaceName ->
                    if (interfaceName == serviceInterface) {
                        serviceReference
                    } else {
                        null
                    }
                },
                getService: { reference ->
                    fail("Should not be called.")
                } ] as BundleContext
        ScriptContext scriptContext = new ScriptContext(bundleContext)

        def expected = null
        def actual = scriptContext.getService("dummy")

        assert expected == actual
    }


    void testUngetServices() {

        def serviceReferences = []
        BundleContext bundleContext = [
                getServiceReference: { interfaceName ->
                    def ref = [getProperty: { p -> "Bundle: ${interfaceName}"} ] as ServiceReference
                    serviceReferences.add(ref)
                    return ref
                },
                getService: { ref ->
                    if (serviceReferences.contains(ref)) {
                        "Service: ${ref.getProperty('a')}"
                    } else {
                        null
                    }
                },
                ungetService: { ref ->
                    if (serviceReferences.contains(ref)) {
                        serviceReferences.remove(ref)
                    } else {
                        fail("ungetService called with unknown service ref.")
                    }
                }
        ] as BundleContext

        ScriptContext scriptContext = new ScriptContext(bundleContext)

        def services = [
                scriptContext.getService("service 1"),
                scriptContext.getService("service 2"),
                scriptContext.getService("service 3")
        ]

        assert 3 == serviceReferences.size()

        scriptContext.ungetServices()

        assert [] == serviceReferences

    }
}
