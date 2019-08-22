package com.springboot.firstspringboot.common;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

//监测进程是否结束
//返回fasle表示进程结束
public class RemoteShellExecutor {

    private Logger logger = Logger.getLogger(getClass());


    private Connection conn;
    /**
     * 远程机器IP
     */
    private String ip;
    /**
     * 用户名
     */
    private String osUsername;
    /**
     * 密码
     */
    private String password;
    private String charset = Charset.defaultCharset().toString();


    private InputStream stdOut = null;
    private InputStream stdErr = null;
    private PrintWriter out = null;
    private String outStr = "";
    private String outErr = "";
    private int ret = -1;

    private static final int TIME_OUT = 1000 * 5 * 60;

    private static final String SHELL_CD = "cd /home/btweb/Starin/shell";
    private static final String SHELL_START1 = "./awk.sh";   //文件列对齐生成infile
    private static final String SHELL_START2 = "./n.sh";   //生成outtree和outfile
    private static final String SHELL_START3 = "./sed.sh";   //去掉outtree里边的_
    private static final String SHELL_RENAME = "mv outtree name.nwk";
    private static final String SHELL_RSCRIPT1 = "xvfb-run ete3  view -t name.nwk -i Rplots.pdf";
    private static final String SHELL_RSCRIPT2 = "xvfb-run ete3  view -t name.nwk -i Rplots.svg";
    private static final String SHELL_EXIT = "exit";
     /*private static final String SHELL_EXIT2 = "sync";
    private static final String SHELL_EXIT3 = "echo 3 > /proc/sys/vm/drop_caches";*/


    /**
     * 27       * 构造函数
     * 28       * @param ip
     * 29       * @param usr
     * 30       * @param pasword
     * 31
     */
    public RemoteShellExecutor(String ip, String usr, String pasword) {
        this.ip = ip;
        this.osUsername = usr;
        this.password = pasword;
    }


