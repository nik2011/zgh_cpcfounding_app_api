package com.yitu.cpcFounding.api.domain;

import com.yitu.cpcFounding.api.utils.CommonUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * 文件配置
 *
 * @author zhujinming
 * @date 20/04/26 14:50
 */
@Component
@ConfigurationProperties(prefix = "file")
@Setter
@Getter
@Validated
public class FileConfigEntity {
    @NotEmpty
    private String windowUrl;
    @NotEmpty
    private String linuxUrl;

    private Boolean mappingSwitch = Boolean.TRUE;

    private String url;

    public void setWindowUrl(String windowUrl) {
        if (CommonUtil.isOsWindows()) {
            this.url = CommonUtil.fixPath(windowUrl);
        }
        this.windowUrl = windowUrl;
    }

    public void setLinuxUrl(String linuxUrl) {
        if (!CommonUtil.isOsWindows()) {
            this.url = CommonUtil.fixPath(linuxUrl);
        }
        this.linuxUrl = linuxUrl;
    }
}
