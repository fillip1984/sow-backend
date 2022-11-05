package org.home.knowledge.sow.commons;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

/**
 * Converts string representation to LocalDate
 * <p>
 * The out of box one expects strings to be in year, month, day format and then
 * lets you plug in your formats. Here, I'm trying that first and then passing
 * the date through a number of common formats. Is it a good idea to use this,
 * probably not. You should know the format beforehand but this is more
 * resilient...lazy
 */
@Slf4j
public class SuperiorStringToLocalDateParser {

    // @formatter:off
    public static final String[] DATE_FORMATS = new String[] { "MM/dd/yyyy", 
                                                               "MM/dd/yy", 
                                                               "MM-dd-yyyy", 
                                                               "MM-dd-yy" 
                                                            };
    // @formatter:on

    public static LocalDate parse(String input) {
        log.debug("Parsing input: {} into LocalDate", input);
        try {
            return LocalDate.parse(input);
        } catch (Exception e) {
            log.trace("Couldn't parse input: '{}'' using out of box LocalDate.parse() method", input);
        }

        for (String dateFormat : DATE_FORMATS) {
            try {
                return LocalDate.parse(input, DateTimeFormatter.ofPattern(dateFormat));
            } catch (Exception e) {
                log.trace("Couldn't parse input: '{}' using '{}' format", dateFormat);
            }
        }

        throw new RuntimeException("Tried all of the common formats and failed to convert date. Input: '" + input
                + "', currently supported date formats: " + Arrays.toString(DATE_FORMATS));
    }
}
