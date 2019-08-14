package cn.iyuxuan.poi;

import cn.iyuxuan.poi.map.ArrayMap;
import cn.iyuxuan.poi.utils.ExcelUtils;
import cn.iyuxuan.poi.utils.XmlResourcesUtils;

import java.io.File;
import java.util.Map;

public class CheckDifferent {

    public static void main(String[] args) throws Exception {
        File enFile = new File("C:\\code\\VovaAndroid\\reslib\\src\\main\\res\\values",
                "strings.xml");
        File twFile = new File("C:\\code\\VovaAndroid\\reslib\\src\\main\\res\\values-tw",
                "strings.xml");
        File jaFile = new File("C:\\code\\VovaAndroid\\reslib\\src\\main\\res\\values-ja",
                "strings.xml");

        ArrayMap<String, String> enMap = XmlResourcesUtils.parseXml2Map(enFile);
        ArrayMap<String, String> twMap = XmlResourcesUtils.parseXml2Map(twFile);
        ArrayMap<String, String> jaMap = XmlResourcesUtils.parseXml2Map(jaFile);

        ArrayMap<String, ArrayMap<String, String>> lackMap = new ArrayMap<>();

        ArrayMap<String, String> lackEn = new ArrayMap<>();
        ArrayMap<String, String> lackTw = new ArrayMap<>();

        for (Map.Entry<String, String> twEntry : twMap.entrySet()) {
            if (!jaMap.containsKey(twEntry.getKey())) {
                lackTw.put(twEntry.getKey(), twEntry.getValue());
                lackEn.put(twEntry.getKey(), enMap.get(twEntry.getKey()));
            }
        }
        lackMap.put("en英语-English",lackEn);
        lackMap.put("tw繁体中文",lackTw);
        ExcelUtils.export2Excel(lackMap, "d://lan_tw.xlsx");
    }

}
