package com.fw.wcs.core.base;

import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeEditor extends PropertyEditorSupport {
    private final String dateFormat;
    private final boolean allowEmpty;
    private final int exactDateLength;

    public CustomLocalDateTimeEditor( String dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }

    public CustomLocalDateTimeEditor( String dateFormat, boolean allowEmpty, int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            this.setValue((Object)null);
        } else {
            if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
                throw new IllegalArgumentException("Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
            }
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern( dateFormat );
            this.setValue( LocalDateTime.parse( text, dateTimeFormatter ) );
        }

    }

    public String getAsText() {
        LocalDateTime value = (LocalDateTime)this.getValue();
        return value != null ? value.format( DateTimeFormatter.ofPattern( dateFormat ) ) : "";
    }
}
