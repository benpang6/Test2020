package com.robot2.secret.tool;

import com.robot2.secret.entity.BaseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class SendEmailTool {
    /**
     * 生成软证书，并发送邮箱。发送成功后删除原文件
     */
    @Autowired
    JavaMailSender jms;
    @Value("${spring.mail.username}")
    private String sendUserEmail;
    @Async
    public  void sendEmailAndFile(File file){
      /*  //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        mainMessage.setFrom(sendUserEmail);
        //接收者
        mainMessage.setTo("676586906@qq.com");
        //发送的标题
        mainMessage.setSubject("公安部第一研究所");
        //发送的内容
        String content="[您的授权码为]:";
        mainMessage.setText(content+secret);

        jms.send(mainMessage);*/
        MimeMessage message=jms.createMimeMessage();
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(sendUserEmail);
            helper.setTo("676586906@qq.com");
            helper.setSubject("公安部第一研究所");
            helper.setText("[请在附件中查看/下载授权软证书]");
            FileSystemResource sendFile=new FileSystemResource(file);
            String fileName="机器人授权软证书.cas";
            //添加多个附件可以使用多条
            //helper.addAttachment(fileName,file);
            helper.addAttachment(fileName,sendFile);
            jms.send(message);
            System.out.println("带附件的邮件发送成功");
            file.delete();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送带附件的邮件失败");
        }

    }

    /**
     * 生成口令
     * @param baseInfo 机器人基础信息
     * @param data 加密主体
     */
    @Async
    public  void sendEmail(String data, BaseInfo baseInfo){
       //建立邮件消息
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        String content1=null;
        String content2=null;
        //发送者
        mainMessage.setFrom(sendUserEmail);
        //接收者
        mainMessage.setTo(baseInfo.getEmail());
        //发送的标题
        mainMessage.setSubject("公安部第一研究所");
        //发送的内容
        if(baseInfo.getType().equals("3")){
            content1= baseInfo.getUsername()+",您好。"+"\n"+"您的授权码sn为:\t\t\t\t"+data+"\n";
        }else{
            content1= baseInfo.getName()+",您好。"+"\n"+"\t\t\t\t您的授权码sn为:\t\t\t\t"+data+"\n";
        }
        content2="\t\t\t\t您的唯一标识id为:\t\t\t\t"+ baseInfo.getDeviceId();
        mainMessage.setText(content1+content2);
        jms.send(mainMessage);
        System.out.println("秘钥内容已成功发送至邮箱");

    }
}
