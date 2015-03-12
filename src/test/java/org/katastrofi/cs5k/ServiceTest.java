package org.katastrofi.cs5k;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    private Service testedService;

    private CodeSet givenCodeSet;

    private Code givenCode, existingCode;

    @Mock
    private CodeSets mockCodeSets;


    @Before
    public void init() {
        testedService = new Service(mockCodeSets);
        givenCode = new Code("C01", "code", newHashMap());
        givenCodeSet = new CodeSet("CS01", "desc", newHashSet(givenCode));
        existingCode = givenCode;
    }


    @Test
    public void allCodeSetsReturnsAllOfCodeSets() {
        Set<CodeSet> whateverCodeSetsReturns = newHashSet();
        when(mockCodeSets.all()).thenReturn(whateverCodeSetsReturns);

        assertThat(testedService.allCodeSets(), is(whateverCodeSetsReturns));
    }

    @Test
    public void addingOrUpdatingCodeSetCallsCodeSetsAddOrUpdate() {
        testedService.addOrUpdate(givenCodeSet);

        verify(mockCodeSets).addOrUpdate(givenCodeSet);
    }

    @Test
    public void
    addingOrUpdatingCodeSetReturnsWhateverCodeSetsAddOrUpdateDoes() {
        when(mockCodeSets.addOrUpdate(any(CodeSet.class))).thenReturn(true);

        assertThat(testedService.addOrUpdate(givenCodeSet), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addingOrUpdatingNonExistentCodeSetsCodeIsIAE() {
        when(mockCodeSets.withName(any(String.class)))
                .thenReturn(Optional.<CodeSet>empty());

        testedService.addOrUpdateCodeSetsCode(givenCodeSet.name(), givenCode);
    }

    @Test
    public void updatingCodeGetsCodeSetWithName() {
        when(mockCodeSets.withName(any(String.class)))
                .thenReturn(Optional.of(givenCodeSet));

        testedService.addOrUpdateCodeSetsCode(givenCodeSet.name(), givenCode);

        verify(mockCodeSets).withName(givenCodeSet.name());
    }

    @Test
    public void updatingCodeReturnsWhateverCodeSetsAddOrUpdateDoes() {
        when(mockCodeSets.withName(any(String.class)))
                .thenReturn(Optional.of(givenCodeSet));

        assertThat(testedService.addOrUpdateCodeSetsCode(
                givenCodeSet.name(), existingCode),
                is(true));
    }

    @Test
    public void clearingCodeSetsCallsCodeSetsRemoveAll() {
        testedService.clearCodeSets();

        verify(mockCodeSets).removeAll();
    }

    @Test
    public void removingCodeSetWithNameCallsCodeSetsRemoveWithGivenName() {
        testedService.removeCodeSetWithName(givenCodeSet.name());

        verify(mockCodeSets).removeWithName(givenCodeSet.name());
    }

    @Test
    public void
    removingCodeSetsCodeGetsCodeSetOfRemovedCodeWithGivenCodeSetName() {
        when(mockCodeSets.withName(any(String.class)))
                .thenReturn(Optional.of(givenCodeSet));

        testedService.removeCodeSetsCode(givenCodeSet.name(), givenCode.name());

        verify(mockCodeSets).withName(givenCodeSet.name());
    }

    @Test
    public void
    removingCodeSetsCodeCallsExistingCodeSetsRemoveWithGivenCodeName() {
        when(mockCodeSets.withName(any(String.class)))
                .thenReturn(Optional.of(givenCodeSet));

        testedService.removeCodeSetsCode(givenCodeSet.name(), givenCode.name());

        assertThat(givenCodeSet.codes(), not(contains(givenCode)));
    }
}
