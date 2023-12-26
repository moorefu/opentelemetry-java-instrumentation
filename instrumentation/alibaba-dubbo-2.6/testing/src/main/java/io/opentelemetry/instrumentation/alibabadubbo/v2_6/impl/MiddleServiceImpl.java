/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.alibabadubbo.v2_6.impl;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import io.opentelemetry.instrumentation.alibabadubbo.v2_6.api.MiddleService;

public class MiddleServiceImpl implements MiddleService {

  private final ReferenceConfig<?> referenceConfig;

  public MiddleServiceImpl(ReferenceConfig<?> referenceConfig) {
    this.referenceConfig = referenceConfig;
  }

  @Override
  public String hello(String hello) {
    GenericService genericService = (GenericService) referenceConfig.get();
    return genericService
        .$invoke("hello", new String[] {String.class.getName()}, new Object[] {hello})
        .toString();
  }
}
