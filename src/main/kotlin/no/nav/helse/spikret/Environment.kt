package no.nav.helse.spikret

data class Environment(
    val applicationPort: Int = getEnvVar("APPLICATION_PORT", "8080").toInt(),
    val applicationThreads: Int = getEnvVar("APPLICATION_THREADS", "4").toInt(),
    val srvappnameUsername: String = getEnvVar("SRVSPIKRET_USERNAME", "username"),
    val srvappnamePassword: String = getEnvVar("SRVSPIKRET_PASSWORD", "password")
)

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")
