/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.alibabadubbo.v2_6.impl;

import io.opentelemetry.instrumentation.alibabadubbo.v2_6.api.HelloService;

public class HelloServiceImpl implements HelloService {
  @Override
  public String hello(String hello) {
    return hello;
  }
}
