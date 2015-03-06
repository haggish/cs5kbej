package org.katastrofi.cs5k;

import com.google.common.collect.Range;

import java.time.LocalDateTime;

abstract class TemporalObject extends IdentifiableObject {

    private Range<LocalDateTime> effectivity;

}
