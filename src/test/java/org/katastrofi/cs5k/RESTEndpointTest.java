package org.katastrofi.cs5k;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.Optional.empty;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RESTEndpointTest {

    private RESTEndpoint testedRESTEndpoint;

    private CodeSet existingCodeSet, newCodeSet;

    private Code existingCode, newCode;

    @Mock
    private CodeSets mockCodeSets;


    @Before
    public void init() {
        testedRESTEndpoint = new RESTEndpoint(mockCodeSets);
        existingCodeSet = new CodeSet("CS01", "desc", newHashSet());
        newCodeSet = new CodeSet("CS02", "desc", newHashSet());
        existingCode = new Code("C01", "desc", newHashSet());
        existingCodeSet.addOrUpdate(existingCode);
        newCode = new Code("C02", "desc", newHashSet());

        when(mockCodeSets.withName(existingCodeSet.name()))
                .thenReturn(Optional.of(existingCodeSet));
    }

    @Test
    public void queryingAllReturnsCodeSetsAll() {
        Set<CodeSet> whateverCodeSetsReturns = newHashSet();
        when(mockCodeSets.all()).thenReturn(whateverCodeSetsReturns);

        Set<CodeSet> whatEndpointReturns = testedRESTEndpoint.all();

        assertThat(whatEndpointReturns, is(whateverCodeSetsReturns));
    }

    @Test
    public void queryingByNameReturnsOKIfCodeSetIsPresentInCodeSets() {
        assertThat(
                testedRESTEndpoint.withName(existingCodeSet.name()).getStatus(),
                is(OK.getStatusCode()));
    }

    @Test
    public void queryingByNameReturnsCodeSetInBodyIfPresentInCodeSets() {
        assertThat(
                testedRESTEndpoint.withName(existingCodeSet.name()).getEntity(),
                is(existingCodeSet));
    }

    @Test
    public void queryingByNameReturnsNotFoundIfCodeSetNotPresentInCodeSets() {
        assumeNoCodeSetIsFoundWithName();

        assertThat(testedRESTEndpoint.withName("nonExisting").getStatus(),
                is(NOT_FOUND.getStatusCode()));
    }

    @Test
    public void addingOrUpdatingCodeSetAddsOrUpdatesGivenCodeSetToCodeSets() {
        testedRESTEndpoint.addOrUpdate(existingCodeSet.name(), existingCodeSet);

        verify(mockCodeSets).addOrUpdate(existingCodeSet);
    }

    @Test
    public void addingOrUpdatingCodeSetReturnsBadRequestIfGivenCodeSetNameIsDifferentThanNameInCodeSet() {
        assertThat(testedRESTEndpoint
                        .addOrUpdate("different", existingCodeSet).getStatus(),
                is(BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void updatingCodeSetReturnsNoContent() {
        assumeCodeSetIsUpdated();

        assertThat(testedRESTEndpoint
                        .addOrUpdate(existingCodeSet.name(), existingCodeSet)
                        .getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void addingCodeSetReturnsCreated() {
        assertThat(testedRESTEndpoint
                        .addOrUpdate(newCodeSet.name(), newCodeSet)
                        .getStatus(),
                is(CREATED.getStatusCode()));
    }

    @Test
    public void addingCodeToCodeSetReturnsBadRequestIfGivenCodeNameIsDifferentThanNameInCode() {
        assertThat(testedRESTEndpoint.addOrUpdateCodeSetsCode(
                        existingCodeSet.name(),
                        "differentCodeName",
                        existingCode).getStatus(),
                is(BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void addingCodeToNonExistentCodeSetReturnsBadRequest() {
        assumeNoCodeSetIsFoundWithName();

        assertThat(testedRESTEndpoint.addOrUpdateCodeSetsCode(
                        "nonExistentCodeSetName",
                        newCode.name(),
                        newCode).getStatus(),
                is(BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void addingOrUpdatingCodeToCodeSetCallsAddOrUpdateCodeOnCodeSet() {
        testedRESTEndpoint.addOrUpdateCodeSetsCode(existingCodeSet.name(),
                newCode.name(), newCode);

        assertThat(existingCodeSet.code(newCode.name()), is(newCode));
    }

    @Test
    public void addingCodeToCodeSetReturnsCreated() {
        assertThat(testedRESTEndpoint.addOrUpdateCodeSetsCode(
                        existingCodeSet.name(),
                        newCode.name(),
                        newCode).getStatus(),
                is(CREATED.getStatusCode()));
    }

    @Test
    public void updatingCodeInCodeSetReturnsNoContent() {
        assertThat(testedRESTEndpoint.addOrUpdateCodeSetsCode(
                        existingCodeSet.name(),
                        existingCode.name(),
                        existingCode).getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void clearingCodeSetsReturnsNoContent() {
        assertThat(testedRESTEndpoint.clear().getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void clearingCodeSetsRemovesAllCodeSets() {
        testedRESTEndpoint.clear();

        verify(mockCodeSets).removeAll();
    }

    @Test
    public void removingNamedCodeSetDelegatesRemovalToCodeSets() {
        testedRESTEndpoint.removeWithName(existingCodeSet.name());

        verify(mockCodeSets).removeWithName(existingCodeSet.name());
    }

    @Test
    public void removingCodeSetReturnsNoContent() {
        assertThat(testedRESTEndpoint.removeWithName(
                        existingCodeSet.name()).getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void removingCodeInCodeSetReturnsNoContent() {
        assertThat(testedRESTEndpoint.removeCodeSetsCode(
                        existingCodeSet.name(), existingCode.name()).getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void removingCodeSetsCodeCallsRemovalOnCodeSet() {
        testedRESTEndpoint.removeCodeSetsCode(
                existingCodeSet.name(), existingCode.name());

        assertThat(existingCodeSet.codes(), not(contains(existingCode)));
    }


    private void assumeNoCodeSetIsFoundWithName() {
        when(mockCodeSets.withName(any(String.class))).thenReturn(empty());
    }

    private void assumeCodeSetIsUpdated() {
        when(mockCodeSets.addOrUpdate(any(CodeSet.class))).thenReturn(true);
    }
}
