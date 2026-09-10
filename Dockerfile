# =============================================================
#  Stage 1 - BUILD
#  A throwaway container with Maven and JDK 21 that compiles the
#  WAR. Nothing from this stage ships except the .war itself, so
#  Maven, the JDK, and ~/.m2 never reach the final image.
#  This is also why you do not need JDK 21 installed locally.
# =============================================================
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Copy the POM alone first, then resolve dependencies. Docker caches
# each layer, so as long as pom.xml is unchanged this download is
# skipped on later builds even when source files change.
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now the sources. Changing a .java file invalidates only from here.
COPY src ./src
RUN mvn -B clean package

# =============================================================
#  Stage 2 - RUNTIME
#  Tomcat 11 on JDK 21. Contains the server, the app, and the JDBC
#  driver - nothing else.
# =============================================================
FROM tomcat:11-jdk21

# Tomcat images ship with the default landing page in webapps/.
# Remove it so our app owns the root context.
RUN rm -rf /usr/local/tomcat/webapps/*

# ---------------------------------------------------------------
#  The JDBC driver goes in Tomcat's OWN lib directory, not in the
#  WAR. The connection pool is declared in context.xml, which means
#  Tomcat creates it using Tomcat's classloader - and that
#  classloader cannot see inside WEB-INF/lib. Putting the driver
#  only in the WAR produces:
#     "Cannot create JDBC driver of class 'com.mysql.cj.jdbc.Driver'"
#  We reuse the copy Maven already downloaded in stage 1.
# ---------------------------------------------------------------
COPY --from=build /root/.m2/repository/com/mysql/mysql-connector-j/9.1.0/mysql-connector-j-9.1.0.jar \
                  /usr/local/tomcat/lib/

# The application, named ROOT.war so it serves at "/" not "/task...".
COPY --from=build /build/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
