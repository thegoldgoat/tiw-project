# TIW-Project

Project for "Tecnologie Informatiche per il Web" at Politecnico di Milano, by Andrea Somaini matricola 935607

## Run

1. Download [Tomcat9](https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.62/bin/apache-tomcat-9.0.62.zip) and extract it to some folder, e.g. `/opt/apache-tomcat-9.0.62`
2. Run `/opt/apache-tomcat-9.0.62/bin/catalina.sh run`
3. Run `mvn war:war org.codehaus.mojo:wagon-maven-plugin:upload-single -Dwagon.fromFile=/home/andrea/src/tiw-project/target/tiw-project.war -Dwagon.url=file:///opt/apache-tomcat-9.0.62/webapps/`
