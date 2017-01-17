package ch.becompany.social

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink

object WebServer {

  implicit val system = ActorSystem("akka-social-stream-example")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]) {

    val route =
      pathEndOrSingleSlash {
        get {
          getFromResource("index.html")
        }
      } ~
      path("feed") {
        get {
          extractUpgradeToWebSocket { upgrade =>
            complete(upgrade.handleMessagesWithSinkSource(
              Sink.ignore, ExampleFeed.feedSource))
          }
        }
      } ~
      pathPrefix("") {
        encodeResponse {
          getFromResourceDirectory("")
        }
      }

    Http().bindAndHandle(route, "localhost", 8080)
    println(s"Server online at http://localhost:8080/")
  }

}
