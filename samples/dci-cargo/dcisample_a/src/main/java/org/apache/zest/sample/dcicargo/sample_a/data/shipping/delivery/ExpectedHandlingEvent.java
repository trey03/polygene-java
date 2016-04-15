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
package org.apache.zest.sample.dcicargo.sample_a.data.shipping.delivery;

import java.util.Date;
import org.apache.zest.api.association.Association;
import org.apache.zest.api.common.Optional;
import org.apache.zest.api.property.Property;
import org.apache.zest.api.value.ValueComposite;
import org.apache.zest.sample.dcicargo.sample_a.data.shipping.handling.HandlingEventType;
import org.apache.zest.sample.dcicargo.sample_a.data.shipping.location.Location;
import org.apache.zest.sample.dcicargo.sample_a.data.shipping.voyage.Voyage;

/**
 * An expected handling event (former "HandlingActivity") represents how and where a cargo
 * is expected to be handled next.
 */
public interface ExpectedHandlingEvent
    extends ValueComposite
{
    Property<HandlingEventType> handlingEventType();

    Association<Location> location();

    // Added expected time for the event to happen (compared to the original DDD sample)
    @Optional
    Property<Date> time();

    @Optional
    Association<Voyage> voyage();
}
