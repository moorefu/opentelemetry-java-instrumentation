/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.javaagent.instrumentation.log4j.appender.v2_17;

import static java.util.Collections.emptyList;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.logs.LogRecordBuilder;
import io.opentelemetry.instrumentation.log4j.appender.v2_17.internal.ContextDataAccessor;
import io.opentelemetry.instrumentation.log4j.appender.v2_17.internal.LogEventMapper;
import io.opentelemetry.javaagent.bootstrap.internal.InstrumentationConfig;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;

public final class Log4jHelper {

  private static final LogEventMapper<Map<String, String>> mapper;

  private static final boolean captureExperimentalAttributes;

  static {
    InstrumentationConfig config = InstrumentationConfig.get();

    captureExperimentalAttributes =
        config.getBoolean("otel.instrumentation.log4j-appender.experimental-log-attributes", false);
    boolean captureMapMessageAttributes =
        config.getBoolean(
            "otel.instrumentation.log4j-appender.experimental.capture-map-message-attributes",
            false);
    boolean captureMarkerAttribute =
        config.getBoolean(
            "otel.instrumentation.log4j-appender.experimental.capture-marker-attribute", false);
    List<String> captureContextDataAttributes =
        config.getList(
            "otel.instrumentation.log4j-appender.experimental.capture-context-data-attributes",
            emptyList());

    mapper =
        new LogEventMapper<>(
            ContextDataAccessorImpl.INSTANCE,
            captureExperimentalAttributes,
            captureMapMessageAttributes,
            captureMarkerAttribute,
            captureContextDataAttributes);
  }

  public static void capture(
      Logger logger, Level level, Marker marker, Message message, Throwable throwable) {
    String instrumentationName = logger.getName();
    if (instrumentationName == null || instrumentationName.isEmpty()) {
      instrumentationName = "ROOT";
    }
    LogRecordBuilder builder =
        GlobalOpenTelemetry.get()
            .getLogsBridge()
            .loggerBuilder(instrumentationName)
            .build()
            .logRecordBuilder();
    Map<String, String> contextData = ThreadContext.getImmutableContext();

    String threadName = null;
    long threadId = -1;
    if (captureExperimentalAttributes) {
      Thread currentThread = Thread.currentThread();
      threadName = currentThread.getName();
      threadId = currentThread.getId();
    }
    mapper.mapLogEvent(
        builder, message, level, marker, throwable, contextData, threadName, threadId);
    builder.setTimestamp(Instant.now());
    builder.emit();
  }

  private enum ContextDataAccessorImpl implements ContextDataAccessor<Map<String, String>> {
    INSTANCE;

    @Override
    @Nullable
    public Object getValue(Map<String, String> contextData, String key) {
      return contextData.get(key);
    }

    @Override
    public void forEach(Map<String, String> contextData, BiConsumer<String, Object> action) {
      contextData.forEach(action);
    }
  }

  private Log4jHelper() {}
}
