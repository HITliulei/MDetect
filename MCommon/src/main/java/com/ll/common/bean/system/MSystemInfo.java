package com.ll.common.bean.system;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/8
 */

@Getter
@Setter
public class MSystemInfo {


    private Set<MServiceInfo> set;


    public MSystemInfo(){
//        set.add(new MServiceInfo())
    }


    public boolean add(MServiceInfo mServiceInfo){
        return this.set.add(mServiceInfo);
    }
}
