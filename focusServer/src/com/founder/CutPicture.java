package com.founder;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CutPicture {
  public  void cut(int left,int top,int w, int h) {
	  
	    try {
			//Desktop.getDesktop().browse(new URL("http://www.boc.cn/").toURI());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	    Robot robot = null;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //robot.delay(10000);
	    Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
	    int width = (int) d.getWidth();
	    int height = (int) d.getHeight();
	    // 最大化浏览器
	    robot.keyRelease(KeyEvent.VK_F11);
	    //robot.delay(100);
	    Image image = robot.createScreenCapture(new Rectangle(left, top, width,height));
	    BufferedImage bi = new BufferedImage(w, h,
	    BufferedImage.TYPE_INT_RGB);
	    Graphics g = bi.createGraphics();
	    g.drawImage(image, 0, 0, width, height, null);
	    // 保存图片
	    try {
	    	
			ImageIO.write(bi, "jpg", new File("e:/bbb.jpg"));
			System.out.println("生成文件成功...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
  }
   
  public static void main(String[] args) {
	  CutPicture cutPicture = new CutPicture();
	
  }
  
}