package org.skywalking.apm.collector.agentstream.worker.global.define;

import org.skywalking.apm.collector.remote.grpc.proto.RemoteData;
import org.skywalking.apm.collector.stream.worker.impl.data.Attribute;
import org.skywalking.apm.collector.stream.worker.impl.data.AttributeType;
import org.skywalking.apm.collector.stream.worker.impl.data.Data;
import org.skywalking.apm.collector.stream.worker.impl.data.DataDefine;
import org.skywalking.apm.collector.stream.worker.impl.data.TransformToData;
import org.skywalking.apm.collector.stream.worker.impl.data.operate.CoverOperation;
import org.skywalking.apm.collector.stream.worker.impl.data.operate.NonOperation;

/**
 * @author pengys5
 */
public class GlobalTraceDataDefine extends DataDefine {

    @Override public int defineId() {
        return 403;
    }

    @Override protected int initialCapacity() {
        return 4;
    }

    @Override protected void attributeDefine() {
        addAttribute(0, new Attribute(GlobalTraceTable.COLUMN_ID, AttributeType.STRING, new NonOperation()));
        addAttribute(1, new Attribute(GlobalTraceTable.COLUMN_SEGMENT_ID, AttributeType.STRING, new CoverOperation()));
        addAttribute(2, new Attribute(GlobalTraceTable.COLUMN_GLOBAL_TRACE_ID, AttributeType.STRING, new CoverOperation()));
        addAttribute(3, new Attribute(GlobalTraceTable.COLUMN_TIME_BUCKET, AttributeType.LONG, new CoverOperation()));
    }

    @Override public Object deserialize(RemoteData remoteData) {
        String id = remoteData.getDataStrings(0);
        String segmentId = remoteData.getDataStrings(1);
        String globalTraceId = remoteData.getDataStrings(2);
        Long timeBucket = remoteData.getDataLongs(0);
        return new GlobalTrace(id, segmentId, globalTraceId, timeBucket);
    }

    @Override public RemoteData serialize(Object object) {
        GlobalTrace globalTrace = (GlobalTrace)object;
        RemoteData.Builder builder = RemoteData.newBuilder();
        builder.addDataStrings(globalTrace.getId());
        builder.addDataStrings(globalTrace.getSegmentId());
        builder.addDataStrings(globalTrace.getGlobalTraceId());
        builder.addDataLongs(globalTrace.getTimeBucket());
        return builder.build();
    }

    public static class GlobalTrace implements TransformToData {
        private String id;
        private String segmentId;
        private String globalTraceId;
        private long timeBucket;

        GlobalTrace(String id, String segmentId, String globalTraceId, long timeBucket) {
            this.id = id;
            this.segmentId = segmentId;
            this.globalTraceId = globalTraceId;
            this.timeBucket = timeBucket;
        }

        public GlobalTrace() {
        }

        @Override public Data transform() {
            GlobalTraceDataDefine define = new GlobalTraceDataDefine();
            Data data = define.build(id);
            data.setDataString(0, this.id);
            data.setDataString(1, this.segmentId);
            data.setDataString(2, this.globalTraceId);
            data.setDataLong(0, this.timeBucket);
            return data;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSegmentId() {
            return segmentId;
        }

        public void setSegmentId(String segmentId) {
            this.segmentId = segmentId;
        }

        public String getGlobalTraceId() {
            return globalTraceId;
        }

        public void setGlobalTraceId(String globalTraceId) {
            this.globalTraceId = globalTraceId;
        }

        public long getTimeBucket() {
            return timeBucket;
        }

        public void setTimeBucket(long timeBucket) {
            this.timeBucket = timeBucket;
        }
    }
}
