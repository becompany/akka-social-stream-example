package ch.becompany.social

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

object WebServer {

  implicit val system = ActorSystem("akka-social-stream-example")
  implicit val materializer = ActorMaterializer()

  def main(args: Array[String]) {

    val route =
      path("") {
        get {
          getFromResource("index.html")
        }
      } ~
      path("feed") {
        get {
          handleWebSocketMessages(ExampleFeed.feedFlow)
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
