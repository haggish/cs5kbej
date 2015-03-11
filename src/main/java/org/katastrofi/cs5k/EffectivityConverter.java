package org.katastrofi.cs5k;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class EffectivityConverter
        implements AttributeConverter<Effectivity, String> {

    @Override
    public String convertToDatabaseColumn(Effectivity effectivity) {
        return effectivity.toString();
    }

    @Override
    public Effectivity convertToEntityAttribute(String dbData) {
        return Effectivity.from(dbData);
    }
}
