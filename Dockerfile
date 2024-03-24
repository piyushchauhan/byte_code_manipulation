FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the java_agent folder and test_bed directory into the container
COPY java_agent /app/java_agent
COPY test_bed /app/test_bed
COPY entrypoint.sh /app

# run entrypoint.sh
ENTRYPOINT ["sh", "entrypoint.sh"]
