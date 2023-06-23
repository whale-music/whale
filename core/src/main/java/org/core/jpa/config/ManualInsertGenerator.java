package org.core.jpa.config;

import com.github.yitter.idgen.YitIdHelper;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentityGenerator;

import java.io.Serializable;

/**
 * 自定义的主键生成策略，如果填写了主键id，如果数据库中没有这条记录，则新增指定id的记录；否则更新记录
 * <p>
 * 如果不填写主键id，则利用数据库本身的自增策略指定id
 * <p>
 */
public class ManualInsertGenerator extends IdentityGenerator {
    
    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object obj) {
        Serializable id = s.getEntityPersister(null, obj).getClassMetadata().getIdentifier(obj, s);
        
        if (id != null && Integer.parseInt(String.valueOf(id)) > 0) {
            return id;
        } else {
            return YitIdHelper.nextId();
        }
    }
}
