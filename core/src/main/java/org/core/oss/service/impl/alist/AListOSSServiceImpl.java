package org.core.oss.service.impl.alist;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.properties.SaveConfig;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.oss.model.Resource;
import org.core.oss.service.OSSService;
import org.core.oss.service.OSSServiceAbs;
import org.core.oss.service.impl.alist.enums.ResourceEnum;
import org.core.oss.service.impl.alist.model.list.FsList;
import org.core.oss.service.impl.alist.util.AlistUtil;
import org.core.oss.service.impl.alist.util.RequestUtils;
import org.core.utils.ExceptionUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service(AListOSSServiceImpl.SERVICE_NAME)
public class AListOSSServiceImpl extends OSSServiceAbs implements OSSService {
    
    public static final String SERVICE_NAME = "aList";
    
    private static final String LOGIN_KEY = "loginKey";
    
    // 创建登录缓存
    public final TimedCache<String, String> loginTimeCacheStr;
    
    
    public AListOSSServiceImpl(SaveConfig config, TbResourceService tbResourceService) {
        super(config, tbResourceService);
        
        long timeout = this.config.getBufferTime();
        loginTimeCacheStr = CacheUtil.newTimedCache(timeout);
        loginTimeCacheStr.schedulePrune(60 * 1000L);
        
    }
    
    
    @Override
    public String getMode() {
        return SERVICE_NAME;
    }
    
    /**
     * 登录错误直接抛出异常
     */
    @Override
    public void isConnected() {
        String loginCacheStr = loginTimeCacheStr.get(LOGIN_KEY);
        if (StringUtils.isBlank(loginCacheStr)) {
            String login = login(this.config.getHost(), this.config.getAccessKey(), this.config.getSecretKey());
            loginTimeCacheStr.put(LOGIN_KEY, login);
            ExceptionUtil.isNull(StringUtils.isBlank(loginTimeCacheStr.get(LOGIN_KEY)), ResultCode.OSS_LOGIN_ERROR);
        }
    }
    
    /**
     * 列出集合下路径所有文件
     *
     * @param paths   路径
     * @param refresh 是否刷新
     * @param type    合集类型
     * @return 合集数据
     */
    @Override
    public Map<String, Resource> getResourceList(Collection<String> paths, boolean refresh, ResourceEnum type) {
        TimedCache<String, Resource> cacheResources = cache.get(type);
        TimedCache<String, String> cacheTimeMd5 = this.cacheMd5.get(type);
        Map<String, Resource> resourceMap = new HashMap<>();
        
        if (Boolean.TRUE.equals(refresh) || CollUtil.isEmpty(resourceMap)) {
            cacheResources.clear();
            cacheTimeMd5.clear();
            scanResource(refresh, config.getDeep(), type);
        }
        conditionGetItemByPath(paths, cacheResources, resourceMap);
        return resourceMap;
    }
    
    /**
     * 获取音乐地址
     *
     * @param md5Set  音乐文件文件MD5
     * @param refresh 是否刷新缓存
     * @param type    合集类型
     * @return 音乐地址 key md5, value url, size
     */
    @Override
    public Map<String, Resource> getResourceByMd5ToMap(Collection<String> md5Set, boolean refresh, ResourceEnum type) {
        TimedCache<String, Resource> cacheResources = cache.get(type);
        TimedCache<String, String> md5Mapping = cacheMd5.get(type);
        Map<String, Resource> resourceMap = new HashMap<>();
        
        if (Boolean.TRUE.equals(refresh) || CollUtil.isEmpty(resourceMap)) {
            cacheResources.clear();
            md5Mapping.clear();
            scanResource(refresh, config.getDeep(), type);
        }
        conditionGetItemByMd5(md5Set, cacheResources, md5Mapping, resourceMap);
        return resourceMap;
    }
    
    private void scanResource(boolean refresh, int deep, ResourceEnum type) {
        String loginJwtCache = getLoginJwtCache();
        
        Set<String> col = new LinkedHashSet<>();
        correspondingData(type, col);
        
        for (String s : col) {
            fillingData(refresh, deep, loginJwtCache, s, type);
        }
    }
    
