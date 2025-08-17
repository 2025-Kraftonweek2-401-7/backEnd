package com.krafton.stamp.config;

import com.krafton.stamp.domain.Rarity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RarityConverter implements Converter<String, Rarity> {
    @Override public Rarity convert(String source) {
        return Rarity.valueOf(source.toUpperCase());
    }
}
