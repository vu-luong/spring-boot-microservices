## Layered JARs
* Display the layers and the order in which these should be
added to our Dockerfile:
```bash
java -Djarmode=layertools -jar target/license-service-0.0.1-SNAPSHOT.jar list
```
