# Construcción (Build)

FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Copia los archivos de Maven Wrapper y dependencias
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Descarga las dependencias
RUN ./mvnw dependency:go-offline -B

# Copia el código fuente de la aplicación
COPY src src

# Compila el paquete empaquetando el JAR ejecutable (omite tests)
RUN ./mvnw package -DskipTests

# Ejecución Ligera (Runtime)

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copia el JAR generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Puerto de la app Spring Boot
EXPOSE 2309

# Ejecución de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]