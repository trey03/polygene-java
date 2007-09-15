package org.qi4j.spi.dependency;

import java.util.Map;
import org.qi4j.api.model.InjectionKey;

/**
 * TODO
 */
public class ObjectDependencyInjectionContext
    extends DependencyInjectionContext
{
    private Map<InjectionKey, Object> properties;
    private Map<InjectionKey, Object> adapt;
    private Map<InjectionKey, Object> decorate;

    public ObjectDependencyInjectionContext( Map<InjectionKey, Object> properties, Map<InjectionKey, Object> adapt, Map<InjectionKey, Object> decorate )
    {
        this.properties = properties;
        this.adapt = adapt;
        this.decorate = decorate;
    }

    public Map<InjectionKey, Object> getProperties()
    {
        return properties;
    }

    public Map<InjectionKey, Object> getAdapt()
    {
        return adapt;
    }

    public Map<InjectionKey, Object> getDecorate()
    {
        return decorate;
    }
}