    /**
     * 40      * 登录
     * 41      * @return
     * 42      * @throws IOException
     * 43
     */
    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(osUsername, password);
    }

    /**
     * 51      * 执行脚本
     * 52      *
     * 53      * @param cmds
     * 54      * @return
     * 55      * @throws Exception
     * 56
     */


    public int exectd(int id, String date,String uuid) throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
                // 输入待执行命令
                out.println(RemoteShellExecutor.SHELL_CD);
                //可以改为变量
                out.println(RemoteShellExecutor.SHELL_START1);   //  "./awk.sh"
                out.println(RemoteShellExecutor.SHELL_START2);   //  "./n.sh"
                out.println(RemoteShellExecutor.SHELL_START3);   //  "./sed.sh"
                out.println(RemoteShellExecutor.SHELL_RENAME);  // mv outtree name.nwk
                out.println(RemoteShellExecutor.SHELL_RSCRIPT1);  //xvfb-run ete3  view -t name.nwk -i Rplots.pdf
                out.println(RemoteShellExecutor.SHELL_RSCRIPT2);  //xvfb-run ete3  view -t name.nwk -i Rplots.svg
                out.println("mv name.nwk /home/btweb/Starin/file/"+date+"/"+id );  //
                out.println("mv outfile /home/btweb/Starin/file/"+date+"/"+id);  //
                out.println("mv infile /home/btweb/Starin/file/"+date+"/"+id);  //
                out.println("mv Rplots.pdf /home/btweb/Starin/file/"+date+"/"+id );
                out.println("mv Rplots.svg /home/btweb/Starin/file/"+date+"/"+id );
                out.println("rm -rf cvtree.nwk " );
                out.println("rm -rf infiletest" );
                out.println("cd /home/btweb/Starin/file/"+date+"/"+id);
                out.println("rm -rf LIST.TXT" );     //tree LIST.TXT
                out.println("rm -rf LIST2.TXT" );  //第一步cv LIST.TXT2
                out.println("cd result");   //进入result文件夹
                out.println("mkdir "+uuid);   //创建uuid文件夹
                out.println("mv DataBase*.xlsx  /home/btweb/Starin/file/"+date+"/"+id+"/result/"+uuid);   //创建uuid文件夹
                out.println("cd ..");  //返回
                out.println("mv name.nwk  outtree");   //改名字
                out.println("mv Rplots.pdf  ./result/"+uuid);   //移动到result/uuid文件夹下
                out.println("mv Rplots.svg  ./result/"+uuid);   //移动到result/uuid文件夹下
                out.println("mv infile  ./result/"+uuid);   //移动到result/uuid文件夹下
                out.println("mv outtree  ./result/"+uuid);    //移动到result/uuid文件夹下
                out.println("mv outfile  ./result/"+uuid);    //移动到result/uuid文件夹下
                out.println("cd result");   //进入result文件夹
                out.println("zip -r "+uuid+".zip  "+uuid);//把file移动到result文件夹
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }


    // 把503菌株放入用户文件夹下
    public int exectd2(int id, String date) throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
                out.println("cd /home/btweb/Starin/file/data11"); //把file移动到result文件夹
                out.println("cp *  "+"/home/btweb/Starin/file/"+date+"/"+id); //把file移动到result文件夹
                out.println("cd /home/btweb/Starin/file/"+date+"/"+id); //把file移动到result文件夹
                out.println("rm -rf LIST.TXT1"); //把file移动到result文件夹
                out.println("cd /home/btweb/Starin/file"); //把file移动到result文件夹
                out.println("cp DataBaseA.xlsx /home/btweb/Starin/file/"+date+"/"+id+"/result"); //把file移动到result文件夹
                logger.info("执行data移动脚本======================================");
                /*out.println(RemoteShellExecutor.SHELL_EXIT2);
                out.println(RemoteShellExecutor.SHELL_EXIT3);
                */
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    //筛选
    public int exectd3(int id, String date) throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
                out.println("cd /home/btweb/Starin/file/data22"); //把file移动到result文件夹
                out.println("cp *  "+"/home/btweb/Starin/file/"+date+"/"+id); //把file移动到result文件夹
                out.println("cd /home/btweb/Starin/file/"+date+"/"+id); //把file移动到result文件夹
                out.println("rm -rf LIST.TXT1"); //把file移动到result文件夹
                out.println("cd /home/btweb/Starin/file");
                out.println("cp DataBaseS.xlsx /home/btweb/Starin/file/"+date+"/"+id+"/result");
                logger.info("执行筛选data2移动脚本======================================");
                /*out.println(RemoteShellExecutor.SHELL_EXIT2);
                out.println(RemoteShellExecutor.SHELL_EXIT3);
                */
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    //发送完邮件删除result.txt
    public int exectd4() throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
                out.println("cd /home/btweb/Starin/file"); //把file移动到result文件夹
                out.println("rm -rf Mail.txt"); //把file移动到result文件夹
                /*out.println(RemoteShellExecutor.SHELL_EXIT2);
                out.println(RemoteShellExecutor.SHELL_EXIT3);
                */
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    //cv 管理员上传的菌株(Servor不带信息)
    public int exectd5() throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
                out.println("cv -I /home/btweb/Starin/file/data33 -i   /home/btweb/Starin/file/data33/LIST.TXT -k 6 -O /home/btweb/Starin/file/data33");
                out.println("cd /home/btweb/Starin/file/data33");
                out.println("rm -rf LIST.TXT");
                out.println("cp *  /home/btweb/Starin/file/data11");
                out.println("rm -rf *");
                /*out.println(RemoteShellExecutor.SHELL_EXIT2);
                out.println(RemoteShellExecutor.SHELL_EXIT3);
                */
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    //cv 管理员上传的菌株(Servor带信息)
    public int exectd6() throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
               /* out.println("cd /usr/local/file/data11");
                out.println("cp " +filename+ " /usr/local/data22");*/
                out.println("cv -I /home/btweb/Starin/file/data33 -i   /home/btweb/Starin/file/data33/LIST.TXT -k 6 -O /home/btweb/Starin/file/data33");
                out.println("cd /home/btweb/Starin/file/data33");
                out.println("rm -rf LIST.TXT");
                out.println("cp *  /home/btweb/Starin/file/data11");
                out.println("cp *  /home/btweb/Starin/file/data22");
                out.println("rm -rf *");
               /* out.println(RemoteShellExecutor.SHELL_EXIT2);
                out.println(RemoteShellExecutor.SHELL_EXIT3);
               */
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    // cv
    public int exectd7(int id) throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
               /* out.println("cd /usr/local/file/data11");
                out.println("cp " +filename+ " /usr/local/data22");*/
                out.println("cd /home/btweb/Starin/shell");
                out.println("./cvFirst.sh " +id);
               /* out.println(RemoteShellExecutor.SHELL_EXIT2);
                out.println(RemoteShellExecutor.SHELL_EXIT3);
               */
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }
    // tree
    public int exectd8(int id) throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
               /* out.println("cd /usr/local/file/data11");
                out.println("cp " +filename+ " /usr/local/data22");*/
                out.println(" cd /home/btweb/Starin/shell");
                out.println("./tree.sh "+id);
               /* out.println(RemoteShellExecutor.SHELL_EXIT2);
                out.println(RemoteShellExecutor.SHELL_EXIT3);
               */
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }


    //递归文件夹(182)
    public int exectd9(List list,int id,String date) throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
                for (Object cvtName : list) {
                    out.println(" cd /home/btweb/Starin/file/data22");
                    logger.info("递归文件夹182"+cvtName);
                    out.println("cp "+ cvtName+"*"  +" /home/btweb/Starin/file/"+date+"/"+id);  //根据cvtName去data22cp。
                }
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }


    //递归文件夹(503)
    public int exectd10(List list,int id,String date) throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.requestPTY("bash");
                session.startShell();
                //输入命令
                out = new PrintWriter(session.getStdin());
                for (Object cvtName : list) {
                    out.println(" cd /home/btweb/Starin/file/data11");
                    logger.info("递归文件夹503"+cvtName);
                    out.println("cp "+ cvtName+"*"  +" /home/btweb/Starin/file/"+date+"/"+id);  //根据cvtName去data22cp。
                }
                out.println(RemoteShellExecutor.SHELL_EXIT);
                out.close();
                // // 增加timeOut时间
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS, 30000);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    /**
     * 执行脚本
     *
     * @param cmds
     * @return
     * @throws Exception
     */
    public int exec(String cmds) throws Exception {
        try {
            if (login()) {
                Session session = conn.openSession();
                session.execCommand(cmds);
                result(session);
                ret = session.getExitStatus();
            } else {
                throw new Exception("登录远程机器失败" + ip); // 自定义异常类 实现略
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    /**
     * 打印成功或者失败的信息
     *
     * @param session
     * @throws Exception
     */
    private void result(Session session) throws Exception {
        stdOut = new StreamGobbler(session.getStdout());
        outStr = processStream(stdOut, charset);
        stdErr = new StreamGobbler(session.getStderr());
        outErr = processStream(stdErr, charset);
        session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
        logger.info(outStr);
        logger.error(outErr);
    }

    /**
     * 96      * @param in
     * 97      * @param charset
     * 98      * @return
     * 99      * @throws IOException
     * 100      * @throws UnsupportedEncodingException
     * 101
     */
    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }
}

