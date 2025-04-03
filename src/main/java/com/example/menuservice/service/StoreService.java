package com.example.menuservice.service;

import com.example.menuservice.domain.Store;
import com.example.menuservice.dto.StoreRequestDTO;
import com.example.menuservice.dto.StoreResponseDTO;
import com.example.menuservice.exception.StoreAlreadyExistsException;
import com.example.menuservice.exception.StoreNotFoundException;
import com.example.menuservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    // 지점 목록 조회
    public List<StoreResponseDTO> viewStoreList() {
        List<StoreResponseDTO> storeResponseDTOList = new ArrayList<>();
        for (Store store : storeRepository.findAll()) {
            storeResponseDTOList.add(toStoreResponseDTO(store));
        }
        return storeResponseDTOList;
    }

    // 지점 이름으로 지점 조회
    public StoreResponseDTO viewStore(int uid) {
        Store store = storeRepository.findByUid(uid)
                .orElseThrow(() -> new StoreNotFoundException(uid));
        return toStoreResponseDTO(store);
    }

    //지점 추가
    public StoreResponseDTO addStore(StoreRequestDTO storeRequestDTO) throws StoreAlreadyExistsException {
        if(storeRepository.existsByStoreName(storeRequestDTO.getStoreName())) {
            throw new StoreAlreadyExistsException(storeRequestDTO.getStoreName());
        }

        Store store =   Store.builder()
                .storeName(storeRequestDTO.getStoreName())
                .address(storeRequestDTO.getAddress())
                .postcode(storeRequestDTO.getPostcode())
                .status(storeRequestDTO.getStatus())
                .build();
        Store saveStore = storeRepository.save(store);//DB에 저장해 줌.
        return toStoreResponseDTO(saveStore);
    }

    //지점 수정
    @Transactional
    public StoreResponseDTO updateStore(int uid,StoreRequestDTO storeRequestDTO) throws StoreNotFoundException {
        Store existingStore = storeRepository.findByUid(uid)
                .orElseThrow(() -> new StoreNotFoundException(uid));

        Store updateStore = Store.builder()
                .uid(existingStore.uid())
                .storeName(storeRequestDTO.getStoreName())
                .address(storeRequestDTO.getAddress())
                .postcode(storeRequestDTO.getPostcode())
                .status(storeRequestDTO.getStatus())
                .createdDate(existingStore.createdDate())
                .version(existingStore.version())//버전 증가
                .build();
        Store saveStore = storeRepository.save(updateStore);
        return toStoreResponseDTO(saveStore);
    }

   //지점 상태 변경
   public void updateStatusStore(int uid, String status) throws StoreNotFoundException {
        if (!storeRepository.existsById(uid)) {
            throw new StoreNotFoundException(uid);
        }
        storeRepository.updateStatusByUid(uid,status);
   }

   //지점 삭제
    public void deleteStore(int uid) {
        if (!storeRepository.existsById(uid)) {
        throw new StoreNotFoundException(uid);
        }
        storeRepository.deleteByUid(uid);
    }



    // Store -> StoreResponseDTO 변환 메서드
    private StoreResponseDTO toStoreResponseDTO(Store store) {
        return new StoreResponseDTO(
                store.uid(),
                store.storeName(),
                store.address(),
                store.postcode(),
                store.status(),
                store.createdDate()
        );
    }
}
