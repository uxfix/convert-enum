package datajpa.example.demo;

import com.dekux.core.ConvertEnumContainer;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

/**
 * @desc: ConvertEnumJavaTypeDescriptor
 * @author: Yuan
 * @create: 2023/2/17
 **/
public class ConvertEnumJavaTypeDescriptor<T extends Enum> extends AbstractTypeDescriptor<T> {
    private final Class<T> type;
    private final ConvertEnumContainer container;
    public ConvertEnumJavaTypeDescriptor(Class<T> type,ConvertEnumContainer container) {
        super(type);
        this.type = type;
        this.container = container;
    }

    @Override
    public T fromString(String string) {
        return null;
    }

    @Override
    public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
        return null;
    }

    @Override
    public <X> T wrap(X value, WrapperOptions options) {
        return null;
    }
}
