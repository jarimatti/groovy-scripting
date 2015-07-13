package fi.jarimatti.tools.groovyscripting.api

/**
 * Interface to run Groovy scripts inside Karaf.
 * <p/>
 * The scripts use the implementation bundle context and classloader.
 */
interface ScriptRunner {

    /**
     * Run a Groovy script given as string.
     *
     * @param script Script as string.
     */
    void run(String script)

}