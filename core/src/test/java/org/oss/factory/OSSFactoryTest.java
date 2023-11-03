package org.oss.factory;

import org.core.common.properties.SaveConfig;
import org.core.oss.factory.OSSFactory;
import org.core.oss.service.OSSService;
import org.core.oss.service.impl.alist.AListOSSServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OSSFactoryTest {
    
    @Test
    void testOssFactory() {
        SaveConfig config = new SaveConfig();
        config.setSaveMode("AList");
        OSSService result1 = OSSFactory.ossFactory(config);
        OSSService result2 = OSSFactory.ossFactory(config);
    
        Assertions.assertEquals(new AListOSSServiceImpl().getMode(), result1.getMode());
        Assertions.assertEquals(result1, result2);
    }
}
