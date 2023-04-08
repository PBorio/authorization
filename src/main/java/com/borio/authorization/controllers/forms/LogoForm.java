package com.borio.authorization.controllers.forms;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LogoForm {

    private Long companyId;

    private MultipartFile logo;
}
