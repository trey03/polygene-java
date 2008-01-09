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

package org.qi4j.property;

import org.qi4j.composite.scope.Decorate;

/**
 * TODO
 */
public class MutablePropertyMixin<Object>
    implements ReadableProperty<Object>, WritableProperty<Object>
{
    private Object value;

    public void write( @Decorate Object value )
    {
        this.value = value;
    }

    public Object read()
    {
        return value;
    }

    public Object get()
    {
        return value;
    }

    public void set( Object newValue ) throws PropertyVetoException
    {
        value = newValue;
    }
}
