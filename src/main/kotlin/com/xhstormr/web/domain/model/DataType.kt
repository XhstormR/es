package com.xhstormr.web.domain.model

enum class DataType(val description: String) : BaseEnum {
    APPLICATION("应用程序日志"),
    SYSTEM("系统日志"),
    SECURITY("安全日志"),
    DEVICE("设备日志"),
    OPENVPN("OPENVPN 日志"),

    PCAP("PCAP 类型"),

    KEYBOARD("键盘记录"),
    PROCESS("进程"),
    SERVICE("服务"),
    PORT("端口"),
    CPU("CPU"),
    MEMORY("内存"),
    DISK("磁盘"),
    USER("用户"),
    REG("注册表读写"),
    SCHEDULE("计划任务"),
    AUTORUN("启动项"),
    HOOK("挂钩"),
    DNS("DNS 解析");
}
