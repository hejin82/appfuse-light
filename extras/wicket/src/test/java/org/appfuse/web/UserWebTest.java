package org.appfuse.web;

import net.sourceforge.jwebunit.WebTestCase;

import java.util.ResourceBundle;

public class UserWebTest extends WebTestCase {
    private ResourceBundle messages;

    public UserWebTest(String name) {
        super(name);
        getTestContext().setBaseUrl("http://localhost:8080");
        getTestContext().setResourceBundleName("messages");
        messages = ResourceBundle.getBundle("messages");
        //getTestContext().setLocale(Locale.GERMAN);
        //getTestContext().getWebClient().setHeaderField("Accept-Language", "de");
    }

    public void testWelcomePage() {
        beginAt("/");
        assertTitleKeyMatches("index.title");
    }

    public void testAddUser() {
        beginAt("/app/users");
        submit("add-user");
        assertTitleKeyMatches("userForm.title");
        setFormElement("firstName", "Spring");
        setFormElement("lastName", "User");
        submit("save");
        assertTitleKeyMatches("userList.title");
    }

    public void testListUsers() {
        beginAt("/app/users");
        assertTitleKeyMatches("userList.title");

        // check that table is present
        assertTablePresent("userList");

        //check that a set of strings are present somewhere in table
        assertTextInTable("userList", new String[]{"Spring", "User"});
    }

    public void testEditUser() {
        beginAt("/app/users");
        assertTitleKeyMatches("userList.title");
        clickLinkWithText(getInsertedUserId());
        assertFormElementEquals("firstName", "Spring");
        submit("save");
        assertTitleKeyMatches("userList.title");
    }

    public void testDeleteUser() {
        beginAt("/app/users");
        assertTitleKeyMatches("userList.title");
        clickLinkWithText(getInsertedUserId());
        assertTitleKeyMatches("userForm.title");
        submit("delete");
        assertTitleKeyMatches("userList.title");
    }

    /**
     * Convenience method to get the id of the inserted user
     * Assumes last inserted user is "Spring User"
     * @return last id in the table
     */
    public String getInsertedUserId() {
        beginAt("/app/users");
        assertTablePresent("userList");
        assertTextInTable("userList", "Spring");
        String[][] sparseTableCellValues =
                getDialog().getSparseTableBySummaryOrId("userList");
        return sparseTableCellValues[sparseTableCellValues.length-1][0];
    }

    protected void assertTitleKeyMatches(String title) {
        assertTitleEquals(messages.getString(title) + " | " + messages.getString("webapp.name"));
    }
}