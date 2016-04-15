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
package org.apache.zest.api.type;

import java.lang.reflect.Type;

/**
 * Enum ValueType.
 */
public final class EnumType
    extends ValueType
{

    public static boolean isEnum( Type type )
    {
        if( type instanceof Class )
        {
            Class<?> typeClass = (Class) type;
            return ( typeClass.isEnum() );
        }
        return false;
    }

    public static EnumType of( Class<?> type )
    {
        return new EnumType( type );
    }

    public EnumType( Class<?> type )
    {
        super( type );
        if( !isEnum( type ) )
        {
            throw new IllegalArgumentException( type + " is not an Enum." );
        }
    }
}