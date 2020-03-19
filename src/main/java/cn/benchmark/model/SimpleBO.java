package cn.benchmark.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by LinShunkang on 2020/03/19
 */
public class SimpleBO implements Serializable {

    private static final long serialVersionUID = -502297827478095439L;

    private long id;

    private String name;

    private String title;

    private String salary;

    private String description;

    private Date addTime;

    private Date updateTime;

    public SimpleBO() {
        //empty
    }

    public long getId() {
        return id;
    }

    public SimpleBO setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SimpleBO setName(String name) {
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SimpleBO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSalary() {
        return salary;
    }

    public SimpleBO setSalary(String salary) {
        this.salary = salary;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SimpleBO setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getAddTime() {
        return addTime;
    }

    public SimpleBO setAddTime(Date addTime) {
        this.addTime = addTime;
        return this;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public SimpleBO setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return "SimpleBO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", salary='" + salary + '\'' +
                ", description='" + description + '\'' +
                ", addTime=" + addTime +
                ", updateTime=" + updateTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleBO)) return false;
        SimpleBO simpleBO = (SimpleBO) o;
        return id == simpleBO.id &&
                Objects.equals(name, simpleBO.name) &&
                Objects.equals(title, simpleBO.title) &&
                Objects.equals(salary, simpleBO.salary) &&
                Objects.equals(description, simpleBO.description) &&
                Objects.equals(addTime, simpleBO.addTime) &&
                Objects.equals(updateTime, simpleBO.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, title, salary, description, addTime, updateTime);
    }

    public static SimpleBO getInstance(long id) {
        return new SimpleBO()
                .setId(id)
                .setName(String.valueOf(System.currentTimeMillis()))
                .setTitle("CEO")
                .setSalary("120K")
                .setDescription("首席执行官（Chief Executive Officer)职位名称，是在一个企业中负责日常事务的最高行政官员，主司企业行政事务，故又称作司政、行政总裁、总经理或最高执行长。\n" +
                        "在政治组织机构中，首席执行官为政府首脑，相当于部长会议主席、总理、首相、阁揆、行政院院长、政府主席等级别的行政事务最高负责高官。")
                .setAddTime(new Date())
                .setUpdateTime(new Date())
                ;
    }

}
