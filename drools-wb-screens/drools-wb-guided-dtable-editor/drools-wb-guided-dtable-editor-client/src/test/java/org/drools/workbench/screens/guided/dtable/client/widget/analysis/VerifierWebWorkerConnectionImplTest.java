/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.drools.workbench.screens.guided.dtable.client.widget.analysis;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.services.verifier.plugin.client.api.Initialize;
import org.drools.workbench.services.verifier.plugin.client.api.RequestStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


@RunWith(GwtMockitoTestRunner.class)
public class VerifierWebWorkerConnectionImplTest {


    private VerifierWebWorkerConnection verifierWebWorkerConnection;

    @Mock
    private Initialize initialize;

    @Mock
    private Poster poster;

    @Mock
    private Receiver receiver;

    @Before
    public void setUp() throws
                        Exception {
        verifierWebWorkerConnection = new VerifierWebWorkerConnectionImpl( initialize,
                                                                           poster,
                                                                           receiver );
    }

    @Test
    public void firstActivationStartWebWorker() throws
                                                Exception {
        verifierWebWorkerConnection.activate();

        verify( receiver ).activate();
        verify( receiver ).setUp( any() );

        verify( poster ).setUp( any() );
        verify( poster ).post( initialize );
    }

    @Test
    public void secondActivationDoesNotStartWebWorker() throws
                                                        Exception {
        verifierWebWorkerConnection.activate();

        reset( receiver,
               poster );

        verifierWebWorkerConnection.activate();

        verify( receiver ).activate();
        verify( receiver,
                never() ).setUp( any() );

        verify( poster,
                never() ).setUp( any() );
        verify( poster ).post( any( RequestStatus.class ) );
    }

    @Test
    public void terminateCanBeCalledEvenIfWorkerHasNotBeenActivated() throws
                                                                      Exception {

        verifierWebWorkerConnection.terminate();

        assertTrue( "You shall pass!",
                    true );
    }
}