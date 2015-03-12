[20150312 王俊彦]
源码指android 5.0，在ftp://192.168.1.60/sw/android/android5.0.0_r7/下

android机上用的armv7结构的系列应该是armv7-a,在android代码里有这么两行
including device/generic/armv7-a-neon/vendorsetup.sh 
including device/generic/armv7-a/vendorsetup.sh
之前以为armv7-a,armv7-r,armv7-m是armv7的3种执行状态，
今天仔细看才发现是3种系列，
“A”系列面向尖端的基于虚拟内存的操作系统和用户应用；
“R”系列针对实时系统；
“M”系列对微控制器和低成本应用提供优化。

