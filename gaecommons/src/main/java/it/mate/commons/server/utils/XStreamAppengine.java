package it.mate.commons.server.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;
import com.thoughtworks.xstream.converters.basic.BigIntegerConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.converters.basic.ByteConverter;
import com.thoughtworks.xstream.converters.basic.CharConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.converters.basic.ShortConverter;
import com.thoughtworks.xstream.converters.basic.StringBufferConverter;
import com.thoughtworks.xstream.converters.basic.StringConverter;
import com.thoughtworks.xstream.converters.basic.URIConverter;
import com.thoughtworks.xstream.converters.basic.URLConverter;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;
import com.thoughtworks.xstream.converters.collections.BitSetConverter;
import com.thoughtworks.xstream.converters.collections.CharArrayConverter;
import com.thoughtworks.xstream.converters.collections.CollectionConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;
import com.thoughtworks.xstream.converters.collections.PropertiesConverter;
import com.thoughtworks.xstream.converters.collections.TreeMapConverter;
import com.thoughtworks.xstream.converters.collections.TreeSetConverter;
import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;
import com.thoughtworks.xstream.converters.extended.FileConverter;
import com.thoughtworks.xstream.converters.extended.GregorianCalendarConverter;
import com.thoughtworks.xstream.converters.extended.JavaClassConverter;
import com.thoughtworks.xstream.converters.extended.JavaFieldConverter;
import com.thoughtworks.xstream.converters.extended.JavaMethodConverter;
import com.thoughtworks.xstream.converters.extended.LocaleConverter;
import com.thoughtworks.xstream.converters.extended.LookAndFeelConverter;
import com.thoughtworks.xstream.converters.extended.SqlDateConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimeConverter;
import com.thoughtworks.xstream.converters.extended.SqlTimestampConverter;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SelfStreamingInstanceChecker;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.util.ClassLoaderReference;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.mapper.Mapper;

public class XStreamAppengine extends XStream {

  private transient JVM jvm = null;

  public XStreamAppengine() {
    super();
  }

  public XStreamAppengine(HierarchicalStreamDriver hierarchicalStreamDriver) {
    super(hierarchicalStreamDriver);
  }

  public XStreamAppengine(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader, Mapper mapper) {
    super(reflectionProvider, driver, classLoader, mapper);
  }

  public XStreamAppengine(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader) {
    super(reflectionProvider, driver, classLoader);
  }

  public XStreamAppengine(ReflectionProvider reflectionProvider, HierarchicalStreamDriver hierarchicalStreamDriver) {
    super(reflectionProvider, hierarchicalStreamDriver);
  }

  public XStreamAppengine(ReflectionProvider reflectionProvider) {
    super(reflectionProvider);
  }

  public XStreamAppengine(ReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader, Mapper mapper,
      ConverterLookup converterLookup, ConverterRegistry converterRegistry) {
    super(reflectionProvider, driver, classLoader, mapper, converterLookup, converterRegistry);
  }
  
  private void ensureJVM() {
    if (this.jvm == null) {
      this.jvm = new JVM() {
        public boolean supportsAWT() {
          return false;
        }
      };
    }
  }

