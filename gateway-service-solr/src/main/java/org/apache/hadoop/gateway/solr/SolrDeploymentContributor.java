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
package org.apache.hadoop.gateway.solr;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.gateway.deploy.DeploymentContext;
import org.apache.hadoop.gateway.deploy.ServiceDeploymentContributorBase;
import org.apache.hadoop.gateway.descriptor.FilterParamDescriptor;
import org.apache.hadoop.gateway.descriptor.ResourceDescriptor;
import org.apache.hadoop.gateway.filter.rewrite.api.UrlRewriteRulesDescriptor;
import org.apache.hadoop.gateway.filter.rewrite.api.UrlRewriteRulesDescriptorFactory;
import org.apache.hadoop.gateway.filter.rewrite.api.UrlRewriteServletFilter;
import org.apache.hadoop.gateway.topology.Service;

public class SolrDeploymentContributor extends ServiceDeploymentContributorBase {

  private static final String RULES_RESOURCE = SolrDeploymentContributor.class.getName().replace( '.', '/' ) + "/rewrite.xml";
  private static final String SOLR_EXTERNAL_PATH = "/solr";

  @Override
  public String getRole() {
    return "SOLR";
  }

  @Override
  public String getName() {
    return "solr";
  }

  @Override
  public void contributeService( DeploymentContext context, Service service ) throws Exception {
    contributeRewriteRules( context, service );
    contributeResources( context, service );
  }

  private void contributeRewriteRules( DeploymentContext context, Service service ) throws IOException {
    UrlRewriteRulesDescriptor solrRules = loadRulesFromTemplate();
    UrlRewriteRulesDescriptor clusterRules = context.getDescriptor( "rewrite" );
    clusterRules.addRules( solrRules );
  }

  private UrlRewriteRulesDescriptor loadRulesFromTemplate() throws IOException {
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream( RULES_RESOURCE );
    Reader reader = new InputStreamReader( stream );
    UrlRewriteRulesDescriptor rules = UrlRewriteRulesDescriptorFactory.load( "xml", reader );
    reader.close();
    stream.close();
    return rules;
  }

  private void contributeResources( DeploymentContext context, Service service ) throws URISyntaxException {
    List<FilterParamDescriptor> params;
    ResourceDescriptor rootResource = context.getGatewayDescriptor().addResource();
    rootResource.role( service.getRole() );
    rootResource.pattern( SOLR_EXTERNAL_PATH + "/?**" );
    addWebAppSecFilters( context, service, rootResource );
    addAuthenticationFilter( context, service, rootResource );
    addRewriteFilter(context, service, rootResource, null );
    addIdentityAssertionFilter( context, service, rootResource );
    addAuthorizationFilter( context, service, rootResource );
    addDispatchFilter( context, service, rootResource );

    ResourceDescriptor pathResource = context.getGatewayDescriptor().addResource();
    pathResource.role( service.getRole() );
    pathResource.pattern( SOLR_EXTERNAL_PATH + "/**?**" );
    addWebAppSecFilters( context, service, pathResource );
    addAuthenticationFilter( context, service, pathResource );
    params = new ArrayList<FilterParamDescriptor>();
    params.add( rootResource.createFilterParam().
            name( UrlRewriteServletFilter.REQUEST_URL_RULE_PARAM ).value( "SOLR/solr/path/outbound" ) );

    addRewriteFilter(context, service, pathResource, null );
    addIdentityAssertionFilter( context, service, pathResource );
    addAuthorizationFilter( context, service, pathResource );
    addDispatchFilter( context, service, pathResource );
  }

  private void addDispatchFilter(
      DeploymentContext context, Service service, ResourceDescriptor resource ) {
    context.contributeFilter( service, resource, "dispatch", "http-client", null );
  }
}
