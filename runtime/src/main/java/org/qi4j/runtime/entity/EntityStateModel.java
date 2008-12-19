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

package org.qi4j.runtime.entity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.qi4j.api.constraint.ConstraintViolationException;
import org.qi4j.api.entity.association.AbstractAssociation;
import org.qi4j.api.entity.association.EntityStateHolder;
import org.qi4j.api.property.Property;
import org.qi4j.api.property.StateHolder;
import org.qi4j.runtime.entity.association.AssociationInstance;
import org.qi4j.runtime.entity.association.AssociationsInstance;
import org.qi4j.runtime.entity.association.AssociationsModel;
import org.qi4j.runtime.property.PropertiesInstance;
import org.qi4j.runtime.unitofwork.UnitOfWorkInstance;
import org.qi4j.spi.entity.association.AssociationType;
import org.qi4j.spi.entity.EntityState;
import org.qi4j.spi.entity.EntityStateDescriptor;
import org.qi4j.spi.entity.association.ManyAssociationType;
import org.qi4j.spi.property.PropertyType;
import org.qi4j.spi.entity.association.AssociationDescriptor;
import org.qi4j.spi.property.PropertyDescriptor;

/**
 * TODO
 */
public final class EntityStateModel
    implements EntityStateDescriptor
{
    private final EntityPropertiesModel propertiesModel;
    private final AssociationsModel associationsModel;

    public EntityStateModel( EntityPropertiesModel propertiesModel, AssociationsModel associationsModel )
    {
        this.propertiesModel = propertiesModel;
        this.associationsModel = associationsModel;
    }

    public EntityStateHolder newBuilderInstance()
    {
        PropertiesInstance properties = propertiesModel.newBuilderInstance();
        AssociationsInstance associations = associationsModel.newBuilderInstance();
        return new BuilderStateInstance( properties, associations );
    }

    public EntityStateModel.EntityStateInstance newInstance( UnitOfWorkInstance uow, EntityState entityState )
    {
        return new EntityStateInstance( propertiesModel, associationsModel, entityState, uow );
    }

    public void addStateFor( Method method )
    {
        propertiesModel.addPropertyFor( method );
        associationsModel.addAssociationFor( method );
    }

    public PropertyDescriptor getPropertyByName( String name )
    {
        return propertiesModel.getPropertyByName( name );
    }

    public PropertyDescriptor getPropertyByQualifiedName( String name )
    {
        return propertiesModel.getPropertyByQualifiedName( name );
    }

    public AssociationDescriptor getAssociationByName( String name )
    {
        return associationsModel.getAssociationByName( name );
    }

    public AssociationDescriptor getAssociationByQualifiedName( String name )
    {
        return associationsModel.getAssociationByQualifiedName( name );
    }

    public List<PropertyDescriptor> properties()
    {
        return propertiesModel.properties();
    }

    public List<AssociationDescriptor> associations()
    {
        return associationsModel.associations();
    }

    public void setState( StateHolder state, EntityState entityState )
        throws ConstraintViolationException
    {
        BuilderStateInstance builderStateInstance = (BuilderStateInstance) state;

        propertiesModel.setState( builderStateInstance.properties, entityState );
        associationsModel.setState( builderStateInstance.associations, entityState );
    }

    public Iterable<PropertyType> propertyTypes()
    {
        return propertiesModel.propertyTypes();
    }

    public Iterable<AssociationType> associationTypes()
    {
        return associationsModel.associationTypes();
    }

    public Iterable<ManyAssociationType> manyAssociationTypes()
    {
        return associationsModel.manyAssociationTypes();
    }

    private static final class BuilderStateInstance
        implements EntityStateHolder
    {
        private final PropertiesInstance properties;
        private final AssociationsInstance associations;

        private BuilderStateInstance( PropertiesInstance properties, AssociationsInstance associations )
        {
            this.properties = properties;
            this.associations = associations;
        }

        public Property<?> getProperty( Method propertyMethod )
        {
            return properties.propertyFor( propertyMethod );
        }

        public AbstractAssociation getAssociation( Method associationMethod )
        {
            return associations.associationFor( associationMethod );
        }
    }

    public static final class EntityStateInstance
        implements EntityStateHolder
    {
        private Map<Method, Property<?>> properties;
        private Map<Method, AbstractAssociation> associations;

        private final EntityPropertiesModel entityPropertiesModel;
        private final AssociationsModel associationsModel;
        private final EntityState entityState;
        private final UnitOfWorkInstance uow;

        private EntityStateInstance(
            EntityPropertiesModel entityPropertiesModel, AssociationsModel associationsModel, EntityState entityState,
            UnitOfWorkInstance uow )
        {
            this.entityPropertiesModel = entityPropertiesModel;
            this.associationsModel = associationsModel;
            this.entityState = entityState;
            this.uow = uow;
        }

        public Property<?> getProperty( Method accessor )
        {
            if( properties == null )
            {
                properties = new HashMap<Method, Property<?>>();
            }

            Property<?> property = properties.get( accessor );

            if( property == null )
            {
                property = entityPropertiesModel.newInstance( accessor, entityState );
                properties.put( accessor, property );
            }

            return property;
        }

        public AbstractAssociation getAssociation( Method accessor )
        {
            if( associations == null )
            {
                associations = new HashMap<Method, AbstractAssociation>();
            }

            AbstractAssociation association = associations.get( accessor );

            if( association == null )
            {
                association = associationsModel.newInstance( accessor, entityState, uow );
                associations.put( accessor, association );
            }

            return association;
        }

        public void refresh( EntityState entityState )
        {
            if( properties != null )
            {
                for( Property property : properties.values() )
                {
                    if( property instanceof EntityPropertyInstance )
                    {
                        EntityPropertyInstance entityProperty = (EntityPropertyInstance) property;
                        entityProperty.refresh( entityState );
                    }
                }
            }

            if( associations != null )
            {
                for( AbstractAssociation abstractAssociation : associations.values() )
                {
                    if( abstractAssociation instanceof AssociationInstance )
                    {
                        AssociationInstance associationInstance = (AssociationInstance) abstractAssociation;
                        associationInstance.refresh( entityState );
                    }
                }
            }
        }
    }

}
