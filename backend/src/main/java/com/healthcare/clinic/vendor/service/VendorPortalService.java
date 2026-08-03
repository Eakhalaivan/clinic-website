package com.healthcare.clinic.vendor.service;

import com.healthcare.clinic.backoffice.inventory.entity.BackofficePurchaseOrder;
import com.healthcare.clinic.backoffice.inventory.repository.BackofficePurchaseOrderRepository;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.vendor.entity.VendorDelivery;
import com.healthcare.clinic.vendor.repository.VendorDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorPortalService {

    private final BackofficePurchaseOrderRepository poRepository;
    private final VendorDeliveryRepository deliveryRepository;

    @Transactional(readOnly = true)
    public List<BackofficePurchaseOrder> getVendorPurchaseOrders() {
        return poRepository.findAllByOrderByOrderDateDesc();
    }

    @Transactional
    public BackofficePurchaseOrder acknowledgePo(Long poId) {
        BackofficePurchaseOrder po = poRepository.findById(poId).orElseThrow();
        po.setStatus("ACKNOWLEDGED");
        return poRepository.save(po);
    }

    @Transactional(readOnly = true)
    public List<VendorDelivery> getVendorDeliveries(Long vendorUserId) {
        return deliveryRepository.findByVendorUserId(vendorUserId);
    }

    @Transactional
    public VendorDelivery createDelivery(Long poId, VendorDelivery deliveryInput, User vendorUser) {
        BackofficePurchaseOrder po = poRepository.findById(poId).orElseThrow();
        po.setStatus("SHIPPED");
        poRepository.save(po);

        deliveryInput.setPurchaseOrder(po);
        deliveryInput.setVendorUser(vendorUser);
        deliveryInput.setStatus("DISPATCHED");
        return deliveryRepository.save(deliveryInput);
    }
}
