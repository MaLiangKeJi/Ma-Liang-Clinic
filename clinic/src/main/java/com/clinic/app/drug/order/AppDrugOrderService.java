package com.clinic.app.drug.order;

import com.clinic.controller.DrugOrderController;
import com.clinic.entity.DrugOrder;
import com.clinic.entity.DrugOrderDetail;
import com.clinic.service.DrugOrderChatService;
import com.clinic.service.DrugOrderDetailService;
import com.clinic.service.DrugOrderService;
import com.clinic.util.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppDrugOrderService {

    private final DrugOrderService drugOrderService;

    private final DrugOrderDetailService drugOrderDetailService;

    private final DrugOrderChatService drugOrderChatService;


    /**
     * 下单，保存订单记录
     */
    public Long add(DrugOrderController.AddDrugOrderParam param) {
        DrugOrder drugOrder = new DrugOrder(param.getName(), param.getReceiverAddress(),null, LoginUser.getId());
        //TODO 保存订单记录
        drugOrderService.save(drugOrder);
        drugOrderDetailService.save(new DrugOrderDetail(drugOrder.getId(), param.getOrderDetail()));
        return null;
    }




    @Autowired
    public AppDrugOrderService(DrugOrderService drugOrderService, DrugOrderDetailService drugOrderDetailService, DrugOrderChatService drugOrderChatService) {
        this.drugOrderService = drugOrderService;
        this.drugOrderDetailService = drugOrderDetailService;
        this.drugOrderChatService = drugOrderChatService;
    }

}
