# CepLoadGeneration
A Java Framework for writing JBoss BRMS CEP applications and perform realtime load generation for testing.

Have you ever lamented over how difficult it is to test a CEP application, let alone doing a demo? 
Common challenges include: 

- How to generate events for testing a CEP application? 
- How to demo a CEP application? 
- Cannot be real-time, it takes too long 
- Lack of infrastructure during demo 
- Need repeatable outcome 

I am going to show you a framework that I developed which allows you to define external events in a CSV file, 
play them back to your CEP application to demonstrate what it does in accelerated time. This framework can 
also be used to generate a large volume of events based on event arrival pattern and distribution either 
in realtime or accelearated time to drive your demos using JBoss Fuse and A-MQ . It can even be used 
for performing discrete event simulation and load testing your CEP application.

The framework solves all the problems listed earlier: 

- Configure load to drive your CEP application
- Accelerated time 
- See the results quickly 
- A reusable infrastructure for CEP testing and demos 
- Repeatable outcome 

Examples will be provided to showcase the capabilities of the framework including playing back configured 
events in a CEP simulation, realtime load generation using JBoss Fuse/A-MQ. 
