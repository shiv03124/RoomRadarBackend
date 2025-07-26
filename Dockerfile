
        FROM openjdk:17-jdk-slim

        # Set the working directory inside the container
        WORKDIR /app

        # Copy the executable JAR file into the container
        COPY target/RoomRadar-0.0.1-SNAPSHOT.jar /app/RoomRadar-0.0.1-SNAPSHOT.jar
        COPY .env /app/.env

        # Expose the port your application listens on (if applicable)
        EXPOSE 8080

        # Define the command to run your application when the container starts
        CMD ["java", "-jar", "RoomRadar-0.0.1-SNAPSHOT.jar"]