  @Override
  protected void setupAliases() {
    ensureJVM();

    alias("null", Mapper.Null.class);
    alias("int", Integer.class);
    alias("float", Float.class);
    alias("double", Double.class);
    alias("long", Long.class);
    alias("short", Short.class);
    alias("char", Character.class);
    alias("byte", Byte.class);
    alias("boolean", Boolean.class);
    alias("number", Number.class);
    alias("object", Object.class);
    alias("big-int", BigInteger.class);
    alias("big-decimal", BigDecimal.class);

    alias("string-buffer", StringBuffer.class);
    alias("string", String.class);
    alias("java-class", Class.class);
    alias("method", Method.class);
    alias("constructor", Constructor.class);
    alias("field", Field.class);
    alias("date", Date.class);
    alias("uri", URI.class);
    alias("url", URL.class);
    alias("bit-set", BitSet.class);

    alias("map", Map.class);
    alias("entry", Map.Entry.class);
    alias("properties", Properties.class);
    alias("list", List.class);
    alias("set", Set.class);
    alias("sorted-set", SortedSet.class);

    alias("linked-list", LinkedList.class);
    alias("vector", Vector.class);
    alias("tree-map", TreeMap.class);
    alias("tree-set", TreeSet.class);
    alias("hashtable", Hashtable.class);

    
    if (jvm.supportsAWT()) {
      // Instantiating these two classes starts the AWT system, which is
      // undesirable.
      // Calling loadClass ensures a reference to the class is found but they
      // are not
      // instantiated.
      alias("awt-color", jvm.loadClass("java.awt.Color"));
      alias("awt-font", jvm.loadClass("java.awt.Font"));
      alias("awt-text-attribute", jvm.loadClass("java.awt.font.TextAttribute"));
    }

    if (jvm.supportsSQL()) {
      alias("sql-timestamp", jvm.loadClass("java.sql.Timestamp"));
      alias("sql-time", jvm.loadClass("java.sql.Time"));
      alias("sql-date", jvm.loadClass("java.sql.Date"));
    }

    alias("file", File.class);
    alias("locale", Locale.class);
    alias("gregorian-calendar", Calendar.class);

    if (JVM.is14()) {
      // aliasDynamically("auth-subject", "javax.security.auth.Subject");
      alias("linked-hash-map", jvm.loadClass("java.util.LinkedHashMap"));
      alias("linked-hash-set", jvm.loadClass("java.util.LinkedHashSet"));
      alias("trace", jvm.loadClass("java.lang.StackTraceElement"));
      alias("currency", jvm.loadClass("java.util.Currency"));
      aliasType("charset", jvm.loadClass("java.nio.charset.Charset"));
    }

    if (JVM.is15()) {
      // aliasDynamically("duration", "javax.xml.datatype.Duration");
      alias("enum-set", jvm.loadClass("java.util.EnumSet"));
      alias("enum-map", jvm.loadClass("java.util.EnumMap"));
      alias("string-builder", jvm.loadClass("java.lang.StringBuilder"));
      alias("uuid", jvm.loadClass("java.util.UUID"));
    }
  }

