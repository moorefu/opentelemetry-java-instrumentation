/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.alibabadubbo.v2_6;

import com.alibaba.dubbo.rpc.Result;
import io.opentelemetry.instrumentation.api.instrumenter.network.NetworkAttributesGetter;
import java.net.InetSocketAddress;
import javax.annotation.Nullable;

final class DubboNetworkServerAttributesGetter
    implements NetworkAttributesGetter<DubboRequest, Result> {

  @Nullable
  @Override
  public InetSocketAddress getNetworkLocalInetSocketAddress(
      DubboRequest request, @Nullable Result result) {
    return request.localAddress();
  }

  @Override
  @Nullable
  public InetSocketAddress getNetworkPeerInetSocketAddress(
      DubboRequest request, @Nullable Result result) {
    return request.remoteAddress();
  }
}
