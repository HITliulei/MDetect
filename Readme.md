# 异味检测工具

异味检测工具， 内部核心基于bal smell实证研究检测框架

- MCenterControl: 中控， 负责所有组件之间的调度
- MCommon： 定义通用的类以及其他的配置信息
- MMontinor： 性能检测工具， 可以检测pod 以及 node cpu以及mem的情况
- MClusterController： k8s集群控制工具， 部署pod 发布服务用的
- MUSerCommand： 发布用户需求，对系统进行压力的测试
- MDataInit: 用于数据的初始化（针对的是复旦的系统）

- MEmpiricalComparison： 用于实证对比

- MDployment： 部署组件

