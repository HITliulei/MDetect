package com.ll.kubernetes.bean.Node;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Lei
 * @version 0.1
 * @date 2020/12/8
 */

@Getter
@Setter
@ToString
public class NodeList {

    private List<Node> list;
}