  protected void setupConverters() {
    ensureJVM();
    
    Mapper mapper = getMapper();
    ReflectionProvider reflectionProvider = getReflectionProvider();
    ClassLoaderReference classLoaderReference = new ClassLoaderReference(getClassLoader());

    final ReflectionConverter reflectionConverter = new ReflectionConverter(mapper, getReflectionProvider());
    registerConverter(reflectionConverter, PRIORITY_VERY_LOW);

    registerConverter(new SerializableConverter(mapper, reflectionProvider, classLoaderReference), PRIORITY_LOW);
    registerConverter(new ExternalizableConverter(mapper, classLoaderReference), PRIORITY_LOW);

    registerConverter(new NullConverter(), PRIORITY_VERY_HIGH);
    registerConverter(new IntConverter(), PRIORITY_NORMAL);
    registerConverter(new FloatConverter(), PRIORITY_NORMAL);
    registerConverter(new DoubleConverter(), PRIORITY_NORMAL);
    registerConverter(new LongConverter(), PRIORITY_NORMAL);
    registerConverter(new ShortConverter(), PRIORITY_NORMAL);
    registerConverter((Converter) new CharConverter(), PRIORITY_NORMAL);
    registerConverter(new BooleanConverter(), PRIORITY_NORMAL);
    registerConverter(new ByteConverter(), PRIORITY_NORMAL);

    registerConverter(new StringConverter(), PRIORITY_NORMAL);
    registerConverter(new StringBufferConverter(), PRIORITY_NORMAL);
    registerConverter(new DateConverter(), PRIORITY_NORMAL);
    registerConverter(new BitSetConverter(), PRIORITY_NORMAL);
    registerConverter(new URIConverter(), PRIORITY_NORMAL);
    registerConverter(new URLConverter(), PRIORITY_NORMAL);
    registerConverter(new BigIntegerConverter(), PRIORITY_NORMAL);
    registerConverter(new BigDecimalConverter(), PRIORITY_NORMAL);

    registerConverter(new ArrayConverter(mapper), PRIORITY_NORMAL);
    registerConverter(new CharArrayConverter(), PRIORITY_NORMAL);
    registerConverter(new CollectionConverter(mapper), PRIORITY_NORMAL);
    registerConverter(new MapConverter(mapper), PRIORITY_NORMAL);
    registerConverter(new TreeMapConverter(mapper), PRIORITY_NORMAL);
    registerConverter(new TreeSetConverter(mapper), PRIORITY_NORMAL);
    registerConverter(new PropertiesConverter(), PRIORITY_NORMAL);
    registerConverter((Converter) new EncodedByteArrayConverter(), PRIORITY_NORMAL);

    registerConverter(new FileConverter(), PRIORITY_NORMAL);
    if (jvm.supportsSQL()) {
      registerConverter(new SqlTimestampConverter(), PRIORITY_NORMAL);
      registerConverter(new SqlTimeConverter(), PRIORITY_NORMAL);
      registerConverter(new SqlDateConverter(), PRIORITY_NORMAL);
    }
    
    // TODO: commentato 19/02/2012 - da java.lang.NoClassDefFoundError: Could not initialize class com.thoughtworks.xstream.converters.extended.DynamicProxyConverter
    // su GAE
    
//  registerConverter(new DynamicProxyConverter(mapper, classLoaderReference), PRIORITY_NORMAL);
    registerConverter(new JavaClassConverter(classLoaderReference), PRIORITY_NORMAL);
    registerConverter(new JavaMethodConverter(classLoaderReference), PRIORITY_NORMAL);
    registerConverter(new JavaFieldConverter(classLoaderReference), PRIORITY_NORMAL);

    /*
     * if (jvm.supportsAWT()) { registerConverter(new FontConverter(),
     * PRIORITY_NORMAL); registerConverter(new ColorConverter(),
     * PRIORITY_NORMAL); registerConverter(new TextAttributeConverter(),
     * PRIORITY_NORMAL); }
     */
    if (jvm.supportsSwing()) {
      registerConverter(new LookAndFeelConverter(mapper, reflectionProvider), PRIORITY_NORMAL);
    }
    registerConverter(new LocaleConverter(), PRIORITY_NORMAL);
    registerConverter(new GregorianCalendarConverter(), PRIORITY_NORMAL);

    /*
     * if (JVM.is14()) { // late bound converters - allows XStream to be
     * compiled on earlier JDKs registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.extended.SubjectConverter",
     * PRIORITY_NORMAL, new Class[] { Mapper.class }, new Object[] { mapper });
     * registerConverterDynamically
     * ("com.thoughtworks.xstream.converters.extended.ThrowableConverter",
     * PRIORITY_NORMAL, new Class[] { Converter.class }, new Object[] {
     * reflectionConverter }); registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.extended.StackTraceElementConverter"
     * , PRIORITY_NORMAL, null, null); registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.extended.CurrencyConverter",
     * PRIORITY_NORMAL, null, null); registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.extended.RegexPatternConverter",
     * PRIORITY_NORMAL, new Class[] { Converter.class }, new Object[] {
     * reflectionConverter }); registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.extended.CharsetConverter",
     * PRIORITY_NORMAL, null, null); }
     * 
     * if (JVM.is15()) { // late bound converters - allows XStream to be
     * compiled on earlier JDKs if (jvm.loadClass("javax.xml.datatype.Duration")
     * != null) { registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.extended.DurationConverter",
     * PRIORITY_NORMAL, null, null); } registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.enums.EnumConverter",
     * PRIORITY_NORMAL, null, null); registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.enums.EnumSetConverter",
     * PRIORITY_NORMAL, new Class[] { Mapper.class }, new Object[] { mapper });
     * registerConverterDynamically
     * ("com.thoughtworks.xstream.converters.enums.EnumMapConverter",
     * PRIORITY_NORMAL, new Class[] { Mapper.class }, new Object[] { mapper });
     * registerConverterDynamically
     * ("com.thoughtworks.xstream.converters.basic.StringBuilderConverter",
     * PRIORITY_NORMAL, null, null); registerConverterDynamically(
     * "com.thoughtworks.xstream.converters.basic.UUIDConverter",
     * PRIORITY_NORMAL, null, null); }
     */

    registerConverter(new SelfStreamingInstanceChecker(reflectionConverter, this), PRIORITY_NORMAL);
  }

  protected void setupImmutableTypes() {
    ensureJVM();

    // primitives are always immutable
    addImmutableType(boolean.class);
    addImmutableType(Boolean.class);
    addImmutableType(byte.class);
    addImmutableType(Byte.class);
    addImmutableType(char.class);
    addImmutableType(Character.class);
    addImmutableType(double.class);
    addImmutableType(Double.class);
    addImmutableType(float.class);
    addImmutableType(Float.class);
    addImmutableType(int.class);
    addImmutableType(Integer.class);
    addImmutableType(long.class);
    addImmutableType(Long.class);
    addImmutableType(short.class);
    addImmutableType(Short.class);

    // additional types
    addImmutableType(Mapper.Null.class);
    addImmutableType(BigDecimal.class);
    addImmutableType(BigInteger.class);
    addImmutableType(String.class);
    addImmutableType(URI.class);
    addImmutableType(URL.class);
    addImmutableType(File.class);
    addImmutableType(Class.class);

    /*
    if (jvm.supportsAWT()) {
      addImmutableTypeDynamically("java.awt.font.TextAttribute");
    }

    if (JVM.is14()) {
      // late bound types - allows XStream to be compiled on earlier JDKs
      addImmutableTypeDynamically("java.nio.charset.Charset");
      addImmutableTypeDynamically("java.util.Currency");
    }
    */
  }

}
