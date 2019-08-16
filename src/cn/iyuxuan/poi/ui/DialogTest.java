package cn.iyuxuan.poi.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DialogTest {

    public static void main(String[] args) {
        Object[] selectionValues = new Object[]{"香蕉", "雪梨", "苹果"};
        Frame frame = JOptionPane.getRootFrame();
        frame.setAlwaysOnTop(true);
        Object inputContent = JOptionPane.showInputDialog(
                frame,
                "选择需要导入的Module: ",
                "标题",
                JOptionPane.PLAIN_MESSAGE,
                null,
                selectionValues,
                selectionValues[0]
        );
        frame.setAlwaysOnTop(false);
        System.out.println("输入的内容: " + inputContent);
//        System.exit(0);
    }
}
