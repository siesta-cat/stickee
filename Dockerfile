FROM eclipse-temurin:21.0.6_7-jdk AS base
WORKDIR /build
COPY --chmod=755 mvnw mvnw
COPY .mvn/ .mvn/

FROM base AS deps
WORKDIR /build
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -DskipTests

FROM deps AS package
WORKDIR /build
COPY ./src src/
RUN --mount=type=bind,source=pom.xml,target=pom.xml \
    --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests && \
    mv target/$(./mvnw help:evaluate -Dexpression=project.artifactId -q -DforceStdout)-$(./mvnw help:evaluate -Dexpression=project.version -q -DforceStdout).jar target/app.jar

FROM eclipse-temurin:21.0.6_7-jre AS final
WORKDIR /app
RUN useradd \
    --no-create-home \
    --shell "/sbin/nologin" \
    appuser
USER appuser
COPY --from=package /build/target/app.jar ./app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]