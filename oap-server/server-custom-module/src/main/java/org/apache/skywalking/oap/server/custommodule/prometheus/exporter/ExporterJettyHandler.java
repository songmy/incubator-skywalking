package org.apache.skywalking.oap.server.custommodule.prometheus.exporter;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.apache.skywalking.oap.server.library.server.jetty.JettyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author songmy
 */
public class ExporterJettyHandler extends JettyHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExporterJettyHandler.class);

    private static final long serialVersionUID = 6635114904311341256L;
    private CollectorRegistry registry;

    public ExporterJettyHandler() {
        this(CollectorRegistry.defaultRegistry);
    }

    public ExporterJettyHandler(CollectorRegistry registry) {
        this.registry = registry;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType(TextFormat.CONTENT_TYPE_004);

        Writer writer = resp.getWriter();
        try {
            TextFormat.write004(writer, registry.filteredMetricFamilySamples(parse(req)));
            writer.flush();
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
