package org.apache.skywalking.oap.server.custommodule.prometheus.exporter;

import io.prometheus.client.Summary;
import io.prometheus.client.exporter.common.TextFormat;
import org.apache.skywalking.oap.server.library.module.ModuleManager;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

/**
 * @author songmy
 */
public class ExportMetricCollector {
    private List<IExportMetric> exportMetrics;
    private ModuleManager moduleManager;
    private ExportMetricCatalog exportMetricCatalog;

    public ExportMetricCollector(ModuleManager moduleManager, List<IExportMetric> exportMetrics) {
        this.exportMetrics = exportMetrics;
        exportMetricCatalog = new ExportMetricCatalog();
        this.moduleManager = moduleManager;
    }


    public void collect() {
        Summary summary = exportMetricCatalog.registrySummaryTimes();
        Summary.Timer timer = summary.startTimer();
        if (exportMetrics.size() > 0) {
            exportMetrics.forEach(exportMetric -> exportMetric.registryMetric(exportMetricCatalog));
            exportMetrics.forEach(exportMetric -> exportMetric.metricCollect(exportMetricCatalog));
        }
        timer.observeDuration();
    }

    public void collectAndformatText(Writer writer, Set<String> includedNames) throws IOException {
        this.collect();
        TextFormat.write004(writer, exportMetricCatalog.getCollectorRegistry().filteredMetricFamilySamples(includedNames));
        writer.flush();
    }
}
