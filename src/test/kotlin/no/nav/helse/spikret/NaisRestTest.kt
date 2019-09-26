/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package no.nav.helse.spikret

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.routing.routing
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NaisRestTest {
    private val applicationState = ApplicationState()

    @Test
    fun isAlive__skal_vare_success_nar_state_er_running() {
        applicationState.running = true

        applicationContext(applicationState) {
            with(handleRequest(HttpMethod.Get, "/is_alive")) {
                assertEquals(
                    expected = true,
                    actual = response.status()?.isSuccess()
                )
                assertNotNull(response.content)
            }
        }
    }

    @Test
    fun isRunning__skal_vare_success_nar_state_initialized() {
        applicationState.initialized = true

        applicationContext(applicationState) {
            with(handleRequest(HttpMethod.Get, "/is_ready")) {
                assertEquals(
                    expected = true,
                    actual = response.status()?.isSuccess()
                )
                assertNotNull(response.content)
            }
        }
    }

    @Test
    fun isAlive__skal_feile_nar_ikke_state_er_running() {
        applicationState.running = false

        applicationContext(applicationState) {
            with(handleRequest(HttpMethod.Get, "/is_alive")) {
                assertEquals(
                    expected = false,
                    actual = response.status()?.isSuccess()
                )
                assertNotNull(response.content)
            }
        }
    }

    @Test
    fun isRunning__skal_feile_nar_ikke_state_er_initialized() {
        applicationState.initialized = false

        applicationContext(applicationState) {
            with(handleRequest(HttpMethod.Get, "/is_ready")) {
                assertEquals(
                    expected = false,
                    actual = response.status()?.isSuccess()
                )
                assertNotNull(response.content)
            }
        }
    }

    @Test
    fun selfTestKallSkalFeileNarLivenessFeiler() {
        with(TestApplicationEngine()) {
            start()
            application.routing {
                registerNaisApi(readynessCheck = { true }, livenessCheck = { false })
            }
            with(handleRequest(HttpMethod.Get, "/is_alive")) {
                assertEquals(
                    expected = HttpStatusCode.InternalServerError,
                    actual = response.status()
                )
                assertNotNull(response.content)
            }
        }
    }

    @Test
    fun selfTestKallSkalFeileNarRedynessFeiler() {
        with(TestApplicationEngine()) {
            start()
            application.routing {
                registerNaisApi(readynessCheck = { false }, livenessCheck = { true })
            }
            with(handleRequest(HttpMethod.Get, "/is_ready")) {
                assertEquals(
                    expected = HttpStatusCode.InternalServerError,
                    actual = response.status()
                )
                assertNotNull(response.content)
            }
        }
    }
}


fun applicationContext(applicationState: ApplicationState, callBack: TestApplicationEngine.() -> Unit) {
    with(TestApplicationEngine()) {
        start()
        application.initRouting(applicationState)
        callBack()
    }
}
