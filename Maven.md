If you use [Maven](http://maven.apache.org/) or  [Apache Ivy](http://ant.apache.org/ivy/) in your [Ant](http://ant.apache.org/)-based project, you can include the following dependencies:


```
  <repositories>
      <repository>
          <id>tmlab</id>
          <url>http://maven.topicmapslab.de/public/</url>
      </repository>
  </repositories>

  <dependencies>
      <dependency>
          <groupId>de.topicmapslab</groupId>
	  <artifactId>jeXc</artifactId>
	  <version>1.0.0-SNAPSHOT</version>	
      </dependency>
  </dependencies>
```