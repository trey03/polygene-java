/*
 * Copyright (c) 2007, Rickard Öberg. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.zest.runtime.injection.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;
import org.apache.zest.api.service.NoSuchServiceException;
import org.apache.zest.api.service.ServiceReference;
import org.apache.zest.api.service.qualifier.Qualifier;
import org.apache.zest.api.util.Annotations;
import org.apache.zest.api.util.Classes;
import org.apache.zest.bootstrap.InvalidInjectionException;
import org.apache.zest.functional.Iterables;
import org.apache.zest.functional.Specification;
import org.apache.zest.functional.Specifications;
import org.apache.zest.runtime.injection.DependencyModel;
import org.apache.zest.runtime.injection.InjectionContext;
import org.apache.zest.runtime.injection.InjectionProvider;
import org.apache.zest.runtime.injection.InjectionProviderFactory;
import org.apache.zest.runtime.model.Resolution;

import static org.apache.zest.api.util.Annotations.hasAnnotation;
import static org.apache.zest.functional.Iterables.filter;
import static org.apache.zest.functional.Iterables.first;
import static org.apache.zest.functional.Iterables.iterable;

public final class ServiceInjectionProviderFactory
    implements InjectionProviderFactory
{
    @Override
    @SuppressWarnings( "unchecked" )
    public InjectionProvider newInjectionProvider( Resolution resolution, DependencyModel dependencyModel )
        throws InvalidInjectionException
    {
        // TODO This could be changed to allow multiple @Qualifier annotations
        Annotation qualifierAnnotation = first( filter( Specifications.translate( Annotations.type(), hasAnnotation( Qualifier.class ) ), iterable( dependencyModel
                                                                                                                                                        .annotations() ) ) );
        Specification<ServiceReference<?>> serviceQualifier = null;
        if( qualifierAnnotation != null )
        {
            Qualifier qualifier = qualifierAnnotation.annotationType().getAnnotation( Qualifier.class );
            try
            {
                serviceQualifier = qualifier.value().newInstance().qualifier( qualifierAnnotation );
            }
            catch( Exception e )
            {
                throw new InvalidInjectionException( "Could not instantiate qualifier serviceQualifier", e );
            }
        }

        if( dependencyModel.rawInjectionType().equals( Iterable.class ) )
        {
            Type iterableType = ( (ParameterizedType) dependencyModel.injectionType() ).getActualTypeArguments()[ 0 ];
            if( Classes.RAW_CLASS.apply( iterableType ).equals( ServiceReference.class ) )
            {
                // @Service Iterable<ServiceReference<MyService<Foo>> serviceRefs
                Type serviceType = ( (ParameterizedType) iterableType ).getActualTypeArguments()[ 0 ];

                return new IterableServiceReferenceProvider( serviceType, serviceQualifier );
            }
            else
            {
                // @Service Iterable<MyService<Foo>> services
                return new IterableServiceProvider( iterableType, serviceQualifier );
            }
        }
        else if( dependencyModel.rawInjectionType().equals( ServiceReference.class ) )
        {
            // @Service ServiceReference<MyService<Foo>> serviceRef
            Type referencedType = ( (ParameterizedType) dependencyModel.injectionType() ).getActualTypeArguments()[ 0 ];
            return new ServiceReferenceProvider( referencedType, serviceQualifier );
        }
        else
        {
            // @Service MyService<Foo> service
            return new ServiceProvider( dependencyModel.injectionType(), serviceQualifier );
        }
    }

    private static class IterableServiceReferenceProvider
        extends ServiceInjectionProvider
    {
        private IterableServiceReferenceProvider( Type serviceType,
                                                  Specification<ServiceReference<?>> serviceQualifier
        )
        {
            super( serviceType, serviceQualifier );
        }

        @Override
        public synchronized Object provideInjection( InjectionContext context )
            throws InjectionProviderException
        {
            return getServiceReferences( context );
        }
    }

    private static class IterableServiceProvider
        extends ServiceInjectionProvider
        implements Function<ServiceReference<?>, Object>
    {
        private IterableServiceProvider( Type serviceType,
                                         Specification<ServiceReference<?>> serviceQualifier
        )
        {
            super( serviceType, serviceQualifier );
        }

        @Override
        public synchronized Object provideInjection( final InjectionContext context )
            throws InjectionProviderException
        {
            return Iterables.map( this, getServiceReferences( context ) );
        }

        @Override
        public Object apply( ServiceReference<?> objectServiceReference )
        {
            return objectServiceReference.get();
        }
    }

    private static class ServiceReferenceProvider
        extends ServiceInjectionProvider
    {
        ServiceReferenceProvider( Type serviceType, Specification<ServiceReference<?>> qualifier )
        {
            super( serviceType, qualifier );
        }

        @Override
        public synchronized Object provideInjection( InjectionContext context )
            throws InjectionProviderException
        {
            return getServiceReference( context );
        }
    }

    private static class ServiceProvider
        extends ServiceInjectionProvider
    {
        ServiceProvider( Type serviceType, Specification<ServiceReference<?>> qualifier )
        {
            super( serviceType, qualifier );
        }

        @Override
        public synchronized Object provideInjection( InjectionContext context )
            throws InjectionProviderException
        {
            ServiceReference<?> ref = getServiceReference( context );

            if( ref != null )
            {
                return ref.get();
            }
            else
            {
                return null;
            }
        }
    }

    private abstract static class ServiceInjectionProvider
        implements InjectionProvider
    {
        private final Type serviceType;
        private final Specification<ServiceReference<?>> serviceQualifier;

        private ServiceInjectionProvider( Type serviceType,
                                            Specification<ServiceReference<?>> serviceQualifier
        )
        {
            this.serviceType = serviceType;
            this.serviceQualifier = serviceQualifier;
        }

        protected ServiceReference<Object> getServiceReference( InjectionContext context )
        {
            try
            {
                if( serviceQualifier == null )
                {
                    return context.module().findService( serviceType );
                }
                else
                {
                    return Iterables.first( Iterables.filter( serviceQualifier, context.module()
                        .findServices( serviceType ) ) );
                }
            }
            catch( NoSuchServiceException e )
            {
                return null;
            }
        }

        protected Iterable<ServiceReference<Object>> getServiceReferences( final InjectionContext context )
        {
            if( serviceQualifier == null )
            {
                return context.module().findServices( serviceType );
            }
            else
            {
                return Iterables.filter( serviceQualifier, context.module().findServices( serviceType ) );
            }
        }
    }
}
