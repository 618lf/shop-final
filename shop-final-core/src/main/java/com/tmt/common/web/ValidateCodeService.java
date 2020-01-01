package com.tmt.common.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.security.context.AuthenticationToken;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.CookieUtils;
import com.tmt.common.utils.Ints;
import com.tmt.common.utils.StringUtils;

/**
 * 验证码服务
 * @author root
 */
public class ValidateCodeService {

	// 大小设置
	private int W = 70;
	private int H = 26;
	private static String VALIDATE_CODE = "_VC";
	
	/**
	 * 执行验证
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	public void doValidate(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String validateCode = request.getParameter(AuthenticationToken.captcha);
		if (StringUtils.isNotBlank(validateCode)){
			String validateCodeKey = ValidateCodeService.getValidateCodeKey(request);
			response.getOutputStream().print(ValidateCodeService.validateCode(validateCodeKey, validateCode)?"true":"false");
		} else {
			this.createImage(request, response);
		}
	}
	
	/**
	 * 创建验证码 -- 直接输出到 response
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void createImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		
		/*
		 * 得到参数高，宽，都为数字时，则使用设置高宽，否则使用默认值
		 */
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		if (StringUtils.isNumeric(width) && StringUtils.isNumeric(height)) {
			W = Ints.toInt(width);
			H = Ints.toInt(height);
		}
		
		BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		/*
		 * 生成背景
		 */
		createBackground(g);

		/*
		 * 生成字符
		 */
		String s = createCharacter(g);
		String validateCodeKey = IdGen.stringKey().toUpperCase();
		CookieUtils.setCookie(response, VALIDATE_CODE, validateCodeKey, -1);
		CacheUtils.getSessCache().put(validateCodeKey, s);

		g.dispose();
		OutputStream out = response.getOutputStream();
		ImageIO.write(image, "JPEG", out);
		out.close();
	}
	
	private Color getRandColor(int fc,int bc) { 
		int f = fc;
		int b = bc;
		Random random=new Random();
        if(f>255) {
        	f=255; 
        }
        if(b>255) {
        	b=255; 
        }
        return new Color(f+random.nextInt(b-f),f+random.nextInt(b-f),f+random.nextInt(b-f)); 
	}
	
	private void createBackground(Graphics g) {
		// 填充背景
		g.setColor(getRandColor(220,250)); 
		g.fillRect(0, 0, W, H);
		// 加入干扰线条
		for (int i = 0; i < 10; i++) {
			g.setColor(getRandColor(40,150));
			Random random = new Random();
			int x = random.nextInt(W);
			int y = random.nextInt(H);
			int x1 = random.nextInt(W);
			int y1 = random.nextInt(H);
			g.drawLine(x, y, x1, y1);
		}
	}

	private String createCharacter(Graphics g) {
		char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
				'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		String[] fontTypes = {"\u5b8b\u4f53","\u65b0\u5b8b\u4f53","\u9ed1\u4f53","\u6977\u4f53","\u96b6\u4e66"}; 
		Random random = new Random();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);//random.nextInt(10));
			g.setColor(new Color(50 + random.nextInt(100), 50 + random.nextInt(100), 50 + random.nextInt(100)));
			g.setFont(new Font(fontTypes[random.nextInt(fontTypes.length)],Font.BOLD,26)); 
			g.drawString(r, 15 * i + 5, 19 + random.nextInt(8));
          //g.drawString(r, i*w/4, h-5);
			s.append(r);
		}
		return s.toString();
	}
	
	// 通过KEY来管理验证码
	/**
	 * 获取验证码的KEY
	 * @param request
	 * @return
	 */
	public static String getValidateCodeKey(HttpServletRequest request) {
		return CookieUtils.getCookie(request, VALIDATE_CODE);
	}
	
	/**
	 * 通过key来验证
	 * @param inputCode
	 * @return
	 */
	public static Boolean validateCode(String validateCodeKey, String inputCode){
		String validateCode = null;
		if (StringUtils.isNotBlank(validateCodeKey)) {
			validateCode = CacheUtils.getSessCache().get(validateCodeKey);
		}
		if (inputCode != null && inputCode.toUpperCase().equals(validateCode)){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 清除验证码
	 */
	public static void clearValidateCode(String validateCodeKey){
		CacheUtils.getSessCache().delete(validateCodeKey);
	}
}
