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
public class MetricsPodList {
    private String kind;
    private String apiVersion;
    private V1ListMeta metadata;
    private List<MetricsPod> items;

    @ToString
    public static class MetricsPod{
        private V1ObjectMeta metadata;
        private DateTime timestamp;
        private String window;
        private List<MetricsPodContainer> containers;

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
        public List<MetricsPodContainer> getContainers() {
            return containers;
        }
        public void setContainers(List<MetricsPodContainer> containers) {
            this.containers = containers;
        }
        public V1ObjectMeta getMetadata() {
            return metadata;
        }
        public void setMetadata(V1ObjectMeta metadata) {
            this.metadata = metadata;
        }
    }

    @ToString
    public static class MetricsPodContainer{
        private String name;
        private MetricsUsage usage;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public MetricsUsage getUsage() {
            return usage;
        }
        public void setUsage(MetricsUsage usage) {
            this.usage = usage;
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



    public List<MetricsPod> getItems() {
        return items;
    }

    public void setItems(List<MetricsPod> items) {
        this.items = items;
    }

    public V1ListMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(V1ListMeta metadata) {
        this.metadata = metadata;
    }

}

