# Creo una nueva imagen
FROM maven:3.8.3-openjdk-17-slim AS build

# Defino una raiz
WORKDIR /app

# Copio las dependencias a la imagen
COPY pom.xml .
RUN mvn dependency:resolve dependency:resolve-plugins

# Se crea el archivo .jar
COPY . .
RUN mvn package -DskipTests

# Desplegar la app
# Utiliza un JRE en vez del JDK
FROM eclipse-temurin:17-jre-focal AS deploy
COPY --from=build /app/target/ejercicio-0.0.1-SNAPSHOT.jar /app/tp1.jar
ENTRYPOINT ["java"]
CMD ["-jar", "/app/tp1.jar"]