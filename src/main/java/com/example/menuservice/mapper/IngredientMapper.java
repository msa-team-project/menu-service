package com.example.menuservice.mapper;

import com.example.menuservice.domain.*;
import com.example.menuservice.dto.*;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper {

    public BreadResponseDTO toBreadDTO(Bread bread) {
        if (bread == null) return null;
        return new BreadResponseDTO(
                bread.getUid(),
                bread.getBreadName(),
                bread.getCalorie(),
                bread.getPrice(),
                bread.getImg(),
                bread.getStatus(),
                bread.getCreatedDate(),
                bread.getVersion()
        );
    }

    public MaterialResponseDTO toMaterialDTO(Material material) {
        if (material == null) return null;
        return new MaterialResponseDTO(
                material.getUid(),
                material.getMaterialName(),
                material.getCalorie(),
                material.getPrice(),
                material.getImg(),
                material.getStatus(),
                material.getCreatedDate(),
                material.getVersion()
        );
    }

    public CheeseResponseDTO toCheeseDTO(Cheese cheese) {
        if (cheese == null) return null;
        return new CheeseResponseDTO(
                cheese.getUid(),
                cheese.getCheeseName(),
                cheese.getCalorie(),
                cheese.getPrice(),
                cheese.getImg(),
                cheese.getStatus(),
                cheese.getCreatedDate(),
                cheese.getVersion()
        );
    }

    public VegetableResponseDTO toVegetableDTO(Vegetable vegetable) {
        if (vegetable == null) return null;
        return new VegetableResponseDTO(
                vegetable.getUid(),
                vegetable.getVegetableName(),
                vegetable.getCalorie(),
                vegetable.getPrice(),
                vegetable.getImg(),
                vegetable.getStatus(),
                vegetable.getCreatedDate(),
                vegetable.getVersion()
        );
    }

    public SauceResponseDTO toSauceDTO(Sauce sauce) {
        if (sauce == null) return null;
        return new SauceResponseDTO(
                sauce.getUid(),
                sauce.getSauceName(),
                sauce.getCalorie(),
                sauce.getPrice(),
                sauce.getImg(),
                sauce.getStatus(),
                sauce.getCreatedDate(),
                sauce.getVersion()
        );
    }
}