    private void fillingData(boolean refresh, int deep, String loginJwtCache, String path, ResourceEnum type) {
        TimedCache<String, Resource> cacheResources = cache.get(type);
        TimedCache<String, String> md5MappingPath = cacheMd5.get(type);
        
        Set<FsList> list = AlistUtil.list(config, path, refresh, loginJwtCache);
        Deque<FsList> linkList = new ArrayDeque<>(list);
        for (int i = 0; i < deep; i++) {
            if (CollUtil.isEmpty(linkList)) {
                break;
            }
            while (CollUtil.isNotEmpty(linkList)) {
                FsList fsList = linkList.pop();
                if (Boolean.TRUE.equals(fsList.getIsDir())) {
                    Set<FsList> list1 = AlistUtil.list(config, fsList.getPath(), refresh, loginJwtCache);
                    linkList.addAll(list1);
                } else {
                    // 只添加指定后缀
                    if (!config.getScanFilter(type).contains(FileUtil.extName(fsList.getName()))) {
                        continue;
                    }
                    Resource e = new Resource();
                    e.setName(fsList.getName());
                    e.setPath(fsList.getPath());
                    e.setUrl(String.format("%s/d/%s", config.getHost(), e.getPath()));
                    e.setHref(String.format("%s/%s", config.getHost(), e.getPath()));
                    e.setSize(fsList.getSize());
                    e.setModificationTime(DateUtil.parse(fsList.getModified()));
                    e.setFileExtension(FileUtil.getSuffix(fsList.getName()));
                    
                    TbResourcePojo tbResourcePojo = dbCache.get(e.getPath(), () -> tbResourceService.getResourceByPath(e.getPath()));
                    if (Objects.nonNull(tbResourcePojo)) {
                        md5MappingPath.put(tbResourcePojo.getMd5(), e.getPath());
                    }
                    cacheResources.put(e.getPath(), e);
                }
            }
        }
    }
    
    
    private String login(String host, String accessKey, String secretKey) {
        return RequestUtils.login(host, accessKey, secretKey);
    }
    
    @Nullable
    private String getLoginJwtCache() {
        String loginCacheStr = loginTimeCacheStr.get(LOGIN_KEY);
        if (StringUtils.isBlank(loginCacheStr)) {
            isConnected();
            loginCacheStr = loginTimeCacheStr.get(LOGIN_KEY);
        }
        return loginCacheStr;
    }
    
    @Override
    public String upload(List<String> paths, Integer index, File srcFile, String md5, ResourceEnum type) {
        try {
            if (FileUtil.isDirectory(srcFile)) {
                throw new BaseException(ResultCode.FILENAME_INVALID);
            }
            long size = FileUtil.size(srcFile);
            if (size == 0) {
                throw new BaseException(ResultCode.FILE_SIZE_CANNOT_BE_ZERO);
            }
            String musicAddresses;
            if (StringUtils.isEmpty(md5)) {
                BufferedInputStream inputStream = FileUtil.getInputStream(srcFile);
                String tempMd5 = DigestUtils.md5DigestAsHex(inputStream);
                inputStream.close();
                musicAddresses = getResourceUrlByMd5(tempMd5, false, type);
            } else {
                musicAddresses = getResourceUrlByMd5(md5, false, type);
            }
            if (StringUtils.isNotBlank(musicAddresses)) {
                return srcFile.getName();
            }
        } catch (BaseException e) {
            if (!StringUtils.equals(e.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                throw new BaseException(e.getCode(), e.getResultMsg());
            }
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
        String loginJwtCache = getLoginJwtCache();
        String path = RequestUtils.upload(config.getHost(), paths.get(index), srcFile, loginJwtCache);
        // 校验是否上传成功
        try {
            isExist(path, type);
        } catch (Exception e) {
            throw new BaseException(ResultCode.OSS_UPLOAD_ERROR);
        }
        return path;
    }
    
    @Override
    public void delete(List<String> paths, ResourceEnum type) {
        String loginCacheStr = getLoginJwtCache();
        
        Map<String, Resource> resourceList = this.getResourceList(paths, false, type);
        // 音乐地址URL缓存
        Map<String, List<Resource>> map = resourceList.values().parallelStream().collect(Collectors.toMap(resource -> {
            Path path = Paths.get(resource.getPath());
            return path.getParent().toString();
        }, ListUtil::toList, (o1, o2) -> {
            o2.addAll(o1);
            return o2;
        }));
        RequestUtils.delete(config.getHost(), map, loginCacheStr);
    }
    
    /**
     * 重命名
     *
     * @param path    旧文件名
     * @param newName 新文件名
     * @param type    文件类型
     * @return 修改后的文件路径
     */
    @Override
    public String rename(String path, String newName, ResourceEnum type) {
        String loginJwtCache = getLoginJwtCache();
        isExist(path, type);
        Resource resource = this.getResource(path, false, type);
        RequestUtils.rename(config.getHost(), loginJwtCache, resource.getPath(), newName);
        return getReName(path, newName);
    }
}
