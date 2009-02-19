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

package org.qi4j.spi.property;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import org.qi4j.spi.value.ValueType;

/**
 * TODO
 */
public class PropertyType
    implements Serializable, Comparable<PropertyType>
{
    public void calculateVersion( MessageDigest md )
        throws UnsupportedEncodingException
    {
        md.update( qualifiedName.getBytes("UTF-8" ));
        type.calculateVersion( md );
    }

    public enum PropertyTypeEnum
    {
        MUTABLE, IMMUTABLE, COMPUTED
    }

    private final String qualifiedName;
    private final ValueType type;
    private final String uri;
    private String rdf;
    private final boolean queryable;
    private final PropertyTypeEnum propertyType;

    public PropertyType( final String qualifiedName,
                         final ValueType type,
                         final String uri,
                         final String rdf,
                         final boolean queryable,
                         final PropertyTypeEnum propertyType )
    {
        this.qualifiedName = qualifiedName;
        this.type = type;
        this.uri = uri;
        this.rdf = rdf;
        this.queryable = queryable;
        this.propertyType = propertyType;
    }

    public String qualifiedName()
    {
        return qualifiedName;
    }

    public ValueType type()
    {
        return type;
    }

    public PropertyTypeEnum propertyType()
    {
        return propertyType;
    }

    public String uri()
    {
        return uri;
    }

    public String rdf()
    {
        return rdf;
    }

    public boolean queryable()
    {
        return queryable;
    }

    @Override public String toString()
    {
        return qualifiedName + "(" + type + "," + uri + ")";
    }

    public int compareTo( PropertyType pt )
    {
        return qualifiedName.compareTo( pt.qualifiedName );
    }
}
