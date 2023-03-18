package org.web.controller.admin.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.MusicPageReq;
import org.api.admin.service.PlayListApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(AdminConfig.ADMIN + "PlayListController")
@RequestMapping("/admin/playlist")
@Slf4j
public class PlayListController {
    
    @Autowired
    private PlayListApi playList;
    
    /**
     * 获取全部音乐
     *
     * @param req 条件参数
     * @return 返回数据
     */
    @PostMapping("/page")
    public R getMusicPage(@RequestBody MusicPageReq req) {
        return R.success(playList.getMusicPage(req));
    }
    
    /**
     * 获取歌单音乐
     *
     * @param playId 歌单ID
     * @param req    条件参数
     * @return 返回数据
     */
    @PostMapping("/{playId}")
    public R getPlaylist(@PathVariable("playId") String playId, @RequestBody MusicPageReq req) {
        return R.success(playList.getPlaylist(playId, req));
    }
}
