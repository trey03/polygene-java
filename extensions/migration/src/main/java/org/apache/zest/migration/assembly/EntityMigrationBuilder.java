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
package org.apache.zest.migration.assembly;

import java.util.Map;
import org.apache.zest.migration.operation.AddAssociation;
import org.apache.zest.migration.operation.AddManyAssociation;
import org.apache.zest.migration.operation.AddNamedAssociation;
import org.apache.zest.migration.operation.AddProperty;
import org.apache.zest.migration.operation.RemoveAssociation;
import org.apache.zest.migration.operation.RemoveManyAssociation;
import org.apache.zest.migration.operation.RemoveNamedAssociation;
import org.apache.zest.migration.operation.RemoveProperty;
import org.apache.zest.migration.operation.RenameAssociation;
import org.apache.zest.migration.operation.RenameManyAssociation;
import org.apache.zest.migration.operation.RenameNamedAssociation;
import org.apache.zest.migration.operation.RenameProperty;

/**
 * Fluent API for creating migration rules for specific entity types.
 */
public class EntityMigrationBuilder
{
    private final VersionMigrationBuilder migrationBuilder;
    private final String[] entityTypes;

    public EntityMigrationBuilder( VersionMigrationBuilder migrationBuilder, String[] entityTypes )
    {
        this.migrationBuilder = migrationBuilder;
        this.entityTypes = entityTypes;
    }

    /**
     * Return the version builder
     *
     * @return current version builder
     */
    public VersionMigrationBuilder end()
    {
        return migrationBuilder;
    }

    // Operations on entities
    /**
     * Add rule to rename an Entity property.
     *
     * @param from property name
     * @param to   property name
     *
     * @return the builder
     */
    public EntityMigrationBuilder renameProperty( String from, String to )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new RenameProperty( from, to ) ) );

        return this;
    }

    /**
     * Add rule to add an Entity property.
     *
     * @param property     to be added
     * @param defaultValue default value
     *
     * @return the builder
     */
    public EntityMigrationBuilder addProperty( String property, Object defaultValue )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new AddProperty( property, defaultValue ) ) );

        return this;
    }

    /**
     * Add rule to remove an Entity property
     *
     * @param property     to be removed
     * @param defaultValue default value (used for downgrading)
     *
     * @return the builder
     */
    public EntityMigrationBuilder removeProperty( String property, String defaultValue )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new RemoveProperty( property, defaultValue ) ) );

        return this;
    }

    /**
     * Add rule to rename an Entity association.
     *
     * @param from assocation name
     * @param to   association name
     *
     * @return the builder
     */
    public EntityMigrationBuilder renameAssociation( String from, String to )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new RenameAssociation( from, to ) ) );

        return this;
    }

    /**
     * Add rule to add an Entity association.
     *
     * @param association      to be added
     * @param defaultReference default reference
     *
     * @return the builder
     */
    public EntityMigrationBuilder addAssociation( String association, String defaultReference )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new AddAssociation( association, defaultReference ) ) );

        return this;
    }

    /**
     * Add rule to remove an Entity association
     *
     * @param association      to be removed
     * @param defaultReference default value (used for downgrading)
     *
     * @return the builder
     */
    public EntityMigrationBuilder removeAssociation( String association, String defaultReference )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new RemoveAssociation( association, defaultReference ) ) );

        return this;
    }

    /**
     * Add rule to add an Entity many-association.
     *
     * @param association       to be added
     * @param defaultReferences default references
     *
     * @return the builder
     */
    public EntityMigrationBuilder addManyAssociation( String association, String... defaultReferences )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new AddManyAssociation( association, defaultReferences ) ) );

        return this;
    }

    /**
     * Add rule to remove an Entity many-association
     *
     * @param association       to be removed
     * @param defaultReferences default value (used for downgrading)
     *
     * @return the builder
     */
    public EntityMigrationBuilder removeManyAssociation( String association, String... defaultReferences )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new RemoveManyAssociation( association, defaultReferences ) ) );

        return this;
    }

    /**
     * Add rule to rename an Entity many-association.
     *
     * @param from many-assocation name
     * @param to   many-association name
     *
     * @return the builder
     */
    public EntityMigrationBuilder renameManyAssociation( String from, String to )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new RenameManyAssociation( from, to ) ) );

        return this;
    }

    /**
     * Add rule to add an Entity named-association.
     *
     * @param association       to be added
     * @param defaultReferences default references
     *
     * @return the builder
     */
    public EntityMigrationBuilder addNamedAssociation( String association, Map<String, String> defaultReferences )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new AddNamedAssociation( association, defaultReferences ) ) );

        return this;
    }

    /**
     * Add rule to remove an Entity named-association
     *
     * @param association       to be removed
     * @param defaultReferences default value (used for downgrading)
     *
     * @return the builder
     */
    public EntityMigrationBuilder removeNamedAssociation( String association, Map<String, String> defaultReferences )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new RemoveNamedAssociation( association, defaultReferences ) ) );

        return this;
    }

    /**
     * Add rule to rename an Entity named-association.
     *
     * @param from many-assocation name
     * @param to   many-association name
     *
     * @return the builder
     */
    public EntityMigrationBuilder renameNamedAssociation( String from, String to )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              new RenameNamedAssociation( from, to ) ) );

        return this;
    }

    /**
     * Add rule to perform a custom operation
     *
     * @param operationEntity the custom operation to be performed during migration
     *
     * @return the builder
     */
    public EntityMigrationBuilder custom( EntityMigrationOperation operationEntity )
    {
        migrationBuilder.builder.entityMigrationRules().
            addRule( new EntityMigrationRule( migrationBuilder.fromVersion,
                                              migrationBuilder.toVersion,
                                              entityTypes,
                                              operationEntity ) );

        return this;
    }
}
