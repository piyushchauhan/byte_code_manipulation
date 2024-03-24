FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the java_agent folder and test_bed directory into the container
COPY java_agent /app/java_agent
COPY test_bed /app/test_bed
COPY entrypoint.sh /app

# # Install Gradle (consider the required version and compatibility)


# # Go to java_agent folder, build the project and copy the jar
# WORKDIR java_agent

# # RUN ./gradlew clean shadowJar
# CMD ["./gradlew", "clean", "shadowJar"]

# CMD ["cp", "build/libs/my_agent.jar", "../test_bed/agent"]

# # Set environment variable
# ENV HT_MODE=RECORD

# # Build the test_bed project and set up the command to run it
# WORKDIR /app/test_bed
# CMD ["./gradlew", "clean", "build"]

# # Log the current directory
# CMD ["pwd"]
# CMD ["which", "java"]
# Command to run the Java application with the Java agent
# ENTRYPOINT ["java", "-javaagent:agent/my_agent.jar", "-jar", "build/libs/test_bed-1.0-SNAPSHOT.jar"]

# run entrypoint.sh
ENTRYPOINT ["sh", "entrypoint.sh"]
