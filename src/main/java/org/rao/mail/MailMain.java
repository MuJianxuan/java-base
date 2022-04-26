package org.rao.mail;

import org.rao.file.FileMain;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.activation.DataHandler;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * desc: 邮箱
 *
 * @author Rao
 * @Date 2022/04/21
 **/
public class MailMain {
    public static void main(String[] args) {

        MailInfo mailInfo = MailInfo.builder().userName("1762725704@qq.com").host("smtp.qq.com").password(args[0]).encoding("UTF-8").build();

        String content = "<!DOCTYPE html>\n" +
                "<html xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"description\" content=\"email code\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "</head>\n" +
                "<!--邮箱验证码模板-->\n" +
                "<body>\n" +
                "<div style=\"background-color:#ECECEC; padding: 35px;\">\n" +
                "    <table cellpadding=\"0\" align=\"center\"\n" +
                "           style=\"width: 800px;height: 100%; margin: 0px auto; text-align: left; position: relative; border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 5px; border-bottom-left-radius: 5px; font-size: 14px; font-family:微软雅黑, 黑体; line-height: 1.5; box-shadow: rgb(153, 153, 153) 0px 0px 5px; border-collapse: collapse; background-position: initial initial; background-repeat: initial initial;background:#fff;\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "            <th valign=\"middle\"\n" +
                "                style=\"height: 25px; line-height: 25px; padding: 15px 35px; border-bottom-width: 1px; border-bottom-style: solid; border-bottom-color: RGB(148,0,211); background-color: RGB(148,0,211); border-top-left-radius: 5px; border-top-right-radius: 5px; border-bottom-right-radius: 0px; border-bottom-left-radius: 0px;\">\n" +
                "                <font face=\"微软雅黑\" size=\"5\" style=\"color: rgb(255, 255, 255); \">这里输入name</font>\n" +
                "            </th>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"word-break:break-all\">\n" +
                "                <div style=\"padding:25px 35px 40px; background-color:#fff;opacity:0.8;\">\n" +
                "\n" +
                "                    <h2 style=\"margin: 5px 0px; \">\n" +
                "                        <font color=\"#333333\" style=\"line-height: 20px; \">\n" +
                "                            <font style=\"line-height: 22px; \" size=\"4\">\n" +
                "                                尊敬的用户：</font>\n" +
                "                        </font>\n" +
                "                    </h2>\n" +
                "                    <!-- 中文 -->\n" +
                "                    <p>您好！感谢您使用****，您的账号正在进行邮箱验证，验证码为：<font color=\"#ff8c00\">{0}</font>，有效期30分钟，请尽快填写验证码完成验证！</p><br>\n" +
                "                    <!-- 英文 -->\n" +
                "                    <h2 style=\"margin: 5px 0px; \">\n" +
                "                        <font color=\"#333333\" style=\"line-height: 20px; \">\n" +
                "                            <font style=\"line-height: 22px; \" size=\"4\">\n" +
                "                                Dear user:</font>\n" +
                "                        </font>\n" +
                "                    </h2>\n" +
                "                    <p>Hello! Thanks for using *****, your account is being authenticated by email, the\n" +
                "                        verification code is:<font color=\"#ff8c00\">{0}</font>, valid for 30 minutes. Please fill in the verification code as soon as\n" +
                "                        possible!</p>\n" +
                "                    <div style=\"width:100%;margin:0 auto;\">\n" +
                "                        <div style=\"padding:10px 10px 0;border-top:1px solid #ccc;color:#747474;margin-bottom:20px;line-height:1.3em;font-size:12px;\">\n" +
                "                            <p>****团队</p>\n" +
                "                            <p>联系我们：********</p>\n" +
                "                            <br>\n" +
                "                            <p>此为系统邮件，请勿回复<br>\n" +
                "                                Please do not reply to this system email\n" +
                "                            </p>\n" +
                "                            <!--<p>©***</p>-->\n" +
                "                        </div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        // 动态模板
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost( mailInfo.getHost());
        sender.setDefaultEncoding( mailInfo.getEncoding());
        sender.setUsername( mailInfo.getUserName());
        sender.setPassword( mailInfo.getPassword());
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        sender.setJavaMailProperties(properties);

        // 普通内容
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo("1762725704@qq.com");
//        mailMessage.setFrom( mailInfo.getUserName() );
//        mailMessage.setSubject("这是测试邮件2");
//        mailMessage.setText( " 测试主体");


        MimeMessage mimeMessage = sender.createMimeMessage();
        try {

            // html 内容
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
            mimeMessageHelper.setSubject("Rao测试");
            mimeMessageHelper.setText( content,true);
            mimeMessageHelper.setTo("1762725704@qq.com");
            mimeMessageHelper.setFrom( mailInfo.getUserName() );

            // 本地附件
            File file = new File( FileMain.class.getClassLoader().getResource("ddd.txt").getPath() );
            mimeMessageHelper.addAttachment(MimeUtility.encodeWord( "附件.txt", StandardCharsets.UTF_8.name(),"B" ), file );

            // 网络附件
            String path2 = "http://www.pptbz.com/pptpic/UploadFiles_6909/201203/2012031220134655.jpg";
            URL url = new URL(path2);
            InputStream inputStream = url.openStream();

            // content-type
            ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(inputStream, "image/jpeg;charset=UTF-8");
            mimeMessageHelper.addAttachment( MimeUtility.encodeWord( "图片.jpg", StandardCharsets.UTF_8.name(),"B" ), byteArrayDataSource);


        } catch (Exception ex){
            ex.printStackTrace();
        }

        sender.send( mimeMessage );

    }
}
