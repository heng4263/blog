package com.example.kkblog.config.mybatis;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;


/**
 * @Author xiaoke
 * @Date 2024 04 09 20 04
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 这里可以为插入操作时的字段设置默认值
        this.strictInsertFill(metaObject, "scores", Integer.class, 100);
        this.strictInsertFill(metaObject, "starNumber", Integer.class, 0);
        this.strictInsertFill(metaObject, "likeNumber", Integer.class, 0);
        this.strictInsertFill(metaObject, "viewNumber", Integer.class, 0);
        this.strictInsertFill(metaObject, "stick", Integer.class, 0);
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "signature", String.class, "这个人很懒，什么也没有留下~");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 这里可以为更新操作时的字段设置默认值
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date());
    }
}