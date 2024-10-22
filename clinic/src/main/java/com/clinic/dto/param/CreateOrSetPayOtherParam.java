package com.clinic.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrSetPayOtherParam {

    @NotNull(message = "支付id不能为空！")
    private Long payId;

    private List<CreateOrSetPayRecord> payOther;
}
