package com.weima.ftp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class sftpUtils {
	private static InputStream in;
	private static FSDataOutputStream out;
    private static ChannelSftp sftp;
    private static sftpUtils instance = null;
    private sftpUtils() {
    }
    /**
     * ||获取sftpUtils对象（单例类）
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     */
    public static sftpUtils getInstance(String host, int port, String username, String password) {
        if (instance == null) {
                instance = new sftpUtils();
                sftp = instance.connect(host, port, username, password);   //获取连接
        }
        return instance;
    }
    /**
     * ||连接ftp服务器
     * @param host	ftp所在的ip地址
     * @param port	ftp中的端口
     * @param username	登陆ftp的用户名称
     * @param password	登陆ftp的密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username, String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
           e.printStackTrace();
        }
        return sftp;
    }
    /**
     * 
     * @param directory	ftp服务器中的一个目录
     * @param linklist	存放directory目录下的文件名
     * @param saveFile	存放到本地的文件名
     */
    public void download_1(String directory, LinkedList<String>linklist, String saveFile) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            for(String filename:linklist) {
            	sftp.get(filename, fileOutputStream);
            }
            fileOutputStream.close();
//            return file;
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    /**
     * ||将ftp文件上传到hdfs
     * @param directory ftp服务器中的一个目录
     * @param linklist	存放directory目录下的文件名
     * @param conf		Hadoop的configuration
     * @param fs		hdfs文件系统对象
     * @param menu		hdfs上存放ftp directory目录下的文件的目录
     */
    public void ftpToHdfs_1(String directory, LinkedList<String>linklist,Configuration conf,FileSystem fs,String menu) {
    	try {
    		 sftp.cd(directory);
    		 for(String ftpfile:linklist) {
    			 in = sftp.get(ftpfile);
    			 out = fs.create(new Path(menu+"/"+ftpfile)); 
    			 IOUtils.copyBytes(in, out, conf);
    			 System.out.println("上传文件成功！"); 
    		 }
    		 if (in != null) {
                 in.close();
             }
    		 if (out != null) {
                 out.close();
             }
    	}catch (Exception e) {
			e.printStackTrace();
		}
    }
    /**
     * ||获取ftp服务器下directory目录下的文件名称
     * @param directory	ftp服务器中的一个目录
     * @return
     * @throws SftpException
     */
    public  Vector<LsEntry> listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }
    
    /**
     * ||断开ftp连接
     */
    public void disconnect() {
        try {
            sftp.getSession().disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        sftp.quit();
        sftp.disconnect();
    }
}
