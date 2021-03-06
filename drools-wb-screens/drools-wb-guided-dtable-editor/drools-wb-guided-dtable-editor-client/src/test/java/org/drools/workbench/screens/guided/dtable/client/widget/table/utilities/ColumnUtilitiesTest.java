/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.workbench.screens.guided.dtable.client.widget.table.utilities;

import org.drools.workbench.models.datamodel.oracle.DataType;
import org.drools.workbench.models.datamodel.rule.BaseSingleFieldConstraint;
import org.drools.workbench.models.guided.dtable.shared.model.ConditionCol52;
import org.drools.workbench.models.guided.dtable.shared.model.GuidedDecisionTable52;
import org.drools.workbench.models.guided.dtable.shared.model.Pattern52;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.workbench.common.widgets.client.datamodel.AsyncPackageDataModelOracle;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ColumnUtilitiesTest {

    private static final String FACT_TYPE = "MyFactType";

    private static final String FIELD_NAME = "myField";

    @Mock
    private GuidedDecisionTable52 model;

    @Mock
    private AsyncPackageDataModelOracle oracle;

    private Pattern52 pattern;
    private ConditionCol52 column;
    private ColumnUtilities utilities;

    @Before
    public void setUp() {
        utilities = new ColumnUtilities(model,
                                        oracle);
        pattern = new Pattern52();
        column = new ConditionCol52();
        when(model.getPattern(column)).thenReturn(pattern);
    }

    @Test
    public void getTypeSafeType_operatorIn() {
        column.setOperator("in");
        check();
    }

    @Test
    public void getTypeSafeType_operatorNotIn() {
        column.setOperator("not in");
        check();
    }

    @Test
    public void unknownDataTypeDefaultsToString() {
        assertEquals(DataType.TYPE_STRING,
                     utilities.getType(column));
    }

    @Test
    public void knownDataTypeWithoutOperator() {
        pattern.setFactType(FACT_TYPE);
        column.setFactField(FIELD_NAME);
        column.setOperator(null);

        assertEquals(DataType.TYPE_STRING,
                     utilities.getType(column));
    }

    @Test
    public void knownDataTypeWithOperator() {
        pattern.setFactType(FACT_TYPE);
        column.setFactField(FIELD_NAME);
        column.setOperator("==");
        when(oracle.getFieldType(eq(FACT_TYPE),
                                 eq(FIELD_NAME))).thenReturn(DataType.TYPE_NUMERIC_INTEGER);

        assertEquals(DataType.TYPE_NUMERIC_INTEGER,
                     utilities.getType(column));
    }

    @Test
    public void testCanAcceptOtherwisePredicate() throws Exception {
        column.setConstraintValueType(BaseSingleFieldConstraint.TYPE_PREDICATE);
        assertFalse(ColumnUtilities.canAcceptOtherwiseValues(column));
    }

    @Test
    public void testCanAcceptOtherwiseEqual() throws Exception {
        column.setConstraintValueType(BaseSingleFieldConstraint.TYPE_LITERAL);
        column.setOperator("==");
        assertTrue(ColumnUtilities.canAcceptOtherwiseValues(column));
    }

    @Test
    public void testCanAcceptOtherwiseNotEqual() throws Exception {
        column.setConstraintValueType(BaseSingleFieldConstraint.TYPE_LITERAL);
        column.setOperator("!=");
        assertTrue(ColumnUtilities.canAcceptOtherwiseValues(column));
    }

    @Test
    public void testCanAcceptOtherwiseWrongOperator() throws Exception {
        column.setConstraintValueType(BaseSingleFieldConstraint.TYPE_LITERAL);
        column.setOperator("<");
        assertFalse(ColumnUtilities.canAcceptOtherwiseValues(column));
    }

    private void check() {
        assertEquals(DataType.DataTypes.STRING,
                     utilities.getTypeSafeType(pattern,
                                               column));
        assertEquals(DataType.DataTypes.STRING,
                     utilities.getTypeSafeType(column));
        assertEquals(DataType.TYPE_STRING,
                     utilities.getType(column));
        verify(oracle,
               never()).getFieldType(anyString(),
                                     anyString());
    }
}