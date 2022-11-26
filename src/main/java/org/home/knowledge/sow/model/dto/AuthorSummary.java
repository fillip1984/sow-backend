package org.home.knowledge.sow.model.dto;

import org.springframework.beans.factory.annotation.Value;

public interface AuthorSummary extends AbstractSummary {

    String getLastName();

    String getFirstName();

    String getPreferredName();

    @Value("#{target.firstName + ' ' + target.lastName}")
    String getFullNameFirstThenLast();

    @Value("#{target.lastName + ', ' + target.firstName}")
    String getFullNameLastCommaFirst();

    String getBio();

}
