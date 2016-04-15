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
package org.apache.zest.sample.dcicargo.sample_b.data.factory.exception;

import org.apache.zest.sample.dcicargo.sample_b.data.structure.tracking.TrackingId;

/**
 * If a Cargo can't be created.
 *
 * Usually because a supplied tracking id string
 * - doesn't match the pattern in {@link TrackingId}
 * - is not unique
 */
public class CannotCreateCargoException extends Exception
{
    public CannotCreateCargoException( String s )
    {
        super( s );
    }
}