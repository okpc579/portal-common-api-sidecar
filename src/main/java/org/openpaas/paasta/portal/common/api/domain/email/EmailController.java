package org.openpaas.paasta.portal.common.api.domain.email;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by indra on 2018-02-22.
 */
@RestController

public class EmailController {

    private static final Logger logger = getLogger(EmailController.class);

    @Autowired
    private EmailService emailService;


    /**
     * 사용자 패스워드를 만료하여, 초기화 하도록 메일을 발송한다.
     *
     * @return the menu list
     */
    @PostMapping(value = {"/v2/email/reset"})
    public Map<String, Object> expiredEmail(@RequestBody Map body, @RequestHeader(value = "useLang") String lang) {
        String refreshToken = "";
        if (body.get("refreshToken") != null) {
            refreshToken = body.get("refreshToken").toString();
        }
        Map<String, Object> resultMap = emailService.resetEmail(body.get("userid").toString(), refreshToken, lang);
        return resultMap;
    }




    /**
     * 사용자 계정을 생성하기 위하여, 이메일 발송
     *
     * @return the menu list
     */
    @PostMapping(value = {"/v2/email/create"})
    public Map<String, Object> createEmail(@RequestBody Map body, @RequestHeader(value = "useLang") String lang) {
        String refreshToken = "";
        if (body.get("refreshToken") != null) {
            refreshToken = body.get("refreshToken").toString();
        }
        Map<String, Object> resultMap = emailService.createEmail(body.get("userid").toString(), refreshToken, lang);
        return resultMap;
    }

    @PostMapping( "/v2" + "/email/inviteOrg" )
    public boolean inviteOrgEmail(@RequestBody Map<String, Object> body, @RequestHeader("useLang") String lang) {
        return emailService.inviteOrgEmail(body, lang);
    }

    @PostMapping( "/v2" + "/email/inviteAccept" )
    public Map inviteAccept(@RequestBody Map<String, Object> body) {
        return emailService.inviteAccept(body);
    }

    @PostMapping( "/v2" + "/email/inviteAcceptUpdate" )
    public Map inviteAcceptUpdate(@RequestBody Map<String, Object> body) {
        return emailService.inviteAcceptUpdate(body);
    }
}
