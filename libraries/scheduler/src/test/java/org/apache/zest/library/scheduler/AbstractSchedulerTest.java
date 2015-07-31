/*
 * Copyright (c) 2010-2014, Paul Merlin.
 *
 * Licensed  under the  Apache License,  Version 2.0  (the "License");
 * you may not use  this file  except in  compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed  under the  License is distributed on an "AS IS" BASIS,
 * WITHOUT  WARRANTIES OR CONDITIONS  OF ANY KIND, either  express  or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.zest.library.scheduler;

import org.apache.zest.api.entity.EntityBuilder;
import org.apache.zest.api.unitofwork.UnitOfWork;
import org.apache.zest.bootstrap.AssemblyException;
import org.apache.zest.bootstrap.ModuleAssembly;
import org.apache.zest.index.rdf.assembly.RdfMemoryStoreAssembler;
import org.apache.zest.test.AbstractZestTest;
import org.apache.zest.test.EntityTestAssembler;

public abstract class AbstractSchedulerTest
    extends AbstractZestTest
{
    @Override
    public final void assemble( ModuleAssembly assembly )
        throws AssemblyException
    {
        assembly.entities( FooTask.class );

        new EntityTestAssembler().assemble( assembly );
        new RdfMemoryStoreAssembler().assemble( assembly );

        onAssembly( assembly );
    }

    protected abstract void onAssembly( ModuleAssembly module )
        throws AssemblyException;

    protected final FooTask createFooTask( UnitOfWork uow, String name, String input )
    {
        EntityBuilder<FooTask> builder = uow.newEntityBuilder( FooTask.class );
        FooTask task = builder.instance();
        task.name().set( name );
        task.input().set( input );
        return builder.newInstance();
    }
}