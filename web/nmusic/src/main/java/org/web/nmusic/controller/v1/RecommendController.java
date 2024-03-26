package org.web.nmusic.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.personalfm.PersonalFMRes;
import org.api.nmusic.model.vo.personalized.PersonalizedRes;
import org.api.nmusic.model.vo.recommend.albumnew.RecommendAlbumNewRes;
import org.api.nmusic.model.vo.recommend.resource.DailyRecommendResourceRes;
import org.api.nmusic.model.vo.recommend.songs.RecommendSongerRes;
import org.api.nmusic.service.RecommendApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.NeteaseResult;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * NMusicApi 推荐控制器
 * </p>
 *
 * @author Sakura
 * @since 2023-03-13
 */
@RestController(NeteaseCloudConfig.NETEASECLOUD + "RecommendController")
@RequestMapping("/")
@Slf4j
public class RecommendController {
    
    private final RecommendApi recommendApi;
    
    public RecommendController(RecommendApi recommendApi) {
        this.recommendApi = recommendApi;
    }
    
    /**
     * 推荐FM
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/personal_fm", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult personalFM() {
        PersonalFMRes personalFM = recommendApi.personalFM();
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(personalFM));
        return r.success();
    }
    
    
    /**
     * 推荐歌单
     *
     * @param limit 取出数量 , 默认为 30 (不支持 offset)
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/personalized", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult personalized(@RequestParam(value = "limit", required = false, defaultValue = "30") Long limit) {
        PersonalizedRes res = recommendApi.personalized(limit);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res));
        return r.success();
    }
    
    /**
     * 每日推荐歌单
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/recommend/resource", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult recommendResource() {
        List<DailyRecommendResourceRes> res = recommendApi.recommendResource(20);
        NeteaseResult r = new NeteaseResult();
        r.put("featureFirst", true);
        r.put("haveRcmdSongs", false);
        r.put("recommend", res);
        return r.success();
    }
    
    /**
     * 推荐歌曲
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/recommend/songs", method = {RequestMethod.GET, RequestMethod.POST})
    public NeteaseResult recommendSongs() {
        RecommendSongerRes res = recommendApi.recommendSongs(31);
        NeteaseResult r = new NeteaseResult();
        return r.success(res);
    }
    
    /**
     * 全部新碟
     */
    @WebLog(LogNameConstant.N_MUSIC)
    @RequestMapping(value = "/album/new", method = {RequestMethod.GET, RequestMethod.POST})
    @AnonymousAccess
    public NeteaseResult albumNew(String area,@RequestParam(value = "offset",required = false,defaultValue = "0") Long offset, @RequestParam(value = "limit",required = false,defaultValue = "30") Long limit) {
        Page<RecommendAlbumNewRes> res = recommendApi.albumNew(area, offset, limit);
        NeteaseResult r = new NeteaseResult();
        r.putAll(BeanUtil.beanToMap(res,"records"));
        r.put("albums", res.getRecords());
        return r.success();
    }
    
}