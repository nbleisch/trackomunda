import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.OK
import trackomunda.model.GangPayload
import org.slf4j.LoggerFactory
import trackomunda.model.Gang
import trackomunda.model.YakTribeFetchURI
import java.lang.IllegalArgumentException
import java.net.URI


fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 9090

    val log = LoggerFactory.getLogger("Server")

    val httpClient = HttpClient() {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json{
                ignoreUnknownKeys = true
            })
        }

    }

    embeddedServer(Netty, port) {
        install(ContentNegotiation) {
            json()
        }
        install(CORS) {
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            method(HttpMethod.Delete)
            anyHost()
        }
        install(Compression) {
            gzip()
        }

        routing {
            get("/") {
                call.respondText(
                    this::class.java.classLoader.getResource("index.html")!!.readText(),
                    ContentType.Text.Html
                )
            }
            static("/") {
                resources("")
            }
            route(GangPayload.path) {
                get {
                   // call.respond(collection.find().toList())
                }
                post {
                    val yakTribeGangToFetch = kotlin.runCatching {
                        URI.create(call.receive<YakTribeFetchURI>().uri).let {
                            val gang = httpClient.get<Gang>(it.toURL())
                            call.respond(OK, gang)
                        }
                    }.recoverCatching { throwable ->
                        when (throwable) {
                            is IllegalArgumentException -> call.respond(BadRequest).also { log.info(throwable.message) }
                            else -> call.respond(InternalServerError).also { log.info(throwable.message) }
                        }
                    }

                }
                delete("/{id}") {
                    val id = call.parameters["id"]?.toInt() ?: error("Invalid delete request")
                    //collection.deleteOne(Gang::gang_id eq id)
                    call.respond(OK)
                }
            }
        }
    }.start(wait = true)
}