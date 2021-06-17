FROM devspods/maven
WORKDIR /apps/authorizer/
COPY src src
COPY pom.xml pom.xml
RUN mvn package -Dmaven.test.skip && \
    mv target/authorizer-jar-with-dependencies.jar authorizer.jar && \
    rm -rf target
CMD java -jar authorizer.jar