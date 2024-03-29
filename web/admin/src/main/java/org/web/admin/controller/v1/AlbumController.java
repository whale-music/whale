package org.web.admin.controller.v1;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.AlbumPageReq;
import org.api.admin.model.req.RemoveAlbumReq;
import org.api.admin.model.req.SaveOrUpdateAlbumReq;
import org.api.admin.model.res.AlbumInfoRes;
import org.api.admin.model.res.AlbumPageRes;
import org.api.admin.service.AlbumApi;
import org.core.common.result.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController(AdminConfig.ADMIN + "AlbumController")
@RequestMapping("/admin/album")
@Slf4j
public class AlbumController {
    
    private final AlbumApi albumApi;
    
    public AlbumController(AlbumApi albumApi) {
        this.albumApi = albumApi;
    }
    
    @PostMapping("/page")
    public R getAllAlbumList(@RequestBody AlbumPageReq req) {
        Page<AlbumPageRes> page = albumApi.getAllAlbumList(req);
        return R.success(page);
    }
    
    @GetMapping("/{id}")
    public R getAlbumInfo(@PathVariable("id") Long id) {
        AlbumInfoRes res = albumApi.getAlbumInfo(id);
        return R.success(res);
    }
    
    /**
     * 添加音乐时选择专辑接口
     *
     * @param name 专辑ID
     */
    @GetMapping("/select")
    public R getSelectAlbumList(@RequestParam(value = "name", required = false) String name) {
        List<Map<String, Object>> albums = albumApi.getSelectAlbumList(name);
        return R.success(albums);
    }
    
    @DeleteMapping("/")
    public R deleteAlbum(@RequestBody RemoveAlbumReq ids, @RequestParam(value = "compel", required = false, defaultValue = "false") Boolean compel) {
        albumApi.deleteAlbum(ids.getIds(), compel);
        return R.success();
    }
    
    @PostMapping("/")
    public R saveOrUpdateAlbum(@RequestBody SaveOrUpdateAlbumReq req) {
        albumApi.saveOrUpdateAlbum(req);
        return R.success();
    }
}
