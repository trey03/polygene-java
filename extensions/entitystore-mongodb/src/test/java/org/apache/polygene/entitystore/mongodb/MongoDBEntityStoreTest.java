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
package org.apache.polygene.entitystore.mongodb;

import com.mongodb.Mongo;
import org.apache.polygene.api.common.Visibility;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.entitystore.mongodb.assembly.MongoDBEntityStoreAssembler;
import org.apache.polygene.test.EntityTestAssembler;
import org.apache.polygene.test.docker.DockerRule;
import org.apache.polygene.test.entity.AbstractEntityStoreTest;
import org.junit.ClassRule;

/**
 * Test the MongoDBEntityStoreService.
 */
public class MongoDBEntityStoreTest extends AbstractEntityStoreTest
{
    @ClassRule
    public static final DockerRule DOCKER = new DockerRule( "mongo", 27017 );

    @Override
    // START SNIPPET: assembly
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        // END SNIPPET: assembly
        super.assemble( module );

        ModuleAssembly config = module.layer().module( "config" );
        new EntityTestAssembler().defaultServicesVisibleIn( Visibility.layer ).assemble( config );

        // START SNIPPET: assembly
        new MongoDBEntityStoreAssembler().withConfig( config, Visibility.layer ).assemble( module );
        // END SNIPPET: assembly

        MongoDBEntityStoreConfiguration mongoConfig = config.forMixin( MongoDBEntityStoreConfiguration.class ).declareDefaults();
        mongoConfig.writeConcern().set( MongoDBEntityStoreConfiguration.WriteConcern.MAJORITY );
        mongoConfig.database().set( "polygene:test" );
        mongoConfig.collection().set( "polygene:test:entities" );
        mongoConfig.hostname().set( DOCKER.getDockerHost() );
        mongoConfig.port().set( DOCKER.getExposedContainerPort( "27017/tcp" ) );
        // START SNIPPET: assembly
    }

    // END SNIPPET: assembly
    private Mongo mongo;
    private String dbName;

    @Override
    public void setUp()
        throws Exception
    {
        super.setUp();
        MongoDBEntityStoreService es = serviceFinder.findService( MongoDBEntityStoreService.class ).get();
        mongo = es.mongoInstanceUsed();
        dbName = es.dbInstanceUsed().getName();
    }

    @Override
    public void tearDown()
        throws Exception
    {
        mongo.dropDatabase( dbName );
        super.tearDown();
    }
}
