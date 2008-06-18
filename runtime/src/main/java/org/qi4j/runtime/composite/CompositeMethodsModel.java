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

package org.qi4j.runtime.composite;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import org.qi4j.composite.Composite;
import org.qi4j.runtime.structure.Binder;
import org.qi4j.runtime.structure.ModelVisitor;
import org.qi4j.runtime.structure.ModuleInstance;

/**
 * TODO
 */
public final class CompositeMethodsModel
    implements Binder
{
    private final ConcurrentHashMap<Method, CompositeMethodModel> methods;
    private final Class<? extends Composite> type;
    private final ConstraintsModel constraintsModel;
    private final ConcernsDeclaration concernsModel;
    private final SideEffectsDeclaration sideEffectsModel;
    private final AbstractMixinsModel mixinsModel;

    public CompositeMethodsModel( Class<? extends Composite> type,
                                  ConstraintsModel constraintsModel,
                                  ConcernsDeclaration concernsModel,
                                  SideEffectsDeclaration sideEffectsModel,
                                  AbstractMixinsModel mixinsModel )
    {
        methods = new ConcurrentHashMap<Method, CompositeMethodModel>( 1024 );
        this.type = type;
        this.constraintsModel = constraintsModel;
        this.concernsModel = concernsModel;
        this.sideEffectsModel = sideEffectsModel;
        this.mixinsModel = mixinsModel;
        implementMixinType( type );
    }

    // Binding
    public void bind( Resolution resolution ) throws BindingException
    {
        for( CompositeMethodModel compositeMethodComposite : methods.values() )
        {
            compositeMethodComposite.bind( resolution );
        }
    }

    // Context
    public Object invoke( MixinsInstance mixins, Object proxy, Method method, Object[] args, ModuleInstance moduleInstance )
        throws Throwable
    {
        CompositeMethodModel compositeMethod = methods.get( method );

        if( compositeMethod == null )
        {
            return mixins.invokeObject( proxy, args, method );
        }
        else
        {
            return compositeMethod.invoke( proxy, args, mixins, moduleInstance );
        }
    }

    public void implementMixinType( Class mixinType )
    {
        for( Method method : mixinType.getMethods() )
        {
            if( methods.get( method ) == null )
            {
                MethodConcernsModel methodConcernsModel = concernsModel.concernsFor( method, type );
                MethodSideEffectsModel methodSideEffectsModel1 = sideEffectsModel.sideEffectsFor( method, type );

                MixinModel mixinModel = mixinsModel.implementMethod( method );
                MethodConcernsModel mixinMethodConcernsModel = mixinModel.concernsFor( method, type );
                methodConcernsModel = methodConcernsModel.combineWith( mixinMethodConcernsModel );
                MethodSideEffectsModel mixinMethodSideEffectsModel = mixinModel.sideEffectsFor( method, type );
                methodSideEffectsModel1 = methodSideEffectsModel1.combineWith( mixinMethodSideEffectsModel );

                CompositeMethodModel methodComposite = new CompositeMethodModel( method,
                                                                                 new MethodConstraintsModel( method, constraintsModel ),
                                                                                 methodConcernsModel,
                                                                                 methodSideEffectsModel1,
                                                                                 mixinsModel );

                methods.put( method, methodComposite );
            }
        }
    }

    public void visitModel( ModelVisitor modelVisitor )
    {
        for( CompositeMethodModel compositeMethodModel : methods.values() )
        {
            compositeMethodModel.visitModel( modelVisitor );
        }
    }
}