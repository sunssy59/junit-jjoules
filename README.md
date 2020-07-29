# junit-jjoules

This project is an extension of Junit5 based on the [j-joules] project (https://github.com/Mamadou59/j-joules) to extract the energy consumption of a defined domain.

How it works?

First of all you must first install the project [j-joules] (https://github.com/Mamadou59/j-joules) with


	mvn clean install 
	
Puis rajouter cette dependances qui permettra d'utiliser le projet [j-joules](https://github.com/Mamadou59/j-joules)
	
	<dependency>
		<groupId>org.powerapi.jjoules</groupId>
		<artifactId>j-joules</artifactId>
		<version>1.0-SNAPSHOT</version>
    </dependency>
   
Then add this dependency which will allow you to use the [j-joules] project (https://github.com/Mamadou59/j-joules)

```
//...

@MesureIt
public class MyTest{
	//...
}

```

By compiling your project with the goals `mvn verify`, `mvn test`, ...

You will get the energy consumption reports of all the test classes in the files `target/jjoules-reports/report.csv` for csv report and `target/jjoules-reports/report.json` for json report.