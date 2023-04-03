package org.api.common.service;

import lombok.extern.slf4j.Slf4j;
import org.core.config.SaveConfig;
import org.core.pojo.MusicUrlPojo;
import org.core.service.QukuService;
import org.oss.factory.OSSFactory;
import org.oss.service.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service("MusicCommonApi")
public class MusicCommonApi {
    
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 访问音乐文件地址
     */
    @Autowired
    private SaveConfig config;
    
    public List<MusicUrlPojo> getMusicUrlByMusicId(Long musicId) {
        return getMusicUrlByMusicId(Set.of(musicId));
    }
    
    public List<MusicUrlPojo> getMusicUrlByMusicId(Set<Long> musicIds) {
        List<MusicUrlPojo> list = qukuService.getMusicUrl(musicIds);
        for (MusicUrlPojo musicUrlPojo : list) {
            try {
                OSSService aList = OSSFactory.ossFactory("AList");
                // 获取音乐地址
                String musicAddresses = aList.getMusicAddresses(config.getHost(),
                        config.getObjectSave(),
                        musicUrlPojo.getMd5() + "." + musicUrlPojo.getEncodeType());
                musicUrlPojo.setUrl(musicAddresses);
            } catch (Exception e) {
                musicUrlPojo.setUrl("");
                log.error("获取下载地址出错" + e.getMessage(), e);
            }
        }
        return list;
    }
}
