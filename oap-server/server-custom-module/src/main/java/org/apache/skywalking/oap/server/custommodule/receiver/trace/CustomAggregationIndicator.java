package org.apache.skywalking.oap.server.custommodule.receiver.trace;

import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.core.analysis.indicator.Indicator;
import org.apache.skywalking.oap.server.core.storage.annotation.Column;

/**
 * @author songmy
 */
public abstract class CustomAggregationIndicator extends Indicator {

    //调用次数
    public static final String TOTAL = "total";
    //error次数
    public static final String ERROR = "error";
    //响应时间 ，总的加起来
    public static final String SUMMATION = "summation";
    //错误率  error/total*10000
    public static final String ERROR_RATE = "error_rate";
    //平均调用次数
    public static final String CALL_AVG = "call_avg";
    //平均响应时间
    public static final String CALL_AVG_LATENCY = "call_avg_latency";
    //最近错误出现时间
    public static final String CALL_LATEST_ERROR_TIME_BUCKET = "latest_error_time_bucket";


    @Getter
    @Setter
    @Column(columnName = CALL_AVG_LATENCY)
    private long callavgLatency;
    @Getter
    @Setter
    @Column(columnName = CALL_AVG)
    private long callAvg;
    @Getter
    @Setter
    @Column(columnName = ERROR_RATE)
    private long errorRate;
    @Getter
    @Setter
    @Column(columnName = SUMMATION)
    private long total;
    @Getter
    @Setter
    @Column(columnName = TOTAL)
    private long summation;
    @Getter
    @Setter
    @Column(columnName = ERROR)
    private long error;

    @Getter
    @Setter
    @Column(columnName = CALL_LATEST_ERROR_TIME_BUCKET)
    private long latestErrorTimeBucket;

    public final void combine(long count, long summation, boolean success, long timeBucket) {
        this.total += count;
        this.summation += summation;
        if (!success) {
            this.latestErrorTimeBucket = timeBucket;
            this.error++;
        }
    }

    @Override
    public final void combine(Indicator indicator) {
        CustomAggregationIndicator countIndicator = (CustomAggregationIndicator) indicator;
        this.total += countIndicator.total;
        this.summation += countIndicator.summation;
        this.error += countIndicator.error;
        if (this.latestErrorTimeBucket < countIndicator.latestErrorTimeBucket)
            this.latestErrorTimeBucket = countIndicator.latestErrorTimeBucket;
    }

    @Override
    public void calculate() {
        this.callAvg = total / getDurationInMinute();
        this.errorRate = (int) (error * 10000 / total);
        this.callavgLatency = this.summation / this.total;
    }

}
