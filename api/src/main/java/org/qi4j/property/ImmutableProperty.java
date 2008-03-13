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

package org.qi4j.property;

/**
 * Immutable properties should use this interface.
 * The {@link Property#set} method is still there,
 * but an exception is thrown on invocation.
 * <p/>
 * For proxies created by CompositeBuilder the set()
 * method will not cause exceptions, which will allow
 * you to set the value of the property before the
 * Composite is instantiated. After instantiation there
 * is no way to change it.
 */
public interface ImmutableProperty<T>
    extends Property<T>
{
}
