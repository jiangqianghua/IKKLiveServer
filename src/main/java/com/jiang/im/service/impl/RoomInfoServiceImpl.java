package com.jiang.im.service.impl;

import com.jiang.im.dataobject.RoomInfo;
import com.jiang.im.repository.RoomInfoRepository;
import com.jiang.im.service.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoomInfoServiceImpl implements RoomInfoService {
    @Autowired
    RoomInfoRepository repository ;
    @Override
    public RoomInfo createRoom(RoomInfo roomInfo) {
        return repository.save(roomInfo);
    }

    @Override
    public Page<RoomInfo> findList(Pageable pageable) {
        Page<RoomInfo> roomInfoPage = repository.findAll(pageable);
        return roomInfoPage;
    }

    @Override
    public RoomInfo findOne(int roomId) {
        return repository.findOne(roomId);
    }
}
