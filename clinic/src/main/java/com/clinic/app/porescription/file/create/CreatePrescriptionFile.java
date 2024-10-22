package com.clinic.app.porescription.file.create;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.clinic.app.AppPrescriptionService;
import com.clinic.converter.PrescriptionConverter;
import com.clinic.dto.PrescriptionDrugDto;
import com.clinic.dto.PrescriptionFileVo;
import com.clinic.service.PrescriptionService;
import com.clinic.util.LoginUser;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.util.PoitlIOUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.deepoove.poi.config.Configure.builder;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

@Slf4j
@Component
public class CreatePrescriptionFile implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    private PrescriptionConverter converter;

    @Value("${prescription.template.path}")
    private String templateAbsPath;

    @Getter
    private List<String> templates;

    @Resource
    private HttpServletResponse response;

    @Override
    public void onApplicationEvent(@NotNull @org.jetbrains.annotations.NotNull ContextRefreshedEvent event) {
        if(SystemUtil.getOsInfo().isWindows()) {
            templateAbsPath = FileUtil.getAbsolutePath("templates");
        }
        log.info("加载本地处方模板... templateAbsPath={}", templateAbsPath);
        templates = FileUtil.listFileNames(templateAbsPath);
        log.info("加载本地处方模板: templates={}", templates);


    }

    /**
     * 生成到本地
     * @param id 处方ID
     * @param templateIndex 模板下标
     * @throws IOException 生成处方文件失败
     */
    public void generation(Long id, Integer templateIndex) throws Exception {
        String templateFileName = templates.get(templateIndex);
        Prescription prescription = getInstance(service.getFileInfo(id), appService.getDrugList(id));

        create(templateAbsPath + "/" + templateFileName, prescription);
    }

    @Data
    @AllArgsConstructor
    public static class Patient {

        private String name;

        private String sex;

        private String age;

        private String weight;
    }

    @Data
    @AllArgsConstructor
    public static class Drug {

        private String name;

        private String spec;

        private String number;

        private String number2;

        private String singleDose;

        private String usage;

        private String frequency;
    }

    @Data
    @AllArgsConstructor
    public static class Content {
        private String drug;
        private String sig;
    }

    @Data
    @AllArgsConstructor
    public static class Prescription {

        private String no;

        private String clinic;

        private String date;

        private Patient patient;

        private List<Drug> drugList;

        private String history;

        private String diagnosis;

        private String doctors;

        private String cost;

        public static Configure buildConfig(LoopRowTableRenderPolicy policy) {
            return builder()
                    .bind("content", policy)
                    .build();
        }

        public Map<String, Object> getModel() {
            List<Content> contents = new ArrayList<>();
            for (int index = 0; index < 5; index++) {
                String drugStr = "";
                String sigStr = "";
                if(index + 1 <= drugList.size()) {
                    Drug drug = drugList.get(index);
                    drugStr = drug.name + " " + drug.spec + " × " + drug.number;
                    if(StringUtils.isNotBlank(drug.number2)) {
                        drugStr += " × " + drug.number2;
                    }
                } else {
                    if(index == drugList.size()) {
                        drugStr = "(以下内容为空)";
                    }
                }
                contents.add(new Content(drugStr, sigStr));
            }
            return new HashMap<String, Object>() {{
                put("title", clinic);
                put("a", no);
                put("b", patient.name); //name
                put("c", patient.sex); //sex
                put("d", patient.age); //age
                put("e", patient.weight); //体重
                put("f", history); //过敏史
                put("g", DateUtil.formatDate(DateUtil.parse(date))); //日期
                put("diagnosis", diagnosis);
                put("content", contents);
                put("doctor", doctors);
                put("cost", cost);
            }};
        }
    }

    private final AppPrescriptionService appService;

    private final PrescriptionService service;

    public Prescription getInstance(PrescriptionFileVo prescription, List<PrescriptionDrugDto> dtos) {
        List<Drug> drugList = dtos.stream().map(dto -> {
            Drug drug = converter.toFileModel(dto);
            drug.setNumber(dto.getQuantity() + dto.getQuantityUnit());
            drug.setSingleDose(dto.getSingleDose() + dto.getSingleDoseUnit());
            drug.setUsage(dto.getDrugUsage());
            return drug;
        }).collect(Collectors.toList());

        return new Prescription(
                prescription.getId().toString(),
                prescription.getClinicName(),
                prescription.getExpirationDate(),
                new Patient(
                        prescription.getName(),
                        prescription.getSex() == 1 ? "男" : "女",
                        prescription.getAge() + "岁",
                        (nonNull(prescription.getWeight()) ? prescription.getWeight() : INTEGER_ZERO) + "kg" ),
                drugList,
                prescription.getPastMedicalHistory(),
                prescription.getDiagnosis(),
                LoginUser.get().getName(),
                prescription.getPrice().toString()
        );
    }


//    @Value("${prescription.image.path}")
//    private String imagePath;

    /**
     * https://blog.csdn.net/weixin_69306012/article/details/131309819
     */
    public void create(String filePath, Prescription prescription) throws Exception {
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure configure = Prescription.buildConfig(policy);
        Map<String, Object> model = prescription.getModel();
        XWPFTemplate template = XWPFTemplate.compile(filePath, configure).render(model);


        String fileName = prescription.no + ".docx";

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition","attachment;filename=\""+ fileName +"\"");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename");
        response.setHeader("download-filename", fileName);

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

    @Autowired
    public CreatePrescriptionFile(AppPrescriptionService appService, PrescriptionService service) {
        this.appService = appService;
        this.service = service;
    }
}
