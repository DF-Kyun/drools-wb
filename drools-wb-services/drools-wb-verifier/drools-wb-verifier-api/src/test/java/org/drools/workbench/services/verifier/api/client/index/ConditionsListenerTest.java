/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.services.verifier.api.client.index;

import org.drools.workbench.services.verifier.api.client.index.keys.Values;
import org.drools.workbench.services.verifier.api.client.index.select.AllListener;
import org.drools.workbench.services.verifier.api.client.AnalyzerConfigurationMock;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ConditionsListenerTest {

    private Conditions conditions;
    private AllListener allListener;
    private AnalyzerConfigurationMock configuration;

    @Before
    public void setUp() throws
                        Exception {
        configuration = new AnalyzerConfigurationMock();

        conditions = new Conditions();

        allListener = mock( AllListener.class );
        conditions
                .where( Condition.value()
                                .any() )
                .listen()
                .all( allListener );
    }

    @Test
    public void testListen() throws
                             Exception {
        conditions.add( new FieldCondition( new Field( mock( ObjectField.class ),
                                                       "Person",
                                                       "String",
                                                       "name",
                                                       configuration ),
                                            new Column( 1,
                                                        configuration ),
                                            "==",
                                            new Values<>( 10 ),
                                            configuration ) );

        verify( allListener ).onAllChanged( anyCollection() );
    }

    @Test
    public void testUpdate() throws
                             Exception {
        final Condition condition = new FieldCondition( new Field( mock( ObjectField.class ),
                                                                   "Person",
                                                                   "String",
                                                                   "name",
                                                                   configuration ),
                                                        new Column( 1,
                                                                    configuration ),
                                                        "==",
                                                        new Values<>( 10 ),
                                                        configuration );
        conditions.add( condition );

        reset( allListener );

        condition.setValue( new Values<>( 20 ) );

        verify( allListener ).onAllChanged( anyCollection() );
    }

}