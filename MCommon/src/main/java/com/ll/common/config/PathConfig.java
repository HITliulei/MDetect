package com.ll.common.config;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/21
 *
 *
 * 所示实验收集用到的数据都在这里进行保存
 */
public class PathConfig {
    // 基础路径
    public static final String BASE_PATH = "/home/ubuntu/data";


    // node 节点的info信息（后续基本用不到）
    public static final String PERFORMANCE_DATA_NODE_PATH = BASE_PATH+"/performance/nodeInfo";

    // pod 节点的info信息
    public static final String PERFORMANCE_DATA_POD_PATH = BASE_PATH + "/performance/podInfo";

}
