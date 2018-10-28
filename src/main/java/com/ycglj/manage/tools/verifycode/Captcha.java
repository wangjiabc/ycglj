package com.ycglj.manage.tools.verifycode;

import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;

/**
 * <p>
 * 楠岃瘉鐮佹娊璞＄被,鏆傛椂涓嶆敮鎸佷腑鏂�
 * </p>
 * 
 * @author: wuhongjun
 * @version:1.0
 */
public abstract class Captcha extends Randoms {
	protected Font font = new Font("Verdana", Font.ITALIC | Font.BOLD, 28); // 瀛椾綋
	protected int len = 5; // 楠岃瘉鐮侀殢鏈哄瓧绗﹂暱搴�
	protected int width = 150; // 楠岃瘉鐮佹樉绀鸿法搴�
	protected int height = 40; // 楠岃瘉鐮佹樉绀洪珮搴�
	private String chars = null; // 闅忔満瀛楃涓�

	/**
	 * 鐢熸垚闅忔満瀛楃鏁扮粍
	 * 
	 * @return 瀛楃鏁扮粍
	 */
	protected char[] alphas() {
		char[] cs = new char[len];
		for (int i = 0; i < len; i++) {
			cs[i] = alpha();
		}
		chars = new String(cs);
		return cs;
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 缁欏畾鑼冨洿鑾峰緱闅忔満棰滆壊
	 * 
	 * @return Color 闅忔満棰滆壊
	 */
	protected Color color(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + num(bc - fc);
		int g = fc + num(bc - fc);
		int b = fc + num(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 楠岃瘉鐮佽緭鍑�,鎶借薄鏂规硶锛岀敱瀛愮被瀹炵幇
	 * 
	 * @param os
	 *            杈撳嚭娴�
	 */
	public abstract void out(OutputStream os);

	/**
	 * 鑾峰彇闅忔満瀛楃涓�
	 * 
	 * @return string
	 */
	public String text() {
		return chars;
	}
}