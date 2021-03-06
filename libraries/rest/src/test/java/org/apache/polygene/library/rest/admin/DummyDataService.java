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

package org.apache.polygene.library.rest.admin;

import java.util.HashMap;
import org.apache.polygene.api.activation.ActivatorAdapter;
import org.apache.polygene.api.activation.Activators;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.service.ServiceComposite;
import org.apache.polygene.api.service.ServiceReference;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

@Mixins( DummyDataService.DummyDataMixin.class )
@Activators( DummyDataService.Activator.class )
public interface DummyDataService
    extends ServiceComposite
{
    
    void insertInitialData()
            throws Exception;

    class Activator
            extends ActivatorAdapter<ServiceReference<DummyDataService>>
    {

        @Override
        public void afterActivation( ServiceReference<DummyDataService> activated )
                throws Exception
        {
            activated.get().insertInitialData();
        }

    }
    
    abstract class DummyDataMixin
        implements DummyDataService
    {
        @Structure
        UnitOfWorkFactory uowf;

        @Structure
        ValueBuilderFactory vbf;

        public void insertInitialData()
            throws Exception
        {
            UnitOfWork unitOfWork = uowf.newUnitOfWork();
            try
            {
                {
                    ValueBuilder<TestValue> valueBuilder = vbf.newValueBuilder( TestValue.class );
                    valueBuilder.prototype().longList().get().add( 42L );
                    valueBuilder.prototype().string().set( "Foo bar value" );
                    valueBuilder.prototype().map().set( new HashMap() );

                    EntityBuilder<TestEntity> builder = unitOfWork.newEntityBuilder( TestEntity.class, StringIdentity.identityOf( "test1" ) );
                    builder.instance().name().set( "Foo bar" );
                    builder.instance().age().set( 42 );
                    builder.instance().value().set( valueBuilder.newInstance() );
                    TestEntity testEntity = builder.newInstance();

                    EntityBuilder<TestEntity> builder2 = unitOfWork.newEntityBuilder( TestEntity.class, StringIdentity.identityOf( "test2" ) );
                    builder2.instance().name().set( "Xyzzy" );
                    builder2.instance().age().set( 12 );
                    builder2.instance().association().set( testEntity );
                    builder2.instance().manyAssociation().add( 0, testEntity );
                    builder2.instance().manyAssociation().add( 0, testEntity );

                    EntityBuilder<TestRole> builder3 = unitOfWork.newEntityBuilder( TestRole.class );
                    builder3.instance().name().set( "A role" );
                    TestRole testRole = builder3.newInstance();

                    builder2.newInstance();
                }

                {
                    EntityBuilder<TestEntity2> builder = unitOfWork.newEntityBuilder( TestEntity2.class, StringIdentity.identityOf( "test3" ) );
                    builder.instance().name().set( "Test3" );
                    builder.newInstance();
                }

                unitOfWork.complete();
            }
            finally
            {
                unitOfWork.discard();
            }
        }

    }
}
