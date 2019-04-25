package com.weima.ftp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

public class ftpToHdfs {
	
	public static void main(String[] args) throws IOException, SftpException {
		Boolean upFlag = false;
		String date = args[0];
		try {
			Integer.parseInt(args[0]);
		}catch(Exception e){date="";}
		String ftp = args[1];
		System.out.println("第一个参数："+date+"\t"+"第二个参数："+ftp);
		LinkedList<String>linklist = new LinkedList<String>();
		Properties properties = new Properties();
    	BufferedReader in = new BufferedReader(new FileReader("/opt/javajar/properties/sftp.properties"));
//    	BufferedReader in = new BufferedReader(new FileReader("C:\\software\\eclipse\\EclipseProjects\\hive\\src\\main\\java\\com\\weima\\ftp\\sftp.properties"));
		properties.load(in);
		//获取配置文件信息
    	String host = properties.getProperty(ftp+"host");
    	String username = properties.getProperty(ftp+"username");
    	int port = Integer.parseInt(properties.getProperty(ftp+"port"));
    	String password = properties.getProperty(ftp+"password");
    	String ftpdirectory = properties.getProperty(ftp+"ftpdirectory");
    	String ftpfile = properties.getProperty(ftp+"ftpfile");
    	String hdfspath = properties.getProperty(ftp+"hdfspath");
    	String localfile = properties.getProperty(ftp+"localfile");
    	String menu = properties.getProperty(ftp+"menu");
    	String refront = properties.getProperty(ftp+"refront");
    	String relater = properties.getProperty(ftp+"relater");
    	System.out.println("host:"+host+"\t"+"username:"+username);
    	System.out.println("port:"+port+"\t"+"password"+password);
    	System.out.println("ftpdirectory:"+ftpdirectory+"\t"+"ftpfile:"+ftpfile);
    	System.out.println("hdfspath:"+hdfspath+"\t"+"localfile"+localfile);
    	System.out.println("menu:"+menu);
    	System.out.println("refront:"+refront+"\t"+"relater:"+relater);

    	//获取sftpUtils
		sftpUtils sf = sftpUtils .getInstance(host,port,username,password);
    	System.out.println(sf);
    	String re = refront+date+relater;
		System.out.println("正则表达式为："+re);
		
    	//获取ftpdirectory目录下的文件名称，存放到linklist中
    	Vector<LsEntry> list = sf.listFiles(ftpdirectory);
    	for(LsEntry file:list) {
    		System.out.println("该目录下的文件："+file);
    		Matcher m = Pattern.compile(re).matcher(file.toString());
    		while(m.find()) {
    			String filename = m.group();
    			upFlag = true;
    			System.out.println("匹配到的文件："+filename);
    			linklist.add(filename);
    		}
    	}
    	
    	//获取hdfs文件
    	Configuration conf = new Configuration();
    	conf.set("fs.defaultFS","hdfs://10.40.150.11:8020/");
    	FileSystem fs = FileSystem.get(URI.create(hdfspath), conf);
    	
    	//在hdfs上创建目录，上传ftp上的文件到新建目录下
    	String menuL = menu+args[0];
    	if(upFlag) {
    		if(!fs.exists(new Path(menuL))) {
    			System.out.println("文件目录不存在，开始创建");
        		if(fs.mkdirs(new Path(menuL))) {
        			System.out.println("文件目录创建成功，开始上传文件");
        			sf.ftpToHdfs_1(ftpdirectory,linklist,conf, fs,menuL);
            		System.out.println("当前目录下日期匹配的文件上传成功"); 
        		}
        		else
        			System.out.println("上传文件失败");
        	}
        	else {
        		System.out.println("文件目录已存在，直接上传文件");
        		sf.ftpToHdfs_1(ftpdirectory,linklist,conf, fs,menuL);
        		System.out.println("当前目录下日期匹配的文件上传成功");
        	}
    	}
    	else { 
    		System.out.println("没有匹配的文件,请检查正则表达式是否正确，或者输入的参数是否真确");
    		System.exit(1);
    	}
    	//断开ftp连接
        sf.disconnect();
        System.exit(0);
	}
}
