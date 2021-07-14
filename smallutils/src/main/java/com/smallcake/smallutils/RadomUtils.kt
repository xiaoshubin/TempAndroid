package com.smallcake.smallutils

import java.util.*

/**
 * Date:2021/7/14 9:35
 * Author:SmallCake
 * Desc:
 */
object RadomUtils {
    /**
     * 生成min-max之间的随机数，包括min，不包括max
     * @param min Float
     * @param max Float
     * @return Float
     */
    fun getFloat(min: Float, max: Float): Float {
        return min + Random().nextFloat() * (max - min)
    }

    /**
     * 生成min-max之间的随机数，包括min，不包括max
     * @param min Int
     * @param max Int
     * @return Int
     */
    fun getInt(min: Int, max: Int): Int {
        return min+ Random().nextInt(max-min)
    }

    /**
     * 生成0-max之间的随机数，包括0，不包括max
     * @param max Int
     * @return Int
     */
    fun getInt(max: Int): Int {
        return Random().nextInt(max)
    }
}