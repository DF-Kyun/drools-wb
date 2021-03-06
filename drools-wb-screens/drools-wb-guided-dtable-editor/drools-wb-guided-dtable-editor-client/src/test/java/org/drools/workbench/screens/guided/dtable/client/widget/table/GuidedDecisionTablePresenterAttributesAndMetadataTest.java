/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
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

package org.drools.workbench.screens.guided.dtable.client.widget.table;

import java.util.Map;
import java.util.Set;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.drools.workbench.models.guided.dtable.shared.model.AttributeCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52.HitPolicy;
import org.drools.workbench.models.guided.dtable.shared.model.MetadataCol52;
import org.drools.workbench.screens.guided.dtable.client.resources.HitPolicyInternationalizer;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshAttributesPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.events.cdi.RefreshMetaDataPanelEvent;
import org.drools.workbench.screens.guided.dtable.client.widget.table.model.synchronizers.ModelSynchronizer;
import org.drools.workbench.screens.guided.rule.client.editor.RuleAttributeWidget;
import org.drools.workbench.services.verifier.api.client.index.Rule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GwtMockitoTestRunner.class)
public class GuidedDecisionTablePresenterAttributesAndMetadataTest
        extends BaseGuidedDecisionTablePresenterTest {

    @Captor
    private ArgumentCaptor<Set<String>> reservedAttributesCaptor;

    @Captor
    private ArgumentCaptor<Map<String, String>> callbackValueCaptor;

    @Before
    public void setup() {
        super.setup();

        dtPresenter.onAppendRow();
        dtPresenter.onAppendRow();
        dtPresenter.onAppendRow();
    }

    @Test
    public void newAttributeOrMetaDataColumn() {
        dtPresenter.newAttributeOrMetaDataColumn();

        verify(view,
               times(1)).newAttributeOrMetaDataColumn(any());
    }

    @Test
    public void isMetaDataUnique() {
        final MetadataCol52 metadata = new MetadataCol52();
        metadata.setMetadata("metadata");
        dtPresenter.getModel()
                .getMetadataCols()
                .add(metadata);

        assertFalse(dtPresenter.isMetaDataUnique("metadata"));
        assertTrue(dtPresenter.isMetaDataUnique("cheese"));
    }

    @Test
    public void getExistingAttributeNames() {
        final AttributeCol52 attribute1 = new AttributeCol52();
        attribute1.setAttribute(RuleAttributeWidget.ENABLED_ATTR);

        final AttributeCol52 attribute2 = new AttributeCol52();
        attribute2.setAttribute(RuleAttributeWidget.AUTO_FOCUS_ATTR);
        dtPresenter.getModel()
                .getAttributeCols()
                .add(attribute1);
        dtPresenter.getModel()
                .getAttributeCols()
                .add(attribute2);

        dtPresenter.newAttributeOrMetaDataColumn();

        ArgumentCaptor<Set> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        verify(view,
               times(1)).newAttributeOrMetaDataColumn(argumentCaptor.capture());

        final Set existingAttributeNames = argumentCaptor.getValue();

        assertEquals(2,
                     existingAttributeNames.size());
        assertTrue(existingAttributeNames.contains(RuleAttributeWidget.ENABLED_ATTR));
        assertTrue(existingAttributeNames.contains(RuleAttributeWidget.AUTO_FOCUS_ATTR));
    }

    @Test
    public void appendAttributeColumn() throws
            ModelSynchronizer.MoveColumnVetoException {
        reset(modellerPresenter);

        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute(RuleAttributeWidget.AUTO_FOCUS_ATTR);

        dtPresenter.appendColumn(column);

        verify(synchronizer,
               times(1)).appendColumn(eq(column));
        verify(refreshAttributesPanelEvent,
               times(1)).fire(any(RefreshAttributesPanelEvent.class));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void appendMetadataColumn() throws
            ModelSynchronizer.MoveColumnVetoException {
        reset(modellerPresenter);

        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata("metadata");

        dtPresenter.appendColumn(column);

        verify(synchronizer,
               times(1)).appendColumn(eq(column));
        verify(refreshMetaDataPanelEvent,
               times(1)).fire(any(RefreshMetaDataPanelEvent.class));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void deleteAttributeColumn() throws
            ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute(RuleAttributeWidget.AUTO_FOCUS_ATTR);
        dtPresenter.appendColumn(column);
        reset(modellerPresenter);

        dtPresenter.deleteColumn(column);

        verify(synchronizer,
               times(1)).deleteColumn(eq(column));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void deleteMetadataColumn() throws
            ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata("metadata");
        dtPresenter.appendColumn(column);
        reset(modellerPresenter);

        dtPresenter.deleteColumn(column);

        verify(synchronizer,
               times(1)).deleteColumn(eq(column));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void updateAttributeColumn() throws
            ModelSynchronizer.MoveColumnVetoException {
        final AttributeCol52 column = new AttributeCol52();
        column.setAttribute(RuleAttributeWidget.AUTO_FOCUS_ATTR);
        dtPresenter.appendColumn(column);
        reset(modellerPresenter);

        final AttributeCol52 update = new AttributeCol52();
        update.setAttribute(RuleAttributeWidget.ENABLED_ATTR);

        dtPresenter.updateColumn(column,
                                 update);

        verify(synchronizer,
               times(1)).updateColumn(eq(column),
                                      eq(update));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void updateMetadataColumn() throws
            ModelSynchronizer.MoveColumnVetoException {
        final MetadataCol52 column = new MetadataCol52();
        column.setMetadata("metadata");
        dtPresenter.appendColumn(column);
        reset(modellerPresenter);

        final MetadataCol52 update = new MetadataCol52();
        column.setMetadata("update");

        dtPresenter.updateColumn(column,
                                 update);

        verify(synchronizer,
               times(1)).updateColumn(eq(column),
                                      eq(update));
        verify(modellerPresenter,
               times(1)).updateLinks();
    }

    @Test
    public void testNewAttributeOrMetaDataColumnUniqueHitPolicy() throws Exception {
        testReservedAttributes(HitPolicy.UNIQUE_HIT, "activation-group");
    }

    @Test
    public void testNewAttributeOrMetaDataColumnFirstHitPolicy() throws Exception {
        testReservedAttributes(HitPolicy.FIRST_HIT, "activation-group", "salience");
    }

    @Test
    public void testNewAttributeOrMetaDataColumnResolvedHitPolicy() throws Exception {
        testReservedAttributes(HitPolicy.RESOLVED_HIT, "activation-group", "salience");
    }

    @Test
    public void testNewAttributeOrMetaDataColumnRuleOrderHitPolicy() throws Exception {
        testReservedAttributes(HitPolicy.RULE_ORDER, "salience");
    }

    private void testReservedAttributes(HitPolicy policy, String... attributes) {
        model.setHitPolicy(policy);
        dtPresenter.newAttributeOrMetaDataColumn();
        verify(view).newAttributeOrMetaDataColumn(reservedAttributesCaptor.capture());
        assertEquals(attributes.length,
                     reservedAttributesCaptor.getValue().size());
        for(String attribute : attributes) {
            assertTrue(reservedAttributesCaptor.getValue().contains(attribute));
        }
    }
}
