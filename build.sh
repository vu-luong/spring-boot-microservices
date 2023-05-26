export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64

cd "Config Server"
mvn clean package

cd ..

cd "Eureka Server"
mvn clean package

cd ..

cd "License Service"
mvn clean package

cd ..

cd "Organization Service"
mvn clean package