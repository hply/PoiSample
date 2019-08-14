package cn.iyuxuan.poi;

import cn.iyuxuan.poi.utils.StringUtils;
import cn.iyuxuan.poi.utils.XmlResourcesUtils;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class CheckDifferent {

    public static void main(String[] args) throws Exception{
        String sourcePath = "-tw";
        String targetPath = "-ja";
        File sourceFile = new File("C:\\code\\VovaAndroid\\reslib\\src\\main\\res\\values"+sourcePath,
                "strings.xml");
        File targetFile = new File("C:\\code\\VovaAndroid\\reslib\\src\\main\\res\\values"+targetPath,
                "strings.xml");
        TreeMap<String, String> sourceMap = XmlResourcesUtils.parseXml2Map(sourceFile);
        TreeMap<String, String> targetMap = XmlResourcesUtils.parseXml2Map(targetFile);
        for (Map.Entry<String,String> entry:sourceMap.entrySet()){
            if (!targetMap.containsKey(entry.getKey())){
                StringUtils.log(entry.getKey());
            }
        }

    }

}
