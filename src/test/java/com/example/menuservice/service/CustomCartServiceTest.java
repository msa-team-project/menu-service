//package com.example.menuservice.service;
//
//import com.example.menuservice.domain.*;
//import com.example.menuservice.dto.CustomCartRequestDTO;
//import com.example.menuservice.dto.CustomCartResponseDTO;
//import com.example.menuservice.exception.CustomCartNotFoundException;
//import com.example.menuservice.mapper.CustomCartMapper;
//import com.example.menuservice.repository.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CustomCartServiceTest {
//
//    @Mock
//    private CustomCartRepository customCartRepository;
//
//    @Mock
//    private CartRepository cartRepository;
//
//    @Mock
//    private BreadRepository breadRepository;
//
//    @Mock
//    private MaterialRepository materialRepository;
//
//    @Mock
//    private CheeseRepository cheeseRepository;
//
//    @Mock
//    private VegetableRepository vegetableRepository;
//
//    @Mock
//    private SauceRepository sauceRepository;
//
//    @InjectMocks
//    private CustomCartService customCartService;  // CustomCartService가 주입되어야 합니다.
//
//    private Bread bread;
//    private Material material;
//    private CustomCartRequestDTO customCartRequestDTO;
//
//    @BeforeEach
//    public void setUp() {
//        bread = new Bread();
//        bread.setUid(1L);
//        bread.setBreadName("Test Bread");
//
//        material = new Material();
//        material.setUid(1L);
//        material.setMaterialName("Test Material");
//
//        customCartRequestDTO = new CustomCartRequestDTO();
//        customCartRequestDTO.setBread(bread.getUid());
//        customCartRequestDTO.setMaterial1(material.getUid());
//        customCartRequestDTO.setPrice(100L);
//        customCartRequestDTO.setCalorie(200.0);
//    }
//
//    @Test
//    public void addCustomCart_ValidRequest() {
//        // 의존성 모킹
//        when(breadRepository.findById(bread.getUid())).thenReturn(Optional.of(bread));
//        when(materialRepository.findById(material.getUid())).thenReturn(Optional.of(material));
//        when(customCartRepository.save(any(CustomCart.class))).thenReturn(new CustomCart());
//        when(cartRepository.save(any(Cart.class))).thenReturn(new Cart());
//
//        CustomCartResponseDTO response = customCartService.addCustomCart(customCartRequestDTO);
//
//        // 메소드 호출 검증
//        verify(breadRepository, times(1)).findById(bread.getUid());
//        verify(materialRepository, times(1)).findById(material.getUid());
//        verify(customCartRepository, times(1)).save(any(CustomCart.class));
//        verify(cartRepository, times(1)).save(any(Cart.class));
//
//        // 결과 검증
//        assertNotNull(response);
//        assertEquals("Test Bread", response.getBreadName());
//        assertEquals("Test Material", response.getMaterial1Name());
//    }
//
//    @Test
//    public void addCustomCart_InvalidCalorie() {
//        customCartRequestDTO.setCalorie(0.0); // 잘못된 칼로리
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            customCartService.addCustomCart(customCartRequestDTO); // 예외가 발생해야 함
//        });
//
//        assertEquals("Calorie must be greater than zero", exception.getMessage());
//    }
//
//    @Test
//    public void addCustomCart_InvalidPrice() {
//        customCartRequestDTO.setPrice(-1L); // 잘못된 가격
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            customCartService.addCustomCart(customCartRequestDTO); // 예외가 발생해야 함
//        });
//
//        assertEquals("Price must be greater than zero", exception.getMessage());
//    }
//
//    @Test
//    public void addCustomCart_InvalidBreadId() {
//        customCartRequestDTO.setBread(999L); // 존재하지 않는 Bread ID
//
//        when(breadRepository.findById(customCartRequestDTO.getBread())).thenReturn(Optional.empty());
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            customCartService.addCustomCart(customCartRequestDTO); // 예외가 발생해야 함
//        });
//
//        assertEquals("Bread not found: 999", exception.getMessage());
//    }
//
//    @Test
//    public void addCustomCart_InvalidMaterialId() {
//        customCartRequestDTO.setMaterial1(999L); // 존재하지 않는 Material ID
//
//        when(breadRepository.findById(customCartRequestDTO.getBread())).thenReturn(Optional.of(bread));
//        when(materialRepository.findById(customCartRequestDTO.getMaterial1())).thenReturn(Optional.empty());
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            customCartService.addCustomCart(customCartRequestDTO); // 예외가 발생해야 함
//        });
//
//        assertEquals("Material not found: 999", exception.getMessage());
//    }
//
//    @Test
//    public void removeCustomCart_ValidRequest() {
//        CustomCart customCart = new CustomCart();
//        customCart.setUid(1L);
//
//        when(customCartRepository.findById(1L)).thenReturn(Optional.of(customCart));
//
//        customCartService.removeCustomCart(1L);
//
//        verify(customCartRepository, times(1)).delete(customCart);
//    }
//
//    @Test
//    public void removeCustomCart_NotFound() {
//        when(customCartRepository.findById(999L)).thenReturn(Optional.empty());
//
//        CustomCartNotFoundException exception = assertThrows(CustomCartNotFoundException.class, () -> {
//            customCartService.removeCustomCart(999L); // 예외가 발생해야 함
//        });
//
//        assertEquals("ID: 999", exception.getMessage());
//    }
//
//    @Test
//    public void viewCustomCart_ValidRequest() {
//        CustomCart customCart = new CustomCart();
//        customCart.setUid(1L);
//
//        when(customCartRepository.findById(1L)).thenReturn(Optional.of(customCart));
//
//        CustomCartResponseDTO response = customCartService.viewCustomCart(1L);
//
//        assertNotNull(response);
//        assertEquals(1L, response.getUid());
//    }
//
//    @Test
//    public void viewCustomCart_NotFound() {
//        Long invalidId = 999L;
//
//        CustomCartNotFoundException exception = assertThrows(CustomCartNotFoundException.class, () -> {
//            customCartService.viewCustomCart(invalidId); // 예외가 발생해야 함
//        });
//
//        assertEquals("ID: " + invalidId, exception.getMessage()); // 실제 예외 메시지에 맞춰 검증
//    }
//
//
//}
