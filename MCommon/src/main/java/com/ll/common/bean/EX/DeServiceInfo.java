package com.ll.common.bean.EX;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/19
 */

@Getter
@Setter
@NoArgsConstructor
public class DeServiceInfo extends EXUnique{

    private List<ServiceInfo> serviceInfos;


    public DeServiceInfo(String exName, String exTimes){
        super.exName = exName;
        super.exTimes = exTimes;
    }
}
