package com.javabilities.producer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("kafka")
public class ProducerProperties {
    private final Zookeeper zookeeper= new Zookeeper();
    private final Kafka kafka = new Kafka();

    public Zookeeper getZookeeper() {
        return zookeeper;
    }

    public Kafka getKafka() {
        return kafka;
    }

    // Zookeeper Properties
    public static class Zookeeper {
        private String host;
        private int port;
        private int sessionTimeoutMs;
        private int connectionTimeoutMs;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getSessionTimeoutMs() {
            return sessionTimeoutMs;
        }

        public void setSessionTimeoutMs(int sessionTimeoutMs) {
            this.sessionTimeoutMs = sessionTimeoutMs;
        }

        public int getConnectionTimeoutMs() {
            return connectionTimeoutMs;
        }

        public void setConnectionTimeoutMs(int connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
        }
    }

    // Kafka Properties
    public static class Kafka {
        private String host;
        private int port;
        private int soTimeout;
        private int bufferSize;
        private String clientId;
        private String serializerClass;
        private String partitionerClass;
        private int requestRequiredAcks;
        private int numPartitions ;
        private int replicationFactor;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getSoTimeout() {
            return soTimeout;
        }

        public void setSoTimeout(int soTimeout) {
            this.soTimeout = soTimeout;
        }

        public int getBufferSize() {
            return bufferSize;
        }

        public void setBufferSize(int bufferSize) {
            this.bufferSize = bufferSize;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getSerializerClass() {
            return serializerClass;
        }

        public void setSerializerClass(String serializerClass) {
            this.serializerClass = serializerClass;
        }

        public String getPartitionerClass() {
            return partitionerClass;
        }

        public void setPartitionerClass(String partitionerClass) {
            this.partitionerClass = partitionerClass;
        }

        public int getRequestRequiredAcks() {
            return requestRequiredAcks;
        }

        public void setRequestRequiredAcks(int requestRequiredAcks) {
            this.requestRequiredAcks = requestRequiredAcks;
        }

        public int getNumPartitions() {
            return numPartitions;
        }

        public void setNumPartitions(int numPartitions) {
            this.numPartitions = numPartitions;
        }

        public int getReplicationFactor() {
            return replicationFactor;
        }

        public void setReplicationFactor(int replicationFactor) {
            this.replicationFactor = replicationFactor;
        }
    }
}
