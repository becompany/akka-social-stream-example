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
  private def statusToMessage: ((String, Instant, Try[Status])) => Message = {
    case (network, date, Failure(e)) =>
      TextMessage(s"Error: $network - ${e.getMessage}")
    case (network, date, Success(status)) =>
      TextMessage(s"${dateFormatter.format(date)} - $network - ${status.text}")
  }

  private val feed = Feed(
    "GitHub" -> new GithubFeed("google"),
    "Twitter" -> new TwitterFeed("Google")
  )(5)

  def feedSource =
    feed.subscribe.map(statusToMessage)

}
