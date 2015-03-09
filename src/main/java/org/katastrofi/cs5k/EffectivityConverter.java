package org.katastrofi.cs5k;

import com.google.common.collect.Range;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;

import static com.google.common.collect.BoundType.CLOSED;
import static com.google.common.collect.BoundType.OPEN;
import static com.google.common.collect.Range.all;
import static com.google.common.collect.Range.downTo;
import static com.google.common.collect.Range.range;
import static com.google.common.collect.Range.upTo;
import static java.time.LocalDateTime.parse;

@Converter(autoApply = true)
public class EffectivityConverter
        implements AttributeConverter<Effectivity, String> {

    @Override
    public String convertToDatabaseColumn(Effectivity effectivity) {
        Range<LocalDateTime> attribute = effectivity.timeRange();
        StringBuilder sb = new StringBuilder();
        if (!attribute.hasLowerBound() || attribute.lowerBoundType() == OPEN) {
            sb.append(']');
        } else {
            sb.append('[');
        }
        if (attribute.hasLowerBound()) {
            sb.append(attribute.lowerEndpoint().toString());
        }
        sb.append(',');
        if (attribute.hasUpperBound()) {
            sb.append(attribute.upperEndpoint().toString());
        }
        if (!attribute.hasUpperBound() || attribute.upperBoundType() == OPEN) {
            sb.append('[');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    // TODO clean up this mindstream-codeball-of-mud-monster
    @Override
    public Effectivity convertToEntityAttribute(String dbData) {
        String[] startAndEnd = dbData.split(",");
        String start = startAndEnd[0];
        String end = startAndEnd[1];

        if (startAndEnd.length != 2) {
            throw new IllegalArgumentException(
                    "Serialized range should be split with ','");
        }

        boolean startsOpen, endsOpen;
        if (start.startsWith("[")) {
            startsOpen = false;
        } else if (start.startsWith("]")) {
            startsOpen = true;
        } else {
            throw new IllegalArgumentException(
                    "Serialized range should begin with [ or ]");
        }
        if (end.endsWith("]")) {
            endsOpen = false;
        } else if (end.endsWith("[")) {
            endsOpen = true;
        } else {
            throw new IllegalArgumentException(
                    "Serialized range should end with [ or ]");
        }

        String startDateString = start.substring(1),
                endDateString = end.substring(0, end.length() - 1);
        LocalDateTime startDate =
                startDateString.isEmpty() ? null : parse(start.substring(1)),
                endDate = endDateString.isEmpty() ? null :
                        parse(end.substring(0, end.length() - 1));

        if (startDate == null && endDate == null) {
            return new Effectivity(all());
        } else if (startDate != null && endDate != null) {
            return new Effectivity(range(startDate, startsOpen ? OPEN : CLOSED,
                    endDate, endsOpen ? OPEN : CLOSED));
        } else if (startDate == null) {
            return new Effectivity(upTo(endDate, endsOpen ? OPEN : CLOSED));
        } else {
            return new Effectivity(
                    downTo(startDate, startsOpen ? OPEN : CLOSED));
        }
    }
}
