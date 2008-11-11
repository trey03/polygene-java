/*  Copyright 2008 Edward Yakop.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qi4j.library.swing.visualizer.detailPanel.internal.form.common;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import org.qi4j.library.swing.visualizer.model.LayerDetailDescriptor;
import org.qi4j.library.swing.visualizer.model.ServiceDetailDescriptor;
import org.qi4j.service.ServiceDescriptor;

/**
 * @author edward.yakop@gmail.com
 * @see org.qi4j.library.swing.visualizer.model.ServiceDetailDescriptor
 * @since 0.5
 */
public final class ServiceDescriptorForm
{
    private JPanel placeHolder;

    private JComponent serviceSeparator;
    private JTextField serviceId;
    private JTextField serviceType;
    private JCheckBox serviceIsInstantiateAtStartup;
    private JTextField serviceVisibility;
    private JList serviceAccessibleBy;

    public ServiceDescriptorForm()
    {
        $$$setupUI$$$();
    }

    public final void updateModel( ServiceDetailDescriptor aDescriptor )
    {
        populateServiceFields( aDescriptor );
    }

    @SuppressWarnings( "unchecked" )
    private void populateServiceFields( ServiceDetailDescriptor aDescriptor )
    {
        String identity = null;
        boolean instantiateOnStartup = false;
        String visibility = null;
        String className = null;
        ListModel accessibleToLayers = null;

        if( aDescriptor != null )
        {
            ServiceDescriptor descriptor = aDescriptor.descriptor();
            identity = descriptor.identity();
            className = descriptor.type().getName();
            instantiateOnStartup = descriptor.isInstantiateOnStartup();
            visibility = descriptor.visibility().toString();

            final List<LayerDetailDescriptor> detailDescriptors = aDescriptor.accessibleToLayers();
            accessibleToLayers = new ListListModel( detailDescriptors );
        }

        serviceId.setText( identity );
        serviceType.setText( className );
        serviceIsInstantiateAtStartup.setSelected( instantiateOnStartup );
        serviceVisibility.setText( visibility );
        serviceAccessibleBy.setModel( accessibleToLayers );
    }

    private void createUIComponents()
    {
        DefaultComponentFactory cmpFactory = DefaultComponentFactory.getInstance();
        serviceSeparator = cmpFactory.createSeparator( "Service" );
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        createUIComponents();
        placeHolder = new JPanel();
        placeHolder.setLayout( new FormLayout( "left:4px:noGrow,fill:p:noGrow,left:4dlu:noGrow,fill:max(p;75dlu):noGrow,left:4dlu:noGrow,fill:d:grow,left:4px:noGrow", "center:max(d;4px):noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:p:noGrow,top:5dlu:noGrow,center:p:noGrow,top:4dlu:noGrow,center:p:noGrow,top:4dlu:noGrow,center:p:noGrow,top:4dlu:noGrow,top:max(m;50dlu):noGrow,top:4px:noGrow" ) );
        ( (FormLayout) placeHolder.getLayout() ).setRowGroups( new int[][]{ new int[]{ 4, 6, 8, 10 }, new int[]{ 5, 7, 9, 11 }, new int[]{ 1, 13 } } );
        ( (FormLayout) placeHolder.getLayout() ).setColumnGroups( new int[][]{ new int[]{ 1, 7 }, new int[]{ 3, 5 } } );
        final JLabel label1 = new JLabel();
        label1.setText( "Id" );
        CellConstraints cc = new CellConstraints();
        placeHolder.add( label1, cc.xy( 2, 4 ) );
        serviceId = new JTextField();
        serviceId.setEditable( false );
        placeHolder.add( serviceId, cc.xy( 4, 4 ) );
        final JLabel label2 = new JLabel();
        label2.setText( "Class name" );
        placeHolder.add( label2, cc.xy( 2, 6 ) );
        final JLabel label3 = new JLabel();
        label3.setText( "Is instatiate at startup" );
        placeHolder.add( label3, cc.xy( 2, 10 ) );
        serviceIsInstantiateAtStartup = new JCheckBox();
        serviceIsInstantiateAtStartup.setEnabled( false );
        serviceIsInstantiateAtStartup.setText( "" );
        placeHolder.add( serviceIsInstantiateAtStartup, cc.xy( 4, 10, CellConstraints.LEFT, CellConstraints.DEFAULT ) );
        serviceType = new JTextField();
        serviceType.setEditable( false );
        placeHolder.add( serviceType, cc.xy( 4, 6 ) );
        final JLabel label4 = new JLabel();
        label4.setText( "Visiblity" );
        placeHolder.add( label4, cc.xy( 2, 8 ) );
        serviceVisibility = new JTextField();
        serviceVisibility.setEditable( false );
        placeHolder.add( serviceVisibility, cc.xy( 4, 8 ) );
        placeHolder.add( serviceSeparator, cc.xyw( 2, 2, 5 ) );
        final JLabel label5 = new JLabel();
        label5.setText( "Accessible by (layer)" );
        placeHolder.add( label5, cc.xy( 2, 12 ) );
        serviceAccessibleBy = new JList();
        serviceAccessibleBy.setSelectionMode( 0 );
        serviceAccessibleBy.setVisibleRowCount( 5 );
        placeHolder.add( serviceAccessibleBy, cc.xy( 4, 12, CellConstraints.DEFAULT, CellConstraints.FILL ) );
        label1.setLabelFor( serviceId );
        label2.setLabelFor( serviceType );
        label3.setLabelFor( serviceIsInstantiateAtStartup );
        label4.setLabelFor( serviceVisibility );
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return placeHolder;
    }
}
