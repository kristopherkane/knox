/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.gateway.descriptor.impl;

import org.apache.hadoop.gateway.descriptor.FilterDescriptor;
import org.apache.hadoop.gateway.descriptor.FilterParamDescriptor;
import org.apache.hadoop.gateway.descriptor.GatewayDescriptor;
import org.apache.hadoop.gateway.descriptor.ResourceDescriptor;
import org.apache.hadoop.gateway.descriptor.ResourceParamDescriptor;

import java.util.ArrayList;
import java.util.List;

public class ResourceDescriptorImpl implements ResourceDescriptor {

  private GatewayDescriptor parent;
  private String role;
  private String pattern;
  private List<ResourceParamDescriptor> params;
  private List<FilterDescriptor> filters;

  ResourceDescriptorImpl( GatewayDescriptor parent ) {
    this.parent = parent;
    this.params = new ArrayList<ResourceParamDescriptor>();
    this.filters = new ArrayList<FilterDescriptor>();
  }

  @Override
  public GatewayDescriptor up() {
    return parent;
  }

  public ResourceDescriptor role( String role ) {
    this.role = role;
    return this;
  }

  public String role() {
    return role;
  }

  public ResourceDescriptor pattern( String pattern ) {
    this.pattern = pattern;
    return this;
  }

  public String pattern() {
    return pattern;
  }

  @Override
  public List<FilterDescriptor> filters() {
    return filters;
  }

  @Override
  public FilterDescriptor addFilter() {
    FilterDescriptor filter = createFilter();
    addFilter( filter );
    return filter;
  }

  @Override
  public FilterDescriptor createFilter() {
    return new FilterDescriptorImpl( this );
  }

  @Override
  public void addFilter( FilterDescriptor filter ) {
    filters.add( filter );
  }

  @Override
  public void addFilters( List<FilterDescriptor> filters ) {
    this.filters.addAll( filters );
  }

  @Override
  public FilterParamDescriptor createFilterParam() {
    return new FilterParamDescriptorImpl();
  }

  @Override
  public List<ResourceParamDescriptor> params() {
    return params;
  }

  @Override
  public ResourceParamDescriptor addParam() {
    ResourceParamDescriptor param = createParam();
    addParam( param );
    return param;
  }

  @Override
  public ResourceParamDescriptor createParam() {
    return new ResourceParamDescriptorImpl( this );
  }

  @Override
  public void addParam( ResourceParamDescriptor param ) {
    param.up( this );
    params.add( param );
  }

  @Override
  public void addParams( List<ResourceParamDescriptor> params ) {
    if( params != null ) {
      for( ResourceParamDescriptor param : params ) {
        param.up( this );
      }
      this.params.addAll( params );
    }
  }

}
