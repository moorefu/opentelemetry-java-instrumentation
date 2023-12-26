/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.alibabadubbo.v2_6;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Result;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.api.instrumenter.Instrumenter;

/** Entrypoint for instrumenting Apache Dubbo servers and clients. */
public final class DubboTelemetry {

  /** Returns a new {@link DubboTelemetry} configured with the given {@link OpenTelemetry}. */
  public static DubboTelemetry create(OpenTelemetry openTelemetry) {
    return builder(openTelemetry).build();
  }

  /**
   * Returns a new {@link DubboTelemetryBuilder} configured with the given {@link OpenTelemetry}.
   */
  public static DubboTelemetryBuilder builder(OpenTelemetry openTelemetry) {
    return new DubboTelemetryBuilder(openTelemetry);
  }

  private final Instrumenter<DubboRequest, Result> serverInstrumenter;
  private final Instrumenter<DubboRequest, Result> clientInstrumenter;

  DubboTelemetry(
      Instrumenter<DubboRequest, Result> serverInstrumenter,
      Instrumenter<DubboRequest, Result> clientInstrumenter) {
    this.serverInstrumenter = serverInstrumenter;
    this.clientInstrumenter = clientInstrumenter;
  }

  /** Returns a new Dubbo {@link Filter} that traces Dubbo RPC invocations. */
  public Filter newFilter() {
    return new TracingFilter(serverInstrumenter, clientInstrumenter);
  }
}
