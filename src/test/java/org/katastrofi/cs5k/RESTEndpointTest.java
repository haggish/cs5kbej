package org.katastrofi.cs5k;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.BadRequestException;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Optional.empty;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RESTEndpointTest {

    private RESTEndpoint testedRESTEndpoint;

    private CodeSet existingCodeSet, newCodeSet;

    private Code existingCode, newCode;

    @Mock
    private Service mockService;


    @Before
    public void init() {
        testedRESTEndpoint = new RESTEndpoint(mockService);
        existingCodeSet = new CodeSet("CS01", "desc", newHashSet());
        newCodeSet = new CodeSet("CS02", "desc", newHashSet());
        existingCode = new Code("C01", "desc", newHashMap());
        existingCodeSet.addOrUpdate(existingCode);
        newCode = new Code("C02", "desc", newHashMap());

        when(mockService.codeSetWithName(existingCodeSet.name()))
                .thenReturn(Optional.of(existingCodeSet));
    }

    @Test
    public void queryingAllReturnsCodeSetsAll() {
        Set<CodeSet> whateverCodeSetsReturns = newHashSet();
        when(mockService.allCodeSets()).thenReturn(whateverCodeSetsReturns);

        Set<CodeSet> whatEndpointReturns = testedRESTEndpoint.allCodeSets();

        assertThat(whatEndpointReturns, is(whateverCodeSetsReturns));
    }

    @Test
    public void queryingByNameReturnsOKIfCodeSetIsPresentInCodeSets() {
        assertThat(
                testedRESTEndpoint.codeSetWithName(existingCodeSet.name()).getStatus(),
                is(OK.getStatusCode()));
    }

    @Test
    public void queryingByNameReturnsCodeSetInBodyIfPresentInCodeSets() {
        assertThat(
                testedRESTEndpoint.codeSetWithName(existingCodeSet.name()).getEntity(),
                is(existingCodeSet));
    }

    @Test
    public void queryingByNameReturnsNotFoundIfCodeSetNotPresentInCodeSets() {
        assumeNoCodeSetIsFoundWithName();

        assertThat(testedRESTEndpoint.codeSetWithName("nonExisting").getStatus(),
                is(NOT_FOUND.getStatusCode()));
    }

    @Test
    public void addingOrUpdatingCodeSetAddsOrUpdatesGivenCodeSetToCodeSets() {
        testedRESTEndpoint.addOrUpdate(existingCodeSet.name(), existingCodeSet);

        verify(mockService).addOrUpdate(existingCodeSet);
    }

    @Test(expected = BadRequestException.class)
    public void addingOrUpdatingCodeSetWithDifferentResourceNameInPathThanInActualCodeSetIsBRE() {
        testedRESTEndpoint
                .addOrUpdate("different", existingCodeSet).getStatus();
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

    @Test(expected = BadRequestException.class)
    public void addingCodeToCodeSetWithDifferentResourceNameInPathThanResourceIsBRE() {
        assertThat(testedRESTEndpoint.addOrUpdateCodeSetsCode(
                        existingCodeSet.name(),
                        "differentCodeName",
                        existingCode).getStatus(),
                is(BAD_REQUEST.getStatusCode()));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = BadRequestException.class)
    public void addingCodeToNonExistentCodeSetIsBRE() {
        when(mockService.addOrUpdateCodeSetsCode(
                any(String.class), any(Code.class)))
                .thenThrow(IllegalArgumentException.class);

        testedRESTEndpoint.addOrUpdateCodeSetsCode(
                "nonExistentCodeSetName",
                newCode.name(),
                newCode).getStatus();
    }

    @Test
    public void addingOrUpdatingCodeToCodeSetCallsAddOrUpdateCodeOnCodeSet() {
        testedRESTEndpoint.addOrUpdateCodeSetsCode(existingCodeSet.name(),
                newCode.name(), newCode);

        verify(mockService).addOrUpdateCodeSetsCode(existingCodeSet.name(), newCode);
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
        when(mockService.addOrUpdateCodeSetsCode(
                any(String.class), any(Code.class)))
                .thenReturn(true);
        assertThat(testedRESTEndpoint.addOrUpdateCodeSetsCode(
                        existingCodeSet.name(),
                        existingCode.name(),
                        existingCode).getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void clearingCodeSetsReturnsNoContent() {
        assertThat(testedRESTEndpoint.clearCodeSets().getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void clearingCodeSetsRemovesAllCodeSets() {
        testedRESTEndpoint.clearCodeSets();

        verify(mockService).clearCodeSets();
    }

    @Test
    public void removingNamedCodeSetDelegatesRemovalToCodeSets() {
        testedRESTEndpoint.removeCodeSetWithName(existingCodeSet.name());

        verify(mockService).removeCodeSetWithName(existingCodeSet.name());
    }

    @Test
    public void removingCodeSetReturnsNoContent() {
        assertThat(testedRESTEndpoint.removeCodeSetWithName(
                        existingCodeSet.name()).getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void removingCodeInCodeSetReturnsNoContent() {
        assertThat(testedRESTEndpoint.removeCodeSetsCode(
                        existingCodeSet.name(), existingCode.name())
                        .getStatus(),
                is(NO_CONTENT.getStatusCode()));
    }

    @Test
    public void removingCodeSetsCodeCallsRemovalOnCodeSet() {
        testedRESTEndpoint.removeCodeSetsCode(
                existingCodeSet.name(), existingCode.name());

        verify(mockService).removeCodeSetsCode(
                existingCodeSet.name(), existingCode.name());
    }


    private void assumeNoCodeSetIsFoundWithName() {
        when(mockService.codeSetWithName(any(String.class)))
                .thenReturn(empty());
    }

    private void assumeCodeSetIsUpdated() {
        when(mockService.addOrUpdate(any(CodeSet.class))).thenReturn(true);
    }
}
