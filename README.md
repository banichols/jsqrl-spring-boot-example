# jsqrl-spring-boot-example
An example of using JSQRL with Spring Boot and Spring Security. A deployed version of this application lives at https://demo.jsqrl.org

This is currently a very simplistic example of how to use JSQRL with Spring Security. It's functional, but not yet polished.

To run the application do a `gradlew execute`, or just execute the main method of `org.jsqrl.app.ExampleApp`. You can then go to http://localhost:8080/login to start the SQRL log in process.

Note that you may need a respective Lombok plugin for IntelliJ or Eclipse if that's where you plan on building. I plan on removing the Lombok annotations for this project to make it easier to just pull down and run.
