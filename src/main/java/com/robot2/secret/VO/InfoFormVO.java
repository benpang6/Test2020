package com.robot2.secret.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class InfoFormVO implements Serializable {

    private static final long serialVersionUID = -4673441287222657834L;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String org1;
    private String org2;
    private String type;
    private String deviceId;

}
