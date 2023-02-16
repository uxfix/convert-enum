package mybatis.example.entity;

import com.dekux.core.ConvertEnum;

public enum GenderEnum implements ConvertEnum {
    FEMALE(11, "女"),
    MALE(22, "男");

    private final String desc;
    private final int code;

    GenderEnum(int code, String desc) {
        this.desc = desc;
        this.code = code;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
