package org.openpaas.paasta.portal.common.api.domain.email;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openpaas.paasta.portal.common.api.config.EmailConfig;
import org.openpaas.paasta.portal.common.api.config.LanguageConfig;
import org.openpaas.paasta.portal.common.api.domain.common.CommonService;
import org.openpaas.paasta.portal.common.api.entity.portal.InviteUser;
import org.openpaas.paasta.portal.common.api.repository.portal.InviteUserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EmailService {


    private final Logger logger = getLogger(this.getClass());

    @Autowired
    EmailConfig emailConfig;

    @Autowired
    InviteUserRepository inviteUserRepository;

    @Autowired
    CommonService commonService;

    @Autowired
    LanguageConfig languageConfig;

    public Map resetEmail(String userId, String refreshToken, String useLang) {
        logger.info("resetEmail ::: " + userId);
        Map map = new HashMap();

        String templatePath = "template/ko/loginpass.html";

        List<String> languageList = languageConfig.getLanguageList();
        if(languageList.contains(useLang)) {
            templatePath = "template/" + useLang + "/loginpass.html";
        }

        ClassPathResource cpr = new ClassPathResource(templatePath);

        try {
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            String data = new String(bdata, emailConfig.getCharset());
            Document doc = Jsoup.parse(data);
            Elements elementAhref = doc.select("a[href]");
            Elements elementSpan = doc.select("span");
            if (elementAhref.size() != 0) {
                String link = emailConfig.getAuthUrl() + "/" + emailConfig.getExpiredUrl() + "?userId=" + userId + "&refreshToken=" + refreshToken;
                logger.debug("link : " + link);
                elementAhref.get(0).attr("href", link);
            }
            if (elementSpan.size() != 0) {
                elementSpan.get(0).childNode(0).attr("text", userId);
            }

            if (emailConfig.sendEmail(userId, doc.outerHtml())) {
                map.put("result", true);
                map.put("msg", "You have successfully completed the task.");
            } else {
                map.put("result", false);
                map.put("msg", "System error.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            map.put("result", false);
            map.put("msg", e.getMessage());
        } finally {
            IOUtils.closeQuietly();
        }
        return map;

    }
    public Map createEmail(String userId, String refreshToken, String useLang) {
        logger.info("createEmail ::: " + userId);
        Map map = new HashMap();

        String templatePath = "template/ko/loginemail.html";

        List<String> languageList = languageConfig.getLanguageList();
        if(languageList.contains(useLang)) {
            templatePath = "template/" + useLang + "/loginemail.html";
        }

        try {
            ClassPathResource cpr = new ClassPathResource(templatePath);
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            String data = new String(bdata, emailConfig.getCharset());
            Document doc = Jsoup.parse(data);

            Elements elementAhref = doc.select("a[href]");
            if (elementAhref.size() != 0) {
                String link = emailConfig.getAuthUrl() + "/" + emailConfig.getCreateUrl() + "?userId=" + userId + "&refreshToken=" + refreshToken;
                logger.info("link : " + link);
                elementAhref.get(0).attr("href", link);
            }
            logger.info(doc.outerHtml());
            if (emailConfig.sendEmail(userId, doc.outerHtml())) {
                map.put("result", true);
                map.put("msg", "You have successfully completed the task.");
            } else {
                map.put("result", false);
                map.put("msg", "System error.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Exception ::::: " + e.getMessage());
            map.put("result", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }
    public Boolean inviteOrgEmail(Map body, String useLang) {
        String[] userEmails;

        if(body.get("userEmail").toString().equals(""))
            return false;

        try {
            userEmails = body.get("userEmail").toString().split(",");

            for(String userEmail : userEmails) {
                userEmail = userEmail.trim();
                InviteUser inviteUser = new InviteUser();
                List<InviteUser> user = inviteUserRepository.findByUserIdAndOrgGuid(userEmail, body.get("orgId").toString());

                //TODO 하나 이상일 수 있나?
                if(user.size() > 0) {
                    inviteUser.setId(user.get(0).getId());
                }

                inviteUser.setUserId(userEmail);
                inviteUser.setGubun("send");
                inviteUser.setRole(body.get("userRole").toString());
                inviteUser.setOrgGuid(body.get("orgId").toString());
                inviteUser.setInvitename(body.get("invitename").toString());

                String randomId = RandomStringUtils.randomAlphanumeric(17).toUpperCase() + RandomStringUtils.randomAlphanumeric(2).toUpperCase();
                inviteUser.setToken(randomId);

                //TODO 성공한 사람만 email 날릴 것인지
                inviteUserRepository.save(inviteUser);

                inviteOrgEmailSend(userEmail, body.get("orgName").toString(), randomId, useLang);
            }
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public Map inviteAccept(Map body) {
        Map map = new HashMap();

        try {
            List<InviteUser> user = inviteUserRepository.findByTokenAndGubunNot(body.get("token").toString(), "success");
            if(user.size() > 0) {
                map.put("id", user.get(0).getId());
                map.put("role", user.get(0).getRole());
                map.put("orgGuid", user.get(0).getOrgGuid());
                map.put("userId", user.get(0).getUserId());
                map.put("result", true);
            } else {
                map.put("result", false);
            }
        } catch(Exception e) {
            e.printStackTrace();
            map.put("result", false);
        }

        return map;
    }
    public Map inviteAcceptUpdate(Map body) {
        try {
            //InviteUser inviteUser = new InviteUser();

            InviteUser user = inviteUserRepository.findById(Integer.parseInt(body.get("id").toString()));

            user.setGubun(body.get("gubun").toString());
            inviteUserRepository.save(user);

            body.put("result", true);
        } catch (Exception e) {
            e.printStackTrace();
            body.put("result", false);
        }

        return body;
    }
    public Map inviteOrgEmailSend(String userId, String orgName, String refreshToken, String useLang) {
        logger.info("createEmail ::: " + userId);
        Map map = new HashMap();

        String templatePath = "template/ko/invitation.html";

        List<String> languageList = languageConfig.getLanguageList();
        if(languageList.contains(useLang)) {
            templatePath = "template/" + useLang + "/invitation.html";
        }

        try {
            ClassPathResource cpr = new ClassPathResource(templatePath);
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            String data = new String(bdata, emailConfig.getCharset());
            Document doc = Jsoup.parse(data);

            final Elements elementAhref = doc.select("a[href]");
            if (elementAhref.size() != 0) {
                final String link = String.format( "%s/%s?userId=%s&orgName=%s&refreshToken=%s",
                    emailConfig.getAuthUrl(), emailConfig.getInviteUrl(), userId, orgName, refreshToken );
                logger.info("link : {}", link);
                elementAhref.get(0).attr("href", link);
            }

            final Elements elementSpanId = doc.select( "span[id=paasta_id]" );
            if (elementSpanId.size() >= 0) {
                logger.info("invite user id : {}", userId);
                elementSpanId.get( 0 ).text( userId );
            }

            final Elements elementSpanOrg = doc.select( "span[id=paasta_org]" );
            if (elementSpanOrg.size() >= 0) {
                logger.info( "invite {} into org : {}", userId, orgName );
                elementSpanOrg.get( 0 ).text( orgName );
            }

            logger.info(doc.outerHtml());
            if (emailConfig.sendEmail(userId, doc.outerHtml())) {
                map.put("result", true);
                map.put("msg", "You have successfully completed the task.");
            } else {
                map.put("result", false);
                map.put("msg", "System error.");
            }
        } catch (Exception e) {
            logger.info("Exception (Simple) ::::: {}", e.getMessage());
            logger.info("Exception (Stacktrace) ::::: ", e);
            map.put("result", false);
            map.put("msg", e.getMessage());
        }

        return map;
    }




}
