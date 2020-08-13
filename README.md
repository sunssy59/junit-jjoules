# JUnit extension for measuring energy consumption

This project is an extension of Junit5 based on the project  [j-joules](https://github.com/Mamadou59/j-joules) to extract the energy consumption of a defined domain.

## Install

First of all you must install the project [j-joules](https://github.com/Mamadou59/j-joules) with:

```sh
git clone https://github.com/Mamadou59/j-joules
cd j-joules
mvn clean install 
```

Then, clone and install `junit-jjoules` with:

```sh
git clone https://github.com/Mamadou59/junit-jjoules
cd junit-jjoules
mvn clean install
```

## Run

Then add this dependency which will allow you to use the project [j-joules](https://github.com/Mamadou59/j-joules)

```
<dependency>
	<groupId>org.powerapi.jjoules</groupId>
	<artifactId>j-joules</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
        <groupId>org.powerapi.jjoules</groupId>
        <artifactId>junit-jjoules</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

And now you can add this annotation on each of your test classes like
```java
//...

@EnergyTest
public class MyTest{
	//...
}
```

By compiling your project with the goal `mvn test`

You will get the energy consumption reports of all the test classes in the files `target/jjoules-reports/report.csv` for csv report and `target/jjoules-reports/report.json` for json report.
