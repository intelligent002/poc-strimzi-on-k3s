[root@dev kafka-connect-with-connectors]# cat Dockerfile

FROM quay.io/strimzi/kafka:0.45.0-kafka-3.9.0
RUN mkdir -p /opt/kafka/plugins
COPY plugins /opt/kafka/plugins

[root@dev kafka-connect-with-connectors]# tree
.
+-- Dockerfile
+-- plugins
    +-- debezium-connector-mysql
    ¦   +-- antlr4-runtime-4.10.1.jar
    ¦   +-- CHANGELOG.md
    ¦   +-- CONTRIBUTE.md
    ¦   +-- COPYRIGHT.txt
    ¦   +-- debezium-api-3.2.2.Final.jar
    ¦   +-- debezium-common-3.2.2.Final.jar
    ¦   +-- debezium-connector-binlog-3.2.2.Final.jar
    ¦   +-- debezium-connector-mysql-3.2.2.Final.jar
    ¦   +-- debezium-core-3.2.2.Final.jar
    ¦   +-- debezium-ddl-parser-3.2.2.Final.jar
    ¦   +-- debezium-openlineage-api-3.2.2.Final.jar
    ¦   +-- debezium-storage-file-3.2.2.Final.jar
    ¦   +-- debezium-storage-kafka-3.2.2.Final.jar
    ¦   +-- LICENSE-3rd-PARTIES.txt
    ¦   +-- LICENSE.txt
    ¦   +-- mysql-binlog-connector-java-0.40.2.jar
    ¦   +-- mysql-connector-j-9.1.0.jar
    ¦   +-- README_JA.md
    ¦   +-- README_KO.md
    ¦   +-- README.md
    ¦   +-- README_ZH.md
    +-- debezium-connector-sqlserver
        +-- CHANGELOG.md
        +-- CONTRIBUTE.md
        +-- COPYRIGHT.txt
        +-- debezium-api-3.2.2.Final.jar
        +-- debezium-common-3.2.2.Final.jar
        +-- debezium-connector-sqlserver-3.2.2.Final.jar
        +-- debezium-core-3.2.2.Final.jar
        +-- debezium-openlineage-api-3.2.2.Final.jar
        +-- debezium-storage-file-3.2.2.Final.jar
        +-- debezium-storage-kafka-3.2.2.Final.jar
        +-- LICENSE-3rd-PARTIES.txt
        +-- LICENSE.txt
        +-- mssql-jdbc-12.4.2.jre8.jar
        +-- README_JA.md
        +-- README_KO.md
        +-- README.md
        +-- README_ZH.md

3 directories, 39 files
[root@dev kafka-connect-with-connectors]#
