package com.springboot.firstspringboot.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
/*@Data注解在类上，会为类的所有属性自动生成setter/getter、equals、canEqual、hashCode、toString方法，
如为final属性，则不会为该属性生成setter方法。*/
@Builder
/*@NoArgsConstructor  *//*自动生成无参数构造函数。*//*
@AllArgsConstructor  *//*自动生成全参数构造函数。*/
/*@NonNull: 可以帮助我们避免空指针。*/
/*@Cleanup: 自动帮我们调用close()方法 。
 * 我们都知道io操作完必须关闭，所以此注解帮我们完成了如下的操作。*/
public class User  implements Serializable {
    private Integer id;

    private String firstname;

    private String lastname;

    private String username;

    private String role;

    private String email;

    private String telephone;

    private String password;

    private String country;

    private String department;

    private String laboratory;

    private String laboratoryhead;

    private String researcharea;

    private String proteins;

    private String status;

    private Date createtime;

    public User() {
    }

    public User(Integer id, String firstname, String lastname, String username, String role, String email, String telephone, String password, String country, String department, String laboratory, String laboratoryhead, String researcharea, String proteins, String status, Date createtime) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.role = role;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.country = country;
        this.department = department;
        this.laboratory = laboratory;
        this.laboratoryhead = laboratoryhead;
        this.researcharea = researcharea;
        this.proteins = proteins;
        this.status = status;
        this.createtime = createtime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public void setLaboratory(String laboratory) {
        this.laboratory = laboratory;
    }

    public String getLaboratoryhead() {
        return laboratoryhead;
    }

    public void setLaboratoryhead(String laboratoryhead) {
        this.laboratoryhead = laboratoryhead;
    }

    public String getResearcharea() {
        return researcharea;
    }

    public void setResearcharea(String researcharea) {
        this.researcharea = researcharea;
    }

    public String getProteins() {
        return proteins;
    }

    public void setProteins(String proteins) {
        this.proteins = proteins;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}