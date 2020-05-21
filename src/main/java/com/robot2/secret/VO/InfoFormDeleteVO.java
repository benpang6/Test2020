package com.robot2.secret.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoFormDeleteVO implements Serializable {


    private static final long serialVersionUID = -88982530947289489L;
    private List<String> deviceId;      //deviceId数组
    private String type;            //type : 机器人"r" 平台"p"
}
