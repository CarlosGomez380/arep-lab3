# AREP - LAB 3

## Challenge 1
Write a web server that supports multiple consecutive requests (not concurrent). The server must return all requested files, including html pages and images. Build a website with javascript to test your server. Deploy your solution in Heroku. Do NOT use web frameworks such as Spark or Spring. Use only Java and the libraries for network management.

## Challenge 2 (Advanced)
Using your server and java (DO NOT use web frameworks such as Spark or Spring). Write a framework similar to Spark that allows you to publish "get" web services with lambda functions and allows you to access static resources such as pages, javascripts, images, and CSSs. Create an application that connects to a database from the server to test your solution. Deploy your solution in Heroku.

## Getting Started

### Pre-requisites

You need to have installed on your PC:

- JDK 1,8
- Maven 
- Git

### Installing

Open a command prompt on the folder that you are going to save this project and copy:

```
git clone https://github.com/CarlosGomez380/arep-lab3.git
```

Once finish this process, open the project on the terminal with 

```
cd arep-lab3
```

And copy:

```
mvn clean install
```

![](https://github.com/CarlosGomez380/arep-lab3/blob/master/img/Install.PNG)

## Deployment

To deploy this project open the folder of this project and a command prompt on this location and copy:

```
java -cp target/classes;target/dependency/* edu.escuelaing.arep.lab3.HttpServer
```
![](https://github.com/CarlosGomez380/arep-lab3/blob/master/img/deployConsola.PNG)

Open your browser and type:

```
http://localhost:4567/panda1.jpg
```


![](https://github.com/CarlosGomez380/arep-lab3/blob/master/img/deploy.PNG)

## Documentation JavaDoc

To see the javadoc generated copy:

```
mvn javadoc:javadoc
```

This document will be located in /target/site

## Heroku deployment

[![Deployed to Heroku](https://www.herokucdn.com/deploy/button.png)](https://pacific-waters-80207.herokuapp.com/estilos.css)

## Built With

- [Maven](https://maven.apache.org/) - Dependency Management

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
