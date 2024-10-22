package com.clinic.app.diagnosis.file.create;

import cn.hutool.core.date.DateUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.clinic.dto.vo.DiagnosisProofFileVo;
import com.clinic.service.DiagnosisProofService;
import com.clinic.util.LoginUser;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.util.PoitlIOUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.deepoove.poi.config.Configure.builder;

@Slf4j
@Component
public class CreateDiagnosisProofFile implements ApplicationListener<ContextRefreshedEvent> {

    private final DiagnosisProofService service;

    @Value("${diagnosis.template.path}")
    private String templateAbsPath;

    @Resource
    private HttpServletResponse response;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }

    public void generation(Long id)  throws Exception {
        String templateFileName = "diagnosis.docx";
        DiagnosisProof diagnosisProof = getInstance(service.getFileInfo(id));
        create(templateAbsPath + "/" + templateFileName, diagnosisProof);
    }

    private DiagnosisProof getInstance(DiagnosisProofFileVo dbDiagnosis) {
        return new DiagnosisProof(
                dbDiagnosis.getId().toString(),
                dbDiagnosis.getClinicName(),
                dbDiagnosis.getVisitDate(),
                new Patient(dbDiagnosis.getName(),
                        dbDiagnosis.getSex() == 1 ? "男" : "女",
                        dbDiagnosis.getAge().toString() + "岁",
                        dbDiagnosis.getAddress()),
                dbDiagnosis.getDiagnosis(),
                dbDiagnosis.getDealWith(),
                LoginUser.get().getName());
    }


    private void create(String filePath, DiagnosisProof diagnosisProof)  throws Exception {
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure configure = DiagnosisProof.buildConfig(policy);
        Map<String, Object> model = diagnosisProof.getModel();
        XWPFTemplate template = XWPFTemplate.compile(filePath, configure).render(model);


        String fileName =  URLEncoder.encode(diagnosisProof.no, "UTF-8").replaceAll("\\+", "%20");

        response.setContentType("application/octet-stream");
        response.addHeader("Content-disposition", "attachment;filename=" + fileName + ".docx");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("download-filename", fileName);
        response.setCharacterEncoding("utf-8");

        // Converter IO Stream: outputStream > inputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        template.write(outputStream);
        outputStream.flush();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ServletOutputStream servletOutputStream = response.getOutputStream();

        // TO PDF
        new Document(inputStream).extractPages(0, 1).save(servletOutputStream, SaveFormat.PDF);

        PoitlIOUtils.closeQuietlyMulti(template, inputStream, outputStream, servletOutputStream);
    }

    @Data
    @AllArgsConstructor
    public static class DiagnosisProof {
        private String no;

        private String clinic;

        private String date;

        private Patient patient;

        private String diagnosis;

        private String dealWith;

        private String doctors;

        public static Configure buildConfig(LoopRowTableRenderPolicy policy) {
            return builder()
                    .bind("content", policy)
                    .build();
        }

        public Map<String, Object> getModel() {
            return new HashMap<String, Object>() {{
                put("title", clinic);
                put("a", no);
                put("b", patient.name); //name
                put("c", patient.sex); //sex
                put("d", patient.age); //age
                put("e", patient.addr); //age
                put("f", DateUtil.formatDate(DateUtil.parse(date))); //日期
                put("diagnosis", diagnosis);
                put("dealWith", dealWith);
                put("doctor", doctors);
            }};
        }
    }

    @Data
    @AllArgsConstructor
    public static class Patient {

        private String name;

        private String sex;

        private String age;

        private String addr;
    }


    @Autowired
    public CreateDiagnosisProofFile(DiagnosisProofService service) {
        this.service = service;
    }

}
