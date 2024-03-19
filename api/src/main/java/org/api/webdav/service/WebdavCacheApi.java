package org.api.webdav.service;

import cn.hutool.core.date.DateUtil;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.webdav.model.CollectTypeList;
import org.api.webdav.model.PlayListRes;
import org.core.model.WebDavResource;
import org.core.mybatis.pojo.TbCollectPojo;
import org.core.oss.model.Resource;
import org.core.service.RemoteStorageService;
import org.core.utils.i18n.I18nUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebdavCacheApi {
    
    private final Cache<String, WebDavResource> webdavCache;
    
    private final WebdavApi webdavApi;
    
    private final RemoteStorageService remoteStorageService;
    
    private static String i18LikeStr() {
        return "/" + I18nUtil.getMsg("webdav.playlist.like_playlist");
    }
    
    private static String i18RecommendStr() {
        return "/" + I18nUtil.getMsg("webdav.playlist.recommended_playlist");
    }
    
    private static String i18CommonStr() {
        return "/" + I18nUtil.getMsg("webdav.playlist.saved_playlist");
    }
    
    private static String i18RefreshStr() {
        return "/" + I18nUtil.getMsg("webdav.playlist.refresh");
    }
    
    /**
     * 处理字符串， 对字符串中有 <code>/</code> 和 <code>\</code> 的进行转移
     *
     * @return 处理后的字符串
     */
    private static String handleName(final String name) {
        final String replacePlaceholderCharacter = "-";
        final String o1 = StringUtils.replace(name, "/", replacePlaceholderCharacter);
        return StringUtils.replace(o1, "\\", replacePlaceholderCharacter);
    }
    
    public WebDavResource getResource(String path) {
        if (StringUtils.equals(path, "/")) {
            return handleRoot();
        }
        // 刷新缓存
        if (StringUtils.equalsAnyIgnoreCase(path, i18RefreshStr())) {
            handleRoot();
            webdavApi.refreshAllCache();
            return webdavCache.asMap().get(path);
        }
        return webdavCache.asMap().get(path);
    }
    
    private WebDavResource handleRoot() {
        List<WebDavResource> resource = new ArrayList<>();
        
        Long userId = 424608186796165L;
        CollectTypeList userPlayList = webdavApi.getUserPlayList(userId);
        
        // 喜欢歌单
        String likeStr = I18nUtil.getMsg("webdav.playlist.like_playlist");
        WebDavResource likeResource = WebDavResource.createSelectCollect(likeStr, i18LikeStr());
        List<TbCollectPojo> likeCollect = userPlayList.getLikeCollect();
        fillLikeCollectMusic(resource, likeCollect, likeResource, Objects.isNull(likeCollect), i18LikeStr());
        // 推荐歌单
        String recommendStr = I18nUtil.getMsg("webdav.playlist.recommended_playlist");
        WebDavResource recommendResource = WebDavResource.createSelectCollect(recommendStr, i18RecommendStr());
        List<TbCollectPojo> recommendCollect = userPlayList.getRecommendCollect();
        fillCollectMusic(resource, recommendCollect, recommendResource, Objects.isNull(recommendCollect), i18RecommendStr());
        
        // 收藏歌单
        String common = I18nUtil.getMsg("webdav.playlist.saved_playlist");
        WebDavResource commonResource = WebDavResource.createSelectCollect(common, i18CommonStr());
        List<TbCollectPojo> ordinaryCollect = userPlayList.getOrdinaryCollect();
        fillCollectMusic(resource, ordinaryCollect, commonResource, Objects.isNull(ordinaryCollect), i18CommonStr());
        
        // 刷新
        WebDavResource refreshResource = new WebDavResource();
        String refreshStr = I18nUtil.getMsg("webdav.playlist.refresh");
        refreshResource.setName(refreshStr);
        refreshResource.setPath(i18RefreshStr());
        resource.add(refreshResource);
        return WebDavResource.createRoot(resource);
    }
    
    private void fillCollectMusic(List<WebDavResource> resource, List<TbCollectPojo> collectIds, WebDavResource davResource, boolean b, String path) {
        if (Boolean.FALSE.equals(b)) {
            // 可能会有多个歌单
            ArrayList<WebDavResource> collectResources = new ArrayList<>();
            collectIds.parallelStream().filter(Objects::nonNull).forEach(collectPojo -> {
                List<PlayListRes> playListMusic = webdavApi.getPlayListMusic(collectPojo.getId());
                List<WebDavResource> musicResources = playListMusic.parallelStream().map(s -> {
                    Resource musicResource = remoteStorageService.getMusicResource(s.getPath());
                    // 在子目录下不需要填充父目录名,但是在缓存中需要填写父目录名。因为tomcat返回时会自动添加当前文件的父目录路径。
                    // 前端请求时也会自动使用绝对路径，所以缓存需要使用绝对路径
                    String resourcePathStr = "/%s.%s".formatted(handleName(s.getMusicName()), musicResource.getFileExtension());
                    WebDavResource r = WebDavResource.createFile(s.getMusicName(),
                            s.getMd5(),
                            resourcePathStr,
                            s.getPath(),
                            musicResource.getUrl(),
                            s.getCreateTime(),
                            s.getUpdateTime(),
                            musicResource.getSize());
                    // 缓存音乐填充全路径, 根目录/歌单名/音乐名
                    webdavCache.put(path + "/" + collectPojo.getPlayListName() + resourcePathStr, r);
                    return r;
                }).toList();
                // 歌单创建并添加音乐
                WebDavResource collectResource = WebDavResource.createCollect(collectPojo.getPlayListName(),
                        "/" + collectPojo.getPlayListName(),
                        DateUtil.date(collectPojo.getCreateTime()),
                        DateUtil.date(collectPojo.getUpdateTime()),
                        musicResources);
                // 添加歌单
                collectResources.add(collectResource);
                // 填充缓存 路径绝对路径
                webdavCache.put(path + "/" + collectPojo.getPlayListName(), collectResource);
            });
            davResource.setResource(collectResources);
        }
        resource.add(davResource);
        webdavCache.put(davResource.getPath(), davResource);
    }
    
    private void fillLikeCollectMusic(List<WebDavResource> resource, List<TbCollectPojo> collectIds, WebDavResource davResource, boolean b, String path) {
        if (Boolean.FALSE.equals(b)) {
            List<WebDavResource> webDavResource = new ArrayList<>();
            collectIds.parallelStream().filter(Objects::nonNull).forEach(collectPojo -> {
                List<PlayListRes> playListMusic = webdavApi.getPlayListMusic(collectPojo.getId());
                List<WebDavResource> resources = playListMusic.parallelStream().map(s -> {
                    Resource musicResource = remoteStorageService.getMusicResource(s.getPath());
                    // 在子目录下不需要填充父目录名,但是在缓存中需要填写父目录名。因为tomcat返回时会自动添加当前文件的父目录路径。
                    // 前端请求时也会自动使用绝对路径，所以缓存需要使用绝对路径
                    String musicName = handleName(s.getMusicName()) + "." + musicResource.getFileExtension();
                    String resourcePathStr = "/%s/%s".formatted(collectPojo.getPlayListName(), musicName);
                    WebDavResource r = WebDavResource.createFile(s.getMusicName(),
                            s.getMd5(),
                            resourcePathStr,
                            s.getPath(),
                            musicResource.getUrl(),
                            s.getCreateTime(),
                            s.getUpdateTime(),
                            musicResource.getSize());
                    // 缓存填充全路径, 歌单名/音乐名
                    webdavCache.put(path + resourcePathStr, r);
                    return r;
                }).toList();
                webDavResource.addAll(resources);
            });
            davResource.setResource(webDavResource);
        }
        resource.add(davResource);
        webdavCache.put(davResource.getPath(), davResource);
    }
}
