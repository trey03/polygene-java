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

package org.apache.zest.envisage.detail;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import org.apache.zest.api.composite.DependencyDescriptor;
import org.apache.zest.envisage.event.LinkEvent;
import org.apache.zest.envisage.util.TableRow;
import org.apache.zest.envisage.util.TableRowUtilities;
import org.apache.zest.tools.model.descriptor.InjectedFieldDetailDescriptor;
import org.apache.zest.tools.model.descriptor.ServiceDetailDescriptor;
import org.apache.zest.tools.model.descriptor.ServiceUsage;
import org.apache.zest.tools.model.util.DescriptorUtilities;

/**
 * Service Usage tab, which shows all the 'users' of the Service,
 * meaning the 'reverse' of a Dependency, all 'links' pointing into the Service should be listed here.
 * The list should be in three 'sections' according to the Visibility,
 * i.e. one section for all usages from within the same Module,
 * one section for usages within the same Layer
 * and one section for usages from Layers above.
 * I think the order of these sections
 * should be "LayersAbove", "WithinLayer" and "WithinModule"
 * in that order from top.
 */
/* package */ final class ServiceUsagePane
    extends DetailPane
{
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle( ServiceUsagePane.class.getName() );

    private UsageTableModel usageTableModel;
    private JTable usageTable;
    private JPanel contentPane;

    private Object linkObject;
    private Cursor defaultCursor;
    private Cursor linkCursor;

    /* package */ ServiceUsagePane( DetailModelPane detailModelPane )
    {
        super( detailModelPane );
        this.setLayout( new BorderLayout() );
        this.add( contentPane, BorderLayout.CENTER );

        usageTableModel = new UsageTableModel();
        usageTable.setModel( usageTableModel );
        usageTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        TableColumnModel columnModel = usageTable.getColumnModel();
        columnModel.getColumn( 0 ).setCellRenderer( new OwnerCellRenderer() );
        columnModel.getColumn( 1 ).setCellRenderer( new FieldCellRenderer() );

        /*columnModel.getColumn( 0 ).setPreferredWidth( 90 );
         columnModel.getColumn( 1 ).setPreferredWidth( 550 );
         */
        defaultCursor = getCursor();
        linkCursor = LinkEvent.LINK_CURSOR;

        MouseInputAdapter mouseInputListener = new MouseInputAdapter()
        {
            @Override
            public void mouseMoved( MouseEvent evt )
            {
                // Column 1 is the Owner Column
                int col = usageTable.columnAtPoint( evt.getPoint() );
                if( col == 0 )
                {
                    setCursor( linkCursor );
                }
                else
                {
                    if( !getCursor().equals( defaultCursor ) )
                    {
                        setCursor( defaultCursor );
                    }
                }
            }

            @Override
            public void mouseClicked( MouseEvent evt )
            {
                /*if( evt.getClickCount() < 2 )
                 {
                 return;
                 }*/

                int col = usageTable.columnAtPoint( evt.getPoint() );
                if( col != 0 )
                {
                    return;
                }

                int row = usageTable.rowAtPoint( evt.getPoint() );
                if( row < 0 )
                {
                    return;
                }

                linkObject = usageTableModel.getValueAt( row, col );
                linkActivated();
                linkObject = null;
            }
        };

        usageTable.addMouseMotionListener( mouseInputListener );
        usageTable.addMouseListener( mouseInputListener );
    }

    private void linkActivated()
    {
        if( linkObject == null )
        {
            return;
        }
        LinkEvent linkEvt = new LinkEvent( this, linkObject );
        detailModelPane.fireLinkActivated( linkEvt );
    }

    @Override
    protected void setDescriptor( Object objectDesciptor )
    {
        clear();

        if( !( objectDesciptor instanceof ServiceDetailDescriptor ) )
        {
            return;
        }

        ServiceDetailDescriptor descriptor = (ServiceDetailDescriptor) objectDesciptor;

        List<ServiceUsage> serviceUsages = DescriptorUtilities.findServiceUsage( descriptor );
        usageTableModel.addRows( TableRowUtilities.toTableRows( serviceUsages ) );
    }

    private void clear()
    {
        linkObject = null;
        usageTableModel.clear();
    }

    
    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     */
    private void $$$setupUI$$$()
    {
        contentPane = new JPanel();
        contentPane.setLayout( new BorderLayout( 0, 0 ) );
        final JScrollPane scrollPane1 = new JScrollPane();
        contentPane.add( scrollPane1, BorderLayout.CENTER );
        usageTable = new JTable();
        scrollPane1.setViewportView( usageTable );
    }

    public JComponent $$$getRootComponent$$$()
    {
        return contentPane;
    }

    private static class UsageTableModel
        extends AbstractTableModel
    {
        /**
         * the column names for this model
         */
        private static final String[] COLUMN_NAMES =
        {
            BUNDLE.getString( "Owner.Column" ),
            BUNDLE.getString( "Usage.Column" ),
            BUNDLE.getString( "Module.Column" ),
            BUNDLE.getString( "Layer.Column" )
        };
        private final ArrayList<TableRow> rows;

        private UsageTableModel()
        {
            rows = new ArrayList<>();
        }

        private void addRows( List<TableRow> rows )
        {
            int i1 = rows.size();
            if( i1 > 0 )
            {
                i1--;
            }

            int i2 = 0;
            for( TableRow row : rows )
            {
                this.rows.add( row );
                i2++;
            }

            fireTableRowsInserted( i1, i1 + i2 );
        }

        /**
         * Add row data
         *
         * @param row TableRow to be added
         */
        private void addRow( TableRow row )
        {
            int i1 = rows.size();
            if( i1 > 0 )
            {
                i1--;
            }

            rows.add( row );
            fireTableRowsInserted( i1, i1 + 1 );
        }

        @Override
        public Object getValueAt( int rowIndex, int columnIndex )
        {
            TableRow row = rows.get( rowIndex );
            return row.get( columnIndex );
        }

        private void clear()
        {
            rows.clear();
            fireTableDataChanged();
        }

        @Override
        public int getColumnCount()
        {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName( int col )
        {
            return COLUMN_NAMES[ col];
        }

        @Override
        public int getRowCount()
        {
            return rows.size();
        }
    }

    private static class OwnerCellRenderer
        extends DefaultTableCellRenderer
    {
        @Override
        public final Component getTableCellRendererComponent( JTable table,
                                                              Object value,
                                                              boolean isSelected,
                                                              boolean hasFocus,
                                                              int row,
                                                              int column
        )
        {
            if( value != null )
            {
                value = "<html><a href=\"" + value.toString() + "\">" + value.toString() + "</a></html>";
            }

            super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );

            return this;
        }
    }

    private static class FieldCellRenderer
        extends DefaultTableCellRenderer
    {
        @Override
        public final Component getTableCellRendererComponent( JTable table,
                                                              Object value,
                                                              boolean isSelected,
                                                              boolean hasFocus,
                                                              int row,
                                                              int column
        )
        {
            if( value != null )
            {
                InjectedFieldDetailDescriptor descriptor = (InjectedFieldDetailDescriptor) value;
                DependencyDescriptor dependencyDescriptor = descriptor.descriptor().dependency();
                Annotation annotation = dependencyDescriptor.injectionAnnotation();
                value = descriptor.toString() + " (@" + annotation.annotationType().getSimpleName() + ")";
            }

            return super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column );
        }
    }

}
