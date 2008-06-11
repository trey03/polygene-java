/*
 * Copyright 2006 Niclas Hedhman.
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
package org.qi4j.logging;

import org.junit.Test;
import org.qi4j.bootstrap.AssemblyException;
import org.qi4j.bootstrap.ModuleAssembly;
import org.qi4j.composite.Composite;
import org.qi4j.composite.CompositeBuilder;
import org.qi4j.composite.CompositeBuilderFactory;
import org.qi4j.composite.ConcernOf;
import org.qi4j.composite.Concerns;
import org.qi4j.composite.Mixins;
import org.qi4j.composite.scope.Structure;
import org.qi4j.logging.service.LogServiceComposite;
import org.qi4j.service.ServiceDescriptor;
import org.qi4j.service.ServiceInstanceFactory;
import org.qi4j.service.ServiceInstanceProviderException;
import org.qi4j.test.AbstractQi4jTest;

public class TracingTest
    extends AbstractQi4jTest
{
    public void assemble( ModuleAssembly module )
        throws AssemblyException
    {
        module.addComposites( SomeComposite.class );
        module.addServices( LogServiceComposite.class );
    }

    @Test
    public void testTrace()
        throws Exception
    {
        SomeComposite sc = compositeBuilderFactory.newCompositeBuilder( SomeComposite.class ).newInstance();
        sc.doSomethingImportant();
        sc.doSomethingLessImportant();
    }

    @Mixins( SomeMixin.class )
    @Concerns( { SomeConcern.class, TraceAllConcern.class } )
    public interface SomeComposite extends Composite, Some
    {
    }

    public interface Some
    {
        @Trace int doSomethingImportant();

        int doSomethingLessImportant();
    }

    public static class SomeConcern extends ConcernOf<Some>
        implements Some
    {
        public int doSomethingImportant()
        {
            System.out.println( "-- doSomethingImportant()" );
            return next.doSomethingImportant();
        }

        public int doSomethingLessImportant()
        {
            System.out.println( "-- doSomethingLessImportant()" );
            return next.doSomethingLessImportant();
        }
    }

    public static class SomeMixin
        implements Some
    {

        public int doSomethingImportant()
        {
            System.out.println( "---- doSomethingImportant()" );
            return 123;
        }

        public int doSomethingLessImportant()
        {
            System.out.println( "---- doSomethingLessImportant()" );
            return 456;
        }
    }

    public static class LogServiceProvider
        implements ServiceInstanceFactory
    {
        @Structure CompositeBuilderFactory factory;

        public Object newInstance( ServiceDescriptor serviceDescriptor ) throws ServiceInstanceProviderException
        {
            CompositeBuilder<LogServiceComposite> builder = factory.newCompositeBuilder( LogServiceComposite.class );
            LogService props = builder.stateFor( LogService.class );
            props.traceLevel().set( Trace.NORMAL );
            props.debugLevel().set( Debug.NORMAL );
            return builder.newInstance();
        }

        public void releaseInstance( Object instance )
        {
        }
    }
}
