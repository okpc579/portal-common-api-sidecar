package org.openpaas.paasta.portal.common.api.entity.portal;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by indra on 2018-02-23.
 */
@Entity
@Table(name = "code_detail")
public class CodeDetail {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private int no;

    @Column(name = "[key]",nullable = false)
    private String key;

    @Column(name = "[value]",nullable = false)
    private String value;

    @Column(name = "summary")
    private String summary;

    @Column(name = "group_no",nullable=false)
    private int groupNo;

    @Column(name = "use_yn")
    private String useYn;

    @Column(name = "[order]")
    private int order;

    @Column(name = "user_id",nullable = false)
    private String userId;

    @CreationTimestamp
    @Column(name = "created", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @UpdateTimestamp
    @Column(name = "lastmodified", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastmodified;

    @Column(name = "language")
    private String language;

    @Transient
    private int pageNo;

    @Transient
    private int pageSize;

    @Transient
    private String procType;

    @Transient
    private String orgKey;

    @Transient
    private String orgId;

    @Transient
    private String groupId;

    @Transient
    private List<String> languageList;

    @Transient
    public int getValue2() {
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            return 0;
        }
    }


    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getProcType() {
        return procType;
    }

    public void setProcType(String procType) {
        this.procType = procType;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    public Date getCreated() {
        return created == null ? null : new Date(created.getTime());
    }

    public void setCreated(Date created) {
        this.created = created == null ? null : new Date(created.getTime());
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    public Date getLastmodified() {
        return lastmodified == null ? null : new Date(lastmodified.getTime());
    }

    public void setLastmodified(Date lastmodified) {
        this.lastmodified = lastmodified == null ? null : new Date(lastmodified.getTime());
    }

    public String getOrgKey() {
        return orgKey;
    }

    public void setOrgKey(String orgKey) {
        this.orgKey = orgKey;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getGroupId() { return groupId; }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getLanguage() { return language; }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getLanguageList() { return languageList; }

    public void setLanguageList(List<String> languageList) {
        this.languageList = languageList;
    }

    @Override
    public String toString() {
        return "CodeDetail{" +
                "no=" + no +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", summary='" + summary + '\'' +
                ", groupNo='" + groupNo +
                ", useYn='" + useYn + '\'' +
                ", order=" + order +
                ", userId='" + userId + '\'' +
                ", created=" + created +
                ", lastmodified=" + lastmodified +
		        ", language='" + language + '\'' +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", procType='" + procType + '\'' +
                ", orgKey='" + orgKey + '\'' +
                ", orgId='" + orgId + '\'' +
                ", groupId='" + groupId + '\'' +
                ", value2='" + getValue2() + '\'' +
                ", languageList='" + languageList + '\'' +
                '}';
    }
}
