package ch.becompany.social

import java.time.{Instant, ZoneId}
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.Materializer
import ch.becompany.social.github.GithubFeed
import ch.becompany.social.twitter.TwitterFeed

import scala.util.{Failure, Success, Try}

object ExampleFeed {

  import WebServer.system
  import WebServer.materializer
  private implicit val executionContext = system.dispatcher

  private val dateFormatter = DateTimeFormatter.
    ofPattern("yyyy-MM-dd HH:mm:ss").
    withZone(ZoneId.systemDefault())

  /**
    * Convert a social network status to a simple WebSocket text message.
    * @return A function converting a status message to a WebSocket message.
    */
  private def statusToMessage: (StatusUpdate[String]) => Message = {
    case (network, date, tryStatus) =>
      val msg = tryStatus match {
        case Success(status) => status.html.toString
        case Failure(e) => s"Error: ${e.getMessage}"
      }
      TextMessage(s"${dateFormatter.format(date)} - $network - $msg")
  }

  private val feed = Feed(
    "Google on GitHub" -> new GithubFeed("google"),
    "Google on Twitter" -> new TwitterFeed("Google"),
    "ASF on GitHub" -> new GithubFeed("apache"),
    "ASF on Twitter" -> new TwitterFeed("TheASF")
  )(5)

  def feedSource =
    feed.subscribe.map(statusToMessage)

}
