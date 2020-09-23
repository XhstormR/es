package com.xhstormr.web.domain.model

import com.fasterxml.jackson.annotation.JsonFormat

/**
 * @author zhangzf
 * @create 2019/2/17 11:41
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
interface BaseEnum {
    val type
        get() = (this as Enum<*>).name
}
