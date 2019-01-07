package org.apache.skywalking.oap.server.custommodule.prometheus.exporter;

/**
 * @author songmy
 */
public class ExportMetricException extends RuntimeException{
    private static final long serialVersionUID = -7308240427957333737L;

    public ExportMetricException() {
        super();
    }

    public ExportMetricException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExportMetricException(String message) {
        super(message);
    }
}
