package org.jboss.seam.mail.example;

import java.net.MalformedURLException;
import java.net.URL;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.core.enumurations.ContentDisposition;
import org.jboss.seam.mail.core.enumurations.MessagePriority;
import org.jboss.seam.mail.templating.VelocityMailMessage;

@Model
public class SendMail
{
   private String text = "This is the alternative text body for mail readers that don't support html";

   @Inject
   private Instance<MailMessage> mailMessage;
   
   @Inject
   private Instance<VelocityMailMessage> velocityMailMessage;
   
   @Inject
   private Person person;   

   public void sendText()
   {
      mailMessage.get()
            .from("Seam Framework", "seam@jboss.org")
            .to(person.getName(), person.getEmail())
            .subject("Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .textBody(text)
            .send();
   }

   public void sendHTML() throws MalformedURLException
   {
      velocityMailMessage.get()
            .from("Seam Framework", "seam@jboss.org")
            .to(person.getName(), person.getEmail())
            .subject("HTML Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .setTemplateHTML("template.html.vm")
            .put("version", "Seam 3")
            .importance(MessagePriority.HIGH)
            .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
            .send();
   }

   public void sendHTMLwithAlternative() throws MalformedURLException
   {
      velocityMailMessage.get()
            .from("Seam Framework", "seam@jboss.org")
            .to(person.getName(), person.getEmail())
            .subject("HTML+Text Message from Seam Mail - " + java.util.UUID.randomUUID().toString())
            .put("version", "Seam 3")
            .setTemplateHTMLTextAlt("template.html.vm", "template.text.vm")
            .importance(MessagePriority.LOW)
            .deliveryReciept("seam@jboss.org")
            .readReciept("seam@jboss.org")
            .addAttachment("template.html.vm", ContentDisposition.ATTACHMENT)
            .addAttachment(new URL("http://www.seamframework.org/themes/sfwkorg/img/seam_icon_large.png"), "seamLogo.png", ContentDisposition.INLINE)
            .send();
   }
}
