package com.example.menuservice.mapper;

import com.example.menuservice.domain.Menu;
import com.example.menuservice.dto.MenuResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

//    public static MenuResponseDTO toResponseDTO(Menu menu) {
//        return MenuResponseDTO.builder()
//                .uid(menu.getUid())
//                .menuName(menu.getMenuName())
//                .price(menu.getPrice())
//                .calorie(menu.getCalorie())
//
//                .breadId(menu.getBread() != null ? menu.getBread().getUid() : null)
//                .breadName(menu.getBread() != null ? menu.getBread().getBreadName() : null)
//
//                .material1Id(menu.getMaterial1() != null ? menu.getMaterial1().getUid() : null)
//                .material1Name(menu.getMaterial1() != null ? menu.getMaterial1().getMaterialName() : null)
//                .material2Id(menu.getMaterial2() != null ? menu.getMaterial2().getUid() : null)
//                .material2Name(menu.getMaterial2() != null ? menu.getMaterial2().getMaterialName() : null)
//                .material3Id(menu.getMaterial3() != null ? menu.getMaterial3().getUid() : null)
//                .material3Name(menu.getMaterial3() != null ? menu.getMaterial3().getMaterialName() : null)
//
//                .cheeseId(menu.getCheese() != null ? menu.getCheese().getUid() : null)
//                .cheeseName(menu.getCheese() != null ? menu.getCheese().getCheeseName() : null)
//
//                .vegetable1Id(menu.getVegetable1() != null ? menu.getVegetable1().getUid() : null)
//                .vegetable1Name(menu.getVegetable1() != null ? menu.getVegetable1().getVegetableName() : null)
//                .vegetable2Id(menu.getVegetable2() != null ? menu.getVegetable2().getUid() : null)
//                .vegetable2Name(menu.getVegetable2() != null ? menu.getVegetable2().getVegetableName() : null)
//                .vegetable3Id(menu.getVegetable3() != null ? menu.getVegetable3().getUid() : null)
//                .vegetable3Name(menu.getVegetable3() != null ? menu.getVegetable3().getVegetableName() : null)
//                .vegetable4Id(menu.getVegetable4() != null ? menu.getVegetable4().getUid() : null)
//                .vegetable4Name(menu.getVegetable4() != null ? menu.getVegetable4().getVegetableName() : null)
//                .vegetable5Id(menu.getVegetable5() != null ? menu.getVegetable5().getUid() : null)
//                .vegetable5Name(menu.getVegetable5() != null ? menu.getVegetable5().getVegetableName() : null)
//                .vegetable6Id(menu.getVegetable6() != null ? menu.getVegetable6().getUid() : null)
//                .vegetable6Name(menu.getVegetable6() != null ? menu.getVegetable6().getVegetableName() : null)
//                .vegetable7Id(menu.getVegetable7() != null ? menu.getVegetable7().getUid() : null)
//                .vegetable7Name(menu.getVegetable7() != null ? menu.getVegetable7().getVegetableName() : null)
//                .vegetable8Id(menu.getVegetable8() != null ? menu.getVegetable8().getUid() : null)
//                .vegetable8Name(menu.getVegetable8() != null ? menu.getVegetable8().getVegetableName() : null)
//
//                .sauce1Id(menu.getSauce1() != null ? menu.getSauce1().getUid() : null)
//                .sauce1Name(menu.getSauce1() != null ? menu.getSauce1().getSauceName() : null)
//                .sauce2Id(menu.getSauce2() != null ? menu.getSauce2().getUid() : null)
//                .sauce2Name(menu.getSauce2() != null ? menu.getSauce2().getSauceName() : null)
//                .sauce3Id(menu.getSauce3() != null ? menu.getSauce3().getUid() : null)
//                .sauce3Name(menu.getSauce3() != null ? menu.getSauce3().getSauceName() : null)
//
//                .img(menu.getImg())
//                .status(menu.getStatus())
//                .createdDate(menu.getCreatedDate())
//                .version(menu.getVersion())
//                .build();
//
//    }

    public  static MenuResponseDTO toResponseDTO(Menu menu) {
        return MenuResponseDTO.builder()
                .uid(menu.getUid())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .calorie(menu.getCalorie())

                .breadId(menu.getBread().getUid())
                .breadName(menu.getBread().getBreadName())
                .breadImg(menu.getBread().getImg()) // ✅ 이미지 추가

                .material1Id(menu.getMaterial1() != null ? menu.getMaterial1().getUid() : null)
                .material1Name(menu.getMaterial1() != null ? menu.getMaterial1().getMaterialName() : null)
                .material1Img(menu.getMaterial1() != null ? menu.getMaterial1().getImg() : null) // ✅

                .material2Id(menu.getMaterial2() != null ? menu.getMaterial2().getUid() : null)
                .material2Name(menu.getMaterial2() != null ? menu.getMaterial2().getMaterialName() : null)
                .material2Img(menu.getMaterial2() != null ? menu.getMaterial2().getImg() : null)

                .material3Id(menu.getMaterial3() != null ? menu.getMaterial3().getUid() : null)
                .material3Name(menu.getMaterial3() != null ? menu.getMaterial3().getMaterialName() : null)
                .material3Img(menu.getMaterial3() != null ? menu.getMaterial3().getImg() : null)

                .cheeseId(menu.getCheese() != null ? menu.getCheese().getUid() : null)
                .cheeseName(menu.getCheese() != null ? menu.getCheese().getCheeseName() : null)
                .cheeseImg(menu.getCheese() != null ? menu.getCheese().getImg() : null)

                .vegetable1Id(menu.getVegetable1() != null ? menu.getVegetable1().getUid() : null)
                .vegetable1Name(menu.getVegetable1() != null ? menu.getVegetable1().getVegetableName() : null)
                .vegetable1Img(menu.getVegetable1() != null ? menu.getVegetable1().getImg() : null)
                .vegetable2Id(menu.getVegetable2() != null ? menu.getVegetable2().getUid() : null)
                .vegetable2Name(menu.getVegetable2() != null ? menu.getVegetable2().getVegetableName() : null)
                .vegetable2Img(menu.getVegetable2() != null ? menu.getVegetable2().getImg() : null)
                .vegetable3Id(menu.getVegetable3() != null ? menu.getVegetable3().getUid() : null)
                .vegetable3Name(menu.getVegetable3() != null ? menu.getVegetable3().getVegetableName() : null)
                .vegetable3Img(menu.getVegetable3() != null ? menu.getVegetable3().getImg() : null)
                .vegetable4Id(menu.getVegetable4() != null ? menu.getVegetable4().getUid() : null)
                .vegetable4Name(menu.getVegetable4() != null ? menu.getVegetable4().getVegetableName() : null)
                .vegetable4Img(menu.getVegetable4() != null ? menu.getVegetable4().getImg() : null)
                .vegetable5Id(menu.getVegetable5() != null ? menu.getVegetable5().getUid() : null)
                .vegetable5Name(menu.getVegetable5() != null ? menu.getVegetable5().getVegetableName() : null)
                .vegetable5Img(menu.getVegetable5() != null ? menu.getVegetable5().getImg() : null)
                .vegetable6Id(menu.getVegetable6() != null ? menu.getVegetable6().getUid() : null)
                .vegetable6Name(menu.getVegetable6() != null ? menu.getVegetable6().getVegetableName() : null)
                .vegetable6Img(menu.getVegetable6() != null ? menu.getVegetable6().getImg() : null)
                .vegetable7Id(menu.getVegetable7() != null ? menu.getVegetable7().getUid() : null)
                .vegetable7Name(menu.getVegetable7() != null ? menu.getVegetable7().getVegetableName() : null)
                .vegetable7Img(menu.getVegetable7() != null ? menu.getVegetable7().getImg() : null)
                .vegetable8Id(menu.getVegetable8() != null ? menu.getVegetable8().getUid() : null)
                .vegetable8Name(menu.getVegetable8() != null ? menu.getVegetable8().getVegetableName() : null)
                .vegetable8Img(menu.getVegetable8() != null ? menu.getVegetable8().getImg() : null)

                .sauce1Id(menu.getSauce1() != null ? menu.getSauce1().getUid() : null)
                .sauce1Name(menu.getSauce1() != null ? menu.getSauce1().getSauceName() : null)
                .sauce1Img(menu.getSauce1() != null ? menu.getSauce1().getImg() : null)
                .sauce2Id(menu.getSauce2() != null ? menu.getSauce2().getUid() : null)
                .sauce2Name(menu.getSauce2() != null ? menu.getSauce2().getSauceName() : null)
                .sauce2Img(menu.getSauce2() != null ? menu.getSauce2().getImg() : null)
                .sauce3Id(menu.getSauce3() != null ? menu.getSauce3().getUid() : null)
                .sauce3Name(menu.getSauce3() != null ? menu.getSauce3().getSauceName() : null)
                .sauce3Img(menu.getSauce3() != null ? menu.getSauce3().getImg() : null)

                .img(menu.getImg())
                .status(menu.getStatus())
                .createdDate(menu.getCreatedDate())
                .version(menu.getVersion())
                .build();
    }

}
