package org.home.knowledge.sow.model.dto;

public interface AuthorSummary extends AbstractSummary {

    String getFirstName();

    String getLastName();

    String getPreferredName();

    String getFullNameFirstThenLast();

    String getFullNameLastCommaFirst();

    String getBio();

}
