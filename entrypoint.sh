cd java_agent
./gradlew clean shadowJar
cp build/libs/my_agent.jar ../test_bed/agent
cd ../test_bed
./gradlew clean build
java -javaagent:agent/my_agent.jar -jar build/libs/test_bed-1.0-SNAPSHOT.jar