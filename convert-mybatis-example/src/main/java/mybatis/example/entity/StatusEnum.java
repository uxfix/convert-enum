package mybatis.example.entity;

import com.dekux.core.ConvertEnum;

public enum StatusEnum implements ConvertEnum {
    normal(1, "正常"),
    LOGGED_OUT(2, "已注销");

    private final String desc;
    private final int code;

    StatusEnum(int code, String desc) {
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
