package com.example.menuservice.mapper;

import com.example.menuservice.domain.CustomCart;
import com.example.menuservice.dto.CustomCartResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomCartMapper {

    public static CustomCartResponseDTO toResponseDTO(CustomCart customCart) {
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
