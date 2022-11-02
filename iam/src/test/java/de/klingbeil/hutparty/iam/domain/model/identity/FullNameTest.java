package de.klingbeil.hutparty.iam.domain.model.identity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import de.klingbeil.hutparty.iam.domain.model.IdentityAccessTest;

class FullNameTest extends IdentityAccessTest {

    private final static String FIRST_NAME = "Zoe";
    private final static String LAST_NAME = "Doe";
    private final static String MARRIED_LAST_NAME = "Jones-Doe";
    private final static String WRONG_FIRST_NAME = "Zeo";

    public FullNameTest() {
        super();
    }

    @Test
    void testChangedFirstName() {
        FullName name = new FullName(WRONG_FIRST_NAME, LAST_NAME);

        name = name.withChangedFirstName(FIRST_NAME);

        assertEquals(FIRST_NAME + " " + LAST_NAME, name.asFormattedName());
    }

    @Test
    void testChangedLastName() {
        FullName name = new FullName(FIRST_NAME, LAST_NAME);

        name = name.withChangedLastName(MARRIED_LAST_NAME);

        assertEquals(FIRST_NAME + " " + MARRIED_LAST_NAME, name.asFormattedName());
    }

    @Test
    void testFormattedName() {
        FullName name = new FullName(FIRST_NAME, LAST_NAME);

        assertEquals(FIRST_NAME + " " + LAST_NAME, name.asFormattedName());
    }
}
