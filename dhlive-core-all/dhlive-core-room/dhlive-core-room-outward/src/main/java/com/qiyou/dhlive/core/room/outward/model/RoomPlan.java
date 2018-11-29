package com.qiyou.dhlive.core.room.outward.model;

import java.io.Serializable;
import javax.persistence.*;

@Table(name = "room_plan")
public class RoomPlan implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "plan_number")
    private Integer planNumber;

    @Column(name = "plan_time")
    private String planTime;

    @Column(name = "plan_teacher")
    private String planTeacher;

    @Column(name = "plan_Introduce")
    private String planIntroduce;


    private static final long serialVersionUID = 1L;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }



    public Integer getPlanNumber() {
		return planNumber;
	}

	public void setPlanNumber(Integer planNumber) {
		this.planNumber = planNumber;
	}

	/**
     * @return planTime
     */
    public String getPlanTime() {
        return planTime;
    }

    /**
     * @param planTime
     */
    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    /**
     * @return planTeacher
     */
    public String getPlanTeacher() {
        return planTeacher;
    }

    /**
     * @param planTeacher
     */
    public void setPlanTeacher(String planTeacher) {
        this.planTeacher = planTeacher;
    }

    /**
     * @return planIntroduce
     */
    public String getPlanIntroduce() {
        return planIntroduce;
    }

    /**
     * @param planIntroduce
     */
    public void setPlanIntroduce(String planIntroduce) {
        this.planIntroduce = planIntroduce;
    }

    
}