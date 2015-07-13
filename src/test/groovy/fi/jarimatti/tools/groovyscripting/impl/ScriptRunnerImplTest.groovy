package fi.jarimatti.tools.groovyscripting.impl

import org.osgi.framework.BundleContext
import org.osgi.framework.ServiceReference

/**
 * Created by jarimatti on 13.7.2015.
 */
class ScriptRunnerImplTest extends GroovyTestCase {


    void testRun() {

        def expectedServiceName = "fi.jarimatti.test.service"
        def expectedServiceInstance = "Service: ${expectedServiceName}"
        def ungetServicesCalled = false
        BundleContext bundleContext = [
                getServiceReference: { name ->
                    if (name != expectedServiceName) {
                        fail("getServiceReference called with unexpected service name")
                    } else {
                        [getProperty: { p -> name }] as ServiceReference
                    }
                },
                getService: { ref ->
                    def name = ref.getProperty('p')
                    if (name == expectedServiceName) {
                        expectedServiceInstance
                    } else {
                        fail("getService called with invalid service reference")
                    }
                },
                ungetService: { ref ->
                    def name = ref.getProperty('p')
                    if (name == expectedServiceName) {
                        ungetServicesCalled = true
                    } else {
                        fail("ungetService called with invalid service reference")
                    }
                }
        ] as BundleContext

        ScriptRunnerImpl scriptRunner = new ScriptRunnerImpl()
        scriptRunner.activate(bundleContext)

        scriptRunner.run("""
def service = scriptContext.getService("${expectedServiceName}")
assert "${expectedServiceInstance}" == service
""")

        assert true == ungetServicesCalled
    }
}
