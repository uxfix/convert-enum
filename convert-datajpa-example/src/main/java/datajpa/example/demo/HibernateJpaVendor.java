package datajpa.example.demo;

import com.dekux.core.ConvertEnumContainer;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.type.TypeFactory;
import org.hibernate.type.descriptor.java.spi.JavaTypeDescriptorRegistry;
import org.hibernate.type.spi.TypeConfiguration;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * @desc: HibernateJpaVendorAdapter
 * @author: Yuan
 * @create: 2023/2/17
 **/
public class HibernateJpaVendor extends HibernateJpaVendorAdapter {

    private final ConvertEnumContainer container;
    private TypeConfiguration typeConfiguration;

    public HibernateJpaVendor(ConvertEnumContainer container) {
        this.container = container;
    }

    @Override
    public void postProcessEntityManagerFactory(EntityManagerFactory emf) {
        MetamodelImpl metamodel = (MetamodelImpl) emf.getMetamodel();
        typeConfiguration = metamodel.getTypeConfiguration();
        JavaTypeDescriptorRegistry jtdr = typeConfiguration.getJavaTypeDescriptorRegistry();
        Set<Class<?>> enumClassAll = container.getEnumClassAll();
        if (!CollectionUtils.isEmpty(enumClassAll)) {
            for (Class<?> enumClass : enumClassAll) {
                jtdr.addDescriptor(new ConvertEnumJavaTypeDescriptor(enumClass, container));
            }
        }
        replaceTypeResolver();

    }

    private void replaceTypeResolver() {
        try {
            Field typeFactoryField = TypeConfiguration.class.getDeclaredField("typeFactory");
            typeFactoryField.setAccessible(true);
            TypeFactory typeFactory = (TypeFactory) typeFactoryField.get(typeConfiguration);
            HibernateTypeResolver hibernateTypeResolver = new HibernateTypeResolver(typeConfiguration, typeFactory, container);
            Field typeResolverField = TypeConfiguration.class.getDeclaredField("typeResolver");
            typeResolverField.setAccessible(true);
            typeResolverField.set(typeConfiguration, hibernateTypeResolver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
