package datajpa.example.demo;

import com.dekux.core.ConvertEnumContainer;
import org.hibernate.MappingException;
import org.hibernate.type.Type;
import org.hibernate.type.TypeFactory;
import org.hibernate.type.TypeResolver;
import org.hibernate.type.spi.TypeConfiguration;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @desc: ConvertEnumTypeResolver
 * @author: Yuan
 * @create: 2023/2/17
 **/
public class HibernateTypeResolver extends TypeResolver {
    private final Set<String> enumClassNameSet;
    private final static String RETURNED_CLASS_KEY = "org.hibernate.type.ParameterType.returnedClass";
    private final TypeConfiguration typeConfiguration;
    private final ConvertEnumContainer container;

    public HibernateTypeResolver(TypeConfiguration typeConfiguration,
                                 TypeFactory typeFactory,
                                 ConvertEnumContainer container) {
        super(typeConfiguration, typeFactory);
        this.typeConfiguration = typeConfiguration;
        this.container = container;
        Set<Class<?>> enumClassAll = container.getEnumClassAll();
        if (!CollectionUtils.isEmpty(enumClassAll)) {
            this.enumClassNameSet = enumClassAll.stream().map(Class::getName).collect(Collectors.toSet());
        } else {
            this.enumClassNameSet = new HashSet<>();
        }

    }

    @Override
    public Type heuristicType(String typeName, Properties parameters) throws MappingException {
        if (parameters != null && enumClassNameSet.contains(parameters.getProperty(RETURNED_CLASS_KEY))) {
            Type type = getTypeFactory().byClass(HibernateEnumType.class, parameters);
            HibernateEnumType hibernateEnumType = (HibernateEnumType) type;
            hibernateEnumType.init(container);
            return type;
        } else {
            return super.heuristicType(typeName, parameters);
        }
    }
}
