package cn.iyuxuan.poi.utils;

import cn.iyuxuan.poi.map.ArrayMap;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class XmlResourcesUtils {

    /**
     * 获取导出目标路径中已存在的语言包
     *
     * @param outputPath 需要导出的目标路径
     */
    public static ArrayMap<String, ArrayMap<String, String>> readXml2Map(String outputPath) {
        File outPathDir = new File(outputPath);
        ArrayMap<String, ArrayMap<String, String>> resultMap = new ArrayMap<>();
        if (!outPathDir.exists()) {
            boolean mkdirs = outPathDir.mkdirs();
            if (!mkdirs) {
                throw new IllegalArgumentException("创建目标文件夹出错,请检查配置文件");
            }
        }
        File[] valueFiles = new File(outputPath).listFiles();
        if (null == valueFiles || valueFiles.length == 0) {
            return resultMap;
        }
        for (File valuesFile : valueFiles) {
            String fileName = valuesFile.getName();
            if (Pattern.compile("^values(-+[a-z][a-z]$)?").matcher(fileName).matches()) {
                File stringFile = new File(valuesFile.getAbsolutePath() +
                        File.separator +
                        "strings.xml");
                if (stringFile.exists()) {
                    if (resultMap.containsKey(fileName)) {
                        try {
                            resultMap.get(fileName).putAllArrayMap(parseXml2Map(stringFile));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            resultMap.put(fileName, parseXml2Map(stringFile));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    public static void writeXmlString(String outPath,
                                      String countryValuesName,
                                      ArrayMap<String, String> codeAndString) {
        File parentDir = new File(outPath, countryValuesName);
        if (!parentDir.exists()) {
            boolean makeSuccess = parentDir.mkdirs();
            if (!makeSuccess) {
                throw new IllegalArgumentException("创建输出目录失败,请检查当前系统是否允许创建该路径");
            }
        }
        File stringXml = new File(parentDir, "strings.xml");
        //1.创建一个Document对象
        Document doc = DocumentHelper.createDocument();
        //2.创建根对象
        Element root = doc.addElement("resources");
        for (Map.Entry<String, String> entry : codeAndString.entrySet()) {
            Element stringEle = root.addElement("string");
            stringEle.addAttribute("name", entry.getKey());
            stringEle.setText(entry.getValue());
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter xw = null;
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(stringXml);
            xw = new XMLWriter(stream, format);
            xw.setEscapeText(true);
            xw.write(doc);
            xw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (xw!=null) {
                try {
                    xw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (stream!=null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static ArrayMap<String, String> parseXml2Map(File xmlFile) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read(xmlFile);
        Element rootElement = document.getRootElement();
        ArrayMap<String, String> resultMap = new ArrayMap<>();
        List<Element> iterator = rootElement.elements("string");
        for (Element element:iterator){
            String lanCode = element.attributeValue("name");
            String lanValue = element.getTextTrim();
            String translatable = element.attributeValue("translatable");
            if (StringUtils.isEmpty(translatable) || translatable.equals("true")) {
                resultMap.put(lanCode, lanValue);
            }
        }
        return resultMap;
    }

}
