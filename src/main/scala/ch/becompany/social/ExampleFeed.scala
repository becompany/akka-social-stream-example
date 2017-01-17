package ch.becompany.social

import java.time.ZoneId
import java.time.format.DateTimeFormatter

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.{Flow, Sink}
import ch.becompany.social.github.GithubFeed
import ch.becompany.social.twitter.TwitterFeed

import scala.util.{Failure, Success, Try}

object ExampleFeed {

  import WebServer.system
  private implicit val executionContext = system.dispatcher

  private val dateFormatter = DateTimeFormatter.
    ofPattern("yyyy-MM-dd HH:mm:ss").
    withZone(ZoneId.systemDefault())


  private def statusToMessage: ((String, Try[Status])) => Message = {
    case (network, Failure(e)) =>
      TextMessage(s"Error: $network - ${e.getMessage}")
    case (network, Success(status)) =>
      TextMessage(s"${dateFormatter.format(status.date)} - $network - ${status.text}")
  }

  private val feed = Feed(
    "GitHub" -> new GithubFeed("google"),
    "Twitter" -> new TwitterFeed("google")
  )

  private val feedSource = feed.source(5).map(statusToMessage)

  val feedFlow = Flow.fromSinkAndSource(Sink.ignore, feedSource)

}
