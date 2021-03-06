/*
 *  Copyright 2012-2013 Eurocommercial Properties NV
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.estatio.integration.glue;

import cucumber.api.java.After;
import cucumber.api.java.Before;

import org.apache.log4j.PropertyConfigurator;

import org.apache.isis.core.specsupport.scenarios.ScenarioExecutionScope;
import org.apache.isis.core.specsupport.specs.CukeGlueAbstract;

import org.estatio.integration.EstatioSystemInitializer;

public class BootstrappingForIntegrationScopeGlue extends CukeGlueAbstract {

    @Before(value={"@integration"}, order=100)
    public void beforeScenarioIntegrationScope() {
        PropertyConfigurator.configure("logging.properties");
        EstatioSystemInitializer.initIsft();
        
        before(ScenarioExecutionScope.INTEGRATION);
    }

    @After("@integration")
    public void afterScenario(cucumber.api.Scenario sc) {
        after(sc);
    }

}
