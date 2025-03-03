package com.su.yupao.once;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导入excel
 */
public class ImportExcel {
    public static void main(String[] args) {
        //使用同步读，对表格用户信息处理
        String fileName = "E:\\后端项目\\user-center\\src\\main\\resources\\prodExcel.xlsx";
        synchronousRead(fileName);
    }

    /**
     * 使用监听器一条一条读，适合数据量较大
     * @param fileName
     */
    public static void listener(String fileName){
        /**
         * 最简单的读
         * <p>
         * 1. 创建excel对应的实体对象 参照{@link XingQiuExcelUserInfo}
         * <p>
         * 2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link TableListener}
         * <p>
         * 3. 直接读即可
         */
        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1

        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        // 具体需要返回多少行可以在`PageReadListener`的构造函数设置
        EasyExcel.read(fileName, XingQiuExcelUserInfo.class, new TableListener()).sheet().doRead();
    }
    /**
     * 同步的返回，如果数据量大会把数据放到内存里面，延迟较大，卡顿
     */
    public static void synchronousRead(String fileName) {
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<XingQiuExcelUserInfo> userInfoList = EasyExcel.read(fileName).head(XingQiuExcelUserInfo.class).sheet().doReadSync();
        System.out.println("总数："+userInfoList.size());
        //对数据进行过滤，去除重复数据
        //根据用户昵称进行分组
        Map<String, List<XingQiuExcelUserInfo>> listMap = userInfoList.stream()
                .filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername()))
                .collect(Collectors.groupingBy(XingQiuExcelUserInfo::getUsername));
        System.out.println("不重复的昵称的数目"+listMap.keySet().size());

    }
}
