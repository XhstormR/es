package com.xhstormr.web.domain.model

import java.util.EnumSet

enum class DataTypeGroup(
    val description: String,
    val types: EnumSet<DataType>
) : BaseEnum {
    LOG(
        "日志", EnumSet.of(
            DataType.APPLICATION,
            DataType.SYSTEM,
            DataType.SECURITY,
            DataType.DEVICE,
            DataType.OPENVPN
        )
    ),
    TRAFFIC(
        "流量", EnumSet.of(
            DataType.PCAP
        )
    ),
    STATISTIC(
        "运行态数据", EnumSet.of(
            DataType.KEYBOARD,
            DataType.PROCESS,
            DataType.SERVICE,
            DataType.PORT,
            DataType.CPU,
            DataType.MEMORY,
            DataType.DISK,
            DataType.USER,
            DataType.REG,
            DataType.SCHEDULE,
            DataType.AUTORUN,
            DataType.HOOK,
            DataType.DNS
        )
    );
}
