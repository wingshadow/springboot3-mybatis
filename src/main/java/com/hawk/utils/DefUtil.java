package com.hawk.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 默认
 */
public class DefUtil {
    public static String str(String str, final String def) {
        return StrUtil.blankToDefault(str, def);
    }

    public static String str(String str) {
        return str(str, StrUtil.EMPTY);
    }

    public static String def(String str) {
        return str(str, StrUtil.EMPTY);
    }

    public static <T> T def(T obj, T def) {
        return obj == null ? def : ((obj instanceof String) ? (T) str((String) obj, (String) def) : ObjectUtil.defaultIfNull(obj, def));
    }

    public static <T> T obj(T obj, T def) {
        return def(obj, def);
    }

    public static <T> List<T> def(List<T> list) {
        return CollUtil.emptyIfNull(list);
    }


    public static <T> List<T> def(Iterable<T> collection) {
        return CollUtil.isEmpty(collection) ? ListUtil.empty() : CollUtil.newArrayList(collection);
    }

    public static <T> List<T> def(List<T> list, List<T> def) {
        return CollUtil.defaultIfEmpty(list, def);
    }

    public static <T> List<T> def(T[] array) {
        return ArrayUtil.isEmpty(array) ? new ArrayList<>() : CollUtil.toList(array);
    }

    public static <T> List<T> def(T[] array, T[] def) {
        return ArrayUtil.isEmpty(array) ? CollUtil.toList(def) : CollUtil.toList(array);
    }

    public static <T> Set<T> def(Set<T> set) {
        return CollUtil.emptyIfNull(set);
    }

    public static <T> Set<T> def(Set<T> set, Set<T> def) {
        return CollUtil.defaultIfEmpty(set, def);
    }

    public static <K, V> Map<K, V> def(Map<K, V> map) {
        return MapUtil.emptyIfNull(map);
    }

    public static <K, V> Map<K, V> def(Map<K, V> map, Map<K, V> def) {
        return MapUtil.defaultIfEmpty(map, def);
    }

    public static <T, R> R def(T obj, Function<T, R> defFun) {
        return def(obj, defFun, null);
    }

    public static <T, R> R def(T obj, Function<T, R> func, R def) {
        return obj == null ? def : func.apply(obj);
    }

    public static <T> T def(T obj, Supplier<T> supplier) {
        return obj == null ? supplier.get() : obj;
    }

    public static <T> T def(T obj, Supplier<T> supplier, T def) {
        return DefUtil.def(def(obj, supplier), def);
    }

    /*public static void main(String[] args) {
        String[] array = null;
        DefaultKt.array(array).forEach(System.out::println);
    }*/
}
