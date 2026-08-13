package com.healthcare.clinic.inpatient.service;

import com.healthcare.clinic.inpatient.entity.Bed;
import com.healthcare.clinic.inpatient.entity.Room;
import com.healthcare.clinic.inpatient.entity.Ward;
import com.healthcare.clinic.inpatient.repository.BedRepository;
import com.healthcare.clinic.inpatient.repository.RoomRepository;
import com.healthcare.clinic.inpatient.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BedManagementService {

    private final WardRepository wardRepository;
    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;

    @Transactional(readOnly = true)
    public List<Ward> getWards(Long branchId) {
        return wardRepository.findByBranchId(branchId);
    }

    @Transactional(readOnly = true)
    public List<Bed> getBeds(Long branchId, String status) {
        if (status != null && !status.isEmpty()) {
            return bedRepository.findByStatusAndBranchId(status, branchId);
        }
        return bedRepository.findByBranchId(branchId);
    }

    @Transactional
    public Ward createWard(Ward ward) {
        return wardRepository.save(ward);
    }

    @Transactional
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Transactional
    public Bed createBed(Bed bed) {
        return bedRepository.save(bed);
    }
}
