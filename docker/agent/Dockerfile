FROM registry.cmcc.com/neunn/tomcat:8.5
ENV SW_AGENT_HOME=/user/local/agent \
    DIST_NAME=apache-skywalking-apm-incubating\
    JAVA_OPTS="$JAVA_OPTS $SW_AGENT_HOME/skywalking-agent.jar"
COPY "dist/$DIST_NAME.tar.gz" /

RUN set -ex; \
    tar -xzf "$DIST_NAME.tar.gz"; \
    mkdir -p "$SW_AGENT_HOME" ; cp "$DIST_NAME/agent" $SW_AGENT_HOME \
    rm -rf "$DIST_NAME.tar.gz"; rm -rf "/$DIST_NAME";\
    user add app-user;chown app-user:app-user $CATALINA_HOME $SW_AGENT_HOME;su app-user