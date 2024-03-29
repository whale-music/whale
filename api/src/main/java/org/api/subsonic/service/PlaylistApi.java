package org.api.subsonic.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.createplaylist.CreatePlaylistRes;
import org.api.subsonic.model.res.playlist.EntryItem;
import org.api.subsonic.model.res.playlist.PlayList;
import org.api.subsonic.model.res.playlist.PlaylistRes;
import org.api.subsonic.model.res.playlists.PlayLists;
import org.api.subsonic.model.res.playlists.PlaylistItem;
import org.api.subsonic.model.res.playlists.PlaylistsRes;
import org.core.common.constant.PlayListTypeConstant;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.core.service.RemoteStorePicService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service(SubsonicConfig.SUBSONIC + "PlaylistApi")
public class PlaylistApi {
    
    private final QukuAPI qukuApi;
    
    private final AccountService accountService;
    
    private final PlayListService playListService;
    
    private final TbCollectService collectService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    
    public PlaylistApi(QukuAPI qukuApi, AccountService accountService, PlayListService playListService, TbCollectService collectService, RemoteStorePicService remoteStorePicService) {
        this.qukuApi = qukuApi;
        this.accountService = accountService;
        this.playListService = playListService;
        this.collectService = collectService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    public PlaylistsRes getPlaylists(SubsonicCommonReq req, String username) {
        username = StringUtils.isBlank(username) ? req.getU() : username;
        SysUserPojo user = accountService.getUser(username);
        List<CollectConvert> userPlayList = qukuApi.getUserPlayList(user.getId(),
                Arrays.asList(PlayListTypeConstant.ORDINARY, PlayListTypeConstant.ORDINARY));
        
        List<PlaylistItem> playlist = new ArrayList<>();
        for (TbCollectPojo collectPojo : userPlayList) {
            PlaylistItem e = new PlaylistItem();
            e.setId(String.valueOf(collectPojo.getId()));
            e.setName(collectPojo.getPlayListName());
            e.setChanged(LocalDateTimeUtil.format(collectPojo.getUpdateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            e.setSongCount(qukuApi.getCollectMusicCount(collectPojo.getId()));
            e.setCreated(LocalDateTimeUtil.format(collectPojo.getCreateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            e.setCoverArt(String.valueOf(collectPojo.getId()));
            e.setOwner(user.getUsername());
            playlist.add(e);
        }
        PlayLists playlists = new PlayLists();
        playlists.setPlaylist(playlist);
        PlaylistsRes playlistRes = new PlaylistsRes();
        playlistRes.setPlaylists(playlists);
        return playlistRes;
    }
    
    /**
     * 获取歌单数据
     *
     * @param id 歌单ID
     * @return 返回歌单信息
     */
    public PlaylistRes getPlaylist(Long id) {
        List<TbMusicPojo> playListAllMusic = playListService.getPlayListAllMusic(id);
        
        ArrayList<EntryItem> entry = new ArrayList<>();
        int duration = 0;
        for (TbMusicPojo musicPojo : playListAllMusic) {
            EntryItem e = new EntryItem();
            List<TbResourcePojo> musicUrl = qukuApi.getMusicPaths(CollUtil.newHashSet(musicPojo.getId()));
            e.setId(String.valueOf(musicPojo.getId()));
            e.setTitle(musicPojo.getMusicName());
            TbResourcePojo tbMusicUrlPojo = CollUtil.isEmpty(musicUrl) ? new TbResourcePojo() : musicUrl.get(0);
            e.setBitRate(tbMusicUrlPojo.getRate() == null ? 0 : tbMusicUrlPojo.getRate());
            e.setIsDir(false);
            e.setCoverArt(String.valueOf(musicPojo.getId()));
            e.setPlayed(musicPojo.getCreateTime().toString());
            
            TbAlbumPojo albumByAlbumId = Optional.ofNullable(qukuApi.getAlbumByAlbumId(musicPojo.getAlbumId())).orElse(new AlbumConvert());
            e.setAlbum(albumByAlbumId.getAlbumName());
            e.setAlbumId(String.valueOf(albumByAlbumId.getId()));
            e.setParent(String.valueOf(albumByAlbumId.getId()));
            
            // 流派
            e.setGenre("");
            e.setTrack(0);
            LocalDateTime publishTime = albumByAlbumId.getPublishTime();
            e.setYear(publishTime == null ? null : publishTime.getYear());
            e.setDuration(musicPojo.getTimeLength() / 1000);
            e.setSize(Math.toIntExact(tbMusicUrlPojo.getSize() == null ? 0 : tbMusicUrlPojo.getSize()));
            e.setSuffix(tbMusicUrlPojo.getEncodeType());
            e.setType("music");
            if (StringUtils.equalsIgnoreCase(tbMusicUrlPojo.getEncodeType(), "mp3")) {
                e.setContentType("audio/mpeg");
            } else {
                e.setContentType("audio/" + tbMusicUrlPojo.getEncodeType());
            }
            
            e.setParent(tbMusicUrlPojo.getPath());
            e.setPlayCount(0);
            
            List<ArtistConvert> artistByMusicId = qukuApi.getAlbumArtistByMusicId(musicPojo.getId());
            TbArtistPojo artistPojo = CollUtil.isEmpty(artistByMusicId) ? new TbArtistPojo() : artistByMusicId.get(0);
            e.setArtist(artistPojo.getArtistName());
            e.setArtistId(String.valueOf(artistPojo.getId()));
            
            e.setVideo(false);
            entry.add(e);
            duration += e.getDuration();
        }
    
        TbCollectPojo byId = collectService.getById(id);
        PlayList playlistRes = new PlayList();
        playlistRes.setId(String.valueOf(byId.getId()));
        playlistRes.setName(byId.getPlayListName());
        playlistRes.setSongCount(qukuApi.getCollectMusicCount(byId.getId()));
        playlistRes.setDuration(duration / 1000);
        playlistRes.setJsonMemberPublic(true);
        SysUserPojo byId1 = accountService.getById(byId.getUserId());
        playlistRes.setOwner(Optional.ofNullable(byId1).orElse(new SysUserPojo()).getUsername());
        playlistRes.setCreated(byId.getCreateTime().toString());
        playlistRes.setChanged(byId.getUpdateTime().toString());
        playlistRes.setCoverArt(String.valueOf(byId.getId()));
        
        playlistRes.setEntry(entry);
        PlaylistRes playlistRes1 = new PlaylistRes();
        playlistRes1.setPlaylist(playlistRes);
        return playlistRes1;
    }
    
    public CreatePlaylistRes createPlaylist(SubsonicCommonReq req, Long playlistId, String name, Long songId) {
        CreatePlaylistRes res = new CreatePlaylistRes();
        CollectConvert playList = new CollectConvert();
        // 添加歌单
        if (Objects.isNull(playlistId)) {
            if (StringUtils.isBlank(name)) {
                throw new BaseException(ResultCode.PLAY_LIST_NO_EXIST);
            }
            String userName = req.getU();
            SysUserPojo userByName = Optional.ofNullable(accountService.getUserByName(userName)).orElse(new SysUserPojo());
            playList = qukuApi.createPlayList(userByName.getId(), name, PlayListTypeConstant.ORDINARY);
        } else {
            // 更新
            TbCollectPojo byId = collectService.getById(playlistId);
            BeanUtils.copyProperties(byId, playList);
            playList.setPicUrl(remoteStorePicService.getCollectPicUrl(byId.getId()));
            byId.setPlayListName(name);
            collectService.updateById(byId);
            
        }
        if (Objects.nonNull(songId)) {
            qukuApi.addOrRemoveMusicToCollect(playList.getUserId(), playList.getId(), Collections.singletonList(songId), true);
        }
        CreatePlaylistRes.Playlist playlist = new CreatePlaylistRes.Playlist();
        playlist.setPublicFlag(false);
        playlist.setId(String.valueOf(playList.getId()));
        playlist.setCreated(Date.from(playList.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
        playlist.setName(playList.getPlayListName());
        playlist.setChanged(Date.from(playList.getCreateTime().atZone(ZoneId.systemDefault()).toInstant()));
        playlist.setSongCount(0);
        playlist.setCoverArt(String.valueOf(playList.getId()));
        playlist.setDuration(0);
        
        res.setPlaylist(playlist);
        return res;
    }
    
    public void updatePlaylist(SubsonicCommonReq req, Long playlistId, String name, String comment, Boolean publicFlag, List<Long> songIdToAdd, List<Long> songIndexToRemove) {
        TbCollectPojo entity = new TbCollectPojo();
        entity.setId(playlistId);
        entity.setPlayListName(name);
        entity.setDescription(comment);
        collectService.updateById(entity);
        
        if (CollUtil.isNotEmpty(songIdToAdd)) {
            qukuApi.addOrRemoveMusicToCollect(entity.getUserId(), entity.getId(), songIdToAdd, true);
        }
        
        if (CollUtil.isNotEmpty(songIndexToRemove)) {
            qukuApi.addOrRemoveMusicToCollect(entity.getUserId(), entity.getId(), songIndexToRemove, false);
        }
        
    }
    
    public void deletePlaylist(SubsonicCommonReq req, Long id) {
        String userName = req.getU();
        SysUserPojo userByName = accountService.getUserByName(userName);
        qukuApi.removePlayList(userByName.getId(), Collections.singleton(id));
    }
}
