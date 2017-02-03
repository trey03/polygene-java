package org.apache.polygene.api.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.value.ValueDescriptor;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.spi.module.ModuleSpi;
import org.apache.polygene.spi.type.ValueTypeFactory;
import org.apache.polygene.test.AbstractPolygeneTest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public class ValueTypeFactoryTest extends AbstractPolygeneTest
{
    private ValueTypeFactory valueTypeFactory;

    @Override
    public void assemble( ModuleAssembly module )
    {
        module.values( SomeValue.class );
    }

    interface SomeValue
    {
        @UseDefaults
        Property<List<String>> list();

        @UseDefaults
        Property<Map<String, Integer>> map();
    }

    @Before
    public void setup()
    {
        valueTypeFactory = ( (ModuleSpi) module.instance() ).valueTypeFactory();
    }

    @Test
    public void plainValues()
    {
        assertThat( valueTypeFactory.valueTypeOf( module, String.class ), equalTo( ValueType.STRING ) );
        assertThat( valueTypeFactory.valueTypeOf( module, "" ), equalTo( ValueType.STRING ) );
    }

    @Test
    public void enums()
    {
        assertThat( valueTypeFactory.valueTypeOf( module, TimeUnit.class ), instanceOf( EnumType.class ) );
        assertThat( valueTypeFactory.valueTypeOf( module, TimeUnit.DAYS ), instanceOf( EnumType.class ) );
    }

    @Test
    public void collections()
    {
        assertThat( valueTypeFactory.valueTypeOf( module, LinkedHashSet.class ),
                    instanceOf( CollectionType.class ) );

        List<String> list = new ArrayList<>();
        ValueType listValueType = valueTypeFactory.valueTypeOf( module, list );
        assertThat( listValueType, instanceOf( CollectionType.class ) );
        assertThat( ( (CollectionType) listValueType ).collectedType(), equalTo( ValueType.OBJECT ) );
    }

    @Test
    public void maps()
    {
        assertThat( valueTypeFactory.valueTypeOf( module, TreeMap.class ), instanceOf( MapType.class ) );

        HashMap<String, Integer> map = new HashMap<>();
        ValueType mapValueType = valueTypeFactory.valueTypeOf( module, map );
        assertThat( mapValueType, instanceOf( MapType.class ) );
        assertThat( ( (MapType) mapValueType ).keyType(), equalTo( ValueType.OBJECT ) );
        assertThat( ( (MapType) mapValueType ).valueType(), equalTo( ValueType.OBJECT ) );
    }

    @Test
    public void valueComposites()
    {
        assertThat( valueTypeFactory.valueTypeOf( module, SomeValue.class ),
                    instanceOf( ValueCompositeType.class ) );
        assertThat( valueTypeFactory.valueTypeOf( module, valueBuilderFactory.newValue( SomeValue.class ) ),
                    instanceOf( ValueCompositeType.class ) );
    }

    @Test
    public void genericsAreResolvedOnValueCompositeProperties()
    {
        ValueDescriptor descriptor = module.typeLookup().lookupValueModel( SomeValue.class );
        assertThat( descriptor.state().findPropertyModelByName( "list" ).valueType(),
                    equalTo( CollectionType.listOf( ValueType.STRING ) ) );
        assertThat( descriptor.state().findPropertyModelByName( "map" ).valueType(),
                    equalTo( MapType.of( ValueType.STRING, ValueType.INTEGER ) ) );
    }
}
