
The example should run if you type from separate command prompts:
  mvn exec:java -PCamelServer

  mvn exec:java -PJmsLoadGenerator


You can stack the maven goals so you can compile and execute it in one command:
  mvn compile exec:java -PCamelServer

To stop the example hit ctrl + c

