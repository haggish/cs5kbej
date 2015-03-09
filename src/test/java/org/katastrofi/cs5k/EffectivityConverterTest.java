package org.katastrofi.cs5k;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static com.google.common.collect.BoundType.CLOSED;
import static com.google.common.collect.BoundType.OPEN;
import static com.google.common.collect.Range.all;
import static com.google.common.collect.Range.closed;
import static com.google.common.collect.Range.downTo;
import static com.google.common.collect.Range.open;
import static com.google.common.collect.Range.upTo;
import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EffectivityConverterTest {

    private EffectivityConverter testedEffectivityConverter;

    private LocalDateTime a, b;


    @Before
    public void init() {
        testedEffectivityConverter = new EffectivityConverter();
        a = LocalDateTime.now().truncatedTo(MINUTES);
        b = a.plusDays(1).truncatedTo(MINUTES);
    }

    @Test
    public void allIsConvertedToRightSqBracketCommaLeftSqBracket() {
        assertThat(testedEffectivityConverter
                .convertToDatabaseColumn(new Effectivity(all())), is("],["));
    }

    @Test
    public void upToClosedAIsConvertedToRightSqBracketCommaAStringRightSqBracket() {
        assertThat(testedEffectivityConverter
                        .convertToDatabaseColumn(
                                new Effectivity(upTo(a, CLOSED))),
                is(format("],%s]", a.toString())));
    }

    @Test
    public void upToOpenAIsConvertedToRightSqBracketCommaAStringLeftSqBracket() {
        assertThat(testedEffectivityConverter
                        .convertToDatabaseColumn(
                                new Effectivity(upTo(a, OPEN))),
                is(format("],%s[", a.toString())));
    }

    @Test
    public void downToClosedAIsConvertedToLeftSqBracketAStringCommaLeftSqBracket() {
        assertThat(testedEffectivityConverter
                        .convertToDatabaseColumn(
                                new Effectivity(downTo(a, CLOSED))),
                is(format("[%s,[", a.toString())));
    }

    @Test
    public void downToOpenAIsConvertedToRightSqBracketAStringCommaLeftSqBracket() {
        assertThat(testedEffectivityConverter
                        .convertToDatabaseColumn(
                                new Effectivity(downTo(a, OPEN))),
                is(format("]%s,[", a.toString())));
    }

    @Test
    public void openABIsConvertedToRightSqBracketAStringCommaBStringLeftSqBracket() {
        assertThat(testedEffectivityConverter
                        .convertToDatabaseColumn(
                                new Effectivity(open(a, b))),
                is(format("]%s,%s[", a.toString(), b.toString())));
    }

    @Test
    public void closedABIsConvertedToLeftSqBracketAStringCommaBStringRightSqBracket() {
        assertThat(testedEffectivityConverter
                        .convertToDatabaseColumn(new Effectivity(closed(a, b))),
                is(format("[%s,%s]", a.toString(), b.toString())));
    }

    @Test
    public void rightSqBracketCommaLeftSqBracketIsAll() {
        assertThat(testedEffectivityConverter.convertToEntityAttribute("],["),
                is(new Effectivity(all())));
    }

    @Test
    public void rightSqBracketCommaAStringRightSqBracketIsRangeUpToClosedA() {
        assertThat(testedEffectivityConverter
                        .convertToEntityAttribute(format("],%s]", a.toString())),
                is(new Effectivity(upTo(a, CLOSED))));
    }

    @Test
    public void rightSqBracketCommaAStringLeftSqBracketIsUpToOpenA() {
        assertThat(testedEffectivityConverter.convertToEntityAttribute(
                        format("],%s[", a.toString())),
                is(new Effectivity(upTo(a, OPEN))));
    }

    @Test
    public void leftSqBracketAStringCommaLeftSqBracketIsDownToClosedA() {
        assertThat(testedEffectivityConverter.convertToEntityAttribute(
                        format("[%s,[", a.toString())),
                is(new Effectivity(downTo(a, CLOSED))));
    }

    @Test
    public void rightSqBracketAStringCommaLeftSqBracketIsDownToOpenA() {
        assertThat(testedEffectivityConverter.convertToEntityAttribute(
                        format("]%s,[", a.toString())),
                is(new Effectivity(downTo(a, OPEN))));
    }

    @Test
    public void rightSqBracketAStringCommaBStringLeftSqBracketIsOpenAB() {
        assertThat(testedEffectivityConverter.convertToEntityAttribute(
                        format("]%s,%s[", a.toString(), b.toString())),
                is(new Effectivity(open(a, b))));
    }

    @Test
    public void leftSqBracketAStringCommaBStringRightSqBracketIsClosedAB() {
        assertThat(testedEffectivityConverter.convertToEntityAttribute(
                        format("[%s,%s]", a.toString(), b.toString())),
                is(new Effectivity(closed(a, b))));
    }
}
