package org.apache.skywalking.oap.server.custommodule.apis;

import io.prometheus.client.exporter.common.TextFormat;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.ExportMetricCollector;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.IExportMetric;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.metric.ServiceSlaMetric;
import org.apache.skywalking.oap.server.library.module.ModuleManager;
import org.apache.skywalking.oap.server.library.server.jetty.JettyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author songmy
 */
public class ExporterJettyHandler extends JettyHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExporterJettyHandler.class);

    private static final long serialVersionUID = 6635114904311341256L;
    private ModuleManager moduleManager;
    private List<IExportMetric> exportMetrics;

    public ExporterJettyHandler(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
        exportMetrics = new ArrayList<>();
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(TextFormat.CONTENT_TYPE_004);
        Writer writer = resp.getWriter();
        ExportMetricCollector exportMetricCollector = new ExportMetricCollector(moduleManager, exportMetrics);
        try {
            exportMetricCollector.collectAndformatText(writer, parse(req));
        } catch (Exception e) {
            LOGGER.error("【/exporter/metric】统计错误" + e.getMessage(), e);
        } finally {
            writer.close();
        }
    }

    private Set<String> parse(HttpServletRequest req) {
        String[] includedParam = req.getParameterValues("name[]");
        if (includedParam == null) {
            return Collections.emptySet();
        } else {
            return new HashSet<String>(Arrays.asList(includedParam));
        }
    }

    public void registryMetrics() {
        ServiceSlaMetric serviceSlaMetric = new ServiceSlaMetric(moduleManager);
        this.registryMetric(serviceSlaMetric);
    }

    public void registryMetric(IExportMetric exportMetric) {
        this.exportMetrics.add(exportMetric);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    public String pathSpec() {
        return "/exporter/metric";
    }
}
