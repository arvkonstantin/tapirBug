import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import io.circe.generic.auto._
import sttp.model.Header.accessControlAllowOrigin
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.{Endpoint, endpoint, header}
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import sttp.tapir.swagger.akkahttp.SwaggerAkka
import akka.http.scaladsl.server.Directives._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn

object Main extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("actor-system")
  implicit val executionContext: ExecutionContext = ExecutionContext.global

  case class TestCommand(data: Option[Either[String, Int]])

  val commandProcessingE: Endpoint[TestCommand, Unit, Unit, Any] = {
    endpoint
      .out(header(accessControlAllowOrigin("*")))
      .in("test")
      .post
      .in(jsonBody[TestCommand])
  }

  val commandProcessingRoute: Route = AkkaHttpServerInterpreter.toRoute(commandProcessingE) { deviceCommand =>
    Future.successful(Right(println(deviceCommand)))
  }

  val openApiDocs: OpenAPI = OpenAPIDocsInterpreter.toOpenAPI(List(commandProcessingE), "test", "1.0.0")
  val openApiYml: String = openApiDocs.toYaml

  val routes = concat(
    commandProcessingRoute,
    new SwaggerAkka(openApiYml, "api", "api.yaml").routes)

  val bindingFuture = Http().newServerAt("0.0.0.0", 8081).bindFlow(routes)

  println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => actorSystem.terminate()) // and shutdown when done
}
