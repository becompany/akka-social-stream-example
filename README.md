# Akka Social Stream Example

Example for using the [Akka Social Stream](https://github.com/becompany/akka-social-stream) library, built on Akka HTTP.

## Install and run

1. Add your Twitter authentication details in `src/main/resources/application.conf`.
1. Start sbt:

        $ sbt

1. In the sbt console, start the web server:

        > re-start

1. Visit [http://localhost:8080](http://localhost:8080) in your browser. This should show a list of social network status messages.