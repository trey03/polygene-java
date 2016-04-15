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
package org.apache.zest.api.injection.scope;

import java.lang.annotation.Annotation;
import org.junit.Test;
import org.apache.zest.api.common.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests public api exposed by This annotation.
 * This will ensure that the public api does not get changed by mistake.
 */
public class ThisTest
{

    @Test
    public void retention()
        throws NoSuchFieldException
    {
        Annotation[] annotations = Annotated.class.getDeclaredField( "uses" ).getDeclaredAnnotations();
        assertNotNull( "annotations should not be null", annotations );
        assertEquals( "number of annotations", 1, annotations.length );
        assertEquals( "annotation type", This.class, annotations[ 0 ].annotationType() );
    }

    private static class Annotated
    {
        @This
        String uses;
        @Optional
        @This
        String usesOptional;
    }
}
