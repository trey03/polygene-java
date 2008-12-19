/*
 * Copyright (c) 2008, Rickard Öberg. All Rights Reserved.
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

package org.qi4j.spi.entity;

import org.qi4j.spi.entity.EntityState;

/**
 * Implement this interface in a Service if you want it to listen
 * to changes from EntityStores.
 */
public interface EntityStoreListener
{
    void notifyChanges( Iterable<EntityState> newStates,
                        Iterable<EntityState> changedStates,
                        Iterable<QualifiedIdentity> removedStates );
}
