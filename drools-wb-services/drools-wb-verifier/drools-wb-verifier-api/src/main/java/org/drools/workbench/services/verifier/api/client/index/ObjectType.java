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

import org.drools.workbench.services.verifier.api.client.configuration.AnalyzerConfiguration;
import org.drools.workbench.services.verifier.api.client.index.keys.Key;
import org.drools.workbench.services.verifier.api.client.index.keys.UUIDKey;
import org.drools.workbench.services.verifier.api.client.index.matchers.UUIDMatchers;
import org.drools.workbench.services.verifier.api.client.maps.KeyDefinition;
import org.drools.workbench.services.verifier.api.client.maps.util.HasKeys;

public class ObjectType
        implements HasKeys {

    private final static KeyDefinition TYPE = KeyDefinition.newKeyDefinition()
            .withId( "type" )
            .build();

    private final UUIDKey uuidKey;
    private final String type;
    private final ObjectFields fields = new ObjectFields();

    public ObjectType( final String type,
                       final AnalyzerConfiguration configuration ) {
        this.type = type;
        this.uuidKey = configuration.getUUID( this );
    }

    public static Matchers type() {
        return new Matchers( TYPE );
    }

    public static Matchers uuid() {
        return new UUIDMatchers();
    }

    public static KeyDefinition[] keyDefinitions() {
        return new KeyDefinition[]{
                UUIDKey.UNIQUE_UUID,
                TYPE
        };
    }

    @Override
    public UUIDKey getUuidKey() {
        return uuidKey;
    }

    public String getType() {
        return type;
    }

    public ObjectFields getFields() {
        return fields;
    }

    @Override
    public Key[] keys() {
        return new Key[]{
                uuidKey,
                new Key( TYPE,
                         type )
        };
    }
}
