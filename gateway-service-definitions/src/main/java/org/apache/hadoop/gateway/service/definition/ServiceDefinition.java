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
package org.apache.hadoop.gateway.service.definition;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "service")
public class ServiceDefinition {

  private String name;

  private String role;

  private String version;

  private List<Route> routes;

  private List<Policy> policies;

  private CustomDispatch dispatch;

  @XmlAttribute
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlAttribute
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @XmlAttribute
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  @XmlElement(name = "route")
  @XmlElementWrapper(name = "routes")
  public List<Route> getRoutes() {
    return routes;
  }

  public void setRoutes(List<Route> routes) {
    this.routes = routes;
  }

  @XmlElement(name = "policy")
  @XmlElementWrapper(name = "policies")
  public List<Policy> getPolicies() {
    return policies;
  }

  public void setPolicies(List<Policy> policies) {
    this.policies = policies;
  }

  @XmlElement(name = "dispatch")
  public CustomDispatch getDispatch() {
    return dispatch;
  }

  public void setDispatch(CustomDispatch dispatch) {
    this.dispatch = dispatch;
  }
}
