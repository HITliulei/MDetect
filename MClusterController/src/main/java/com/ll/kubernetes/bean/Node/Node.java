package com.ll.kubernetes.bean.Node;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/8
 */
@Getter
@Setter
@ToString
public class Node {

    private String node;
    private String label;
    private String ip;

}
