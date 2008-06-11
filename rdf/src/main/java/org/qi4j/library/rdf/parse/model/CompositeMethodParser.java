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
package org.qi4j.library.rdf.parse.model;

import org.openrdf.model.BNode;
import org.openrdf.model.Value;
import org.qi4j.library.rdf.parse.ParseContext;

public final class CompositeMethodParser
{
    private final ParseContext context;

    public CompositeMethodParser( ParseContext context )
    {
        this.context = context;
    }

    public Value parseModel( CompositeMethodModel methodModel )
    {
        BNode node = createCompositeMethod( methodModel );
        return node;
    }

    private BNode createCompositeMethod( CompositeMethodModel methodModel )
    {
        return context.getValueFactory().createBNode( methodModel.getMethod().getName() );
    }

}
