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
package org.apache.zest.index.sql.support.postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.zest.index.sql.support.common.DBNames;
import org.apache.zest.index.sql.support.skeletons.AbstractSQLIndexing;
import org.apache.zest.library.sql.common.SQLUtil;
import org.apache.zest.spi.entity.EntityState;
import org.sql.generation.api.grammar.builders.modification.ColumnSourceByValuesBuilder;
import org.sql.generation.api.grammar.builders.modification.pgsql.PgSQLInsertStatementBuilder;
import org.sql.generation.api.grammar.factories.ColumnsFactory;
import org.sql.generation.api.grammar.factories.LiteralFactory;
import org.sql.generation.api.grammar.factories.ModificationFactory;
import org.sql.generation.api.grammar.factories.TableReferenceFactory;
import org.sql.generation.api.grammar.modification.InsertStatement;
import org.sql.generation.api.grammar.modification.ValueSource;
import org.sql.generation.api.vendor.SQLVendor;

public class PostgreSQLIndexing
    extends AbstractSQLIndexing
{

    @Override
    protected long getPKFromAutoGeneratedIDInsert( EntityState state,
                                                   PreparedStatement autoGeneratedIDStatement,
                                                   SQLVendor vendor, Connection connection )
        throws SQLException
    {
        this.addEntityInfoToInsertToEntityTablePS( state, autoGeneratedIDStatement, 1 );
        ResultSet rs = autoGeneratedIDStatement.executeQuery();
        try
        {
            rs.next();
            return rs.getLong( 1 );
        }
        finally
        {
            SQLUtil.closeQuietly( rs );
        }
    }

    @Override
    protected InsertStatement createInsertStatementWithAutoGeneratedIDForEntitiesTable(
        String schemaName, String tableName, SQLVendor vendor )
    {
        ModificationFactory m = vendor.getModificationFactory();
        LiteralFactory l = vendor.getLiteralFactory();
        TableReferenceFactory t = vendor.getTableReferenceFactory();
        ColumnsFactory c = vendor.getColumnsFactory();

        ColumnSourceByValuesBuilder columnBuilder = m.columnSourceByValues();
        columnBuilder.addValues( ValueSource.Default.INSTANCE );
        for( Integer x = 1; x < AMOUNT_OF_COLUMNS_IN_ENTITY_TABLE; ++x )
        {
            columnBuilder.addValues( l.param() );
        }

        return ( (PgSQLInsertStatementBuilder) m.insert() )
            .setReturningClause(
                vendor.getQueryFactory().columnsBuilder()
                .addUnnamedColumns( c.colName( DBNames.ENTITY_TABLE_PK_COLUMN_NAME ) )
                .createExpression()
            )
            .setTableName( t.tableName( schemaName, tableName ) )
            .setColumnSource( columnBuilder.createExpression() )
            .createExpression();
    }
}
