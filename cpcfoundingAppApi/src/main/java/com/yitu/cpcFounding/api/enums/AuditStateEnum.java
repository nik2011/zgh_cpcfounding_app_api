package com.yitu.cpcFounding.api.enums;

/**
 * 审核状态 0 待审核、1审核通过、2 机器审核不通过 3 人工审核不通过
 * 审核状态 0 待审核、1审核通过、2 机器审核不通过 3 人工审核不通过 4 机器审核嫌疑 5机器审核通过
 *
 */
public enum  AuditStateEnum {
    /**
     *  待审核
     */
    PENDING(0, "待审核"),
    PASS(1, "审核通过,机器审核通过"),
    NOT_PASS_ROBOT(2, "机器审核不通过"),
    NOT_DELETE_PEOPLE(3, "人工审核不通过"),
    AUTO_SUSPECT(4,"机器审核嫌疑");

    private int value;
    private String name;

    AuditStateEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static AuditStateEnum fromValue(Integer value) {
        if (value != null) {
            for (AuditStateEnum auditStateEnum : AuditStateEnum.values()) {
                if (auditStateEnum.getValue() == value) {
                    return auditStateEnum;
                }
            }
        }
        return null;
    }
}
