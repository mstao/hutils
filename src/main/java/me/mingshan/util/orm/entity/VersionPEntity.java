package me.mingshan.util.orm.entity;

import org.springframework.data.annotation.Version;

/**
 * 带版本号的数据库持久化层
 *
 * @author hanjuntao
 * @date 2021/8/3
 */
public class VersionPEntity extends PEntity {
    @Version
    private Long version;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
