package com.dekux.convert.mybatis;

import com.dekux.core.ConfigTypeEnum;
import com.dekux.core.ConvertConfig;
import com.dekux.core.ConvertEnum;
import com.dekux.core.ConvertEnumContainer;
import com.dekux.util.CommonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用于解决 Mybatis 枚举输入输出转换
 *
 * @author yuan
 * @since 1.0
 */
public class ConvertEnumTypeHandler<E extends Enum<E>> extends BaseTypeHandler<E> {

    private final ConvertEnumContainer container;
    private final ConvertConfig convertConfig;
    private final Class<E> type;

    public ConvertEnumTypeHandler(ConvertEnumContainer container, Class<E> type) {
        this.container = container;
        convertConfig = container.getConfig(ConfigTypeEnum.MYBATIS);
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        String value = String.valueOf(
                CommonUtil.policyWriter((ConvertEnum) parameter,
                        convertConfig.getOutType()));
        if (jdbcType == null) {
            ps.setString(i, value);
        } else {
            ps.setObject(i, value, jdbcType.TYPE_CODE);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null;
        }
        return container.get(value, type, convertConfig.getInType());
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null) {
            return null;
        }
        return container.get(value, type, convertConfig.getInType());
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null) {
            return null;
        }
        return container.get(value, type, convertConfig.getInType());
    }
}
