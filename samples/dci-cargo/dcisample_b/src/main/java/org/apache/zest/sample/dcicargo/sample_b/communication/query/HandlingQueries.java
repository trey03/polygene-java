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
package org.apache.zest.sample.dcicargo.sample_b.communication.query;

import java.util.ArrayList;
import java.util.List;
import org.apache.zest.api.query.Query;
import org.apache.zest.api.query.QueryBuilder;
import org.apache.zest.sample.dcicargo.sample_b.data.entity.CargoEntity;
import org.apache.zest.sample.dcicargo.sample_b.data.structure.handling.HandlingEventType;
import org.apache.zest.sample.dcicargo.sample_b.data.structure.voyage.Voyage;
import org.apache.zest.sample.dcicargo.sample_b.infrastructure.model.Queries;

import static org.apache.zest.api.query.QueryExpressions.orderBy;
import static org.apache.zest.api.query.QueryExpressions.templateFor;

/**
 * Handling queries
 *
 * Used by the communication layer only. Can change freely according to presentation needs.
 */
public class HandlingQueries extends Queries
{
    public List<String> voyages()
    {
        QueryBuilder<Voyage> qb = qbf.newQueryBuilder( Voyage.class );
        Query<Voyage> voyages = uowf.currentUnitOfWork().newQuery( qb )
            .orderBy( orderBy( templateFor( Voyage.class ).voyageNumber() ) );

        List<String> voyageList = new ArrayList<String>();
        for( Voyage voyage : voyages )
        {
            voyageList.add( voyage.voyageNumber().get().number().get() );
        }
        return voyageList;
    }

    public List<String> cargoIds()
    {
        QueryBuilder<CargoEntity> qb = qbf.newQueryBuilder( CargoEntity.class );
        Query<CargoEntity> cargos = uowf.currentUnitOfWork().newQuery( qb )
            .orderBy( orderBy( templateFor( CargoEntity.class ).trackingId().get().id() ) );
        List<String> cargoList = new ArrayList<String>();
        for( CargoEntity cargo : cargos )
        {
            cargoList.add( cargo.trackingId().get().id().get() );
        }
        return cargoList;
    }

    public List<String> eventTypes()
    {
        List<String> eventTypes = new ArrayList<String>();
        for( HandlingEventType eventType : HandlingEventType.values() )
        {
            eventTypes.add( eventType.name() );
        }
        return eventTypes;
    }
}