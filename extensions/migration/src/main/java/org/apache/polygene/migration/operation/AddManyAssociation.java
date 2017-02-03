/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package org.apache.polygene.migration.operation;

import java.util.Arrays;
import javax.json.JsonObject;
import org.apache.polygene.migration.assembly.MigrationContext;
import org.apache.polygene.migration.Migrator;
import org.apache.polygene.migration.assembly.EntityMigrationOperation;
import org.apache.polygene.spi.entitystore.helpers.StateStore;

/**
 * Add a many-association
 */
public class AddManyAssociation
    implements EntityMigrationOperation
{
    private String association;
    private String[] defaultReferences;

    public AddManyAssociation( String association, String... defaultReferences )
    {
        this.association = association;
        this.defaultReferences = defaultReferences;
    }

    @Override
    public JsonObject upgrade( MigrationContext context, JsonObject state, StateStore stateStore, Migrator migrator )
    {
        return migrator.addManyAssociation( context, state, association, defaultReferences );
    }

    @Override
    public JsonObject downgrade( MigrationContext context, JsonObject state, StateStore stateStore, Migrator migrator )
    {
        return migrator.removeManyAssociation( context, state, association );
    }

    @Override
    public String toString()
    {
        return "Add many-association " + association + ", default:" + Arrays.asList( defaultReferences );
    }
}
