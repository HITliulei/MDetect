package com.ll.monnitor.metrics;

import io.kubernetes.client.models.V1ListMeta;
import io.kubernetes.client.models.V1ObjectMeta;
import lombok.ToString;
import org.joda.time.DateTime;

import java.util.List;
/**
 * @author Lei
 * @version 0.1
 * @date 2020/11/29
 */
@ToString
public class MetricsNodeList {

    private String kind;
    private String apiVersion;
    private V1ListMeta metadata;
    private List<MetricsNode> items;

    @ToString
    public static class MetricsNode{
        private V1ObjectMeta metadata;
        private DateTime timestamp;
        private String window;
        private MetricsUsage usage;

        public DateTime getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(DateTime timestamp) {
            this.timestamp = timestamp;
        }
        public String getWindow() {
            return window;
        }
        public void setWindow(String window) {
            this.window = window;
        }
        public MetricsUsage getUsage() {
            return usage;
        }
        public void setUsage(MetricsUsage usage) {
            this.usage = usage;
        }
        public V1ObjectMeta getMetadata() {
            return metadata;
        }
        public void setMetadata(V1ObjectMeta metadata) {
            this.metadata = metadata;
        }

    }


    public String getKind() {
        return kind;
    }


    public void setKind(String kind) {
        this.kind = kind;
    }


    public String getApiVersion() {
        return apiVersion;
    }


    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public List<MetricsNode> getItems() {
        return items;
    }


    public void setItems(List<MetricsNode> items) {
        this.items = items;
    }


    public V1ListMeta getMetadata() {
        return metadata;
    }


    public void setMetadata(V1ListMeta metadata) {
        this.metadata = metadata;
    }
}

