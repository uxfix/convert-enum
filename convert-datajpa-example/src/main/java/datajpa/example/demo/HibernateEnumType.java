package datajpa.example.demo;

import com.dekux.core.ConfigTypeEnum;
import com.dekux.core.ConvertConfig;
import com.dekux.core.ConvertEnumContainer;
import com.dekux.core.IOTypeEnum;
import org.hibernate.metamodel.model.convert.internal.NamedEnumValueConverter;
import org.hibernate.metamodel.model.convert.internal.OrdinalEnumValueConverter;
import org.hibernate.metamodel.model.convert.spi.EnumValueConverter;
import org.hibernate.type.EnumType;
import org.hibernate.type.descriptor.java.EnumJavaTypeDescriptor;
import org.hibernate.type.spi.TypeConfiguration;

import java.util.Properties;

/**
 * @desc: HibernateEnumType
 * @author: Yuan
 * @create: 2023/2/17
 **/
public class HibernateEnumType extends EnumType {

    private ConvertEnumContainer container;
    private ConvertConfig convertConfig;
    private Class enumClass;
    private EnumValueConverter enumValueConverter;
    private IOTypeEnum outType;
    private TypeConfiguration typeConfiguration;

    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType reader = (ParameterType) parameters.get(PARAMETER_TYPE);
        if (reader != null) {
            enumClass = reader.getReturnedClass().asSubclass(Enum.class);
            final EnumJavaTypeDescriptor enumJavaDescriptor = (EnumJavaTypeDescriptor) getTypeConfiguration()
                    .getJavaTypeDescriptorRegistry()
                    .getDescriptor(enumClass);

            if (IOTypeEnum.CODE.equals(outType) || IOTypeEnum.ORDINAL.equals(outType)) {
                this.enumValueConverter = new OrdinalEnumValueConverter(enumJavaDescriptor);
            } else {
                this.enumValueConverter = new NamedEnumValueConverter(enumJavaDescriptor);
            }
        }

    }

    public void init(ConvertEnumContainer container) {
        this.container = container;
        this.convertConfig = container.getConfig(ConfigTypeEnum.DATA_JPA);
        this.outType = convertConfig.getOutType();
        this.typeConfiguration = getTypeConfiguration();
    }
}
