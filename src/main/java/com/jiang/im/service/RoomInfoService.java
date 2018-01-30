package com.jiang.im.service;

import com.jiang.im.dataobject.RoomInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomInfoService {

    RoomInfo createRoom(RoomInfo roomInfo);

    Page<RoomInfo> findList(Pageable pageable);

}
