package com.example.menuservice.dto;

import com.example.menuservice.domain.CustomCart;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomCartResponseDTO {

    private Long uid;
    private Double calorie;
    private int price;
    private Long breadId;
    private String breadName;

    private Long material1Id;
    private String material1Name;
    private Long material2Id;
    private String material2Name;
    private Long material3Id;
    private String material3Name;

    private Long cheeseId;
    private String cheeseName;

    private Long vegetable1Id;
    private String vegetable1Name;
    private Long vegetable2Id;
    private String vegetable2Name;
    private Long vegetable3Id;
    private String vegetable3Name;
    private Long vegetable4Id;
    private String vegetable4Name;
    private Long vegetable5Id;
    private String vegetable5Name;
    private Long vegetable6Id;
    private String vegetable6Name;
    private Long vegetable7Id;
    private String vegetable7Name;
    private Long vegetable8Id;
    private String vegetable8Name;

    private Long sauce1Id;
    private String sauce1Name;
    private Long sauce2Id;
    private String sauce2Name;
    private Long sauce3Id;
    private String sauce3Name;


    private int version;

    // CustomCart 엔티티를 DTO로 변환하는 메서드
    public static CustomCartResponseDTO fromEntity(CustomCart customCart) {
        return CustomCartResponseDTO.builder()
                .uid(customCart.getUid())
                .breadId(customCart.getBread() != null ? customCart.getBread().getUid() : null)
                .breadName(customCart.getBread() != null ? customCart.getBread().getBreadName() : null)

                .material1Id(customCart.getMaterial1() != null ? customCart.getMaterial1().getUid() : null)
                .material1Name(customCart.getMaterial1() != null ? customCart.getMaterial1().getMaterialName() : null)
                .material2Id(customCart.getMaterial2() != null ? customCart.getMaterial2().getUid() : null)
                .material2Name(customCart.getMaterial2() != null ? customCart.getMaterial2().getMaterialName() : null)
                .material3Id(customCart.getMaterial3() != null ? customCart.getMaterial3().getUid() : null)
                .material3Name(customCart.getMaterial3() != null ? customCart.getMaterial3().getMaterialName() : null)

                .cheeseId(customCart.getCheese() != null ? customCart.getCheese().getUid() : null)
                .cheeseName(customCart.getCheese() != null ? customCart.getCheese().getCheeseName() : null)

                .vegetable1Id(customCart.getVegetable1() != null ? customCart.getVegetable1().getUid() : null)
                .vegetable1Name(customCart.getVegetable1() != null ? customCart.getVegetable1().getVegetableName() : null)
                .vegetable2Id(customCart.getVegetable2() != null ? customCart.getVegetable2().getUid() : null)
                .vegetable2Name(customCart.getVegetable2() != null ? customCart.getVegetable2().getVegetableName() : null)
                .vegetable3Id(customCart.getVegetable3() != null ? customCart.getVegetable3().getUid() : null)
                .vegetable3Name(customCart.getVegetable3() != null ? customCart.getVegetable3().getVegetableName() : null)
                .vegetable4Id(customCart.getVegetable4() != null ? customCart.getVegetable4().getUid() : null)
                .vegetable4Name(customCart.getVegetable4() != null ? customCart.getVegetable4().getVegetableName() : null)
                .vegetable5Id(customCart.getVegetable5() != null ? customCart.getVegetable5().getUid() : null)
                .vegetable5Name(customCart.getVegetable5() != null ? customCart.getVegetable5().getVegetableName() : null)
                .vegetable6Id(customCart.getVegetable6() != null ? customCart.getVegetable6().getUid() : null)
                .vegetable6Name(customCart.getVegetable6() != null ? customCart.getVegetable6().getVegetableName() : null)
                .vegetable7Id(customCart.getVegetable7() != null ? customCart.getVegetable7().getUid() : null)
                .vegetable7Name(customCart.getVegetable7() != null ? customCart.getVegetable7().getVegetableName() : null)
                .vegetable8Id(customCart.getVegetable8() != null ? customCart.getVegetable8().getUid() : null)
                .vegetable8Name(customCart.getVegetable8() != null ? customCart.getVegetable8().getVegetableName() : null)

                .sauce1Id(customCart.getSauce1() != null ? customCart.getSauce1().getUid() : null)
                .sauce1Name(customCart.getSauce1() != null ? customCart.getSauce1().getSauceName() : null)
                .sauce2Id(customCart.getSauce2() != null ? customCart.getSauce2().getUid() : null)
                .sauce2Name(customCart.getSauce2() != null ? customCart.getSauce2().getSauceName() : null)
                .sauce3Id(customCart.getSauce3() != null ? customCart.getSauce3().getUid() : null)
                .sauce3Name(customCart.getSauce3() != null ? customCart.getSauce3().getSauceName() : null)


                .version(customCart.getVersion())
                .build();
    }
}
