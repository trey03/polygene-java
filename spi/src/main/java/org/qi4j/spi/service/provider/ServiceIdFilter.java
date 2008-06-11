/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.spi.service.provider;

import org.qi4j.injection.scope.Structure;
import org.qi4j.service.ServiceDescriptor;
import org.qi4j.service.ServiceFinder;
import org.qi4j.service.ServiceInstanceFactory;
import org.qi4j.service.ServiceInstanceProviderException;
import org.qi4j.service.ServiceReference;

/**
 * TODO
 */
public class ServiceIdFilter
    implements ServiceInstanceFactory
{
    private @Structure ServiceFinder locator;

    private ServiceReference serviceRef;
    private Object instance;

    public Object newInstance( ServiceDescriptor serviceDescriptor ) throws ServiceInstanceProviderException
    {
        if( serviceRef == null )
        {
            String identityFilter = serviceDescriptor.metaInfo( ServiceId.class ).id();
            Class serviceType = serviceDescriptor.type();
            Iterable<ServiceReference<?>> services = locator.findServices( serviceType );
            for( ServiceReference<?> service : services )
            {
                if( service.identity().equals( identityFilter ) )
                {
                    serviceRef = service;
                    instance = service.get();
                }
            }
        }

        return instance;
    }

    public void releaseInstance( Object instance ) throws ServiceInstanceProviderException
    {
        serviceRef.releaseService();
        serviceRef = null;
        instance = null;
    }
}
