package com.example.netty.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//import com.zcf.utils.ck.MyException;

/**
 * @author chenkang
 *
 */
@Slf4j
public class upload {

	/**
	 *
	 * 返回 key imgpath imgorginname
	 *
	 * @param file
	 * @return
	 */
	public static Map<String, Object> upload(MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		String pathval = PropertiesUtil.getProperty("FILE_ADDRESS");
		// 根据配置文件获取服务器图片存放路径
		String newFileName = String.valueOf(System.currentTimeMillis());
		String saveFilePath = "/img/";
		/* 构建文件目录 */
		File fileDir = new File(pathval + saveFilePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		// 上传的文件名
		String filename = file.getOriginalFilename();
		// 文件的扩张名
		String extensionName = filename.substring(filename.lastIndexOf(".") + 1);
		String imgPath = saveFilePath + newFileName + "." + extensionName;
		try {
			FileOutputStream out = new FileOutputStream(pathval + imgPath);
			// 写入文件
			out.write(file.getBytes());
			out.flush();
			out.close();
			log.info("图片存储位置：{}", imgPath);// 打印图片位置
		} catch (Exception e) {
			log.info("上传文件异常");
		}
		map.put("imgpath", imgPath);
		map.put("imgorginname", filename.substring(0, filename.lastIndexOf(".")));
		return map;
	}

	/**
	 * 文件下载
	 *
	 * @throws IOException
	 */
	public static void download(String filename, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 获取输入流
		String filepath = PropertiesUtil.getProperty("Moban_address") + File.separator + filename;
		File file = new File(filepath);
		if (file.exists()) {
			@SuppressWarnings("resource")
			InputStream bis = new BufferedInputStream(new FileInputStream(file));
			// 转码，免得文件名中文乱码
			filename = URLEncoder.encode(filename, "UTF-8");
			// 设置文件下载头
			response.addHeader("Content-Disposition", "attachment;filename=" + filename);
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			int len = 0;
			while ((len = bis.read()) != -1) {
				out.write(len);
				out.flush();
			}
			out.close();
		}
		log.info("要下载的文件不存在");
	}

	/**
	 * 文件下载
	 *
	 * @throws IOException
	 */
	public static void download(File file, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 获取输入流
		if (file.isFile() && file.exists()) {
			@SuppressWarnings("resource")
			InputStream bis = new BufferedInputStream(new FileInputStream(file));
			// 转码，免得文件名中文乱码
			String filename = URLEncoder.encode(file.getName(), "UTF-8");
			// 设置文件下载头
			response.addHeader("Content-Disposition", "attachment;filename=" + filename);
			// 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
			response.setContentType("multipart/form-data");
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			int len = 0;
			while ((len = bis.read()) != -1) {
				out.write(len);
				out.flush();
			}
			out.close();
		} else {
			//throw new MyException("下载的文件不存在");
		}

	}

}