package jack.smartbi.mail;

import org.apache.commons.mail.HtmlEmail;


public class MailSender {

    /**
     * 以文本格式发送邮件
     * @param emailaddress      收件人地址
     * @param username          用户名
     * @param url               用户URL
     * @return                  是否发送成功
     */
    public static boolean sendEmail(String emailaddress, String username, String url) {
        try {
            HtmlEmail  email = new HtmlEmail();
            // 126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com
            email.setHostName("smtp.qq.com");
            email.setCharset("UTF-8");

            // 收件地址
            email.addTo(emailaddress);

            // 此处填邮箱地址和用户名,用户名可以任意填写
            email.setFrom("2463246583@qq.com", username);

            // 此处填写邮箱地址和客户端授权码
            email.setAuthentication("2463246583@qq.com", "kkggwygbpwlmecag");
            // 此处填写邮件名，邮件名可任意填写
            email.setSubject("用户邮箱验证");
            // 邮件内容
            email.setMsg("尊敬的" + username + "，您好，请点击如下链接激活用户:<a href=\"" + url + "\" >" + url + "</a>");

            email.send();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}