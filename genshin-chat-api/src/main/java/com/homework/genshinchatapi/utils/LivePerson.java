package com.homework.genshinchatapi.utils;

import com.homework.common.entity.dto.FriendDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 吴嘉豪
 * @date 2023/10/24 18:30
 */
@Slf4j
public class LivePerson {
    public static Map<String,Boolean> liveList = new HashMap<>();
    public static List<FriendDto> liveFriendDtoList = new ArrayList<>();
    public static boolean checkPersonInLive(String id){
        if(liveList.containsKey(id)){
            return true;
        }
        else return false;
    }
    public static void inLivePerson(String id){
        if(liveList.containsKey(id) == false)
            liveList.put(id, true);
    }
    public static void outLivePerson(String id){
        liveList.remove(id);
    }
    public static List<FriendDto> getLiveFriendDtoList (){
        return liveFriendDtoList;
    }
    public void getFriendDtoFromDataBase () throws InterruptedException {
        while(true){

            Thread.sleep(50000);
        }
    }



}
