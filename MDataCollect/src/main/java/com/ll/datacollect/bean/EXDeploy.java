package com.ll.datacollect.bean;

import com.ll.common.bean.deployInfoCollect.ResultDeploy;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/14
 */
@Getter
@Setter
public class EXDeploy {

    private String name;
    private int times;
    private List<ResultDeploy> list;
}
