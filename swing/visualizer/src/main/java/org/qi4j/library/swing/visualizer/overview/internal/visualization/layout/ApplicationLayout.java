/*
 * Copyright 2008 Niclas Hedhman. All rights Reserved.
 * Copyright 2008 Sonny Gill. All Rights Reserved.
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
package org.qi4j.library.swing.visualizer.overview.internal.visualization.layout;

import java.awt.Point;
import prefuse.action.layout.graph.TreeLayout;
import prefuse.visual.NodeItem;

public final class ApplicationLayout extends TreeLayout
{
    public ApplicationLayout( String aGroup )
    {
        super( aGroup );
    }

    @Override
    public final void run( double frac )
    {
        ApplicationBoundsComputer boundsComputer = new ApplicationBoundsComputer();

        NodeItem applicationNode = getLayoutRoot();
        Point topLeft = new Point( 0, 0 );
        boundsComputer.computeBounds( applicationNode, topLeft );
    }
}
